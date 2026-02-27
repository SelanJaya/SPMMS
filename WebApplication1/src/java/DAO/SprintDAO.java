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
import beans.Sprint;

/**
 *
 * /**
 *
 * @author HP
 */
public class SprintDAO {

    public List getSprintsData(int project_id) throws Exception{

        List<Sprint> sprintList = new ArrayList<>();

        String sql = """
                    SELECT sprint_id, sprint_name, sprint_start_date, sprint_end_date, 
                    sprint_goal, sprint_status, restrospective_notes, review_notes
                    FROM sprints WHERE project_id = ?
                    """;
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, project_id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Sprint sprint = new Sprint();
                sprint.setSprint_id(rs.getInt("sprint_id"));
                sprint.setSprint_name(rs.getString("sprint_name"));
                sprint.setSprint_start_date(rs.getString("sprint_start_date"));
                sprint.setSprint_end_date(rs.getString("sprint_end_date"));
                sprint.setSprint_goal(rs.getString("sprint_goal"));
                sprint.setSprint_status(rs.getString("sprint_status"));
                sprint.setRestrospective_notes(rs.getString("restrospective_notes"));
                sprint.setReview_notes(rs.getString("review_notes"));

                sprintList.add(sprint);
            }
            return sprintList;
        } catch (Exception e) {
            System.out.println("Exception Ocurr:" + e);
            throw e;
        }
    }

    public int insertSprintDetails(Sprint sprint) throws Exception{

        String sql = "INSERT INTO sprints (project_id, sprint_name, sprint_start_date, "
                + "sprint_end_date, sprint_goal, sprint_status, "
                + "restrospective_notes, review_notes, sprint_created_at)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, sprint.getProject_id());
            ps.setString(2, sprint.getSprint_name());
            ps.setObject(3, sprint.getSprint_start_date());
            ps.setObject(4, sprint.getSprint_end_date());
            ps.setString(5, sprint.getSprint_goal());
            ps.setString(6, sprint.getSprint_status());
            ps.setString(7, sprint.getRestrospective_notes());
            ps.setString(8, sprint.getReview_notes());
            ps.setTimestamp(9, new java.sql.Timestamp(System.currentTimeMillis()));

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
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return -1;
    }

    public void updateSprintDetails(Sprint sprint) throws Exception{

        String sql = """
                     UPDATE sprints
                     SET sprint_name = ?, 
                         sprint_start_date = ?, 
                         sprint_end_date = ?, 
                         sprint_goal = ?, 
                         sprint_status = ?, 
                         restrospective_notes = ?, 
                         review_notes = ?
                     WHERE sprint_id = ?
                     """;
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

      
            ps.setString(1, sprint.getSprint_name());
            System.out.println("IN DAO : " + sprint.getSprint_name());
            ps.setObject(2, sprint.getSprint_start_date());
            ps.setObject(3, sprint.getSprint_end_date());
            ps.setString(4, sprint.getSprint_goal());
            ps.setString(5, sprint.getSprint_status());
            ps.setString(6, sprint.getRestrospective_notes());
            ps.setString(7, sprint.getReview_notes());
            ps.setInt(8, sprint.getSprint_id());

            ps.executeUpdate() ;
               
        } catch (SQLException e) {
            e.printStackTrace();
            throw  e;
        }
    }
    
    public void deleteSprintDetails(int sprint_id) throws Exception{

        String sql = """
                     DELETE FROM sprints
                     WHERE sprint_id = ?
                     """;
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, sprint_id);

            ps.executeUpdate() ;
               
        } catch (SQLException e) {
            e.printStackTrace();
            throw  e;
        }
    }

}
