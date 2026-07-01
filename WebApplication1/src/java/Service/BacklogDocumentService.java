/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import java.sql.Connection;
import DAO.DBConnection;
import DAO.DocumentDAO;
import DAO.BacklogDocumentDAO;
import beans.Document;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import javax.servlet.http.Part;
import Service.ProjectDocumentService;
/**
 *
 * @author HP
 */
public class BacklogDocumentService {
    
    public int insertDocumentService(Document document, Part file) throws Exception{
        
        Connection con = DBConnection.getConnection();
        con.setAutoCommit(false);
       
        try {
            
            DocumentDAO documentDAO = new DocumentDAO();
            int document_id = documentDAO.insertDocumentData(con, document);
            
            BacklogDocumentDAO backlogDocumentDAO = new BacklogDocumentDAO();
            backlogDocumentDAO.InsertBacklogDocument(con, document.getBacklog_item_id(), document_id);
            
            document.setDocument_id(document_id);
            
            String document_path = insertBacklogDocument_localServer(document, file);
            
            documentDAO.setDocumentPath(con, document_id, document_path);
            
            con.commit();
            
            return document_id;
        } catch (Exception e) {
            throw e;
        } finally{
            con.close();
        }
    }
    
    public String insertBacklogDocument_localServer(Document document, Part filePart) throws Exception{
        
        String filePath = null;
        boolean created;

        try {
            // Base directory where ALL documents will be stored
           // String basePath = "C:\\Users\\HP\\Documents\\SPMMSDocuments";
           String basePath = System.getProperty("java.io.tmpdir") + File.separator + "SPMMSDocuments";
           
            // Create a folder path specific to the project
            // Example: C:\...\SPMMSDocumentsproject_1  (⚠️ missing "\" separator here)
            String projectFolder = basePath + File.separator + "project_" + document.getProject_id();

            // Create a File object representing that folder path
            File folder = new File(projectFolder);

            // ✅ check + create
            if (!folder.exists()) {
                 created = folder.mkdirs(); // creates all missing folders

                if (!created) {
                    throw new IOException("Failed to create directory: " + projectFolder);
                }
            }
            
            // create a folder for a specific backlog id
            String backlogIdFolder = projectFolder + File.separator + "BacklogItem_" + document.getBacklog_item_id();
            
            folder = new File(backlogIdFolder);
            
            created = false;
            if(!folder.exists()){
                created = folder.mkdirs();
                
                if (!created) {
                    throw new IOException("Failed to create directory: " + backlogIdFolder);
                }
            }
            
            // create a specific forder for document type
            String documentTypeFolder = backlogIdFolder + File.separator + document.getDocument_type();
            
            folder = new File(documentTypeFolder);
            
            created = false;
            if(!folder.exists()){
               created = folder.mkdirs();
               
               if (!created) {
                    throw new IOException("Failed to create directory: " + documentTypeFolder);
                }
            }
            
            // filePart.write(filePath);
            // Get the file name from your Document object
            // ⚠️ This assumes you already set document_name earlier in servlet
            // 1. get original file name (to extract extension)
            String originalName = Paths.get(filePart.getSubmittedFileName())
                    .getFileName()
                    .toString();

            // 2. extract extension
            String extension = "";
            int dotIndex = originalName.lastIndexOf(".");
            
            if (dotIndex != -1) {
                extension = originalName.substring(dotIndex); // includes ".pdf"
            }

            // 5. final file name
            String fileName = document.getDocument_id()+ extension;

            // OPTIONAL (recommended): make file name unique to avoid overwrite
            // String uniqueName = System.currentTimeMillis() + "_" + fileName;
            // Build the full path where the file will be stored
            // Uses OS-independent separator (good practice)
            filePath = documentTypeFolder + File.separator + fileName;

            // Save the uploaded file to the specified path
            // filePart contains the actual uploaded file data
            filePart.write(filePath);

            // Store the file path into the document object
            // This will later be saved into the database
            document.setDocument_path(filePath);

            return filePath;
        } catch (Exception e) {

            // 🔥 ROLLBACK: delete file if DB fails
            if (filePath != null) {
                File file = new File(filePath);
                if (file.exists()) {
                    file.delete();
                }
            }
            throw e;
        }
    }
    
    
    public void updateDocumentService(Document document, Part filePart) throws Exception{
        Connection con = DBConnection.getConnection();
        con.setAutoCommit(false);
        
        try{
            DocumentDAO documentDAO = new DocumentDAO();
            documentDAO.updateDocumentData(con, document);
            
            ProjectDocumentService projectDocumentService = new ProjectDocumentService();
            projectDocumentService.updateDocument_LocalServer(document.getDocument_id(), filePart);
            
            con.commit();
        }catch(Exception e){
            System.out.println("Exception Occur : " + e);
        }finally{
            con.close();
        }
        
    }
}
