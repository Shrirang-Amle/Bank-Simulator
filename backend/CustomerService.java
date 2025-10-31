package service;

import dao.Customerdao;
import model.Customer;
import dto.Customerdto;
import util.ValidationUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class CustomerService {

    private final Customerdao dao;
    private static final Pattern PIN_PATTERN = Pattern.compile("^\\d{4}$");

    // --- FIXED ---
    // Added a default constructor so that your CustomerResource can instantiate
    // this service.
    public CustomerService() {
        this.dao = new Customerdao();
    }

    // Constructor for testing
    public CustomerService(Customerdao dao) {
        this.dao = dao;
    }

    public long createCustomerAndReturnId(Customerdto request) throws SQLException, IllegalArgumentException {
        if (!ValidationUtil.isValidName(request.getName()))
            throw new IllegalArgumentException("Invalid Name!");
        if (!ValidationUtil.isValidPhone(request.getPhoneNumber()))
            throw new IllegalArgumentException("Invalid Phone!");
        if (!ValidationUtil.isValidEmail(request.getEmail()))
            throw new IllegalArgumentException("Invalid Email!");
        if (!ValidationUtil.isValidAddress(request.getAddress()))
            throw new IllegalArgumentException("Invalid Address!");
        if (request.getPin() == null || !PIN_PATTERN.matcher(request.getPin()).matches()) {
            throw new IllegalArgumentException("PIN must be exactly 4 digits.");
        }

        Customer c = new Customer(
                0L,
                request.getName(),
                request.getPhoneNumber(),
                request.getEmail(),
                request.getAddress(),
                request.getPin(),
                request.getAadharNumber(),
                request.getDob());
        return dao.createCustomer(c);
    }

    public Customer getCustomer(long id) throws SQLException {
        // --- FIXED ---
        // Was calling 'dao.getCustomerById(id)', which doesn't exist.
        // Now calls the correct overloaded method 'getCustomerById(Connection, long)'.
        return dao.getCustomerById(null, id);
    }

    public String updateCustomer(long id, Customerdto request) throws SQLException, IllegalArgumentException {
        Customer existing = dao.getCustomerById(null, id);
        if (existing == null)
            throw new IllegalArgumentException("Customer not found!");

        if (!ValidationUtil.isValidName(request.getName()))
            throw new IllegalArgumentException("Invalid Name!");
        if (!ValidationUtil.isValidPhone(request.getPhoneNumber()))
            throw new IllegalArgumentException("Invalid Phone!");
        if (!ValidationUtil.isValidEmail(request.getEmail()))
            throw new IllegalArgumentException("Invalid Email!");
        if (!ValidationUtil.isValidAddress(request.getAddress()))
            throw new IllegalArgumentException("Invalid Address!");

        existing.setName(request.getName());
        existing.setPhoneNumber(request.getPhoneNumber());
        existing.setEmail(request.getEmail());
        existing.setAddress(request.getAddress());
        existing.setAadharNumber(request.getAadharNumber());
        existing.setDob(request.getDob());
        if (request.getPin() != null && PIN_PATTERN.matcher(request.getPin()).matches()) {
            existing.setPin(request.getPin());
        }

        dao.updateCustomer(existing);
        return "Customer Updated Successfully!";
    }

    public List<Customer> getAllCustomers() throws SQLException {
        return dao.getAllCustomers();
    }

    public boolean deleteCustomer(long id) throws SQLException {
        Customer existing = dao.getCustomerById(null, id);
        if (existing == null) {
            return false;
        }
        dao.deleteCustomer(id);
        return true;
    }
}
