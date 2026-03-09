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
import beans.Backlog;
import beans.SprintBacklogLink;
import DAO.SprintBacklogLinkDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.el.lang.ELSupport;

/**
 *
 * @author HP
 */
public class SprintBacklogLinkServlet extends HttpServlet {

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
            out.println("<title>Servlet SprintBacklogLinkServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SprintBacklogLinkServlet at " + request.getContextPath() + "</h1>");
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
        processRequest(request, response);
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

        String action = (String) request.getAttribute("action");
        SprintBacklogLink sBLink = (SprintBacklogLink) request.getAttribute("SprintBacklogLinkData");
        
        SprintBacklogLinkDAO sBLDao = new SprintBacklogLinkDAO();
         List<Backlog> backlogArr = new ArrayList<>();
        if ("Add".equalsIgnoreCase(action)) {
            
            
            for (Integer item : sBLink.getBacklog_item_id()) {
                try {

                    System.out.println("sprint_id :" + sBLink.getSprint_id() + " " + item);
                    sBLDao.addSprintBacklogLink(sBLink.getSprint_id(), item);
                    backlogArr.add(sBLDao.getLinkedBacklog(item));
                    
                } catch (Exception e) {
                    throw new ServletException(
                            "Database error", e);
                }
            }
            // ✅ pass back to parent servlet
            request.setAttribute("BacklogData", backlogArr);
        } else if("Update".equalsIgnoreCase(action)){
            
            try {
               backlogArr = sBLDao.updateBacklog(sBLink);
                
            } catch (Exception e) {
                throw new ServletException(
                            "Database error", e);
                }
            }
        
           // ✅ pass back to parent servlet
            request.setAttribute("BacklogData", backlogArr);
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
