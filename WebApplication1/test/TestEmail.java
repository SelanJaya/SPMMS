
import Service.EmailService;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author HP
 */
public class TestEmail {

    public static void main(String[] args) {

        EmailService emailService = new EmailService();

        emailService.sendEmail(
                "s71298@ocean.umt.edu.my",
                "SPMMS Recruitment Test",
                "This is a test email from SPMMS."
        );
    }

}
