/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import DAO.ProjectTeamDAO;
import DAO.TaskAssignmentDAO;
import DAO.ProjectDAO;
import DAO.UserDAO;
import beans.TaskAssignment;
import Service.EmailService;
import beans.ProjectTeamAssignment;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author HP
 */
public class TeamAssignmentService {

    private static final ExecutorService emailExecutor = Executors.newFixedThreadPool(5);

    public void recruitementService(ProjectTeamAssignment projectTeamAssignment) throws Exception {

        try {

            String subject = "Project Recruitment Notification";
            
            ProjectTeamDAO projectTeamDAO = new ProjectTeamDAO();
            
            //logic to assignment 
            Boolean assignmentStatus = projectTeamDAO.findProjectAssignment(projectTeamAssignment.getProject_id(), projectTeamAssignment.getAssign_to());
            
            if (assignmentStatus) {
                projectTeamDAO.reactivateProjectAssignment(projectTeamAssignment.getProject_id(), projectTeamAssignment.getAssign_to());
            } else if (!assignmentStatus){
                 projectTeamDAO.assignTeamMember(projectTeamAssignment);
            } else {
                throw new Error("Insertion Failed");
            }
            
            
            
            ProjectDAO projectDAO = new ProjectDAO();
            String projectName = projectDAO.getProjectNameById(projectTeamAssignment.getProject_id());

            UserDAO userDAO = new UserDAO();
            String assign_by_username = userDAO.getUsernameById(projectTeamAssignment.getAssign_by());

            String message
                    = "Dear " + projectTeamAssignment.getAssign_to_username() + ",\n\n"
                    + "Congratulations! You have been successfully recruited to join the project \""
                    + projectName + "\" under the supervision of "
                    + assign_by_username + ".\n\n"
                    + "We are pleased to welcome you to the project team. "
                    + "Further details regarding your responsibilities and project activities "
                    + "will be communicated by the Project Manager.\n\n"
                    + "Best regards,\n"
                    + assign_by_username;

            // Send email asynchronously
//            emailExecutor.submit(() -> {
//                try {
//                    EmailService emailService = new EmailService();
//                    emailService.sendEmail(
//                            projectTeamAssignment.getAsign_to_Email(),
//                            subject,
//                            message
//                    );
//                    System.out.println(
//                            "Removal email sent to: "
//                            + projectTeamAssignment.getAsign_to_Email()
//                    );
//
//                } catch (Exception e) {
//                    System.err.println(
//                            "Failed to send removal email to: "
//                            + projectTeamAssignment.getAsign_to_Email()
//                    );
//                    e.printStackTrace();
//                }
//            });

            //EmailService emailService = new EmailService( projectTeamAssignment.getAsign_to_Email(), );
            //emailService.sendEmail(taskAssignment., subject, messageText);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception occured in recruitementService :" + e);
            throw e;
        }
    }

    public void removeMemberService(ProjectTeamAssignment projectTeamAssignment) throws Exception {

        try {

            ProjectTeamDAO projectTeamDao = new ProjectTeamDAO();
            projectTeamDao.removeTeamMember(projectTeamAssignment);

            String subject = "Project Membership Removal Notification";

            ProjectDAO projectDAO = new ProjectDAO();
            String projectName = projectDAO.getProjectNameById(projectTeamAssignment.getProject_id());

            UserDAO userDAO = new UserDAO();
            String assign_by_username = userDAO.getUsernameById(projectTeamAssignment.getAssign_by());

            String message
                    = "Dear " + projectTeamAssignment.getAssign_to_username() + ",\n\n"
                    + "This email is to inform you that you have been removed from the project \""
                    + projectName + "\" by "
                    + assign_by_username + ".\n\n"
                    + "Your access and participation in this project have been discontinued effective immediately. "
                    + "If you believe this action was made in error or require further clarification, "
                    + "please contact the Project Manager.\n\n"
                    + "Thank you for your contributions to the project.\n\n"
                    + "Best regards,\n"
                    + assign_by_username;

            // Send email asynchronously
//            emailExecutor.submit(() -> {
//                try {
//                    EmailService emailService = new EmailService();
//
//                    emailService.sendEmail(
//                            projectTeamAssignment.getAsign_to_Email(),
//                            subject,
//                            message
//                    );
//
//                    System.out.println(
//                            "Removal email sent to: "
//                            + projectTeamAssignment.getAsign_to_Email()
//                    );
//
//                } catch (Exception e) {
//                    System.err.println(
//                            "Failed to send removal email to: "
//                            + projectTeamAssignment.getAsign_to_Email()
//                    );
//                    e.printStackTrace();
//                }
//            });

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception occured in removeMemberService : " + e);
            throw e;
        }
    }
}
