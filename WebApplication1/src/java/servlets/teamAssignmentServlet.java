/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import DAO.UserDAO;
import DAO.projectTeamDAO;
import beans.Project;
import beans.User;
import beans.ProjectTeamAssignment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;

/**
 *
 * @author HP
 */
public class teamAssignmentServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet projectAssignmentServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet projectAssignmentServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter(); // Get this AFTER setting content type

        String URLRole, decodedRole, role;

        List<User> users = new ArrayList<>();

        UserDAO userDao = new UserDAO();

        // To carry (Box) the List<User> and status - reduce the number of HTTP response
        Map<String, Object> jsonResponse = new HashMap<>();

        try {
            URLRole = request.getParameter("roleType");

            // URL decoding converts %20 back into a real space
            decodedRole = java.net.URLDecoder.decode(URLRole, "UTF-8");

            System.out.println("Cleaned Role for DB: [" + decodedRole + "]");

            // Check if parameter is missing or empty
            if (decodedRole == null || decodedRole.trim().isEmpty()) {

                response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Error 400

                jsonResponse.put("success", false);
                jsonResponse.put("message", "Role parameter is required.");
            } else {
                users = userDao.getUsersByRole(decodedRole);

                if (users == null) {
                    users = new ArrayList<>(); // Return empty list instead of null
                }

                System.out.println("======= START DAO CONTENT LOG =======");
                if (users != null && !users.isEmpty()) {
                    for (User u : users) {
                        // This prints the specific fields of each User object
                        System.out.println("ID: " + u.getUser_id()
                                + " | Name: " + u.getUsername()
                                + " | Email: " + u.getEmail());
                    }
                } else {
                    System.out.println("No data inside the list.");
                }
                System.out.println("======== END DAO CONTENT LOG ========");

                jsonResponse.put("success", true);
                jsonResponse.put("data", users);
                jsonResponse.put("count", users.size());

            }
        } catch (Throwable t) {

            System.err.println("!!! FATAL ERROR CAUGHT !!!");
            t.printStackTrace(); // This WILL print even if a library is missing

            System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            jsonResponse.put("success", false);
//            jsonResponse.put("message", "Internal Server Error: " + e.getMessage());
        } finally {
            try {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                // 1. Use GsonBuilder to exclude "problem" fields or complex internals
                // This prevents the "Failed making field accessible" error
                Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .create();

                // 2. Generate the JSON
                String jsonOutput = gson.toJson(jsonResponse);

                System.out.println("DEBUG - FINAL JSON CARGO: " + jsonOutput);

                out.print(jsonOutput);
                out.flush();
            } catch (Exception e) {
                System.err.println("JSON Error: " + e.getMessage());
                e.printStackTrace();
            }
        }

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("ENTERED SERVELEET YAAY");

        int projectId, assignedTo, assignedBy;
        String processType;
        boolean status = false;
        List<User> projectTeamAssignment;
        Project projectObj;

        assignedTo = Integer.parseInt(request.getParameter("userId"));
        HttpSession session = request.getSession();

        processType = request.getParameter("processType");

        projectTeamDAO projectTeamDao = new projectTeamDAO();

        assignedBy = (int) session.getAttribute("userId");

        projectObj = (Project) session.getAttribute("project");
        projectId = projectObj.getProjectId();

        if (projectObj != null) {
            projectId = (int) projectObj.getProjectId();
        } else {
            // Optional: Log that the project was missing
            System.out.println("Warning: Attempted to get ID from a null Project object.");
        }

        ProjectTeamAssignment projectTeamAssigmnet = new ProjectTeamAssignment(projectId, assignedTo, assignedBy);
        System.out.println("--- Debug: Project Assignment Logic ---");
        System.out.println("Process Type: " + processType);
        System.out.println("Assigned By (User ID): " + assignedBy);
        System.out.println("Project Object: " + projectObj);
        System.out.println("Project ID: " + projectId);

        if ("teamAssignment".equalsIgnoreCase(processType)) {

            status = projectTeamDao.assignTeamMember(projectTeamAssigmnet);
        } else if ("deleteProcess".equalsIgnoreCase(processType)) {

            System.out.println(" PROJECT ID :" + projectId);
            System.out.println("ASSIGNED USER ID " + assignedTo);
            System.out.println("ASSIGNED USER ID " + assignedBy);

            status = projectTeamDao.removeTeamMember(projectTeamAssigmnet);
        }

        if (status == true) {
            projectTeamAssignment = projectTeamDao.getAssignedMembers(projectId);

            session.setAttribute("projectTeamAssignmentData", projectTeamAssignment);
            response.sendRedirect("teamMembersPage.jsp");
            System.out.println("PROCESSSS " + status);
        }

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
