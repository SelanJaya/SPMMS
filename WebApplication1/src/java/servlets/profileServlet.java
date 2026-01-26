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
import beans.User;
import javax.servlet.http.HttpSession;

/**
 *
 * @author HP
 */
public class profileServlet extends HttpServlet {

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
            out.println("<title>Servlet profileServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet profileServlet at " + request.getContextPath() + "</h1>");
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
//        int user_id;
//        User user;
//
//        HttpSession session = request.getSession();
//        user_id = (int) session.getAttribute("userId");
//
//        UserDAO userDao = new UserDAO();
//        user = userDao.profileInformation(user_id);
//        session.setAttribute("user", user);
        request.getRequestDispatcher("profile.jsp").forward(request, response);
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

        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();

        String processType, username, phone_number, email, user_role, newPassword, confirmPassword;
        int user_id;
        User user;
        boolean status = false;

        processType = request.getParameter("processType");

        if (processType.equalsIgnoreCase("editProfile")) {
            user_id = (int) session.getAttribute("userId");
            username = request.getParameter("username");
            phone_number = request.getParameter("phone_number");
            email = request.getParameter("email");
            user_role = request.getParameter("user_role");
            newPassword = request.getParameter("newPassword");
            
            user = new User(user_id, username, email, phone_number, newPassword, user_role);
            
            UserDAO userDao = new UserDAO();
            
            if(!newPassword.isEmpty()){
                confirmPassword = request.getParameter("confirmPassword");
                if (newPassword.equals(confirmPassword)) {
                   status = userDao.updateProfileWithPassword(user);
                }
            }else{
                status = userDao.updateProfile(user);   
            }
            

            if (status == true) {
                session.setAttribute("user", user);
                request.getRequestDispatcher("profile.jsp").forward(request, response);
            }
        } else if (processType.equalsIgnoreCase("deleteProfile")) {

            // 1. Safety Check: Is the user actually logged in?
            if (session != null && session.getAttribute("userId") != null) {

                user_id = (int) session.getAttribute("userId");
                UserDAO userDao = new UserDAO();
                status = userDao.deleteProfile(user_id);

                // 2. If successfully deleted, log them out immediately
                if (status) {
                    session.invalidate();
                }

                response.setContentType("text/plain");
                out.print(status); // Sends "true" or "false"
                out.flush();

            } else {
                // Session expired or not found
                response.setContentType("text/plain");
                out.print("false");
                out.flush();
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
