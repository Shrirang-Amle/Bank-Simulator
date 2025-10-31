package service;

import model.Customer;
import model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * A simple class to test if the EmailService is working.
 */
public class TestEmail {

    public static void main(String[] args) {
        System.out.println("Attempting to send a test email...");

        // 1. Create the service
        NotificationService notificationService = new NotificationService();

        // 2. Create dummy data
        Customer sender = new Customer();
        sender.setName("Test Sender");
        // --- !!! UPDATE THIS with your email !!! ---
        sender.setEmail("shrirang.amle22@gmail.com");
        sender.setDob(LocalDate.now()); // Add dummy data for any other required fields

        Customer receiver = new Customer();
        receiver.setName("Test Receiver");
        // --- !!! UPDATE THIS with a second email you can check !!! ---
        receiver.setEmail("shrirang.amle2004@gmail.com");
        receiver.setDob(LocalDate.now());

        Transaction transaction = new Transaction();
        transaction.setTransactionId(12345L);
        transaction.setTransactionAmount(new BigDecimal("100.00"));
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setReceiverDetails("Test Receiver Account");
        transaction.setSenderDetails("Test Sender Account");
        transaction.setDescription("Email functionality test");

        try {
            // 3. Call the method
            notificationService.sendTransferNotifications(sender, receiver, transaction);

            System.out.println("Test email function executed.");
            System.out.println("Check your inbox (and spam folder) for the test emails.");

        } catch (Exception e) {
            System.err.println("An error occurred during the email test:");
            e.printStackTrace();
        }
    }
}