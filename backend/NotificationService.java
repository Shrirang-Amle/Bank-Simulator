package service;

import model.Customer;
import model.Transaction;

import jakarta.mail.MessagingException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

/**
 * A service dedicated to sending notifications, like emails or SMS.
 */
public class NotificationService {

        // --- ADD THIS ---
        // Create an instance of the new EmailService
        private final EmailService emailService;

        public NotificationService() {
                this.emailService = new EmailService();
        }
        // ---------------

        public void sendTransferNotifications(Customer sender, Customer receiver, Transaction transaction) {
                // Formatter for display
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedTime = transaction.getTransactionTime().format(formatter);
                BigDecimal amount = transaction.getTransactionAmount();

                // --- Email to Sender ---
                String senderEmail = sender.getEmail();
                String senderSubject = "Transaction Alert: Debit from your account";
                String senderBody = String.format(
                                "Dear %s,\n\n" +
                                                "This is to confirm that your account has been debited with an amount of %.2f on %s.\n\n"
                                                +
                                                "Details:\n" +
                                                "- Amount: %.2f\n" +
                                                "- To Account: %s\n" +
                                                "- Transaction ID: %d\n" +
                                                "- Description: %s\n\n" +
                                                "Thank you for banking with us.",
                                sender.getName(), amount, formattedTime, amount, transaction.getReceiverDetails(),
                                transaction.getTransactionId(), transaction.getDescription());

                // This will now send a real email
                sendEmail(senderEmail, senderSubject, senderBody);

                // --- Email to Receiver ---
                String receiverEmail = receiver.getEmail();
                String receiverSubject = "Transaction Alert: Credit to your account";
                String receiverBody = String.format(
                                "Dear %s,\n\n" +
                                                "This is to confirm that your account has been credited with an amount of %.2f on %s.\n\n"
                                                +
                                                "Details:\n" +
                                                "- Amount: %.2f\n" +
                                                "- From Account: %s\n" +
                                                "- Transaction ID: %d\n" +
                                                "- Description: %s\n\n" +
                                                "Thank you for banking with us.",
                                receiver.getName(), amount, formattedTime, amount, transaction.getSenderDetails(),
                                transaction.getTransactionId(), transaction.getDescription());

                // This will also send a real email
                sendEmail(receiverEmail, receiverSubject, receiverBody);
        }

        /**
         * --- THIS IS THE UPDATED METHOD ---
         * It now calls the real EmailService instead of printing to console.
         * * @param toEmail The recipient's email address.
         * 
         * @param subject The subject of the email.
         * @param body    The content of the email.
         */
        private void sendEmail(String toEmail, String subject, String body) {
                try {
                        // Call the real email service
                        emailService.sendEmail(toEmail, subject, body);

                } catch (MessagingException e) {
                        // Log the error. In a real app, you might add this to a retry queue.
                        System.err.println("Failed to send email to " + toEmail + ": " + e.getMessage());
                        e.printStackTrace();
                }
        }
}