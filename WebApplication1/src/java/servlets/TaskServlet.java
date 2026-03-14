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
import DAO.TaskDAO;
import beans.User;
import beans.Task;
import beans.TaskAssignment;
import Service.TaskServices;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;

/**
 *
 * @author HP
 */
public class TaskServlet extends HttpServlet {

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
            out.println("<title>Servlet TaskServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet TaskServlet at " + request.getContextPath() + "</h1>");
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

        String action = request.getParameter("action");
//        System.out.println("Action in Task " + action);
        Gson gson = new Gson();

        Map<String, Object> result = new HashMap<>();

        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            if ("fetch_user".equalsIgnoreCase(action)) {

                List<User> userList = new ArrayList<>();

                String user_role = request.getParameter("user_role");
                int project_id = Integer.parseInt(request.getParameter("project_id"));

//                System.out.println("Role : " + user_role);

                UserDAO userDao = new UserDAO();
                userList = userDao.getUsersByRole(user_role, project_id);

                System.out.println("User List" + userList);
                userList.forEach(item -> {
                    System.out.println("User ID: " + item.getUser_id());
                });

                result.put("userData", userList);
            } else if ("fetchTasks_Sprint".equalsIgnoreCase(action)) {

                //System.out.println("Start fetchTask");
                List<Task> tasksArr = new ArrayList<>();

                int sprint_id = Integer.parseInt(request.getParameter("sprint_id"));

                TaskDAO taskDao = new TaskDAO();
                tasksArr = taskDao.getTasksBySprintId(sprint_id);

                result.put("tasks", tasksArr);

            } else if ("fetchTask_Edit".equalsIgnoreCase(action)) {
                int task_id = Integer.parseInt(request.getParameter("task_id"));
                TaskDAO taskDAO = new TaskDAO();
                Task task = taskDAO.getTaskByTaskId(task_id);

                result.put("taskData", task);
            } else if ("fetchTask_dependency".equalsIgnoreCase(action)) {
                int sprint_id = Integer.parseInt(request.getParameter("sprint_id"));

                String task_idStr = request.getParameter("task_id");
                Integer task_id = task_idStr != null ? Integer.valueOf(task_idStr) : null;

                TaskDAO taskDAO = new TaskDAO();
                List<Task> taskArr = null;
                if (task_id == null) {
                    taskArr = taskDAO.getLiteTasksBySprintID(sprint_id);
                } else {
                    taskArr = taskDAO.getTask_edit(sprint_id, task_id);
                }

                result.put("taskData", taskArr);
            }

            result.put("status", "Success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", "Failed " + e);

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

        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<>();

        System.out.println("Executed Task");
        try {
            BufferedReader reader = request.getReader();
            StringBuilder jsonBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            String json = jsonBuilder.toString();
            System.out.println("Incoming JSON: " + json);
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

            String action = jsonObject.has("action") ? jsonObject.get("action").getAsString() : "";

            System.out.println("action " + action);

            //Response Initialisation
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            if ("Insert".equalsIgnoreCase(action)) {
                System.out.println("Task Inserted");
                Task task = gson.fromJson(json, Task.class);

                // System.out.println("Dependency received: " + task.getTask_dependency());

//                int task_id = taskDao.insertTask(task);
                TaskAssignment taskAssignment = gson.fromJson(json, TaskAssignment.class);
                task.setTaskAssignment(taskAssignment);
                
                Type type = new TypeToken<List<Integer>>(){}.getType();
                List<Integer> dependencyArr = gson.fromJson(jsonObject.get("taskDepedencies"),type);
                
                TaskServices taskServices = new TaskServices();
                int task_id = taskServices.insertTaskDetails_Assignment(task, dependencyArr);

//                request.setAttribute("action", "Insert");
//                request.setAttribute("taskAssignment", taskAssignment);
//                request.getRequestDispatcher("/TaskAssignmentServlet").include(request, response);
                System.out.println("task Id return " + task_id);
                result.put("task_id", task_id);
                result.put("status", "Success");
            } else if ("updateTaskStatus".equalsIgnoreCase(action)) {

                int task_id = jsonObject.get("task_id").getAsInt();
                String task_status = jsonObject.get("task_status").getAsString();

                TaskDAO taskDao = new TaskDAO();
                taskDao.updateTaskStatus(task_id, task_status);

                result.put("status", "Success");
            } else if ("UpdateTaskDetials".equalsIgnoreCase(action)) {

                Task task = gson.fromJson(json, Task.class);

                TaskAssignment taskAssignment = gson.fromJson(json, TaskAssignment.class);
                task.setTaskAssignment(taskAssignment);
                
                Type type = new TypeToken<List<Integer>>(){}.getType();
                List<Integer> dependencyArr = gson.fromJson(jsonObject.get("taskDepedencies"),type);

                TaskServices taskServices = new TaskServices();
                taskServices.updateTaskDetails_Assignment(task, dependencyArr);
                result.put("task_id", task.getTask_id());
                result.put("status", "Success");
            } else if ("deleteTask".equalsIgnoreCase(action)) {
                int task_id = jsonObject.get("task_id").getAsInt();

                TaskServices taskServices = new TaskServices();
                taskServices.deleteTaskDetails_Assignment(task_id);

                result.put("status", "Success");
            }

        } catch (Exception e) {
            System.out.println("Exception in task : " + e);
            result.put("status", "Failed" + e);
        }

        response.getWriter().write(gson.toJson(result));
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
