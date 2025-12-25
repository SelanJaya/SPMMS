package servlets;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
import DAO.UserDAO;
import DAO.ProjectDAO;
import beans.User;
import beans.Project;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author HP
 */
public class dashboardServlet extends HttpServlet {

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
            out.println("<title>Servlet dashboardServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet dashboardServlet at " + request.getContextPath() + "</h1>");
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
        int user_id;
        List<Project> profileInfo;
        User user;

        HttpSession session = request.getSession();
        user_id = (int) session.getAttribute("userId");

        UserDAO userDao = new UserDAO();
        user = userDao.profileInformation(user_id);
        session.setAttribute("user", user);

        ProjectDAO projectDao = new ProjectDAO();
        profileInfo = projectDao.projectInfo(user_id);
        session.setAttribute("profileInfo", profileInfo);

        // Print a header to find it easily in the logs
        System.out.println("=== DEBUG: Project List Start ===");

        if (profileInfo != null && !profileInfo.isEmpty()) {
            for (Project p : profileInfo) {
                System.out.println("User ID: " + user_id);
                System.out.println("ID: " + p.getProjectId());
                System.out.println("Name: " + p.getProjectName());
                System.out.println("Status: " + p.getProjectStatus());
                System.out.println("Dates: " + p.getProjStartDate() + " to " + p.getProjEndDate());
                System.out.println("-----------------------------");
            }
        } else {
            System.out.println("User ID: " + user_id);
            System.out.println("No projects found for user: " + user.getUser_id());
        }

        System.out.println("=== DEBUG: Project List End ===");

        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
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

        String ProjName, ProjDesc, ProjStatus;
        int user_id;
        List<Project> profileInfo;
        boolean status;
        LocalDate ProjStart, ProjEnd;

        HttpSession session = request.getSession();
        user_id = (int) session.getAttribute("userId");

        ProjName = request.getParameter("ProjName");
        ProjDesc = request.getParameter("ProjDesc");
        ProjStatus = request.getParameter("ProjStatus");
        ProjStart = LocalDate.parse(request.getParameter("ProjStart"));
        ProjEnd = LocalDate.parse(request.getParameter("ProjEnd"));

        Project project = new Project(user_id, ProjName, ProjDesc, ProjStatus, ProjStart, ProjEnd);

        ProjectDAO projectDao = new ProjectDAO();
        status = projectDao.createProject(project);

        if (status == true) {

            profileInfo = projectDao.projectInfo(user_id);
//            session.setAttribute("profileInfo", profileInfo);
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
        } else {
            System.out.println("error");
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
