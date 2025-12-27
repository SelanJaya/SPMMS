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
import beans.User;
import DAO.UserDAO;
import javax.servlet.http.HttpSession;

/**
 *
 * @author HP
 */
public class login_signUpServlet extends HttpServlet {

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
            out.println("<title>Servlet login_signUpServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet login_signUpServlet at " + request.getContextPath() + "</h1>");
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
  
        HttpSession session = request.getSession();
        
        String processType = request.getParameter("processType");
        
        if (processType.equalsIgnoreCase("logOut")) {
            session.invalidate();
            response.sendRedirect("login.jsp");
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

        String processType, username, phone_number, email, role, password, confirmPassword;
        int statusUserID;
        
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        processType = request.getParameter("processType");
        
        if (processType.equalsIgnoreCase("login")) { //LOG IN

            email = request.getParameter("email");
            password = request.getParameter("password");

            User user = new User(email, password);

            UserDAO userDao = new UserDAO();
            statusUserID = userDao.login(user);

            if (statusUserID > 0) {
                session.setAttribute("userId", statusUserID);
                // Store the specific Login Time
                long loginMillis = System.currentTimeMillis();
                session.setAttribute("loginTime", loginMillis);
                
                response.sendRedirect("dashboardServlet?&processType=projectInfo");
            } else {
                request.setAttribute("errorMessage", "Invalid Credential");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }

        } else if ("signUp".equalsIgnoreCase(processType)) {    // For Sign UP
           
            username = request.getParameter("username");
            phone_number = request.getParameter("phone_number");
            email = request.getParameter("email");
            role = request.getParameter("role");
            password = request.getParameter("password");
            confirmPassword = request.getParameter("confirmPassword");

            User user = new User(username, email, phone_number, password, role);

            UserDAO projectDao = new UserDAO();
            statusUserID = projectDao.signUp(user);
            
            if (statusUserID > 0) {
                
                session.setAttribute("userId", statusUserID);
                response.sendRedirect("dashboardServlet");
            }
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
