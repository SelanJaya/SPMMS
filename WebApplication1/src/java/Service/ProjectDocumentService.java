/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import DAO.DBConnection;
import DAO.DocumentDAO;
import DAO.ProjectDocumentDAO;
import DAO.ProjectDAO;
import beans.Document;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.servlet.http.Part;
import java.sql.Connection;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 *
 * @author HP
 */
public class ProjectDocumentService {

    public Document insertDocument_localServer(Part filePart, Document document) throws Exception {

        String filePath = null;

        try {
            // Base directory where ALL documents will be stored
            String basePath = "C:\\Users\\HP\\Documents\\SPMMSDocuments";

            // Create a folder path specific to the project
            // Example: C:\...\SPMMSDocumentsproject_1  (⚠️ missing "\" separator here)
            String projectFolder = basePath + File.separator + "project_" + document.getProject_id();

            // Create a File object representing that folder path
            File folder = new File(projectFolder);

            // ✅ check + create
            if (!folder.exists()) {
                boolean created = folder.mkdirs(); // creates all missing folders

                if (!created) {
                    throw new IOException("Failed to create directory: " + projectFolder);
                }
            }

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
            String fileName = document.getDocument_nameSys() + extension;

            // OPTIONAL (recommended): make file name unique to avoid overwrite
            // String uniqueName = System.currentTimeMillis() + "_" + fileName;
            // Build the full path where the file will be stored
            // Uses OS-independent separator (good practice)
            filePath = projectFolder + File.separator + fileName;

            // Save the uploaded file to the specified path
            // filePart contains the actual uploaded file data
            filePart.write(filePath);

            // Store the file path into the document object
            // This will later be saved into the database
            document.setDocument_path(filePath);

            // After get the path t store into the db
            int document_id = insertDocument_db(document);
            document.setDocument_id(document_id);
            return document;

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

    public int insertDocument_db(Document document) throws Exception {

        Connection con = DBConnection.getConnection();
        con.setAutoCommit(false);
        try {

            DocumentDAO documentDAO = new DocumentDAO();
            int document_id = documentDAO.insertDocumentData(con, document);
            
            // Update the project Status
            if ("Project Charter".equalsIgnoreCase(document.getDocument_type())) {
                ProjectDAO projectDAO = new ProjectDAO();
                projectDAO.updateProjectStatus("Active", document.getProject_id());
            } else if("Project Sign Off".equalsIgnoreCase(document.getDocument_type())){
                ProjectDAO projectDAO = new ProjectDAO();
                projectDAO.updateProjectStatus("Archive", document.getProject_id());
            }
            
            System.out.println("Document id : " + document_id);
            ProjectDocumentDAO pDocumentDAO = new ProjectDocumentDAO();

            System.out.println("Project _ id : " + document.getProject_id());
            pDocumentDAO.insertProjectDocument(con, document_id, document.getProject_id());

            con.commit();

            return document_id;
        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally {
            con.close();
        }
    }

    public void updateDocument_db(Part filePart, Document document) throws Exception {
        System.out.println("Docuement Service reporting update executed");
        Connection con = DBConnection.getConnection();
        con.setAutoCommit(false);

        try {

            DocumentDAO documentDAO = new DocumentDAO();

            System.out.println("before check");

            if (filePart != null && filePart.getSize() > 10) {
                System.out.println("file size" + filePart.getSize());
                updateDocument_LocalServer(document.getDocument_id(), filePart);
            }
            System.out.println("after check");

            documentDAO.updateDocumentData(con, document);
            con.commit();
            System.out.println("After Commit");
        } catch (Exception e) {
            System.out.println("Exception occur : " + e);
            con.rollback();
            throw e;
        } finally {
            con.close();
        }
    }

    public void updateDocument_LocalServer(int document_id, Part filePart) throws Exception {
        DocumentDAO documentDAO = new DocumentDAO();
        String documentPath = documentDAO.getDocumentPath(document_id);

        Path path = Paths.get(documentPath);
        try (InputStream is = filePart.getInputStream()) {
            Files.copy(is, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            System.out.println("Exception occur : " + e);
            throw e;
        }
    }

//    public Document getDocumentinformation(int document_id) throws Exception {
//        Document document = new Document();
//        try {
//
//            // retrive the document meta data (path to retrieve from the file system)            
//            DocumentDAO documentDAO = new DocumentDAO();
//            document = documentDAO.getDocumentsDataEdit(document_id);
//
//            Path path = Paths.get(document.getDocument_path());
//            document.setDocumentContent(Files.newInputStream(path));
//
//            return document;
//        } catch (Exception e) {
//            throw e;
//        }
//    }
    
    public File documentRetrivalService_view(int document_id) throws Exception {

        try {
            DocumentDAO documentDAO = new DocumentDAO();
            String documentPath = documentDAO.getDocumentPath(document_id);

            File file = new File(documentPath);

            if (!file.exists()) {
                throw new Error("Filr not exist");
            }

            return file;
        } catch (Exception e) {
            System.out.println("New Error Ocurs : " + e);
            throw e;

        }
    }

    public void documentDeletionService(int document_id) throws Exception {

        Connection con = DBConnection.getConnection();
        con.setAutoCommit(false);

        DocumentDAO documentDAO = new DocumentDAO();

        try {
            //Get path first
            String documentPath = documentDAO.getDocumentPath(document_id);

            //Delete file FIRST
            if (documentPath != null) {
                File file = new File(documentPath);

                if (file.exists()) {
                    boolean deleted = file.delete();

                    if (!deleted) {
                        throw new Exception("File deletion failed");
                    }
                }
            }

            //3. Then delete DB record
            documentDAO.deleteDocument(con, document_id);

            con.commit();

        } catch (Exception e) {
            con.rollback();
            System.out.println("Exception occurs :" + e);
            throw e;

        } finally {
            con.close();
        }
    }

    public InputStream getDocumentStream(String pathStr) throws Exception {
        Path path = Paths.get(pathStr);
        return Files.newInputStream(path);
    }
}
