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
import beans.Document;
import Service.BacklogDocumentService;
import com.google.gson.Gson;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Part;
import DAO.BacklogDocumentDAO;
import DAO.DocumentDAO;
import Service.ProjectDocumentService;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HP
 */
@MultipartConfig
public class BacklogDocumentServlet extends HttpServlet {

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
            out.println("<title>Servlet BacklogDocumentServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet BacklogDocumentServlet at " + request.getContextPath() + "</h1>");
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
        System.out.println("BacklogDcoumentServletReporting");
        List<Document> listDocuments = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();

        Gson gson = new Gson();

        String action = request.getParameter("action");
        System.out.println(action);

        try {
            if ("fetchDocument".equalsIgnoreCase(action)) {
                int backlog_id = Integer.parseInt(request.getParameter("backlogItem_id"));
                System.out.println(backlog_id);
                BacklogDocumentDAO backlogDocumentDAO = new BacklogDocumentDAO();
                listDocuments = backlogDocumentDAO.getBacklogDocuments(backlog_id);

                result.put("documentData", listDocuments);
                result.put("status", "Success");
            } else if ("fetchDocumentData".equalsIgnoreCase(action)) {

                int document_id = Integer.parseInt(request.getParameter("document_id"));
                System.out.println("Document id : " + document_id);
                DocumentDAO documentDAO = new DocumentDAO();
                Document document = documentDAO.getDocumentDataEdit(document_id);

                System.out.println("Document name : " + document.getDocument_name());
                result.put("documentData", document);
                result.put("status", "Success");
            } else if ("fetchDocument_view".equalsIgnoreCase(action) || "downloadDocument".equalsIgnoreCase(action)) {

                try {
                    int document_id = Integer.parseInt(request.getParameter("document_id"));

                    ProjectDocumentService projectDocumentService = new ProjectDocumentService();
                    File file = projectDocumentService.documentRetrivalService_view(document_id);

                    String mimeType = getServletContext().getMimeType(file.getName());
                    if (mimeType == null) {
                        mimeType = "application/octet_stream";
                    }

                    response.setContentType(mimeType);
                    
                    if ("fetchdocument_view".equalsIgnoreCase(action)) {
                        response.setHeader("Content-Disposition", "inline; filrname=\"" + file.getName() + "\"");
                    } else if ("downloadDocument".equalsIgnoreCase(action)) {
                        response.setHeader("Content-Disposition", "attachment; filrname=\"" + file.getName() + "\"");
                    }

                    try (FileInputStream fis = new FileInputStream(file); OutputStream os = response.getOutputStream()) {
                        byte[] buffer = new byte[4996];
                        int byteRead;

                        while ((byteRead = fis.read(buffer)) != -1) {
                            os.write(buffer, 0, byteRead);
                        }
                    }
                } catch (Exception e) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Exception Ocuurs : " + e);

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
        String action, document_name, document_type;

        System.out.println("backlog document Servlet reporting in do post");
        action = request.getParameter("action");
        System.out.println("Action :" + action);

        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<>();
        try {

            if ("insert".equalsIgnoreCase(action)) {

                Document document = new Document();
                document.setDocument_name(request.getParameter("document_name"));
                document.setDocument_type(request.getParameter("document_type"));
                // document.setProject_id(Integer.parseInt(request.getParameter("project_id")));
                document.setBacklog_item_id(Integer.parseInt(request.getParameter("backlog_item_id")));
                System.out.println("Backlog_id " + document.getDocument_type());

                Part path = request.getPart("documentContent");

                BacklogDocumentService backlogDocumentService = new BacklogDocumentService();
                int document_id = backlogDocumentService.insertDocumentService(document, path);

                result.put("document_id", document_id);
                result.put("status", "Status");
            } else if ("update".equalsIgnoreCase(action)) {

                Document document = new Document();
                document.setDocument_id(Integer.parseInt(request.getParameter("document_id")));
                System.out.println("Document_id : " + document.getDocument_id());
                document.setDocument_name(request.getParameter("document_name"));
                document.setDocument_type(request.getParameter("document_type"));

                Part path = request.getPart("documentContent");
                BacklogDocumentService backlogDocumentService = new BacklogDocumentService();
                backlogDocumentService.updateDocumentService(document, path);

                result.put("status", "Success");
            } else if ("delete".equalsIgnoreCase(action)){
                
                int document_id = Integer.parseInt(request.getParameter("document_id"));
                ProjectDocumentService projectDocumentService = new ProjectDocumentService();
                projectDocumentService.documentDeletionService(document_id);
                
                result.put("status", "Success");
            }

        } catch (Exception e) {
            result.put("status", "Failed");
            e.printStackTrace();
            System.out.println("Exception occur : " + e);
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
