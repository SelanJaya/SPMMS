package servlets;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
import DAO.UserDAO;
import DAO.ProjectDAO;
import DAO.projectTeamDAO;
import beans.User;
import beans.Project;
import beans.ProjectTeamAssignment;
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
        String processType;
        List<Project> profileInfo;
        User user;

        processType = request.getParameter("processType");

        HttpSession session = request.getSession();
        user = (User) session.getAttribute("userInfo");



        ProjectDAO projectDao = new ProjectDAO();
        UserDAO userDao = new UserDAO();

        if ("projectInfo".equalsIgnoreCase(processType)) {

            if ("Project Manager".equalsIgnoreCase(user.getUser_role())) {
                // DISPLAY ALL PROJECT FOLDER
                profileInfo = projectDao.projectInfo(user.getUser_id());
                session.setAttribute("profileInfo", profileInfo);
            } else {

                profileInfo = projectDao.getProjectsByNonPMUserId(user.getUser_id());
                session.setAttribute("profileInfo", profileInfo);
            }

            //GET PROFILE INFO
            user = userDao.profileInformation(user.getUser_id());
            session.setAttribute("user", user);

            request.getRequestDispatcher("dashboard.jsp").forward(request, response);

        } else if ("achivedProject".equalsIgnoreCase(processType)) {

            profileInfo = projectDao.getArchivedProjectsByUserId(user.getUser_id());

            session.setAttribute("profileInfo", profileInfo);
            request.getRequestDispatcher("projectArchive.jsp").forward(request, response);
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

        String ProjName, ProjDesc, ProjType, ProjClient;
        int user_id;
        List<Project> profileInfo;
        int created_key;
        boolean assigned = false;
        LocalDate ProjStart, ProjEnd;

        //RETRIEVE PROJECT INFO
        HttpSession session = request.getSession();
        user_id = (int) session.getAttribute("userId");

        System.out.println("USER ID  " + user_id);

        ProjName = request.getParameter("ProjName");
        ProjDesc = request.getParameter("ProjDesc");
        ProjType = request.getParameter("ProjType");
        ProjClient = request.getParameter("ProjClient");
        ProjStart = LocalDate.parse(request.getParameter("ProjStart"));
        ProjEnd = LocalDate.parse(request.getParameter("ProjEnd"));

        ProjectDAO projectDao = new ProjectDAO();

        try {
            // Create Obj and INSERT proj data
            Project project = new Project(ProjName, ProjDesc, "Active", ProjType, ProjClient, ProjStart, ProjEnd, user_id);
            created_key = projectDao.createProject(project);

            //Craete obj and INSERT assignment project Manager role (auto)
            ProjectTeamAssignment projectTeamAssignmentObj = new ProjectTeamAssignment(created_key, user_id, user_id);
            projectTeamDAO projectTeamDao = new projectTeamDAO();

            assigned = projectTeamDao.assignTeamMember(projectTeamAssignmentObj);

        } catch (Exception e) {
            System.out.println(e);
        }

        if (assigned) {
            profileInfo = projectDao.projectInfo(user_id);
            session.setAttribute("processStatus", assigned);
            session.setAttribute("profileInfo", profileInfo);
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
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
