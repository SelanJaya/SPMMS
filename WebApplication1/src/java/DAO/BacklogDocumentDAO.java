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
public class BacklogDocumentDAO {

    public void InsertBacklogDocument(Connection con, int backlog_item_id, int document_id) throws Exception {

        String sql = """
                     INSERT INTO backlog_documents(document_id, backlog_item_id) 
                     VALUES (?,?)
                     """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, document_id);
            ps.setInt(2, backlog_item_id);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception occurrs : " + e);
            throw e;

        }
    }

    public List<Document> getBacklogDocuments(int backlog_item_id) throws Exception {

        List<Document> documentsList = new ArrayList<>();

        String sql = """
                     SELECT document_id, document_name, document_path, document_type 
                                          FROM documents 
                                          LEFT JOIN backlog_documents bd
                                          USING(document_id)
                                          WHERE bd.backlog_item_id = ?;
                     """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, backlog_item_id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Document document = new Document();
                document.setDocument_id(rs.getInt("document_id"));
                document.setDocument_name(rs.getString("document_name"));
                document.setDocument_path(rs.getString("document_path"));
                document.setDocument_type(rs.getString("document_type"));

                documentsList.add(document);
            }
            
            return documentsList;
        } catch(Exception e){
            System.out.println("Exception occurs in retrieving document data : " + e);
            e.printStackTrace();
            throw e;
        }
    }

}
