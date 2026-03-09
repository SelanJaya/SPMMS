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
import beans.Task;
import beans.TaskAssignment;
import DAO.TaskAssignmentDAO;

/**
 *
 * @author HP
 */
public class TaskDAO {

    public int insertTask(Task task) throws Exception {

        String sql = """
                     INSERT INTO tasks(task_id, task_name, task_description, task_start_date, 
                     task_end_date, task_status, task_dependency, sprint_id) 
                     VALUES (?,?,?,?,?,?,?,?)
                     """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            ps.setInt(1, task.getTask_id());
            ps.setString(2, task.getTask_name());
            ps.setString(3, task.getTask_desc());
            ps.setObject(4, task.getTask_start_date());
            ps.setObject(5, task.getTask_end_date());
            ps.setString(6, task.getTask_status());
            if (task.getTask_dependency() != null) {
                ps.setInt(7, task.getTask_dependency());
            } else {
                ps.setNull(7, java.sql.Types.INTEGER);
            }
            ps.setInt(8, task.getSprint_Id());

            if (ps.executeUpdate() > 0) {
                // 2. Retrieve the generated keys
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int newTaskId = rs.getInt(1); // This is your backlog_item_id
                        System.out.println("Inserted ID: " + newTaskId);
                        return newTaskId;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception occured in INSERT : " + e);
        }
        return -1;
    }

    public List<Task> getTasksBySprintId(int sprint_id) throws Exception {

        List<Task> taskArr = new ArrayList<>();

        String sql = """
                     SELECT task_id, task_name, task_start_date, task_end_date, task_status, task_dependency
                     FROM tasks WHERE sprint_id = ?;
                     """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setInt(1, sprint_id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Task task = new Task();
                task.setTask_id(rs.getInt("task_id"));
                task.setTask_name(rs.getString("task_name"));
                task.setTask_start_date(rs.getString("task_start_date"));
                task.setTask_end_date(rs.getString("task_end_date"));
                task.setTask_status(rs.getString("task_status"));

                //to get null instead of zero
                task.setTask_dependency(rs.getObject("task_dependency", Integer.class));
                taskArr.add(task);
            }

            return taskArr;
        } catch (Exception e) {
            throw e;
        }
    }

    public void updateTaskStatus(int task_id, String task_status) throws Exception {
        String sql = """
                         UPDATE tasks SET task_status = ? WHERE task_id = ?;
                         """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, task_status);
            ps.setInt(2, task_id);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<Task> getTaskByTaskId(int task_id) {
        String sql = """
                         SELECT t.task_id, t.task_name, t.task_description, t.task_start_date,
                         t.task_end_date, t.task_dependency, ta.task_assigned_to, u.username, u.user_role
                         FROM tasks t
                         LEFT JOIN task_assignments ta
                         USING(task_id)
                         LEFT JOIN users u
                         ON ta.task_assigned_to = u.user_id
                         WHERE t.task_id = ?;
                         """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, task_id);

            ResultSet rs = ps.executeQuery();

            List<Task> taskArr = new ArrayList<>();

            while (rs.next()) {
                Task task = new Task();

                task.setTask_id(rs.getInt("task_id"));
                task.setTask_name(rs.getString("task_name"));
                task.setTask_desc(rs.getString("task_description"));
                task.setTask_start_date(rs.getObject("task_start_date", String.class));
                task.setTask_end_date(rs.getObject("task_end_date", String.class));
                task.setTask_dependency(rs.getObject("task_dependency", Integer.class));

                TaskAssignment taskAssignment = new TaskAssignment();
                taskAssignment.setTask_assigned_to(rs.getInt("task_assigned_to"));
                taskAssignment.setUser_name(rs.getString("username"));
                taskAssignment.setTask_assigned_to_Role(rs.getString("user_role"));

                task.setTaskAssignment(taskAssignment);

                taskArr.add(task);
            }
            return taskArr;
        } catch (Exception e) {
        }

        return null;
    }

    public void updateTaskDetails(Connection con, Task task) throws Exception {

        String sql = """
                     UPDATE tasks 
                     SET task_name = ?, 
                         task_description = ?, 
                         task_start_date = ?, 
                         task_end_date = ?, 
                         task_dependency = ?
                     WHERE task_id = ?
                     """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, task.getTask_name());
            ps.setString(2, task.getTask_desc());
            ps.setObject(3, task.getTask_start_date());
            ps.setObject(4, task.getTask_end_date());
            if (task.getTask_dependency() != null) {
                ps.setInt(5, task.getTask_dependency());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }

            ps.setInt(6, task.getTask_id());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Exception in task : " + e);
            throw e;
        }
    }

    public void deleteTask(Connection con, int task_id) throws Exception {

        String sql = """
                    DELETE FROM tasks WHERE task_id = ?
                    """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, task_id);

            ps.executeUpdate();
        } catch (Exception e) {
            throw e;
        }
    }

    public void updateTaskDetails_Assignment(Task task) throws Exception {

        Connection con = DBConnection.getConnection();
        con.setAutoCommit(false);

        try {
            updateTaskDetails(con, task);

            TaskAssignment taskAssignment = new TaskAssignment();
            taskAssignment = task.getTaskAssignment();

            TaskAssignmentDAO taskAssignmentDAO = new TaskAssignmentDAO();
            taskAssignmentDAO.updateTaskAssignment(con, taskAssignment);

            con.commit();

        } catch (Exception e) {
            con.rollback();
            System.out.println("Exception Occurs" + e);
            throw e;
        } finally {
            con.close();
        }
    }

    public void deleteTaskDetails_Assignment(int task_id) throws Exception {

        Connection con = DBConnection.getConnection();
        con.setAutoCommit(false);

        try {
            deleteTask(con, task_id);

            TaskAssignmentDAO taskAssignmentDAO = new TaskAssignmentDAO();
            taskAssignmentDAO.deleteTaskAssignment(con, task_id);

            con.commit();
        } catch (Exception e) {
            con.rollback();
            System.out.println("Exception Occurs" + e);
            throw e;
        } finally {
            con.close();
        }
    }

}
