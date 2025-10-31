package service;

import jakarta.mail.Session;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Handles the technical logic of connecting to an SMTP server
 * and sending an email.
 */
public class EmailService {

    private final Properties properties;
    private final String username;
    private final String password;
    private final String fromEmail; // The "from" address from properties
    private final jakarta.mail.Session session;

    public EmailService() {
        this.properties = loadProperties();
        this.username = properties.getProperty("email.sender.username");
        this.password = properties.getProperty("email.sender.password");
        this.fromEmail = properties.getProperty("email.sender.from"); // Get the "from" address

        // Create a mail session
        this.session = Session.getInstance(properties, new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // For SendGrid, username is "apikey" and password is the API key
                return new PasswordAuthentication(username, password);
            }
        });
    }

    /**
     * Loads properties from the application.properties file.
     */
    private Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (in == null) {
                System.err.println("application.properties not found in classpath.");
                throw new RuntimeException("application.properties not found");
            }
            props.load(in);

            // Filter for email properties only
            Properties emailProps = new Properties();
            emailProps.put("mail.smtp.host", props.getProperty("email.smtp.host"));
            emailProps.put("mail.smtp.port", props.getProperty("email.smtp.port"));
            emailProps.put("mail.smtp.auth", props.getProperty("email.smtp.auth"));
            emailProps.put("mail.smtp.starttls.enable", props.getProperty("email.smtp.starttls.enable"));

            // Store credentials and "from" address
            emailProps.put("email.sender.username", props.getProperty("email.sender.username"));
            emailProps.put("email.sender.password", props.getProperty("email.sender.password"));
            emailProps.put("email.sender.from", props.getProperty("email.sender.from"));

            return emailProps;

        } catch (IOException e) {
            throw new RuntimeException("Failed to load email properties", e);
        }
    }

    /**
     * The actual method that sends the email.
     *
     * @param toEmail The recipient's email address.
     * @param subject The subject line.
     * @param body    The email content.
     */
    public void sendEmail(String toEmail, String subject, String body) throws MessagingException {
        // Create a new MimeMessage object
        Message message = new MimeMessage(session);

        // Set the sender's email address (from properties)
        message.setFrom(new InternetAddress(fromEmail));

        // Set the recipient's email address
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));

        // Set the email subject
        message.setSubject(subject);

        // Set the email body
        message.setText(body);

        // Send the email
        Transport.send(message);

        System.out.println("Email sent successfully to " + toEmail);
    }
}