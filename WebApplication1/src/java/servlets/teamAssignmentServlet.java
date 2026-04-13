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

//                String URLRole, decodedRole, role;
//
//                List<User> users = new ArrayList<>();
//
//                UserDAO userDao = new UserDAO();
//
//                try {
//                    
//                    URLRole = request.getParameter("roleType");
//
//                    // URL decoding converts %20 back into a real space
//                    decodedRole = java.net.URLDecoder.decode(URLRole, "UTF-8");
//
//                    System.out.println("Cleaned Role for DB: [" + decodedRole + "]");
//
//                    // Check if parameter is missing or empty
//                    if (decodedRole == null || decodedRole.trim().isEmpty()) {
//
//                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Error 400
//
//                        jsonResponse.put("success", false);
//                        jsonResponse.put("message", "Role parameter is required.");
//                    } else {
//                        users = userDao.getUsersByRole(decodedRole);
//
//                        if (users == null) {
//                            users = new ArrayList<>(); // Return empty list instead of null
//                        }
//
//                        System.out.println("======= START DAO CONTENT LOG =======");
//                        if (users != null && !users.isEmpty()) {
//                            for (User u : users) {
//                                // This prints the specific fields of each User object
//                                System.out.println("ID: " + u.getUser_id()
//                                        + " | Name: " + u.getUsername()
//                                        + " | Email: " + u.getEmail());
//                            }
//                        } else {
//                            System.out.println("No data inside the list.");
//                        }
//                        System.out.println("======== END DAO CONTENT LOG ========");
//
//                        jsonResponse.put("success", true);
//                        jsonResponse.put("data", users);
//                        jsonResponse.put("count", users.size());
//
//                    }
//
//                } catch (Throwable t) {
//
//                    System.err.println("!!! FATAL ERROR CAUGHT !!!");
//                    t.printStackTrace(); // This WILL print even if a library is missing
//
//                    System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//                    jsonResponse.put("success", false);
            
            ////            jsonResponse.put("message", "Internal Server Error: " + e.getMessage());
//                } finally {
//                    try {
//                        response.setContentType("application/json");
//                        response.setCharacterEncoding("UTF-8");
//
//                        // 1. Use GsonBuilder to exclude "problem" fields or complex internals
//                        // This prevents the "Failed making field accessible" error
//                        gson = new GsonBuilder()
//                                .setPrettyPrinting()
//                                .create();
//
//                        // 2. Generate the JSON
//                        String jsonOutput = gson.toJson(jsonResponse);
//
//                        System.out.println("DEBUG - FINAL JSON CARGO: " + jsonOutput);
//
//                        out.print(jsonOutput);
//                        out.flush();
//                    } catch (Exception e) {
//                        System.err.println("JSON Error: " + e.getMessage());
//                        e.printStackTrace();
//                    }
//                }
//                return;

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

        int projectId, assignedTo, assignedBy;
        String processType;
        boolean status = false;
        List<User> projectTeamAssignmentArr;
        Project projectObj;

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
        Map<String, Object> result =  new HashMap<>();
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

                projectTeamDao.assignTeamMember(projectTeamAssignment);
                
            } else if ("delete".equalsIgnoreCase(action)) {

                ProjectTeamAssignment projectTeamAssignment = gson.fromJson(json, ProjectTeamAssignment.class);

                System.out.println(" PROJECT ID :" + projectTeamAssignment.getProject_id());
                System.out.println("ASSIGNED USER ID " + projectTeamAssignment.getAssign_to());
                System.out.println("ASSIGNED USER ID " + projectTeamAssignment.getAssign_by());

                projectTeamDao.removeTeamMember(projectTeamAssignment);
            }
            result.put("status", "Success");
        } catch (Exception e) {
            System.out.println("Exception occur :" + e);
            e.printStackTrace();
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
