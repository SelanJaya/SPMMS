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
    private String assigned_at;

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

    public LocalDateTime getAssigned_at() {
        return LocalDateTime.parse(assigned_at);
    }

}
