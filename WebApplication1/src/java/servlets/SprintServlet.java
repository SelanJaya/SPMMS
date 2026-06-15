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
import beans.SprintBacklogLink;
import DAO.BacklogDAO;
import DAO.SprintDAO;
import Service.SprintService;
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

        String action = request.getParameter("action") != null
                ? (String) request.getParameter("action")
                : null;

        Map<String, Object> result = new HashMap<>();
        Gson gson = new Gson();

        try {

            if ("redirect".equalsIgnoreCase(action)) {
                int project_id = request.getParameter("project_id") != null
                        ? Integer.parseInt(request.getParameter("project_id"))
                        : null;

                request.setAttribute("project_id", project_id);
                request.getRequestDispatcher("sprint.jsp").forward(request, response);
            } else if ("fetchSprint".equalsIgnoreCase(action)) {

                List<Sprint> sprintList = new ArrayList<>();

                int project_Id = Integer.parseInt(request.getParameter("project_id"));
                SprintService sprintService = new SprintService();
                sprintList = sprintService.getCheckSprintValidity(project_Id);

                result.put("SprintData", sprintList);
            } else if ("fetchBacklog".equalsIgnoreCase(action)) {

                String mode = request.getParameter("mode");
                List<Backlog> backlogList = new ArrayList<>();
                if ("Create".equalsIgnoreCase(mode)) {

                    int project_Id = Integer.parseInt(request.getParameter("project_id"));
                    BacklogDAO backlogDao = new BacklogDAO();
                    backlogList = backlogDao.getHighPriorityBacklog(project_Id);

                } else if ("Edit".equalsIgnoreCase(mode)) {

                    int project_Id = Integer.parseInt(request.getParameter("project_id"));
                    int sprint_id = Integer.parseInt(request.getParameter("sprint_id"));

                    BacklogDAO backlogDao = new BacklogDAO();
                    backlogList = backlogDao.getHighPriorityBacklog_Edit(project_Id, sprint_id);

                }

                result.put("backlogData", backlogList);
            }  else if("fetchSprint_backlog".equalsIgnoreCase(action)){
                List<Backlog> backlogList = new ArrayList<>();
                
                int sprint_id = Integer.parseInt(request.getParameter("sprint_id"));
                BacklogDAO backlogDAO = new BacklogDAO();
                
                backlogList = backlogDAO.getBacklog_assoSprint(sprint_id);
                result.put("AssosiatedBacklog", backlogList);
            }
            result.put("status", "Success");
        } catch (Exception e) {
            System.out.println("Exception Occurs" + e);
        }
        response.getWriter().write(gson.toJson(result));
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

        int project_Id, sprint_id;
        ArrayList<Integer> link_backlog_item_id;

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
            //JSON Object to read the json varible
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

            String action = jsonObject.get("action").getAsString();

            Map<String, Object> result = new HashMap<>();

            Sprint sprint = new Sprint();

            if ("Insert".equalsIgnoreCase(action) || "Update".equalsIgnoreCase(action)) {
                sprint = gson.fromJson(json, Sprint.class);
            }

            if ("Insert".equalsIgnoreCase(action)) {

                //Mappes data to SprintBacklogLink
                SprintBacklogLink spl = new SprintBacklogLink();
                spl = gson.fromJson(json, SprintBacklogLink.class);

                SprintDAO sprintDao = new SprintDAO();
                sprint_id = sprintDao.insertSprintDetails(sprint);

                //ADD sprint ID in the obj spl before send to SprintBacklogLinkServlet
                spl.setSprint_id(sprint_id);

                request.setAttribute("action", "Add");
                request.setAttribute("SprintBacklogLinkData", spl);
                request.getRequestDispatcher("SprintBacklogLinkServlet").include(request, response);

                List<Backlog> backlogArr = (List<Backlog>) request.getAttribute("BacklogData");
                result.put("backlogData", backlogArr);
                result.put("sprint_id", sprint_id);
                result.put("message", "Sprint added successfully");
                

            } else if ("Update".equalsIgnoreCase(action)) {

                SprintDAO sprintDao = new SprintDAO();
                sprintDao.updateSprintDetails(sprint);

                System.out.println("Backlog : " + sprint.getBacklog());

                SprintBacklogLink spl = new SprintBacklogLink();
                spl = gson.fromJson(json, SprintBacklogLink.class);

                request.setAttribute("action", "Update");
                request.setAttribute("SprintBacklogLinkData", spl);
                request.getRequestDispatcher("SprintBacklogLinkServlet").include(request, response);

                List<Backlog> backlogArr = (List<Backlog>) request.getAttribute("BacklogData");
                result.put("backlogData", backlogArr);
                result.put("message", "Sprint updated successfully");

            } else if ("Delete".equalsIgnoreCase(action)) {
                sprint_id = jsonObject.get("sprint_id").getAsInt();
                SprintDAO sprintDAO = new SprintDAO();
                sprintDAO.deleteSprintDetails(sprint_id);
                result.put("message", "Sprint deleted successfully");
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
