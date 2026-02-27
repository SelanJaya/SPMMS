/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import beans.Backlog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import beans.Sprint;
import DAO.BacklogDAO;
import DAO.SprintDAO;
import java.time.LocalTime;

/**
 *
 * @author HP
 */
public class SprintServlet extends HttpServlet {

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
            out.println("<title>Servlet SprintServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SprintServlet at " + request.getContextPath() + "</h1>");
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

        System.out.println("GET TRIGGERED");
        String action = request.getParameter("action") != null
                ? (String) request.getParameter("action")
                : null;
        try {

            int project_id = request.getParameter("project_id") != null
                    ? Integer.parseInt(request.getParameter("project_id"))
                    : null;

            System.out.println(action + " " + project_id);

            request.setAttribute("projectId", project_id);
            request.getRequestDispatcher("sprint.jsp").forward(request, response);
        } catch (Exception e) {
            System.out.println("Exception Occurs" + e);
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
        System.out.println("POST TIGERT : YES \n");

        int project_Id;

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

            //DEBUG 
            System.out.println("RAW JSON" + json);

            //JSON Object to read the json varible
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

            String action = jsonObject.get("action").getAsString();

            System.out.println("Action : " + action);

            Map<String, Object> result = new HashMap<>();

            Sprint sprint = new Sprint();

            if ("Insert".equalsIgnoreCase(action) || "Update".equalsIgnoreCase(action)) {
                sprint = gson.fromJson(json, Sprint.class);
            }

            if ("fetchBacklog".equalsIgnoreCase(action)) {
                List<Backlog> backlogList = new ArrayList<>();

                project_Id = jsonObject.get("project_id").getAsInt();

                BacklogDAO backlogDao = new BacklogDAO();
                backlogList = backlogDao.getHighPriorityBacklog(project_Id);
                System.out.println("DATA FROM DAO: " + backlogList);
                result.put("backlogData", backlogList);

            } else if ("fetchSprint".equalsIgnoreCase(action)) {
                List<Sprint> sprintList = new ArrayList<>();

                project_Id = jsonObject.get("project_id").getAsInt();
                SprintDAO sprintDao = new SprintDAO();
                sprintList = sprintDao.getSprintsData(project_Id);

                if (sprintList.isEmpty()) {
                    throw new IllegalStateException("Sprint list is empty");
                }

                result.put("SprintData", sprintList);
            } else if ("Insert".equalsIgnoreCase(action)) {

                SprintDAO sprintDao = new SprintDAO();
                int sprint_id = sprintDao.insertSprintDetails(sprint);

                result.put("sprint_id", sprint_id);

            } else if ("Update".equalsIgnoreCase(action)) {
                SprintDAO sprintDao = new SprintDAO();
                sprintDao.updateSprintDetails(sprint);

            } else if ("Delete".equalsIgnoreCase(action)) {
                int sprint_id = jsonObject.get("sprint_id").getAsInt();
                SprintDAO sprintDAO = new SprintDAO();
                sprintDAO.deleteSprintDetails(sprint_id);
            }
            result.put("status", "Success");
            response.getWriter().write(gson.toJson(result));
        } catch (Exception e) {
            System.out.println("Excepton Occur" + e);
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
