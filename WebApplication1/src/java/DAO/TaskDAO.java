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
import beans.TaskDependency;
import beans.TaskApproval;
import beans.DashboardInsight;
import DAO.TaskAssignmentDAO;
import DAO.TaskDependencyDAO;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author HP
 */
public class TaskDAO {

    public int insertTask(Task task) throws Exception {

        String sql = """
                     INSERT INTO tasks(task_id, task_name, task_description, task_start_date, 
                     task_end_date, task_status, sprint_id, backlog_item_id) 
                     VALUES (?,?,?,?,?,?,?,?)
                     """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            ps.setInt(1, task.getTask_id());
            ps.setString(2, task.getTask_name());
            ps.setString(3, task.getTask_desc());
            ps.setObject(4, task.getTask_start_date());
            ps.setObject(5, task.getTask_end_date());
            ps.setString(6, task.getTask_status());

            ps.setInt(7, task.getSprint_Id());
            ps.setInt(8, task.getBacklog_id());

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

        Map<Integer, Task> taskMap = new LinkedHashMap<>();

        String sql = """
                     SELECT t.task_id, t.task_name, t.task_start_date, t.task_end_date, t.task_status, td.depends_on_task_id
                     FROM tasks t
                     LEFT JOIN task_dependencies td
                     USING(task_id)
                     WHERE t.sprint_id = ?;
                     """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {

            ps.setInt(1, sprint_id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int taskId = rs.getInt("task_id");

                // Check the task IDin the taskMap                 
                Task task = taskMap.get(taskId);

                // Create a new task obj only if the task return null (Not created yet)
                if (task == null) {

                    task = new Task();
                    task.setTask_id(taskId);
                    task.setTask_name(rs.getString("task_name"));
                    task.setTask_start_date(rs.getString("task_start_date"));
                    task.setTask_end_date(rs.getString("task_end_date"));
                    task.setTask_status(rs.getString("task_status"));

                    //creata an aaray list
                    task.setTaskDepedencies(new ArrayList<>());

                    taskMap.put(taskId, task);
                }
                //get the depends on task id
                Integer depId = rs.getObject("depends_on_task_id", Integer.class);

                // null checker
                if (depId != null) {
                    // add to the taskdependecy arrayList
                    TaskDependency taskDependency = new TaskDependency();
                    taskDependency.setDepend_on_task_id(depId);

                    task.getTaskDepedencies().add(taskDependency);
                }
            }

            return new ArrayList<>(taskMap.values());
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Task> getLiteTasksBySprintID(int sprint_id) throws Exception {

        List<Task> taskArr = new ArrayList<>();

        String sql = """
                    SELECT task_id, task_name
                    FROM tasks WHERE sprint_id = ?;
                    """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, sprint_id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Task task = new Task();
                task.setTask_id(rs.getInt("task_id"));
                task.setTask_name(rs.getString("task_name"));

                taskArr.add(task);
            }

            return taskArr;
        } catch (Exception e) {
            throw e;
        }
    }

