/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import beans.User;
import beans.Project;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HP
 */
public class ProjectDAO {

    public List<Project> projectInfo(int user_id) {
        // FIX 1: Corrected SQL syntax (added missing comma after project_id)
        // FIX 2: Filter by 'proj_created_by' instead of 'project_id' to get ALL user projects
        String sql = "SELECT p.project_id, p.project_name, p.project_desc, "
                + "p.project_status, p.proj_start_date, p.proj_end_date "
                + "FROM projects p "
                + "JOIN users u ON p.proj_created_by = u.user_id "
                + "WHERE u.user_id = ? AND p.project_status != 'Archive'";

        // FIX 3: Initialize the list properly (Do NOT set to null)
        List<Project> projectArr = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, user_id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Project project = new Project();

                // FIX 4: Map the correct columns to the correct setters
                project.setProjectId(rs.getInt("project_id"));
                project.setProjectName(rs.getString("project_name")); // Use correct column
                project.setProjectDesc(rs.getString("project_desc")); // Use description setter
                project.setProjectStatus(rs.getString("project_status"));

                // FIX 5: Use distinct setters for Start and End dates
                project.setProjStartDate(rs.getObject("proj_start_date", java.time.LocalDate.class));
                project.setProjEndDate(rs.getObject("proj_end_date", java.time.LocalDate.class));

                projectArr.add(project);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Print the results
        if (projectArr.isEmpty()) {
            System.out.println("No projects found for user ID: " + user_id);
        } else {
            System.out.println("--- Project Info for User " + user_id + " ---");
            for (Project project : projectArr) {
                System.out.println(project);
            }
            System.out.println("-----------------------------------");
        }

        return projectArr;
    }

    public boolean createProject(Project project) {
        String sql = "INSERT INTO projects (project_name, project_desc, project_status, "
                + "proj_start_date, proj_end_date, proj_created_by) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, project.getProjectName());
            ps.setString(2, project.getProjectDesc());
            // Default new projects to 'Active' or 'Pending'
            ps.setString(3, project.getProjectStatus());

            // Convert LocalDate to SQL Date
            ps.setObject(4, project.getProjStartDate());
            ps.setObject(5, project.getProjEndDate());
            ps.setInt(6, project.getProjCreatedBy());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Project ProjectInfoById(int projectId) {
        String sql = "SELECT * FROM projects WHERE project_id = ?";
        Project project = null;

        try (Connection connection = DBConnection.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, projectId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                project = new Project();
                project.setProjectId(rs.getInt("project_id"));
                project.setProjectName(rs.getString("project_name"));
                project.setProjectDesc(rs.getString("project_desc"));
                project.setProjectStatus(rs.getString("project_status"));

                // Modern Date Mapping
                project.setProjStartDate(rs.getObject("proj_start_date", java.time.LocalDate.class));
                project.setProjEndDate(rs.getObject("proj_end_date", java.time.LocalDate.class));
                project.setProjCreatedAt(rs.getObject("proj_created_at", java.time.LocalDateTime.class));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return project;
    }

    public boolean updateProject(Project project) {
        String sql = "UPDATE projects SET project_name = ?, project_desc = ?, "
                + "project_status = ?, proj_start_date = ?, proj_end_date = ? "
                + "WHERE project_id = ?";

        try (Connection connection = DBConnection.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, project.getProjectName());
            ps.setString(2, project.getProjectDesc());
            ps.setString(3, project.getProjectStatus());

            // Mapping LocalDates to SQL
            ps.setObject(4, project.getProjStartDate());
            ps.setObject(5, project.getProjEndDate());

            // The WHERE clause ID
            ps.setInt(6, project.getProjectId());

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteProject(int projectId) {
        String sql = "DELETE FROM projects WHERE project_id = ?";
        boolean isDeleted = false;

        try (Connection conn = DBConnection.getConnection(); // Your DB connection method
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, projectId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                isDeleted = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isDeleted;
    }

    public List<Project> getArchivedProjectsByUserId(int userId) {
        List<Project> archivedList = new ArrayList<>();
        String sql = "SELECT project_id, project_name, project_desc, proj_created_at FROM projects WHERE proj_created_by = ? AND "
                + "project_status = 'Archive'";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Project p = new Project();
                p.setProjectId(rs.getInt("project_id"));
                p.setProjectName(rs.getString("project_name"));
                p.setProjectDesc(rs.getString("project_desc"));
                p.setProjCreatedAt(rs.getObject("proj_created_at", java.time.LocalDateTime.class));
                archivedList.add(p);

                System.out.println("----------------------------------------");
                System.out.println("Project ID   : " + rs.getInt("project_id"));
                System.out.println("Name         : " + rs.getString("project_name"));
                System.out.println("Description  : " + rs.getString("project_desc"));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return archivedList;
    }

    public boolean updateProjectStatus(int projectId) {
        // Standard SQL for updating a specific column
        String sql = "UPDATE projects SET project_status = 'Active' WHERE project_id = ?";
        boolean isUpdated = false;

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            // Set the ID to target the specific row
            ps.setInt(1, projectId);

            // executeUpdate returns the number of rows affected
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isUpdated = true;
                System.out.println("Status updated successfully for Project ID: " + projectId);
            }

        } catch (SQLException e) {
            System.err.println("Error updating project status: " + e.getMessage());
            e.printStackTrace();
        }

        return isUpdated;
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        // Querying exactly the columns shown in your schema images
        String sql = "SELECT username, email FROM user";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                // 'username' and 'email' match your database column names
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));

                userList.add(user);
            }

            // Console log for your debugging
            System.out.println("DAO Log: " + userList.size() + " users retrieved.");

        } catch (SQLException e) {
            System.err.println("SQL Error in getAllUsers: " + e.getMessage());
            e.printStackTrace();
        }

        return userList;
    }
}
