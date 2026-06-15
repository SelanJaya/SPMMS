/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import beans.TaskApproval;
import beans.Backlog;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import beans.Task;
import beans.TaskAssignment;
import beans.TaskDependency;
import DAO.TaskAssignmentDAO;
import DAO.TaskDependencyDAO;
import com.oracle.wls.shaded.org.apache.bcel.generic.D2F;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author HP
 */
public class TaskApprovalDAO {

    public int insertTaskApproval(TaskApproval taskApproval) throws Exception {
        String sql = """
                     INSERT INTO task_approvals( task_id , taskApproval_status, approved_by, remarks) VALUES (?,?,?,?)
                     """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, taskApproval.getTask_id());
            ps.setString(2, taskApproval.getTaskApproval_status());
            ps.setInt(3, taskApproval.getApproved_by());
            ps.setNull(4, java.sql.Types.VARCHAR);

            if (ps.executeUpdate() > 0) {
                // 2. Retrieve the generated keys
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int TaskAproval_id = rs.getInt(1); // This is your backlog_item_id
                        System.out.println("Inserted ID: " + TaskAproval_id);
                        return TaskAproval_id;
                    }
                }
            }
            return -1;
        } catch (Exception e) {
            System.out.println("Exception occur in insertTaskApproval :" + e);
            e.printStackTrace();
            throw e;
        }
    }
    
    public void updateTaskApproval(TaskApproval taskApproval) throws Exception{
        
        String sql = """
                     UPDATE task_approvals SET taskApproval_status = ?, remarks = ?
                     WHERE approval_id = ?;
                     """;
        
        try(Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, taskApproval.getTaskApproval_status());
            ps.setString(2, taskApproval.getRemarks());
            ps.setInt(3, taskApproval.getApproval_id());
            int status = ps.executeUpdate();
            
            if(status <= 0){
                throw  new Error("Update Failed");
            }
        }catch(Exception e){
            System.out.println("Exception happens updateTaskApproval : " + e);
            e.printStackTrace();
            throw e;
        }
            
    }
    public void updateTaskApprovalRemark(TaskApproval taskApproval) throws Exception{
        
        String sql = """
                     UPDATE task_approvals SET remarks = ?
                     WHERE task_id = ?;
                     """;
        
        try(Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, taskApproval.getRemarks());
            ps.setInt(2, taskApproval.getTask_id());
            
            int status = ps.executeUpdate();
            
            if(status <= 0){
                throw  new Error("Update Failed");
            }
        }catch(Exception e){
            System.out.println("Exception happens updateTaskApproval : " + e);
            e.printStackTrace();
            throw e;
        }
            
    }
    
    public TaskApproval getTaskRejectionReason(int approval_id) throws Exception{
        System.out.println("getTaskRejectionReason executedd" + " " + approval_id);
        
        String sql = """
                     SELECT ta.task_id, ta.remarks
                     FROM task_approvals ta
                     WHERE ta.approval_id = ?;
                     """;
        
        try(Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, approval_id);
            
            ResultSet rs = ps.executeQuery();
            
            TaskApproval taskApproval = new TaskApproval();
            
            while(rs.next()){
               
                taskApproval.setTask_id(rs.getInt("task_id"));
                taskApproval.setRemarks(rs.getString("remarks"));
                System.out.println("task Approval " + taskApproval.getTask_id() + " " + taskApproval.getRemarks());
            }
            
            if(taskApproval == null){
                throw new Error("taskApproval is null");
            }
            
            return taskApproval;
        }catch(Exception e){
            System.out.println("Exception happens getTaskRejectionReason : " + e);
            e.printStackTrace();
            throw e;
        }     
    }
}