    public void updateTaskStatus(int task_id, String task_status) throws Exception {
        String sql = """
                     UPDATE tasks 
                     SET 
                        task_status = ?,
                        
                     actual_startDate =
                            CASE
                                WHEN ? = 'IN PROGRESS' AND actual_startDate IS NULL
                                THEN CURRENT_DATE
                                ELSE actual_startDate
                            END,
                     
                     actual_endDate = 
                            CASE
                                WHEN ? = 'DONE'
                                THEN CURRENT_DATE
                                ELSE NULL
                            END
                         WHERE task_id = ?;
                     """;

//        
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, task_status);
            ps.setString(2, task_status);
            ps.setString(3, task_status);
            ps.setInt(4, task_id);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

//    public Task getTaskByTaskId(int task_id) throws Exception {
//
//        String sql = """
//                        SELECT t.task_id, 
//                            t.task_name, 
//                            t.task_description, 
//                            t.task_start_date,
//                            t.task_end_date, 
//                            t.backlog_item_id,
//                            td.depends_on_task_id,
//                            dt.task_name AS dependency_name,
//                            ta.task_assigned_to, 
//                            u.username, 
//                            u.user_role
//                        FROM tasks t
//                        LEFT JOIN task_dependencies td
//                        ON t.task_id = td.task_id
//                        LEFT JOIN tasks dt                          
//                        ON td.depends_on_task_id = dt.task_id
//                        LEFT JOIN task_assignments ta
//                        ON t.task_id = ta.task_id
//                        LEFT JOIN users u
//                        ON ta.task_assigned_to = u.user_id
//                        WHERE t.task_id = ?;
//                     """;
//
//        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
//
//            ps.setInt(1, task_id);
//
//            ResultSet rs = ps.executeQuery();
//
//            Task task = null;
//            List<TaskAssignment> taskAssignments = new ArrayList<>();
//            while (rs.next()) {
//
//                if (task == null) {
//
//                    task = new Task();
//                    task.setTask_id(rs.getInt("task_id"));
//                    task.setTask_name(rs.getString("task_name"));
//                    task.setTask_desc(rs.getString("task_description"));
//                    task.setTask_start_date(rs.getObject("task_start_date", String.class));
//                    task.setTask_end_date(rs.getObject("task_end_date", String.class));
//                    task.setBacklog_id(rs.getInt("backlog_item_id"));
//                    
//                    task.setTaskDepedencies(new ArrayList<>());
//                    
//                    
//                    TaskAssignment taskAssignment = new TaskAssignment();
//                    taskAssignment.setTask_assigned_to(rs.getInt("task_assigned_to"));
//                    taskAssignment.setUser_name(rs.getString("username"));
//                    taskAssignment.setTask_assigned_to_Role(rs.getString("user_role"));
//                    
//                    
//                    task.setTaskAssignment(taskAssignment);
//                    System.out.println("Task data : " + task.getTask_name() + " " + task.getTask_desc());
//                }
//
//                //get the depends on task id
//                Integer depId = rs.getObject("depends_on_task_id", Integer.class);
//
//                // null checker
//                if (depId != null) {
//                    TaskDependency taskDependency = new TaskDependency();
//                    taskDependency.setDepend_on_task_id(depId);
//                    taskDependency.setDepend_on_task_Name(rs.getString("dependency_name"));
//                    // add to the taskdependecy arrayList
//                    task.getTaskDepedencies().add(taskDependency);
//                }
//            }
//
//            if (task == null) {
//                throw new Exception("Task not found");
//            }
//
//            return task;
//        } catch (Exception e) {
//            throw e;
//        }
//    }
    public Task getTaskByTaskId(int task_id) throws Exception {

        String sql = """
                    SELECT t.task_id,
                           t.task_name,
                           t.task_description,
                           t.task_start_date,
                           t.task_end_date,
                           t.backlog_item_id,
                           td.depends_on_task_id,
                           dt.task_name AS dependency_name,
                           ta.task_assigned_to,
                           u.username,
                           u.user_role
                    FROM tasks t
                    LEFT JOIN task_dependencies td
                        ON t.task_id = td.task_id
                    LEFT JOIN tasks dt
                        ON td.depends_on_task_id = dt.task_id
                    LEFT JOIN task_assignments ta
                        ON t.task_id = ta.task_id
                    LEFT JOIN users u
                        ON ta.task_assigned_to = u.user_id
                    WHERE t.task_id = ? AND ta.task_assignment_status = "ACTIVE"
                 """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, task_id);

            ResultSet rs = ps.executeQuery();

            Task task = null;

            List<TaskDependency> dependencies = new ArrayList<>();
            //List<TaskAssignment> assignments = new ArrayList<>();

            while (rs.next()) {

                if (task == null) {

                    task = new Task();
                    task.setTask_id(rs.getInt("task_id"));
                    task.setTask_name(rs.getString("task_name"));
                    task.setTask_desc(rs.getString("task_description"));
                    task.setTask_start_date(rs.getObject("task_start_date", String.class));
                    task.setTask_end_date(rs.getObject("task_end_date", String.class));
                    task.setBacklog_id(rs.getInt("backlog_item_id"));

                    task.setTaskDepedencies(dependencies);
                    // task.setTaskAssignments(assignments);

                    System.out.println("Task data : " + task.getTask_name() + " " + task.getTask_desc());
                }

                // Dependencies
                Integer depId = rs.getObject("depends_on_task_id", Integer.class);

                if (depId != null) {

                    boolean dependencyExists = dependencies.stream().anyMatch(d -> d.getDepend_on_task_id() == depId);

                    if (!dependencyExists) {

                        TaskDependency dependency
                                = new TaskDependency();

                        dependency.setDepend_on_task_id(depId);
                        dependency.setDepend_on_task_Name(
                                rs.getString("dependency_name"));

                        dependencies.add(dependency);
                    }
                }

                // Assignments
                Integer assignedUserId = rs.getObject("task_assigned_to", Integer.class);

                if (assignedUserId != null) {

                    //boolean assignmentExists = assignments.stream().anyMatch(a -> a.getTask_assigned_to()== assignedUserId);
                    TaskAssignment assignment = new TaskAssignment();

                    assignment.setTask_id(rs.getInt("task_id"));
                    assignment.setTask_assigned_to(assignedUserId);
                    assignment.setUser_name(rs.getString("username"));
                    assignment.setTask_assigned_to_Role(rs.getString("user_role"));

                    task.setTaskAssignment(assignment);
                }
            }

            if (task == null) {
                throw new Exception("Task not found");
            }

