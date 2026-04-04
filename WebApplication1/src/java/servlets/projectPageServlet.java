package servlets;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import beans.Project;
import beans.User;
import DAO.ProjectDAO;
import DAO.ProjectTeamDAO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.servlet.http.HttpSession;

/**
 *
 * @author HP
 */
public class projectPageServlet extends HttpServlet {

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
            out.println("<title>Servlet projectPageServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet projectPageServlet at " + request.getContextPath() + "</h1>");
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
        String ProIdParam;
        int projectId, userId;
        boolean status;
        Project project;

        ProIdParam = request.getParameter("projectId");
        projectId = (ProIdParam != null && !ProIdParam.isEmpty()) ? Integer.parseInt(ProIdParam) : -1;

        ProjectDAO projectDao = new ProjectDAO();

        //DELETE PROJECT FOLDER
        if ("deleteFolder".equalsIgnoreCase(request.getParameter("processType"))) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            try {
                status = projectDao.deleteProject(projectId);

                if (status) {
                    response.getWriter().write("{\"success\": true, \"message\": \"Project deleted successfully.\"}");
                } else {
                    // Case where DAO returned false (e.g., ID not found)
                    response.getWriter().write("{\"success\": false, \"message\": \"Database error: Project record could not be removed.\"}");
                }
            } catch (Exception e) {
                // Case where a hard error occurred (e.g., Folder is locked/Open in another program)
                response.getWriter().write("{\"success\": false, \"message\": \"Server Error: " + e.getMessage() + "\"}");
            }
            return;
        }

        HttpSession session = request.getSession();

        // Retrieve all project details save in session as caching
        project = projectDao.ProjectInfoById(projectId);
        session.setAttribute("project", project);

//        userId = (int)session.getAttribute("userId");

//        projectTeamDAO projectTeamDao = new projectTeamDAO();
//        projectTeamAssignment = projectTeamDao.getAssignedMembers(projectId);
//
//        for (User member : projectTeamAssignment) {
//            System.out.println("User: " + member.getUsername() + " | Email: " + member.getEmail() + " | Role: " + member.getUser_role());
//        }

//        session.setAttribute("projectTeamAssignmentData", projectTeamAssignment);
        request.getRequestDispatcher("projectPage.jsp").forward(request, response);

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

        int projectId, user_id;
        List<Project> projectInfo;
        boolean status;
        String projectName, projectDesc, projectStatus, projectType, processType, projClient, startStr, endStr, projDateStr;
        LocalDate projStartDate, projEndDate;
        LocalDateTime projDate = null;

        processType = request.getParameter("processType");

        projectId = Integer.parseInt(request.getParameter("projectId")); //Shared Parameter
//        projectStatus = request.getParameter("projectStatus"); //Shared Parameter

        ProjectDAO projectDao = new ProjectDAO();

        if ("projectInfoUpdate".equalsIgnoreCase(processType)) {
            //profile Update process
            try {
                projectName = request.getParameter("projectName");
                projectDesc = request.getParameter("projectDesc");
                projectType = request.getParameter("projectType");
                projClient = request.getParameter("projClient");

                // Safety check for dates
                startStr = request.getParameter("projStartDate");
                endStr = request.getParameter("projEndDate");
                projDateStr = request.getParameter("projDate");
                projStartDate = (startStr != null && !startStr.isEmpty()) ? LocalDate.parse(startStr) : null;
                projEndDate = (endStr != null && !endStr.isEmpty()) ? LocalDate.parse(endStr) : null;
                
                if (projDateStr != null && !projDateStr.isEmpty()) {
                    // 1. Replace all whitespace (tabs, double spaces, etc) with one single space
                    String normalized = projDateStr.trim().replaceAll("\\s+", " ");

                    // 2. Parse using the standard single-space pattern
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy MM dd HH:mm");
                    projDate = LocalDateTime.parse(normalized, formatter);
                }

                Project project = new Project(projectId, projectName, projectDesc, "Active", projectType, projClient, projStartDate, projEndDate, projDate);

                status = projectDao.updateProject(project);

                if (status == true) {
                    HttpSession session = request.getSession();

                    user_id = (int) session.getAttribute("userId");
//                    projectInfo = projectDao.projectInfo(user_id);
//                    session.setAttribute("projectInfo", projectInfo);

                    session.setAttribute("project", project);
                    session.setAttribute("processStatus", status);

                    request.getRequestDispatcher("projectPage.jsp").forward(request, response);
                } else {
                    System.out.println("Error }}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}");
                    request.setAttribute("errorMsg", "Database update failed.");
                    request.getRequestDispatcher("projectPage.jsp").forward(request, response);
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("error.jsp");
            }
        }
//         else if ("updateStatus".equalsIgnoreCase(processType)) {
//
//            // 1. Set the response type to JSON
//            response.setContentType("application/json");
//            response.setCharacterEncoding("UTF-8");
//
//            String message = "";
//
//            try {
//                status = projectDao.updateProjectStatus(projectId);
//
//                message = status ? "Project restored successfully!" : "Restore failed.";
//
//            } catch (Exception e) {
//                status = false;
//                message = "Error: " + e.getMessage();
//            }
//
//            // 2. Create the JSON string
//            // Format: {"success": true, "message": "..."}
//            String jsonResponse = String.format("{\"success\": %b, \"message\": \"%s\"}", status, message);
//
//            PrintWriter out = response.getWriter();
//            // 3. Send it back to the JS AJAX call
//            out.print(jsonResponse);
//            out.flush();
//            return;
//        }
    }

//    @Override
//    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//
//        String ProIdParam = request.getParameter("projectId");
//        int projectId = (ProIdParam != null && !ProIdParam.isEmpty()) ? Integer.parseInt(ProIdParam) : -1;
//
//        ProjectDAO projectDao = new ProjectDAO();
//        boolean status;
//        String jsonResponse;
//
//        try {
//            status = projectDao.deleteProject(projectId);
//            if (status) {
//                jsonResponse = "{\"success\": true, \"message\": \"Project deleted successfully.\"}";
//            } else {
//                jsonResponse = "{\"success\": false, \"message\": \"Database error: Project record could not be removed.\"}";
//            }
//        } catch (Exception e) {
//            jsonResponse = "{\"success\": false, \"message\": \"Server Error: " + e.getMessage() + "\"}";
//        }
//
//        response.getWriter().write(jsonResponse);
//        response.getWriter().flush();
//    }
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
