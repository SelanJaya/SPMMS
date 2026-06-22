/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import DAO.ProjectDAO;
import DAO.ProjectTeamDAO;
import DAO.UserDAO;
import DAO.TaskDAO;
import DAO.ProjectAnalyticsDAO;
import Service.DashboardInsightsService;
import beans.DashboardInsight;
import beans.Project;
import beans.ProjectTeamAssignment;
import beans.RiskPredictionResponse;
import beans.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author HP
 */
public class DashboardServlet extends HttpServlet {

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
            out.println("<title>Servlet DashboardServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DashboardServlet at " + request.getContextPath() + "</h1>");
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
        System.out.println("Process type : " + processType + "\n");

        HttpSession session = request.getSession();
        user = (User) session.getAttribute("userInfo");

        Map<String, Object> result = new HashMap<>();
        System.out.println("USer ROLE" + user.getUser_id());

        Gson gson = new Gson();

        ProjectDAO projectDao = new ProjectDAO();
        UserDAO userDao = new UserDAO();

        try {
            if ("redirect".equalsIgnoreCase(processType)) {
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
            } else if ("projectInfo".equalsIgnoreCase(processType)) {
                //List<RiskPredictionResponse> predictions = new ArrayList<>();
                //DashboardInsightsService dashboardInsightsService = new DashboardInsightsService();
                //dashboardInsightsService.ProjectRisk_MLService(user.getUser_id());

                // result.put("riskScore", predictions);
                if ("Project Manager".equalsIgnoreCase(user.getUser_role())) {
                    // DISPLAY ALL PROJECT FOLDER
                    profileInfo = projectDao.projectInfo(user.getUser_id());
                } else {
                    profileInfo = projectDao.getProjectsByNonPMUserId(user.getUser_id());
                }

                result.put("profileInfo", profileInfo);
                user = userDao.profileInformation(user.getUser_id());
                session.setAttribute("user", user);

                //request.getRequestDispatcher("dashboard.jsp").forward(request, response);
            } else if ("achivedProject".equalsIgnoreCase(processType)) {
                System.out.println("Archived Project");
                System.out.println("user _id : " + user.getUser_id());

                profileInfo = projectDao.getArchivedProjectsByUserId(user.getUser_id());

                result.put("profileInfo", profileInfo);
            } else if ("dashboardInsights".equalsIgnoreCase(processType)) {
                user_id = Integer.parseInt(request.getParameter("user_id"));
                String userRole = request.getParameter("userRole");
                System.out.println("User Role : " + userRole);
                List<DashboardInsight> dashboardInsightsArr = new ArrayList<>();

//             ProjectDAO projectDAO = new ProjectDAO();
//             dashboardInsightsArr = projectDAO.getMYActiveProject(user_id);
                DashboardInsight dashboardInsight = new DashboardInsight();
                DashboardInsightsService dashboardInsightsService = new DashboardInsightsService();

                if ("Project Manager".equalsIgnoreCase(userRole)) {
                    System.out.println("PM executed");
                    dashboardInsight = dashboardInsightsService.getDashboardInsightService_PM(user_id, userRole);

                    ProjectAnalyticsDAO projectAnalyticsDAO = new ProjectAnalyticsDAO();
                    dashboardInsight.setActivitys(projectAnalyticsDAO.getRecentActivities_PM(user_id));

                } else if ("Product Owner".equalsIgnoreCase(userRole) || "Scrum Master".equalsIgnoreCase(userRole)) {
                    //My Active Project
                    System.out.println("PO Executed");
                    ProjectDAO projectDAO = new ProjectDAO();
                    dashboardInsight.setActiveProjects(projectDAO.getMYActiveProject_PO(user_id));
                    System.out.println("AAAA " + dashboardInsight.getActiveProjects().getFirst());
                    
                    if("Scrum Master".equalsIgnoreCase(userRole)){
                        ProjectAnalyticsDAO projectAnalyticsDAO = new ProjectAnalyticsDAO();
                        dashboardInsight.setActivitys(projectAnalyticsDAO.getRecentActivities_SM(user_id));
                    }

                } else if ("Developer".equalsIgnoreCase(userRole)) {
                    TaskDAO taskDAO = new TaskDAO();
                    dashboardInsight.setActiveTasks(taskDAO.getMyActiveTask(user_id));

                    ProjectAnalyticsDAO projectAnalyticsDAO = new ProjectAnalyticsDAO();
                    dashboardInsight.setActivitys(projectAnalyticsDAO.getRecentActivities_Dev(user_id));
                }
                result.put("DashboardInsightData", dashboardInsight);
            }
            result.put("status", "Success");
        } catch (Exception e) {
            result.put("status", "fail");
            System.out.println("Exception ocuured at dashboardServlet : " + e);
            e.printStackTrace();
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
        int user_id;
        int created_key;
        //boolean assigned = false;
        Map<String, Object> result = new HashMap<>();

        //RETRIEVE PROJECT INFO
        HttpSession session = request.getSession();
        user_id = (int) session.getAttribute("userId");
        System.out.println("USer Id" + user_id);

        BufferedReader reader = request.getReader();
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }

        String json = jsonBuilder.toString();
        System.out.println("RAW JSON" + json);
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

        Gson gson = new Gson();

        ProjectDAO projectDao = new ProjectDAO();
        try {
            // Create Obj and INSERT proj data
            Project project = gson.fromJson(json, Project.class);

            created_key = projectDao.createProject(project);

            //Craete obj and INSERT assignment project Manager role (auto)
            ProjectTeamAssignment projectTeamAssignmentObj = new ProjectTeamAssignment(created_key, user_id, user_id);
            ProjectTeamDAO projectTeamDao = new ProjectTeamDAO();

            projectTeamDao.assignTeamMember(projectTeamAssignmentObj);
            result.put("status", "Success");
            result.put("processMsg", "Project Successfully Created");

        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", "Failed");
            result.put("processMsg", e.getMessage());
            System.out.println(e);

        }

        response.getWriter().write(gson.toJson(result));

        //response.getWriter().write(gson.toJson(result));
//        if (assigned) {
//            profileInfo = projectDao.projectInfo(user_id);
        //session.setAttribute("processStatus", assigned);
        //session.setAttribute("profileInfo", profileInfo);
//            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
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