            return task;

        } catch (Exception e) {
            throw e;
        }
    }

    public void updateTaskDetails(Connection con, Task task) throws Exception {

        String sql = """
                     UPDATE tasks 
                     SET task_name = ?, 
                         task_description = ?, 
                         task_start_date = ?, 
                         task_end_date = ?
                     WHERE task_id = ?
                     """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, task.getTask_name());
            ps.setString(2, task.getTask_desc());
            ps.setObject(3, task.getTask_start_date());
            ps.setObject(4, task.getTask_end_date());

            ps.setInt(5, task.getTask_id());
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

    public List getTask_edit(int sprint_id, int task_id) throws Exception {

        List<Task> taskArr = new ArrayList<>();

        String sql = """
                     SELECT task_id, task_name
                     FROM tasks t
                     WHERE t.sprint_id = ?
                     AND t.task_id != ?
                     AND t.task_id NOT IN (
                         SELECT depends_on_task_id
                         FROM task_dependencies
                         WHERE task_id = ?
                     );
                     """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {

            ps.setInt(1, sprint_id);
            ps.setInt(2, task_id);
            ps.setInt(3, task_id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Task task = new Task();
                task.setTask_id(rs.getInt("task_id"));
                task.setTask_name(rs.getString("task_name"));
                taskArr.add(task);
            }

            return taskArr;
        } catch (Exception e) {
            throw e;
        }
    }

    public List getTask_Backlog(int sprint_id) throws Exception {

        String sql = """
                    SELECT 
                         t.task_id, 
                         t.task_name, 
                         t.task_status,  
                         t.backlog_item_id, 
                         b.backlog_item_title, 
                         tas.approval_id,
                         tas.taskApproval_status
                     FROM tasks t 
                     LEFT JOIN backlog_items b
                         ON t.backlog_item_id = b.backlog_item_id
                     LEFT JOIN task_approvals tas 
                         ON tas.task_id = t.task_id
                         AND tas.approval_id = (
                             SELECT MAX(approval_id)
                             FROM task_approvals
                             WHERE task_id = t.task_id
                         )
                     WHERE t.sprint_id = ?
                     ORDER BY t.backlog_item_id;
                     """;

        List<Task> taskArr = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, sprint_id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Task task = new Task();
                task.setTask_id(rs.getInt("task_id"));
                task.setTask_name(rs.getString("task_name"));
                task.setTask_status(rs.getString("task_status"));

                TaskApproval taskApproval = new TaskApproval();
                taskApproval.setApproval_id(rs.getInt("approval_id"));
                String status = rs.getString("taskApproval_status");

                if (status == null) {
                    taskApproval.setTaskApproval_status(null);
                } else {
                    taskApproval.setTaskApproval_status(status);
                }

                task.setTaskApproval(taskApproval);

                Backlog backlog = new Backlog();
                backlog.setBacklogI_id(rs.getInt("backlog_item_id"));
                backlog.setBacklogI_title(rs.getString("backlog_item_title"));

                task.setBacklog(backlog);
                taskArr.add(task);
            }

            return taskArr;

        } catch (Exception e) {
            System.out.println("Exception ocured at getTask_Backlog : " + e);
            e.printStackTrace();
            throw e;
        }

    }

    public List getMyActiveTask(int user_id) throws Exception {

        String sql = """
                    SELECT
                    p.project_name,
                        SUM(CASE WHEN t.task_status = 'DONE' THEN 1 ELSE 0 END) AS taskDone,
                        SUM(CASE WHEN t.task_status = 'IN PROGRESS' THEN 1 ELSE 0 END) AS taskInProgress,
                        SUM(CASE WHEN t.task_status = 'TO DO' THEN 1 ELSE 0 END) AS taskToDo
                    FROM tasks t
                    JOIN sprints s USING (sprint_id)
                    LEFT JOIN projects p USING (project_id)
                    LEFT JOIN project_assignments pa USING (project_id)
                    WHERE pa.proj_assign_to = ?
                    GROUP BY p.project_name;
                     """;

        List<DashboardInsight> dashboardInsightArr = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, user_id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DashboardInsight dashboardInsight = new DashboardInsight();
                dashboardInsight.setProject_name(rs.getString("project_name"));
                dashboardInsight.setTaskDone(rs.getInt("taskDone"));
                dashboardInsight.setTaskInProgress(rs.getInt("taskInProgress"));
                dashboardInsight.setTaskToDo(rs.getInt("taskToDo"));

                dashboardInsightArr.add(dashboardInsight);
            }
            return dashboardInsightArr;
        } catch (Exception e) {
            System.out.println("Exception ocured at getTask_Backlog : " + e);
            e.printStackTrace();
            throw e;
        }

    }

    public TaskAssignment getTaskRemovalReason(int taskId, int task_assigned_to) throws SQLException {

        String sql = """
            SELECT removal_reason
            FROM task_assignments
            WHERE task_id = ? AND task_assigned_to = ?
              AND task_assignment_status = 'REMOVED'
            ORDER BY removed_at DESC
            LIMIT 1
        """;
        TaskAssignment taskAssignment = null;
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, taskId);
            ps.setInt(2, task_assigned_to);
             taskAssignment = new TaskAssignment();
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                   taskAssignment.setTask_id(taskId);
                   taskAssignment.setTask_assigned_to(task_assigned_to);
                   taskAssignment.setRemoval_reason(rs.getString("removal_reason"));
                }
            }
        }

        return taskAssignment;
    }
}
