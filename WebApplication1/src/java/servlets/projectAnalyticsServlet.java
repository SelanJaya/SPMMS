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
import Service.ProjectAnalyticsService;
import java.util.HashMap;
import java.util.Map;
import beans.ProjectAnalytics;
import beans.Sprint;
import DAO.SprintDAO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HP
 */
public class projectAnalyticsServlet extends HttpServlet {

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
            out.println("<title>Servlet projectAnalytics</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet projectAnalytics at " + request.getContextPath() + "</h1>");
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

        System.out.println("Servlet reached");
        String action = request.getParameter("action");
        Integer project_id = request.getParameter("project_id") != null ? Integer.parseInt(request.getParameter("project_id")) : null;
        Map<String, Object> result = new HashMap();

        Gson gson = new Gson();

         gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class,
                        new JsonSerializer<LocalDate>() {
                    @Override
                    public JsonElement serialize(
                            LocalDate src,
                            Type typeOfSrc,
                            JsonSerializationContext context
                    ) {
                        return new JsonPrimitive(src.toString());
                    }
                })
                .create();

        try {
            if ("redirect".equalsIgnoreCase(action)) {
                request.setAttribute("project_id", project_id);
                request.getRequestDispatcher("projectAnalytics.jsp").forward(request, response);
            } else if ("fetchInsight".equalsIgnoreCase(action)) {

                ProjectAnalytics projectAnalytics = new ProjectAnalytics();

                ProjectAnalyticsService projectAnalyticsService = new ProjectAnalyticsService();
                projectAnalytics = projectAnalyticsService.getProjectAnalytics_All(project_id);

                result.put("analyticsData", projectAnalytics);
            } else if ("fetchSprintBurndown".equalsIgnoreCase(action)) {

                SprintDAO sprintDAO = new SprintDAO();

                List<Sprint> sprintData = new ArrayList();
                sprintData = sprintDAO.getSprintName_Task(project_id);
                result.put("sprintData", sprintData);
            } else if ("fetchBurnDownData".equalsIgnoreCase(action)) {

                Integer sprint_id = request.getParameter("sprint_id") != null ? Integer.parseInt(request.getParameter("sprint_id")) : null;

                SprintDAO sprintDAO = new SprintDAO();

                ProjectAnalyticsService projectAnalyticsService = new ProjectAnalyticsService();

                List<ProjectAnalytics> projectAnalyticsArr = new ArrayList<>();
                projectAnalyticsArr = projectAnalyticsService.calculateBurnDownChart(sprint_id, project_id);
                result.put("analyticsData", projectAnalyticsArr);
            }
            result.put("status", "Success");
            response.getWriter().write(gson.toJson(result));
        } catch (Exception e) {
            System.out.println("Exception occur in the analytics servlet : " + e);
            e.printStackTrace();
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
