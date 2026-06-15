/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DAO.DBConnection;
import beans.ProjectAnalytics;
import beans.Task;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import beans.Document;
import beans.Sprint;
import java.time.LocalDate;
import java.util.Map;

/**
 *
 * @author HP
 */
public class ProjectAnalyticsDAO {

    public ProjectAnalytics getAVGVelocityData(int project_id) throws Exception {

        ProjectAnalytics projectAnalytics = null;
        String sql = """
                     SELECT 
                         COUNT(t.task_id) AS completed_tasks,
                         COUNT(DISTINCT s.sprint_id) AS total_sprints
                     FROM tasks t
                     JOIN sprints s ON t.sprint_id = s.sprint_id
                     WHERE t.task_status = 'Done'
                     AND s.project_id = ?;
                     """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, project_id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                projectAnalytics = new ProjectAnalytics();
                projectAnalytics.setCompletedTask(rs.getInt("completed_tasks"));
                projectAnalytics.setTotalSprint(rs.getInt("total_sprints"));
            }

            return projectAnalytics;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception occured in getAVGVelocity : " + e);
            throw e;
        }
    }

    public List getAVGSpSuccRateData(int project_id) throws Exception {
        List<ProjectAnalytics> proAnalysisArr = new ArrayList<>();
        String sql = """
                    SELECT 
                    COUNT(t.task_id) AS total_tasks,
                    SUM(CASE WHEN t.task_status = 'Done' THEN 1 ELSE 0 END) AS completed_tasks
                    FROM tasks t 
                    LEFT JOIN sprints s
                    USING(sprint_id)
                    WHERE s.project_id = ?
                    GROUP BY sprint_id;
                     """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, project_id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                ProjectAnalytics projectAnalytics = new ProjectAnalytics();
                projectAnalytics.setCompletedTask(rs.getInt("completed_tasks"));
                projectAnalytics.setTotalTask(rs.getInt("total_tasks"));
                proAnalysisArr.add(projectAnalytics);
            }

            return proAnalysisArr;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception occured in getAVGVelocity : " + e);
            throw e;
        }
    }

    public List getCycleTimeData(int project_id) throws Exception {

        List<Task> taskArr = new ArrayList<>();

        String sql = """
                     SELECT t.actual_startDate, t.actual_endDate
                     FROM tasks t
                     LEFT JOIN sprints s 
                     USING(sprint_id)
                     LEFT JOIN projects p
                     USING(project_id)
                     WHERE task_status = "Done"
                     AND s.project_id = ?;
                     """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, project_id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Task task = new Task();
                System.out.println(rs.getString("actual_startDate"));
                System.out.println(rs.getString("actual_endDate"));
                task.setActual_startDate(rs.getString("actual_startDate"));
                task.setActual_endDate(rs.getString("actual_endDate"));

                taskArr.add(task);
            }
            return taskArr;
        } catch (Exception e) {
            System.out.println("Exception occurs in getCycleTimeData : " + e);
            e.printStackTrace();
            throw e;
        }
    }

    public List getRejectionRateData(int project_id) throws Exception {

        List<ProjectAnalytics> projectAnalyticArr = new ArrayList<>();

        String sql = """
                     SELECT COUNT(task_id) AS total_task, SUM(CASE WHEN ta.taskApproval_status = 'reject' THEN 1 ELSE 0 END) AS rejected_task
                     FROM tasks t
                     LEFT JOIN task_approvals ta 
                     USING(task_id)
                     LEFT JOIN sprints s
                     USING(sprint_id)
                     WHERE s.project_id = ?
                     GROUP BY(sprint_id);
                     """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, project_id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ProjectAnalytics projectAnalytics = new ProjectAnalytics();
                projectAnalytics.setCompletedTask(rs.getInt("total_task"));
                projectAnalytics.setRejectedTask(rs.getInt("rejected_task"));

                projectAnalyticArr.add(projectAnalytics);
            }

            return projectAnalyticArr;
        } catch (Exception e) {
            System.out.println("Exception occurs in getRejectionRateData : " + e);
            e.printStackTrace();
            throw e;
        }
    }

    public List getRejectedRateChart(int project_int) throws Exception {

        List<ProjectAnalytics> projectAnalyticsArr = new ArrayList<>();

        String sql = """
                     SELECT  
                         SUM(CASE 
                                 WHEN ta.taskApproval_status = 'approve' 
                                 THEN 1 
                                 ELSE 0 
                             END) AS approved_task,
                     
                         SUM(CASE 
                                 WHEN ta.taskApproval_status = 'reject' 
                                 THEN 1 
                                 ELSE 0 
                             END) AS rejected_task
                     
                     FROM task_approvals ta
                     
                     LEFT JOIN tasks t 
                     USING(task_id)
                     
                     LEFT JOIN sprints s
                     USING(sprint_id)
                     
                     WHERE s.project_id = ?
                     
                     GROUP BY t.task_id;
                     """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setInt(1, project_int);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ProjectAnalytics projectAnalytics = new ProjectAnalytics();
                projectAnalytics.setApprovedTask(rs.getInt("approved_task"));
                projectAnalytics.setRejectedTask(rs.getInt("rejected_task"));

                projectAnalyticsArr.add(projectAnalytics);
            }

            return projectAnalyticsArr;
        } catch (Exception e) {
            System.out.println("Exception occurs in getRejectedRateChart : " + e);
            e.printStackTrace();
            throw e;
        }
    }

    public List getReworkGraphData(int project_id) throws Exception {
        List<ProjectAnalytics> projectAnalyticsesArr = new ArrayList<>();

        String sql = """
                     SELECT s.sprint_id, SUM(CASE WHEN ta.taskApproval_status = "reject" THEN 1 ELSE 0 END) AS rejectedTask
                                          FROM sprints s
                                          RIGHT JOIN tasks t 
                                          USING(sprint_id)
                                          RIGHT JOIN task_approvals ta
                                          USING(task_id)
                                          where s.project_id = ?
                                          GROUP BY sprint_id;
                     """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, project_id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ProjectAnalytics projectAnalytics = new ProjectAnalytics();
                projectAnalytics.setSprintId(rs.getInt("sprint_id"));
                projectAnalytics.setRejectedTask(rs.getInt("rejectedTask"));

                projectAnalyticsesArr.add(projectAnalytics);
            }

            return projectAnalyticsesArr;
        } catch (Exception e) {
            System.out.println("Exception occurs in getReworkGraph : " + e);
            e.printStackTrace();
            throw e;
        }
    }

