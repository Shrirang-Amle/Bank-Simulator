package model;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

public class Customer {
    private long customerId;
    private String name;
    private String phoneNumber;
    private String email;
    private String address;
    // --- UPDATED --- Consolidated 'customerPin' and 'pin' into one field
    private String pin;
    private String aadharNumber;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;

    public Customer() {
    }

    public Customer(long customerId, String name, String phoneNumber, String email,
            String address, String pin, String aadharNumber, LocalDate dob) {
        this.customerId = customerId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.pin = pin;
        this.aadharNumber = aadharNumber;
        this.dob = dob;
    }

    // Updated Getters and Setters
    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }
}
