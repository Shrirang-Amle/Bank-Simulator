package dto;

import java.time.LocalDate;

public class Customerdto {
    private String name;
    private String phoneNumber;
    private String email;
    private String address;
    private String customerPin;
    private String aadharNumber;
    private LocalDate dob;
    private String pin;

    public Customerdto() {
    }

    public Customerdto(String name, String phoneNumber, String email, String address,
            String customerPin, String aadharNumber, LocalDate dob) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.customerPin = customerPin;
        this.aadharNumber = aadharNumber;
        this.dob = dob;
    }

    // getters/setters
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

    public String getCustomerPin() {
        return customerPin;
    }

    public void setCustomerPin(String customerPin) {
        this.customerPin = customerPin;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    // backward-compatible aliases (in case some code still uses
    // getPhone()/setPhone())
    public String getPhone() {
        return getPhoneNumber();
    }

    public void setPhone(String phone) {
        setPhoneNumber(phone);
    }
}
