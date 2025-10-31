package dao;

import model.Account;
import config.DBConfig;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class AccountDao {
    private final DBConfig dbConfig = DBConfig.getInstance();

    public Account createAccount(Account account) {
        if (getAccountByNumber(account.getAccountNumber()) != null) {
            throw new RuntimeException("Account number already exists");
        }
        String sql = "INSERT INTO account (customerId, createdAt, modifiedAt, balance, accountType, accountName, accountNumber, phoneNumberLinked, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, account.getCustomerId());
            ps.setTimestamp(2, Timestamp.valueOf(account.getCreatedAt()));
            ps.setTimestamp(3, Timestamp.valueOf(account.getModifiedAt()));
            ps.setBigDecimal(4, account.getBalance());
            ps.setString(5, account.getAccountType());
            ps.setString(6, account.getAccountName());
            ps.setString(7, account.getAccountNumber());
            ps.setString(8, account.getPhoneNumberLinked());
            ps.setString(9, account.getStatus());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    account.setAccountId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving account: " + e.getMessage(), e);
        }
        return account;
    }

    public Account updateAccount(String accountNumber, Account account) {
        String sql = "UPDATE account SET customerId=?, modifiedAt=?, balance=?, accountType=?, accountName=?, phoneNumberLinked=?, status=? WHERE accountNumber=?";
        try (Connection conn = dbConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, account.getCustomerId());
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now())); // Always update modifiedAt
            ps.setBigDecimal(3, account.getBalance());
            ps.setString(4, account.getAccountType());
            ps.setString(5, account.getAccountName());
            ps.setString(6, account.getPhoneNumberLinked());
            ps.setString(7, account.getStatus());
            ps.setString(8, accountNumber);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating account: " + e.getMessage(), e);
        }
        return getAccountByNumber(accountNumber); // Return the updated account from DB
    }

    public Account getAccountByNumber(String accountNumber) {
        String sql = "SELECT * FROM account WHERE accountNumber = ?";
        try (Connection conn = dbConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAccount(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching account: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * --- NEW & FIXED ---
     * Implements the missing logic.
     * Fetches an account within an existing transaction and locks the row to
     * prevent race conditions.
     */
    public Account getAccountByNumberForUpdate(Connection conn, String accountNumber) throws SQLException {
        String sql = "SELECT * FROM account WHERE accountNumber = ? FOR UPDATE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAccount(rs);
                }
            }
        }
        return null;
    }

    /**
     * --- NEW & FIXED ---
     * Implements the missing logic.
     * Updates only the balance of an account within an existing transaction.
     */
    public void updateBalance(Connection conn, Account account) throws SQLException {
        String sql = "UPDATE account SET balance = ?, modifiedAt = ? WHERE accountId = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, account.getBalance());
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.setLong(3, account.getAccountId());
            ps.executeUpdate();
        }
    }

    public boolean deleteAccountByNumber(String accountNumber) {
        String sql = "DELETE FROM account WHERE accountNumber = ?";
        try (Connection conn = dbConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting account: " + e.getMessage(), e);
        }
    }

    public List<Account> getAllAccounts() {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT * FROM account";
        try (Connection conn = dbConfig.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToAccount(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all accounts: " + e.getMessage(), e);
        }
        return list;
    }

    // Helper method to avoid code duplication
    private Account mapResultSetToAccount(ResultSet rs) throws SQLException {
        Account account = new Account();
        account.setAccountId(rs.getLong("accountId"));
        account.setCustomerId(rs.getLong("customerId"));
        account.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
        account.setModifiedAt(rs.getTimestamp("modifiedAt").toLocalDateTime());
        account.setBalance(rs.getBigDecimal("balance"));
        account.setAccountType(rs.getString("accountType"));
        account.setAccountName(rs.getString("accountName"));
        account.setAccountNumber(rs.getString("accountNumber"));
        account.setPhoneNumberLinked(rs.getString("phoneNumberLinked"));
        account.setStatus(rs.getString("status"));
        return account;
    }
}