//    public List getBurnDownData_Default(int project_id) throws Exception {
//
//        List<ProjectAnalytics> projectAnalyticsesArr = new ArrayList<>();
//
//        String sql = """
//                     SELECT 
//                         t.actual_endDate,
//                         COUNT(*) AS completed_task,
//                         total.total_task,
//                         total.sprint_start_date
//                     FROM tasks t
//                     JOIN (
//                         SELECT 
//                             sprint_id,
//                             COUNT(*) AS total_task,
//                             s.sprint_start_date AS sprint_start_date
//                         FROM tasks
//                         JOIN sprints s
//                         USING(sprint_id)
//                         WHERE sprint_id = (
//                             SELECT sprint_id
//                             FROM sprints 
//                             WHERE project_id = ?
//                             ORDER BY sprint_id ASC
//                             LIMIT 1
//                         )
//                         GROUP BY sprint_id
//                     ) total
//                     ON t.sprint_id = total.sprint_id
//                     WHERE t.sprint_id = (
//                         SELECT sprint_id
//                         FROM sprints
//                         WHERE project_id = ?
//                         ORDER BY sprint_id ASC
//                         LIMIT 1
//                     )
//                     AND t.actual_endDate IS NOT NULL
//                     GROUP BY 
//                         t.actual_endDate, 
//                         total.total_task,
//                         total.sprint_start_date
//                     ORDER BY t.actual_endDate;
//                     """;
//
//        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
//            ps.setInt(1, project_id);
//            ps.setInt(2, project_id);
//            
//            ResultSet rs = ps.executeQuery();
//
//            while (rs.next()) {
//                ProjectAnalytics projectAnalytics = new ProjectAnalytics();
//                projectAnalytics.setActualEndDate(rs.getString("actual_endDate"));
//                projectAnalytics.setCompletedTask(rs.getInt("completed_task"));
//                projectAnalytics.setTotalTask(rs.getInt("total_task"));
//                
//                Sprint sprint = new Sprint();
//                sprint.setSprint_start_date(rs.getString("sprint_start_date"));
//                projectAnalytics.setSprint(sprint);
//
//                projectAnalyticsesArr.add(projectAnalytics);
//            }
//
//            return projectAnalyticsesArr;
//        } catch (Exception e) {
//            System.out.println("Exception occured in getBurnDownData : " + e);
//            e.printStackTrace();
//            throw e;
//        }
//    }
    
    public List getBurnDownData_Default(int project_id) throws Exception {

        List<ProjectAnalytics> projectAnalyticsesArr = new ArrayList<>();

        String sql = """
                     SELECT 
                         DATE(t.task_start_date) AS task_start_date,
                         DATE(t.task_end_date) AS task_end_Date,
                         total.sprint_start_date,
                         total.sprint_end_date,
                         total.sprint_id
                         
                     
                     FROM tasks t
                     
                     JOIN (
                         SELECT 
                             s.sprint_id,
                             s.sprint_start_date AS sprint_start_date,
                     		s.sprint_end_date AS sprint_end_date
                         FROM tasks
                         JOIN sprints s
                         USING(sprint_id)
                     
                         WHERE sprint_id = (
                             SELECT sprint_id
                             FROM sprints 
                             WHERE project_id = ?
                             ORDER BY sprint_id ASC
                             LIMIT 1
                         )
                     
                         GROUP BY sprint_id
                     ) total
                     
                     ON t.sprint_id = total.sprint_id
                     
                     WHERE t.sprint_id = (
                         SELECT sprint_id
                         FROM sprints
                         WHERE project_id = ?
                         ORDER BY sprint_id ASC
                         LIMIT 1
                     )
                     
                     ORDER BY task_start_date ASC;
                     """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, project_id);
            ps.setInt(2, project_id);
            
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ProjectAnalytics projectAnalytics = new ProjectAnalytics();
                
                projectAnalytics.setTaskStartDate(rs.getObject("task_start_date", LocalDate.class));
                projectAnalytics.setTaskEndDate(rs.getObject("task_end_Date", LocalDate.class));
                projectAnalytics.setSprintStartDate(rs.getObject("sprint_start_date", LocalDate.class));
                projectAnalytics.setSprintEndDate(rs.getObject("sprint_end_date", LocalDate.class));
                projectAnalytics.setSprintId(rs.getInt("sprint_id"));

                projectAnalyticsesArr.add(projectAnalytics);
            }

            return projectAnalyticsesArr;
        } catch (Exception e) {
            System.out.println("Exception occured in getBurnDownData : " + e);
            e.printStackTrace();
            throw e;
        }
    }

    public List getBurnDownData(int sprint_id) throws Exception {

        List<ProjectAnalytics> projectAnalyticsesArr = new ArrayList<>();

        String sql = """
                    SELECT 
                                            DATE(t.task_start_date) AS task_start_date,
                                            DATE(t.task_end_date) AS task_end_Date,
                                            total.sprint_start_date,
                                            total.sprint_end_date
                                            
                                        
                                        FROM tasks t
                                        
                                        JOIN (
                                            SELECT 
                                                sprint_id,
                                                s.sprint_start_date AS sprint_start_date,
                                            s.sprint_end_date AS sprint_end_date
                                            FROM tasks
                                            JOIN sprints s
                                            USING(sprint_id)
                                        
                                            WHERE sprint_id = ?
                                        
                                            GROUP BY sprint_id
                                        ) total
                                        
                                        ON t.sprint_id = total.sprint_id
                                        
                                        WHERE t.sprint_id = ?
                                        
                                        ORDER BY task_start_date ASC;                   
                     """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, sprint_id);
            ps.setInt(2, sprint_id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
               ProjectAnalytics projectAnalytics = new ProjectAnalytics();
                
                projectAnalytics.setTaskStartDate(rs.getObject("task_start_date", LocalDate.class));
                projectAnalytics.setTaskEndDate(rs.getObject("task_end_Date", LocalDate.class));
                projectAnalytics.setSprintStartDate(rs.getObject("sprint_start_date", LocalDate.class));
                projectAnalytics.setSprintEndDate(rs.getObject("sprint_end_date", LocalDate.class));

                projectAnalyticsesArr.add(projectAnalytics);
            }

            return projectAnalyticsesArr;
        } catch (Exception e) {
            System.out.println("Exception occured in getBurnDownData : " + e);
            e.printStackTrace();
            throw e;
        }
    }

}
