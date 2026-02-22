package servlets;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import beans.Backlog;
import DAO.BacklogDAO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import javax.servlet.http.HttpSession;

/**
 *
 * @author HP
 */
public class BacklogServlet extends HttpServlet {

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
            out.println("<title>Servlet backlog</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet backlog at " + request.getContextPath() + "</h1>");
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

        Gson gson = new Gson();

        try {
            BacklogDAO backlogDAO = new BacklogDAO();

            System.out.println("Action TYPE " + action + "\n");

            if ("fetchData".equalsIgnoreCase(action)) {
                int project_id = Integer.parseInt(request.getParameter("project_id"));

                List<Backlog> backlogArr = new ArrayList<>();

                backlogArr = backlogDAO.getBacklogItem(project_id);

                Map<String, Object> backlogData = new HashMap<>();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                backlogData.put("data", backlogArr);
                backlogData.put("status", "Success");

                response.getWriter().write(gson.toJson(backlogData));
            } else if ("redirect".equalsIgnoreCase(action)) {

                request.getRequestDispatcher("backlog.jsp").forward(request, response);
            } else if ("Delete".equalsIgnoreCase(action)) {

                int backlog_id = Integer.parseInt(request.getParameter("backlogId"));

                backlogDAO.deleteBacklogItem(backlog_id);

                Map<String, Object> backlogData = new HashMap<>();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                backlogData.put("status", "Success");

                response.getWriter().write(gson.toJson(backlogData));
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            Map<String, Object> backlogData = new HashMap<>();
            backlogData.put("status", "Failed");
            response.getWriter().write(gson.toJson(backlogData));
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

        int generatedKey;

        //create Gson object to map JSON content to bean variable
        Gson gson = new Gson();

        try {
            Backlog backlog = null;
            List<Backlog> orderList = null;

            // create reader object 
            BufferedReader reader = request.getReader();
            StringBuilder jsonBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            String json = jsonBuilder.toString();
            System.out.println("RAW JSON" + json);
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

            String action = jsonObject.get("action").getAsString();

            System.out.println("Action : " + action);

            if (!"Reorder".equalsIgnoreCase(action)) {
                //Convert the Json strings to bean variable
                backlog = gson.fromJson(json, Backlog.class);
            } else if ("Reorder".equalsIgnoreCase(action)) {

                Type listType = new TypeToken<List<Backlog>>() {
                }.getType();
                orderList = gson.fromJson(jsonObject.get("order"), listType);
            }

            System.out.println("Object mapped \n");

//            if (backlog.getBacklogI_title() == null) {
//                System.out.println("TITLE ID NULLL MYYGOOOODDDDDDDD");
//            }

            // System.out.println("Backlog ID: " + backlog.getBacklogI_id() + "\n");
//            System.out.println("Backlog Title: " + backlog.getBacklogI_title() + "\n");
            BacklogDAO backlogDAO = new BacklogDAO();

            Map<String, Object> result = new HashMap<>();

            if ("Create".equalsIgnoreCase(action)) {
                // insert the Backlog data
                generatedKey = backlogDAO.insertBacklogItem(backlog);

                if (generatedKey > -1) {
                    result.put("key", generatedKey);
                } else {
                    throw new Exception();
                }
            } else if ("Update".equalsIgnoreCase(action)) {
                //Update
                backlogDAO.updateBacklogItem(backlog);
            } else if ("Reorder".equalsIgnoreCase(action)) {
                for (Backlog item : orderList) {
                    backlogDAO.reorderBacklogItem(
                    item.getBacklogI_priority(),
                    item.getBacklogI_id());

                }
            } else {
                System.out.println("WRONG CODE \n");
            }

            result.put("status", "Success");
            response.getWriter().write(gson.toJson(result));

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            System.out.println(e);
            Map<String, Object> result = new HashMap<>();
            result.put("status", "Failed");

            response.getWriter().write(gson.toJson(result));
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

//    private Exception Exception(String wrong_code) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//    }
}
