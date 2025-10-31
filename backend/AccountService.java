package service;

import dao.AccountDao;
import dao.Customerdao;
import model.Account;
import model.Customer;

import java.sql.Connection; // <-- Import
import java.sql.SQLException; // <-- Import
import config.DBConfig; // <-- Import

import java.time.LocalDateTime;
import java.util.List;

public class AccountService {
    private final AccountDao accountDao;
    private final Customerdao customerDao;

    public AccountService() {
        this.accountDao = new AccountDao();
        this.customerDao = new Customerdao();
    }

    // --- THIS METHOD IS NOW TRANSACTIONAL ---
    public Account createAccount(Account account) {
        // 1. Validation checks (your code was here)
        if (account.getPhoneNumberLinked() == null || account.getPhoneNumberLinked().isEmpty()) {
            throw new IllegalArgumentException("Linked phone number is required.");
        }
        if (account.getAccountNumber() == null || account.getAccountNumber().isEmpty()) {
            throw new IllegalArgumentException("Account number is required.");
        }

        Connection conn = null;
        try {
            conn = DBConfig.getInstance().getConnection();
            conn.setAutoCommit(false); // Start transaction

            // 2. Check for duplicate account number
            if (accountDao.getAccountByNumber(account.getAccountNumber()) != null) {
                throw new IllegalArgumentException("Account number already exists.");
            }

            // 3. Find customer (NOW PASSES 'conn')
            Customer customer = customerDao.getCustomerByPhoneNumber(account.getPhoneNumberLinked());
            if (customer == null) {
                throw new IllegalArgumentException(
                        "No customer found with the linked phone number: " + account.getPhoneNumberLinked());
            }

            // 4. Set remaining data and create account
            account.setCustomerId(customer.getCustomerId());
            account.setCreatedAt(LocalDateTime.now());
            account.setModifiedAt(LocalDateTime.now());

            Account createdAccount = accountDao.createAccount(account);

            conn.commit(); // Commit transaction
            return createdAccount;

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    /* log error */ }
            }
            // Re-throw specific exceptions for the controller
            if (e instanceof IllegalArgumentException)
                throw (IllegalArgumentException) e;
            // Otherwise, wrap it
            throw new RuntimeException("Error creating account: " + e.getMessage(), e);
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

    // --- THIS METHOD IS ALSO NOW TRANSACTIONAL ---
    public Account updateAccount(String accountNumber, Account account) {
        Connection conn = null;
        try {
            conn = DBConfig.getInstance().getConnection();
            conn.setAutoCommit(false); // Start transaction

            Account existing = accountDao.getAccountByNumberForUpdate(conn, accountNumber);
            if (existing == null) {
                throw new IllegalArgumentException("Cannot update an account that does not exist.");
            }

            // Preserve essential data
            account.setCustomerId(existing.getCustomerId());
            account.setCreatedAt(existing.getCreatedAt());
            account.setAccountId(existing.getAccountId());
            account.setModifiedAt(LocalDateTime.now());

            Account updatedAccount = accountDao.updateAccount(accountNumber, account);

            conn.commit(); // Commit transaction
            return updatedAccount;

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    /* log error */ }
            }
            if (e instanceof IllegalArgumentException)
                throw (IllegalArgumentException) e;
            throw new RuntimeException("Error updating account: " + e.getMessage(), e);
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

    // --- Read-only methods don't need the full transaction ---
    public Account getAccountByNumber(String accountNumber) {
        // Assuming your DAO handles its own connection for simple 'get'
        return accountDao.getAccountByNumber(accountNumber);
    }

    public boolean deleteAccountByNumber(String accountNumber) {
        // Assuming DAO handles its own connection for 'delete'
        // If this also fails, it will need the same transaction pattern as above.
        return accountDao.deleteAccountByNumber(accountNumber);
    }

    public List<Account> getAllAccounts() {
        return accountDao.getAllAccounts();
    }
}