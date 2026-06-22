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
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author HP
 */
public class TaskAssignmentDAO {

    public boolean checkTaskAssignmentStatus(TaskAssignment taskAssignment) throws Exception {

        String sql = """
                        SELECT task_assignment_status
                        FROM task_assignments
                        WHERE task_id = ? AND
                        task_assigned_to = ?
                    """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, taskAssignment.getTask_id());
            ps.setInt(2, taskAssignment.getTask_assigned_to());

            ResultSet rs = ps.executeQuery();

            return rs.next();
        } catch (Exception e) {
            System.out.println("Exception occurs in checkTaskAssignmentStatus" + e);
            e.printStackTrace();
            throw e;
        }
    }

    public boolean reactivateTaskAssignment(Connection con, TaskAssignment taskAssignment) throws Exception {

        String sql = """
                        UPDATE task_assignments
                        SET task_assignment_status = 'ACTIVE',
                            removal_reason = NULL
                        WHERE task_id = ?
                        AND task_assigned_to = ?
                    """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, taskAssignment.getTask_id());
            ps.setInt(2, taskAssignment.getTask_assigned_to());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Exception occurs in reactivateTaskAssignment" + e);
            e.printStackTrace();
            throw e;
        }
    }

    public void insertTaskAssignment(TaskAssignment taskAssignment) throws Exception {
        String sql = """
                     INSERT INTO task_assignments(task_id, task_assigned_to, task_assigned_by, task_assigned_at) 
                     VALUES (?,?,?,?)
                     """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

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
    
    public void insertTaskAssignment(Connection con, TaskAssignment taskAssignment) throws Exception {
        String sql = """
                     INSERT INTO task_assignments(task_id, task_assigned_to, task_assigned_by, task_assigned_at) 
                     VALUES (?,?,?,?)
                     """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, taskAssignment.getTask_id());
            ps.setInt(2, taskAssignment.getTask_assigned_to());
            ps.setInt(3, taskAssignment.getTask_assigned_by());
            ps.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));

            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Exception occurs in insertTaskAssignment" + e);
            throw e;
        }
    }

    public void updateTaskAssignment(Connection con, TaskAssignment taskAssignment) throws Exception {

        String sql = """
                     UPDATE task_assignments SET task_assigned_to = ?
                     WHERE task_id= ?
                     """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, taskAssignment.getTask_assigned_to());
            ps.setInt(2, taskAssignment.getTask_id());

            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Exception in task assignment :" + e);
            throw e;
        }
    }

    public void removeTaskAssignment(Connection con, TaskAssignment taskAssignment) throws Exception {
        System.out.println("removeTaskAssignment EXECYETD");
        String sql = """
                 UPDATE task_assignments
                 SET task_assignment_status = 'REMOVED',
                     removal_reason = ?,
                     removed_at = ?,
                 WHERE task_id = ?
                 AND task_assigned_to = ?
                 """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, taskAssignment.getRemoval_reason());
            ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            ps.setInt(3, taskAssignment.getTask_id());
            ps.setInt(4, taskAssignment.getTask_assigned_to());
            
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Exception in removeTaskAssignment :" + e);
            throw e;
        }

    }

    public void deleteTaskAssignment(Connection con, int task_id) throws Exception {
        String sql = """
                     DELETE FROM task_assignments WHERE task_id = ?
                     """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, task_id);
            ps.executeUpdate();
        } catch (Exception e) {
            throw e;
        }
    }

}
