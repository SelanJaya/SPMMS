/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import beans.Document;
import DAO.DocumentDAO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import Service.DocumentService;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.annotation.MultipartConfig;

/**
 *
 * @author HP
 */
@MultipartConfig
public class DocumentServlet extends HttpServlet {

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
            out.println("<title>Servlet DocumentServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DocumentServlet at " + request.getContextPath() + "</h1>");
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
        System.out.println("GET EXECUTED");

        Gson gson = new Gson();

        Map<String, Object> result = new HashMap<>();
        String path = null;
        try {
            
            String action = request.getParameter("action");

            if ("fetchdocuments".equalsIgnoreCase(action)) {

                List<Document> documentList = new ArrayList<>();

                int project_id = Integer.parseInt(request.getParameter("project_id"));
                System.out.println("project_id" + project_id);

                DocumentDAO documentDAO = new DocumentDAO();
                documentList = documentDAO.getDocumentsData(project_id);

//                for(Document doc: documentList){
//                    System.out.println("Doc data :" + doc.getDocument_name());
//                }

                result.put("documentData", documentList);
                result.put("status", "Success");
                
            } else if ("fetchdocument_meta".equalsIgnoreCase(action)) {

                System.out.println("Servlet Report for edit data retrival");

                int document_id = Integer.parseInt(request.getParameter("document_id"));

                // retrive the document meta data (path to retrieve from the file system)            
                DocumentDAO documentDAO = new DocumentDAO();
                Document document = documentDAO.getDocumentsDataEdit(document_id);
                
                // set path to retrive the document 
                path = document.getDocument_path();
                
                result.put("documentData", document);
            } else if ("fetchdocument_content".equalsIgnoreCase(action)) { 
                response.setContentType("application/pdf");
                
                DocumentService documentService = new DocumentService();
                
                InputStream is =  documentService.getDocumentStream(path);
                OutputStream os = response.getOutputStream();

                is.transferTo(os);
            }
            response.getWriter().write(gson.toJson(result));
        } catch (Exception e) {
            System.out.println("Exception occur :" + e);
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

        System.out.println("POST REACHED");

        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<>();
        try {

            String action = request.getParameter("action");

            if ("insert".equalsIgnoreCase(action)) {

                Document document = new Document();
                document.setProject_id(Integer.parseInt(request.getParameter("project_id")));
                document.setDocument_name(request.getParameter("document_name"));
                document.setDocument_nameSys(request.getParameter("document_nameSys"));
                document.setDocument_type(request.getParameter("document_type"));
                
                Part filePart = request.getPart("document_pdf");
                DocumentService documentService = new DocumentService();
                Document documentReturn = documentService.insertDocument_localServer(filePart, document);

                result.put("document_id", documentReturn.getDocument_id());
                result.put("document_path", documentReturn.getDocument_path());
                result.put("status", "Success");
            } else if ("edit".equalsIgnoreCase(action)){
                
                System.out.println("Servler reporting edit executed");
                Document document = new Document();
                document.setDocument_id(Integer.parseInt(request.getParameter("document_id")));
                document.setDocument_name(request.getParameter("document_name"));
                document.setDocument_type(request.getParameter("document_type"));
                
                Part filePart = request.getPart("document_pdf") ;

                DocumentService documentService = new DocumentService();
                documentService.updateDocument_db(filePart, document);
                
                result.put("status", "Success");
            } else if("delete".equalsIgnoreCase(action)){
                
                System.out.println("Server reporting deletion occurs");
                Part filePart = request.getPart("document_pdf");
                
                int document_id = Integer.parseInt(request.getParameter("document_id"));
                
                DocumentService documentService = new DocumentService();
                documentService.documentDeletionService(document_id);
                
                result.put("status", "Success");
            }

        } catch (Exception e) {
            result.put("status", "Failed");
            System.out.println("Exception occurs : " + e);
            //throw e;
        }
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

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
