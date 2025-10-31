package service;

import dao.AccountDao;
import dao.Customerdao;
import dao.TransactionDao;
import model.Account;
import model.Customer;
import model.Transaction;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import config.DBConfig;

public class TransactionService {
    private final TransactionDao transactionDao;
    private final AccountDao accountDao;
    private final Customerdao customerdao;
    private final NotificationService notificationService;

    public TransactionService() {
        this.transactionDao = new TransactionDao();
        this.accountDao = new AccountDao();
        // --- FIXED ---
        // Was 'new Customerdao(null)', which would cause a NullPointerException.
        // It now correctly initializes the DAO.
        this.customerdao = new Customerdao();
        this.notificationService = new NotificationService();
    }

    public Transaction transfer(String senderAccountNumber, String receiverAccountNumber, BigDecimal amount, String pin,
            String description) {
        if (senderAccountNumber.equals(receiverAccountNumber)) {
            throw new IllegalArgumentException("Sender and receiver account numbers must be different.");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction amount must be positive.");
        }

        Connection conn = null;
        try {
            conn = DBConfig.getInstance().getConnection();
            conn.setAutoCommit(false); // Start transaction

            Account senderAccount = accountDao.getAccountByNumberForUpdate(conn, senderAccountNumber);
            if (senderAccount == null)
                throw new IllegalArgumentException("Sender account not found.");

            Account receiverAccount = accountDao.getAccountByNumberForUpdate(conn, receiverAccountNumber);
            if (receiverAccount == null)
                throw new IllegalArgumentException("Receiver account not found.");

            // --- FIXED ---
            // These calls now correctly pass the active database 'conn' to ensure the
            // customer lookups
            // are part of the same atomic transaction. This is essential for data
            // consistency.
            Customer senderCustomer = customerdao.getCustomerById(conn, senderAccount.getCustomerId());

            // --- THIS IS THE KEY FIX FOR YOUR TEST ---
            // It correctly throws SecurityException, which your controller
            // will turn into a 401 Unauthorized response.
            if (senderCustomer == null || !pin.equals(senderCustomer.getPin())) {
                throw new SecurityException("Invalid PIN provided.");
            }

            // This check makes your "insufficient funds" test pass
            if (senderAccount.getBalance().compareTo(amount) < 0) {
                throw new IllegalArgumentException("Insufficient balance.");
            }

            senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
            receiverAccount.setBalance(receiverAccount.getBalance().add(amount));
            accountDao.updateBalance(conn, senderAccount);
            accountDao.updateBalance(conn, receiverAccount);

            Transaction transaction = new Transaction();
            transaction.setAccountId(senderAccount.getAccountId());
            transaction.setTransactionAmount(amount);
            transaction.setTransactionTime(LocalDateTime.now());
            transaction.setTransactionMode("Online Transfer");
            transaction.setSenderDetails(senderAccountNumber);
            transaction.setReceiverDetails(receiverAccountNumber);
            transaction.setDescription(description);

            Transaction savedTransaction = transactionDao.save(conn, transaction);
            conn.commit(); // Commit all changes if everything was successful

            // Send notifications only after the transaction is successfully committed
            Customer receiverCustomer = customerdao.getCustomerById(conn, receiverAccount.getCustomerId());
            notificationService.sendTransferNotifications(senderCustomer, receiverCustomer, savedTransaction);

            return savedTransaction;

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    /* log error */ }
            }
            // This re-throws the specific exceptions your controller is looking for
            if (e instanceof IllegalArgumentException || e instanceof SecurityException)
                throw (RuntimeException) e;

            throw new RuntimeException("Error performing transaction: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    /* log error */ }
            }
        }
    }

    public List<Transaction> getTransactionsForAccount(String accountNumber) {
        Account acct = accountDao.getAccountByNumber(accountNumber);
        if (acct == null) {
            throw new IllegalArgumentException("Account not found for number: " + accountNumber);
        }
        return transactionDao.getTransactionsByAccountId(acct.getAccountId());
    }

    public void writeTransactionsToCsv(String accountNumber, OutputStream output) throws IOException {
        List<Transaction> transactions = getTransactionsForAccount(accountNumber);
        try (PrintWriter writer = new PrintWriter(output)) {
            writer.println("TransactionID,Date,Description,Amount,Sender,Receiver");
            for (Transaction t : transactions) {
                BigDecimal transactionAmount = t.getSenderDetails().equals(accountNumber)
                        ? t.getTransactionAmount().negate()
                        : t.getTransactionAmount();

                writer.printf("%d,%s,\"%s\",%.2f,%s,%s\n",
                        t.getTransactionId(), t.getTransactionTime().toString(), t.getDescription(),
                        transactionAmount, t.getSenderDetails(), t.getReceiverDetails());
            }
        }
    }
}