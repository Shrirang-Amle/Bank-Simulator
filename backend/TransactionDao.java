package dao;

import model.Transaction;
import config.DBConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDao {

    private final DBConfig dbConfig = DBConfig.getInstance();

    /**
     * Saves a single transaction record.
     * Note: transactionType is removed as it's no longer needed.
     */
    public Transaction save(Connection conn, Transaction t) throws SQLException {
        String sql = "INSERT INTO bank_transaction (accountId, transactionAmount, transactionTime, transactionMode, receiverDetails, senderDetails, description) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, t.getAccountId());
            ps.setBigDecimal(2, t.getTransactionAmount());
            ps.setTimestamp(3, Timestamp.valueOf(t.getTransactionTime()));
            ps.setString(4, t.getTransactionMode());
            ps.setString(5, t.getReceiverDetails());
            ps.setString(6, t.getSenderDetails());
            ps.setString(7, t.getDescription());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    t.setTransactionId(rs.getLong(1));
                }
            }
        }
        return t;
    }

    /**
     * Gets all transactions related to a specific account, whether the account was
     * the sender or the receiver. This is essential for a complete transaction
     * history.
     */
    public List<Transaction> getTransactionsByAccountId(long accountId) {
        List<Transaction> list = new ArrayList<>();
        // We need to fetch transactions where the account is either the primary account
        // OR the receiver.
        // This is simplified by just using the accountId from the account table.
        // A more robust way is to join tables, but for now we query where the accountId
        // matches.
        // The logic for debit/credit is now handled at the service/reporting layer.
        String sql = "SELECT bt.* FROM bank_transaction bt " +
                "JOIN account a_sender ON bt.senderDetails = a_sender.accountNumber " +
                "JOIN account a_receiver ON bt.receiverDetails = a_receiver.accountNumber " +
                "WHERE a_sender.accountId = ? OR a_receiver.accountId = ? " +
                "ORDER BY bt.transactionTime DESC";

        try (Connection conn = dbConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, accountId);
            ps.setLong(2, accountId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Transaction t = new Transaction();
                    t.setTransactionId(rs.getLong("transactionId"));
                    t.setAccountId(rs.getLong("accountId")); // This is the sender's accountId
                    t.setTransactionAmount(rs.getBigDecimal("transactionAmount"));
                    t.setTransactionTime(rs.getTimestamp("transactionTime").toLocalDateTime());
                    t.setTransactionMode(rs.getString("transactionMode"));
                    t.setReceiverDetails(rs.getString("receiverDetails"));
                    t.setSenderDetails(rs.getString("senderDetails"));
                    t.setDescription(rs.getString("description"));
                    list.add(t);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching transactions: " + e.getMessage(), e);
        }
        return list;
    }
}
