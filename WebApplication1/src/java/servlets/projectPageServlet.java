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
import beans.Project;
import beans.User;
import DAO.ProjectDAO;
import DAO.ProjectTeamDAO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.io.BufferedReader;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;

/**
 *
 * @author HP
 */
public class ProjectPageServlet extends HttpServlet {

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
            out.println("<title>Servlet projectPageServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet projectPageServlet at " + request.getContextPath() + "</h1>");
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
        String ProIdParam, action;
        int project_id, userId;
        boolean status;
        Project project;

        action = request.getParameter("action") != null ? request.getParameter("action") : null;

        System.out.println("Action in GET : " + action);
        ProIdParam = request.getParameter("project_id");
        project_id = (ProIdParam != null && !ProIdParam.isEmpty()) ? Integer.parseInt(ProIdParam) : -1;

        ProjectDAO projectDao = new ProjectDAO();

        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<>();

        try {

            if ("redirect".equalsIgnoreCase(action)) {
                request.setAttribute("project_id", project_id);
                request.getRequestDispatcher("projectPage.jsp").forward(request, response);
            } //DELETE PROJECT FOLDER
//            else if ("deleteFolder".equalsIgnoreCase(request.getParameter("processType"))) {
//                response.setContentType("application/json");
//                response.setCharacterEncoding("UTF-8");
//
//                try {
//                    status = projectDao.deleteProject(project_id);
//
//                    if (status) {
//                        response.getWriter().write("{\"success\": true, \"message\": \"Project deleted successfully.\"}");
//                    } else {
//                        // Case where DAO returned false (e.g., ID not found)
//                        response.getWriter().write("{\"success\": false, \"message\": \"Database error: Project record could not be removed.\"}");
//                    }
//                } catch (Exception e) {
//                    // Case where a hard error occurred (e.g., Folder is locked/Open in another program)
//                    response.getWriter().write("{\"success\": false, \"message\": \"Server Error: " + e.getMessage() + "\"}");
//                }
//                return;
//            }
            else if ("fetchProjectinfo".equalsIgnoreCase(action)) {
                System.out.println("Project Id :" + project_id);
                project = projectDao.ProjectInfoById(project_id);

                gson = new GsonBuilder()
                        .registerTypeAdapter(LocalDateTime.class,
                                (JsonSerializer<LocalDateTime>) (src, type, ctx)
                                -> new JsonPrimitive(src.toString()))
                        .create();

                System.out.println("Porject data retrieve : " + project);

                result.put("projectData", project);
                result.put("status", "Success");
            }
        } catch (Exception e) {
            result.put("status", "failed");
            System.out.println("Excption ocurrs : " + e);
            e.printStackTrace();

        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(gson.toJson(result));

        //HttpSession session = request.getSession();
//        // Retrieve all project details save in session as caching
//        project = projectDao.ProjectInfoById(projectId);
//        session.setAttribute("project", project);
//        userId = (int)session.getAttribute("userId");
//        projectTeamDAO projectTeamDao = new projectTeamDAO();
//        projectTeamAssignment = projectTeamDao.getAssignedMembers(projectId);
//
//        session.setAttribute("projectTeamAssignmentData", projectTeamAssignment);
//        request.getRequestDispatcher("projectPage.jsp").forward(request, response);
//        
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
        System.out.println("Project Servlet Report");

        int projectId, user_id;
        List<Project> projectInfo;
        boolean status;
        String action;
        Project project;


        ProjectDAO projectDao = new ProjectDAO();

        Gson gson = new Gson();

        Map<String, Object> result = new HashMap<>();
        try {

            gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
                        public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
                            return new JsonPrimitive(src.toString());
                        }
                    })
                    .create();
            
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

            action = jsonObject.get("action").getAsString();
            System.out.println("Action  :" + action);
            
            if ("projectInfoUpdate".equalsIgnoreCase(action)) {

                project = gson.fromJson(json, Project.class);
                System.out.println("Project data : " + project.getProjectName());

                projectDao.updateProject(project);

                result.put("status", "Success");
                result.put("message", "Project Update Successful");
            }
            else if ("delete".equalsIgnoreCase(action)) {
                projectId = jsonObject.get("projectId").getAsInt();
                System.out.println("project ID " + projectId);
                projectDao.deleteProject(projectId);
                System.out.println("Deletion success");
                result.put("status", "Success");
            } else if("restoreArchivedProject".equalsIgnoreCase(action)){
                project = gson.fromJson(json, Project.class);
                projectDao.updateProjectEndData(project);
                result.put("status", "Success");
            }
        } catch (Exception e) {
            System.out.println("Error occur : " + e);
            e.printStackTrace();
            result.put("status", "failed");
            result.put("message", e.getMessage());
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
