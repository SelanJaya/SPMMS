/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author HP
 */
public class EmailService {

    private String senderEmail;
    private String senderPassword;

    public void sendEmail(String recipientEmail, String subject, String messageText) {

        try {
            
            Properties config = new Properties();

            InputStream input = getClass()
                    .getClassLoader()
                    .getResourceAsStream("Service/Email.properties");

            config.load(input);

            senderEmail = config.getProperty("email");
            senderPassword = config.getProperty("password");

            Properties props = new Properties();

            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(props,
                    new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                            senderEmail,
                            senderPassword
                    );
                }
            });

            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(senderEmail));

            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recipientEmail)
            );

            message.setSubject(subject);

            message.setText(messageText);

            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
