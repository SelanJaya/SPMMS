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
import DAO.ProjectDAO;
import java.time.LocalDate;
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
        int projectId;
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

        // Retrieve project details
        project = projectDao.ProjectInfoById(projectId);

        HttpSession session = request.getSession();
        session.setAttribute("project", project);

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
        String processType;
        List<Project> profileInfo;
        boolean status;
        String projectName, projectDesc, projectStatus, startStr, endStr;
        LocalDate projStartDate, projEndDate;

        processType = request.getParameter("processType");

        projectId = Integer.parseInt(request.getParameter("projectId")); //Shared Parameter
        projectStatus = request.getParameter("projectStatus"); //Shared Parameter

        ProjectDAO projectDao = new ProjectDAO();

        if ("projectInfoUpdate".equalsIgnoreCase(processType)) {
            //profile Update process
            try {
                projectName = request.getParameter("projectName");
                projectDesc = request.getParameter("projectDesc");

                // Safety check for dates
                startStr = request.getParameter("projStartDate");
                endStr = request.getParameter("projEndDate");
                projStartDate = (startStr != null && !startStr.isEmpty()) ? LocalDate.parse(startStr) : null;
                projEndDate = (endStr != null && !endStr.isEmpty()) ? LocalDate.parse(endStr) : null;

                Project project = new Project(projectId, projectName, projectDesc, projectStatus, projStartDate, projEndDate);

                status = projectDao.updateProject(project);

                if (status == true) {
                    HttpSession session = request.getSession();

                    if (projectStatus.equalsIgnoreCase("Archive")) {
                        user_id = (int) session.getAttribute("userId");
                        profileInfo = projectDao.projectInfo(user_id);
                        session.setAttribute("profileInfo", profileInfo);
                    }

                    session.setAttribute("project", project);
                    session.setAttribute("successMsg", "Project updated successfully!");

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
        } else if ("updateStatus".equalsIgnoreCase(processType)) {

            // 1. Set the response type to JSON
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            PrintWriter out = response.getWriter();
            String feedbackMessage = "";

            try {
                status = projectDao.updateProjectStatus(projectId, projectStatus);

                feedbackMessage = "True";

            } catch (Exception e) {
                status = false;
                feedbackMessage = "Error: " + e.getMessage();
            }

            // 2. Create the JSON string
            // Format: {"success": true, "message": "..."}
            String jsonResponse = String.format("{\"success\": %b, \"message\": \"%s\"}",
                    status, feedbackMessage);

            // 3. Send it back to the JS AJAX call
            out.print(jsonResponse);
            out.flush();
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
