/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;


import DAO.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import beans.Task;
import beans.TaskDependency;

/**
 *
 * @author HP
 */
public class TaskDependencyDAO {
    
    public void insertTaskDependency(Connection con, TaskDependency taskDependency) throws Exception{
        
        String sql = """
                     INSERT INTO task_dependencies( task_id, depends_on_task_id) 
                     VALUES (?,?)
                     """;
        
        try (PreparedStatement ps = con.prepareStatement(sql)){
            
            ps.setInt(1, taskDependency.getTask_id());
            ps.setInt(2, taskDependency.getDepend_on_task_id());
            
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Exception happens in the insert task dependency");
            throw  e;
        }       
    }
    
    public void deleteTaskDependency(Connection con, int task_id) throws Exception{
        
        String sql = """
                     DELETE FROM task_dependencies WHERE task_id = ?
                     """;
        
        try( PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, task_id);
            
            ps.executeUpdate();
        }catch(Exception e){
            System.out.println("Exception happens in the delete task dependency");
            throw  e;
        }
    }
}
