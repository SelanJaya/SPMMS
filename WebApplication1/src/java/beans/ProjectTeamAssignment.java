/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

import java.time.LocalDateTime;

/**
 *
 * @author HP
 */
public class ProjectTeamAssignment {

    private int projectTeam_id;
    private int project_id;
    private int assign_to;
    private int assign_by;
    private int removed_by;
    private String assigned_at, asign_to_Email, assign_to_username, removed_at, removal_reason;

    public ProjectTeamAssignment() {
    }

    public ProjectTeamAssignment(int project_id, int assign_to, int assign_by) {
        this.project_id = project_id;
        this.assign_to = assign_to;
        this.assign_by = assign_by;
    }

    public void setProjectTeam_id(int projectTeam_id) {
        this.projectTeam_id = projectTeam_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public void setAssign_to(int assign_to) {
        this.assign_to = assign_to;
    }

    public void setAssign_by(int assign_by) {
        this.assign_by = assign_by;
    }

    public void setAssigned_at(String assigned_at) {
        this.assigned_at = assigned_at;
    }

    public void setAsign_to_Email(String asign_to_Email) {
        this.asign_to_Email = asign_to_Email;
    }

    public void setAssign_to_username(String assign_to_username) {
        this.assign_to_username = assign_to_username;
    }

    public void setRemoved_by(int removed_by) {
        this.removed_by = removed_by;
    }

    public void setRemoved_at(String removed_at) {
        this.removed_at = removed_at;
    }

    public void setRemoval_reason(String removal_reason) {
        this.removal_reason = removal_reason;
    }
    
    public int getProjectTeam_id() {
        return projectTeam_id;
    }

    public int getProject_id() {
        return project_id;
    }

    public int getAssign_to() {
        return assign_to;
    }

    public int getAssign_by() {
        return assign_by;
    }

    public String getAsign_to_Email() {
        return asign_to_Email;
    }

    public String getAssign_to_username() {
        return assign_to_username;
    }

    public LocalDateTime getAssigned_at() {
        return LocalDateTime.parse(assigned_at);
    }
    
    public int getRemoved_by() {
        return removed_by;
    }

    public String getRemoved_at() {
        return removed_at;
    }

    public String getRemoval_reason() {
        return removal_reason;
    }
    

}
