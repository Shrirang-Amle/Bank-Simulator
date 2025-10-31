package api;

import model.Customer;
import model.Transaction;
import service.NotificationService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/test")
public class TestEmailResource {

    private final NotificationService notificationService;

    public TestEmailResource() {
        this.notificationService = new NotificationService();
    }

    @GET
    @Path("/email")
    @Produces(MediaType.TEXT_PLAIN)
    public Response sendTestEmail() {
        System.out.println("Test email endpoint triggered...");

        try {
            Customer sender = new Customer();
            sender.setName("Test Sender");
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
            transaction.setDescription("Email test from Postman");

            // 2. Call the method
            notificationService.sendTransferNotifications(sender, receiver, transaction);

            String result = "Email test triggered! Check your inbox (and spam) for the test emails.";
            System.out.println(result);
            return Response.ok(result).build();

        } catch (Exception e) {
            String errorMsg = "An error occurred during the email test: " + e.getMessage();
            System.err.println(errorMsg);
            e.printStackTrace();
            return Response.status(500).entity(errorMsg).build();
        }
    }

    public NotificationService getNotificationService() {
        return notificationService;
    }
}