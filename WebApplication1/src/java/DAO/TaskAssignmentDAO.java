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
import beans.TaskAssignment;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author HP
 */
public class TaskAssignmentDAO {
    
    
    public void insertTaskAssignment(TaskAssignment taskAssignment) throws Exception{
        String sql = """
                     INSERT INTO task_assignments(task_id, task_assigned_to, task_assigned_by, task_assigned_at) 
                     VALUES (?,?,?,?)
                     """;
        
        try( Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql) ) {
            
            ps.setInt(1, taskAssignment.getTask_id());
            ps.setInt(2, taskAssignment.getTask_assigned_to());
            ps.setInt(3, taskAssignment.getTask_assigned_by());
            ps.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));
            
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Exception occurs in task" + e);
            throw e;
        }
    }
    
    public void updateTaskAssignment(Connection con , TaskAssignment taskAssignment) throws Exception{
        
        String sql = """
                     UPDATE task_assignments SET task_assigned_to = ?
                     WHERE task_id= ?
                     """;
        
        try (PreparedStatement ps = con.prepareStatement(sql)){
            
            ps.setInt(1, taskAssignment.getTask_assigned_to());
            ps.setInt(2, taskAssignment.getTask_id());
            
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Exception in task assignment :" + e );
            throw e;
        }
    }
    
    public void deleteTaskAssignment(Connection con, int task_id) throws Exception{
        String sql = """
                     DELETE FROM task_assignments WHERE task_id = ?
                     """;
        
        try(PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, task_id);
            ps.executeUpdate();
        } catch (Exception e) {
            throw e;
        }
    }
    
}
