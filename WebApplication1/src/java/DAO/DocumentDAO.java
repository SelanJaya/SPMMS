/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DAO.DBConnection;
import beans.Backlog;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import beans.Document;

/**
 *
 * @author HP
 */
public class DocumentDAO {

    public int insertDocumentData(Connection con, Document document) throws Exception {
        String sql = """
                     INSERT INTO documents( document_name, document_path, document_type)
                     VALUES (?,?,?);
                     """;

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, document.getDocument_name());
            ps.setString(2, document.getDocument_path());
            ps.setString(3, document.getDocument_type());

            if (ps.executeUpdate() > 0) {
                // 2. Retrieve the generated keys
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int newId = rs.getInt(1); // This is your backlog_item_id
                        System.out.println("Inserted ID: " + newId);
                        return newId;
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return -1;
    }

    public void updateDocumentData(Connection con, Document document) throws Exception {
        System.out.println("Docuement DAO reporting update executed");
        String sql = """
                     UPDATE documents SET document_name= ? ,document_type= ? 
                     WHERE document_id = ?
                     """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            System.out.println(document.getDocument_name());
            ps.setString(1, document.getDocument_name());
            ps.setString(2, document.getDocument_type());
            ps.setInt(3, document.getDocument_id());

            int updateS = ps.executeUpdate();
            System.out.println("Document id " + document.getDocument_id());
            System.out.println("Dao success the update status : " + updateS);
        } catch (Exception e) {
            System.out.println("Exception occurs :" + e);
            throw e;
        }
    }

    public List<Document> getDocumentsData(int project_id) throws Exception {
        System.out.println("Documents Lite Executes ");
        List<Document> documentsList = new ArrayList<>();

        String sql = """
                     SELECT document_id, document_name, document_path, document_type 
                     FROM documents 
                     LEFT JOIN project_documents
                     USING(document_id)
                     WHERE project_id = ?
                     """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, project_id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Document document = new Document();
                document.setDocument_id(rs.getInt("document_id"));
                document.setDocument_name(rs.getString("document_name"));
                document.setDocument_path(rs.getString("document_path"));
                document.setDocument_type(rs.getString("document_type"));

                documentsList.add(document);
            }

            //return documentsList;
            return documentsList;
        } catch (Exception e) {
            System.out.println("Exception Occur : " + e);
            throw e;
        }
    }

    public Document getDocumentsDataEdit(int document_id) throws Exception {
        System.out.println("Documents Lite Executes ");

        Document document = null;

        String sql = """
                     SELECT document_name, document_path, document_type 
                     FROM documents 
                     WHERE document_id = ?
                     """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, document_id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                document = new Document();
                document.setDocument_id(document_id);
                document.setDocument_name(rs.getString("document_name"));
                document.setDocument_path(rs.getString("document_path"));
                document.setDocument_type(rs.getString("document_type"));
            }

            return document;
        } catch (Exception e) {
            System.out.println("Exception Occur : " + e);
            throw e;
        }
    }

    public String getDocumentPath(int Document_int) throws Exception {

        String sql = """
                      SELECT document_path FROM documents 
                      WHERE document_id = ?
                      """;
        String document_path = null;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Document_int);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                document_path = rs.getString("document_path");
            }

            return document_path;
        } catch (Exception e) {
            throw e;
        }
    }
    
    public void deleteDocument(Connection con ,int document_id) throws Exception{
        
        String sql = """
                     DELETE FROM documents WHERE document_id = ? 
                     """;
        
        try(PreparedStatement ps = con.prepareStatement(sql))  {
            
            ps.setInt(1, document_id);
            
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception occurs :" + e);
        }
        
    }

}
