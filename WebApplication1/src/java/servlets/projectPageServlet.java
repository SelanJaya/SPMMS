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
        int projectId;
        Project project;

        projectId = Integer.parseInt(request.getParameter("projectId"));

        ProjectDAO projectDao = new ProjectDAO();
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

        int projectId;
        boolean status;
        String projectName, projectDesc, projectStatus, startStr, endStr;
        LocalDate projStartDate, projEndDate;

        try {
            projectId = Integer.parseInt(request.getParameter("projectId"));
            projectName = request.getParameter("projectName");
            projectDesc = request.getParameter("projectDesc");
            projectStatus = request.getParameter("projectStatus");

            // Safety check for dates
            startStr = request.getParameter("projStartDate");
            endStr = request.getParameter("projEndDate");
            projStartDate = (startStr != null && !startStr.isEmpty()) ? LocalDate.parse(startStr) : null;
            projEndDate = (endStr != null && !endStr.isEmpty()) ? LocalDate.parse(endStr) : null;

            Project project = new Project(projectId, projectName, projectDesc, projectStatus, projStartDate, projEndDate);

            System.out.println("---------- PROJECT  Update DEBUG INFO ----------");
            System.out.println("ID         : " + project.getProjectId());
            System.out.println("Name       : " + project.getProjectName());
            System.out.println("Description: " + project.getProjectDesc());
            System.out.println("Status     : " + project.getProjectStatus());
            System.out.println("Start Date : " + project.getProjStartDate());
            System.out.println("End Date   : " + project.getProjEndDate());
            System.out.println("Created By : " + project.getProjCreatedBy());
            System.out.println("----------------------------------------");

            ProjectDAO projectDao = new ProjectDAO();
            status = projectDao.updateProject(project);

            if (status == true) {
                HttpSession session = request.getSession();
                session.setAttribute("project", project);
                session.setAttribute("successMsg", "Project updated successfully!");

                request.getRequestDispatcher("projectPage.jep").forward(request, response);
            } else {
                request.setAttribute("errorMsg", "Database update failed.");
                request.getRequestDispatcher("projectPage.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
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
