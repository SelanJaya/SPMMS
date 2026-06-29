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
import DAO.ProjectTeamDAO;
import beans.Project;
import beans.User;
import beans.ProjectTeamAssignment;
import Service.TeamAssignmentService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
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

        System.out.println("teamAssignmentServlet Get reporting");

        String action = (String) request.getParameter("action");
        System.out.println(action);
        Gson gson = new Gson();
        // To carry(Box) the List<User> and status - reduce the number of HTTP response
        Map<String, Object> jsonResponse = new HashMap<>();

        try {
            if ("redirect".equalsIgnoreCase(action)) {
                int project_id = Integer.parseInt(request.getParameter("project_id"));
                request.setAttribute("project_id", project_id);

                request.getRequestDispatcher("teamMembersPage.jsp").forward(request, response);
            } else if ("fetchTeamAssignment".equalsIgnoreCase(action)) {
                List<User> projectTeamAssignmentArr;

                int projectId = Integer.parseInt(request.getParameter("project_id"));

                ProjectTeamDAO projectTeamDao = new ProjectTeamDAO();
                projectTeamAssignmentArr = projectTeamDao.getAssignedMembers(projectId);

                for (User member : projectTeamAssignmentArr) {
                    System.out.println("User: " + member.getUsername() + " | Email: " + member.getEmail() + " | Role: " + member.getUser_role());
                }

//               request.setAttribute("projectTeamAssignmentData", projectTeamAssignmentArr);
                jsonResponse.put("userAssignedData", projectTeamAssignmentArr);
                System.out.println("in teamAssignment Servlet" + projectTeamAssignmentArr);
//                request.getRequestDispatcher("teamMembersPage.jsp").forward(request, response);
            } else if ("fetchUsers".equalsIgnoreCase(action)) {

                String roleType = request.getParameter("roleType");
                List<User> users = new ArrayList<>();

                UserDAO userDAO = new UserDAO();
                users = userDAO.getUsersByRole(roleType);

                jsonResponse.put("userData", users);
            }
            jsonResponse.put("status", "Success");
        } catch (Exception e) {
            jsonResponse.put("status", "Failed");
            System.out.println("Exception OCuurs : " + e);
            e.printStackTrace();
        }

        response.getWriter().write(gson.toJson(jsonResponse));
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

//        int projectId, assignedTo, assignedBy;
//        String processType;
//        boolean status = false;
//        List<User> projectTeamAssignmentArr;
//        Project projectObj;
//        assignedTo = Integer.parseInt(request.getParameter("userId"));
//        HttpSession session = request.getSession();
//
//        processType = request.getParameter("processType");
//
        ProjectTeamDAO projectTeamDao = new ProjectTeamDAO();
//
//        assignedBy = (int) session.getAttribute("userId");
//
//        projectObj = (Project) session.getAttribute("project");
//        projectId = projectObj.getProjectId();

        Map<String, Object> result = new HashMap<>();
        Gson gson = new Gson();
        try {

            // create reader object 
            BufferedReader reader = request.getReader();
            StringBuilder jsonBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            String json = jsonBuilder.toString();
            System.out.println("RAW JSON" + json);
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

            String action = jsonObject.get("action").getAsString();

            System.out.println("Action : " + action);

            if ("teamAssignment".equalsIgnoreCase(action)) {

                ProjectTeamAssignment projectTeamAssignment = gson.fromJson(json, ProjectTeamAssignment.class);

                TeamAssignmentService teamAssignmentService = new TeamAssignmentService();
                teamAssignmentService.recruitementService(projectTeamAssignment);
                result.put("message", "Team Assignment Successful");
//               projectTeamDao.assignTeamMember(projectTeamAssignment);
            } else if ("delete".equalsIgnoreCase(action)) {

                ProjectTeamAssignment projectTeamAssignment = gson.fromJson(json, ProjectTeamAssignment.class);

                TeamAssignmentService teamAssignmentService = new TeamAssignmentService();
                teamAssignmentService.removeMemberService(projectTeamAssignment);
                result.put("message", "Team Assignment Deletion Successful");
            }
            result.put("status", "Success");
        } catch (Exception e) {
            System.out.println("Exception occur :" + e);
            e.printStackTrace();
            result.put("message", e.getMessage());
            result.put("status", "Failed");
        }

        response.getWriter().write(gson.toJson(result));

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
