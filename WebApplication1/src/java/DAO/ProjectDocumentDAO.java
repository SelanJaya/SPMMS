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
public class ProjectDocumentDAO {
    
    public void insertProjectDocument(Connection con , int document_id, int project_id) throws Exception{
        
        String sql = """
                     INSERT INTO project_documents(document_id, project_id)
                     VALUES (?,?)
                     """;
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, document_id);
            ps.setInt(2, project_id);
            
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Exception Ocurr : " + e );
            throw e;
        }
    }
        
    
    
}
