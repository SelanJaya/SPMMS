/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import beans.Project;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import beans.ProjectTeamAssignment;
import beans.User;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HP
 */
public class projectTeamDAO {

    public projectTeamDAO() {
    }

    public boolean createProject(Project project) {
        // SQL query matching your schema columns
        String sql = "INSERT INTO project (project_name, project_desc, proj_start_date, proj_end_date, project_status, proj_created_by) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, project.getProjectName());
            ps.setString(2, project.getProjectDesc());
            // Map Java LocalDate to SQL Date
            ps.setDate(3, java.sql.Date.valueOf(project.getProjStartDate()));
            ps.setDate(4, java.sql.Date.valueOf(project.getProjEndDate()));
            ps.setString(5, project.getProjectStatus()); // e.g., 'Pending'
            ps.setInt(6, project.getProjCreatedBy()); // User ID of creator

            int result = ps.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean assignTeamMember(ProjectTeamAssignment teamAssignment) {
        String sql = "INSERT INTO project_assignments (project_id, proj_assign_to, proj_assign_by) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, teamAssignment.getProject_id()); //
            ps.setInt(2, teamAssignment.getAssign_to());    //
            ps.setInt(3, teamAssignment.getAssign_by());  //

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<User> getAssignedMembers(int projectId) {
        List<User> assignedUsers = new ArrayList<>();
        // SQL targets the columns from your user and team schemas
        String sql = "SELECT u.user_id, u.username, u.email, u.user_role FROM project_assignments pa "
                + "JOIN users u ON pa.proj_assign_to = u.user_id "
                + "WHERE pa.project_id = ? ";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, projectId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    // Map database columns to the Java object
                    user.setUser_id(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setUser_role(rs.getString("user_role"));
                    assignedUsers.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assignedUsers;
    }

    public boolean removeTeamMember(ProjectTeamAssignment teamAssignment) {
        // SQL targets the primary key 'projectTeamID'
        String sql = "DELETE FROM project_assignments WHERE project_id = ? AND proj_assign_by  = ? AND proj_assign_to  = ?";
        boolean isDeleted = false;

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, teamAssignment.getProject_id()); 
            ps.setInt(2, teamAssignment.getAssign_by()); 
            ps.setInt(3, teamAssignment.getAssign_to()); 

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isDeleted = true;
                System.out.println("DAO Log: Assignment ID " +  teamAssignment.getProject_id() + "  " + teamAssignment.getAssign_to() + " removed.");
            }

        } catch (SQLException e) {
            System.err.println("SQL Error in removeTeamMember: " + e.getMessage());
            e.printStackTrace();
        }

        return isDeleted;
    }
    
    
}
