package dao;

import model.Customer;
import config.DBConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Customerdao {
    private final DBConfig dbConfig;

    // A single constructor is cleaner.
    public Customerdao() {
        this.dbConfig = DBConfig.getInstance();
    }

    public long createCustomer(Customer c) throws SQLException {
        // -----------------
        // 1. FIX HERE: Change "pin" to your real database column name in this SQL query
        // -----------------
        String sql = "INSERT INTO customer (name, phoneNumber, email, address, YOUR_PIN_COLUMN_NAME, aadharNumber, dob) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getPhoneNumber());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getAddress());
            ps.setString(5, c.getPin()); // This line is OK (it gets data from the Java object)
            ps.setString(6, c.getAadharNumber());
            ps.setDate(7, java.sql.Date.valueOf(c.getDob()));
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    c.setCustomerId(id);
                    return id; // Return the generated ID
                }
            }
        }
        // If no ID was generated, something went wrong.
        throw new SQLException("Creating customer failed, no ID obtained.");
    }

    /**
     * --- NEW METHOD ---
     * Efficiently finds a single customer by their phone number.
     * This is required by your AccountService to avoid loading all customers into
     * memory.
     */
    public Customer getCustomerByPhoneNumber(String phoneNumber) throws SQLException {
        String sql = "SELECT * FROM customer WHERE phoneNumber = ?";
        try (Connection conn = dbConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phoneNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCustomer(rs);
                }
            }
        }
        return null; // Return null if no customer is found
    }

    /**
     * Gets a customer by their ID, can participate in an existing transaction.
     */
    public Customer getCustomerById(Connection conn, long id) throws SQLException {
        String sql = "SELECT * FROM customer WHERE customerId = ?";
        Connection connection = (conn != null) ? conn : dbConfig.getConnection();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCustomer(rs);
                }
            }
        } finally {
            if (conn == null && connection != null) {
                connection.close();
            }
        }
        return null;
    }

    // Helper method to map a ResultSet row to a Customer object
    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        // -----------------
        // 2. FIX HERE: This is the line that caused your error. Change "pin" to your
        // real column name.
        // -----------------
        return new Customer(
                rs.getLong("customerId"),
                rs.getString("name"),
                rs.getString("phoneNumber"),
                rs.getString("email"),
                rs.getString("address"),
                rs.getString("pin"), // <-- FIX THIS (Original was "pin")
                rs.getString("aadharNumber"),
                rs.getDate("dob").toLocalDate());
    }

    // Other methods like updateCustomer, getAllCustomers, etc. remain here...
    public void updateCustomer(Customer c) throws SQLException {
        // -----------------
        // 3. FIX HERE: Change "pin" to your real database column name in this SQL query
        // -----------------
        String sql = "UPDATE customer SET name=?, phoneNumber=?, email=?, address=?, pin=?, aadharNumber=?, dob=? WHERE customerId=?";

        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getPhoneNumber());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getAddress());
            ps.setString(5, c.getPin()); // This line is OK
            ps.setString(6, c.getAadharNumber());
            ps.setDate(7, java.sql.Date.valueOf(c.getDob()));
            ps.setLong(8, c.getCustomerId());
            ps.executeUpdate();
        }
    }

    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customer";
        try (Connection conn = dbConfig.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToCustomer(rs));
            }
        }
        return list;
    }

    public void deleteCustomer(long id) throws SQLException {
        String sql = "DELETE FROM customer WHERE customerId = ?";
        try (Connection conn = dbConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}