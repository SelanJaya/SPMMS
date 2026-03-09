/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import beans.Backlog;
import java.util.List;

/**
 *
 * @author HP
 */
public class Sprint {
    private int sprint_id, project_id;
    private List<Backlog> backlog;
    private String sprint_name, sprint_goal, sprint_status, restrospective_notes, review_notes, sprint_start_date, sprint_end_date;
//    LocalDateTime sprint_created_at;

    public Sprint() {
    }    
    
    public Sprint(int sprint_id, int project_id, String sprint_name, String sprint_goal, String restrospective_notes, String review_notes, String sprint_start_date, String sprint_end_date) {
        this.sprint_id = sprint_id;
        this.project_id = project_id;
        this.sprint_name = sprint_name;
        this.sprint_goal = sprint_goal;
        this.restrospective_notes = restrospective_notes;
        this.review_notes = review_notes;
        this.sprint_start_date = sprint_start_date;
        this.sprint_end_date = sprint_end_date;
    }

    public void setSprint_id(int sprint_id) {
        this.sprint_id = sprint_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public void setBacklog(List<Backlog> backlog) {
        this.backlog = backlog;
    }

    public void setSprint_name(String sprint_name) {
        this.sprint_name = sprint_name;
    }

    public void setSprint_goal(String sprint_goal) {
        this.sprint_goal = sprint_goal;
    }

    public void setSprint_status(String sprint_status) {
        this.sprint_status = sprint_status;
    }

    public void setRestrospective_notes(String restrospective_notes) {
        this.restrospective_notes = restrospective_notes;
    }

    public void setReview_notes(String review_notes) {
        this.review_notes = review_notes;
    }

    public void setSprint_start_date(String sprint_start_date) {
        this.sprint_start_date = sprint_start_date;
    }

    public void setSprint_end_date(String sprint_end_date) {
        this.sprint_end_date = sprint_end_date;
    }

//    public void setSprint_created_at(LocalDateTime sprint_created_at) {
//        this.sprint_created_at = sprint_created_at;
//    }

    public int getSprint_id() {
        return sprint_id;
    }

    public int getProject_id() {
        return project_id;
    }

    public List<Backlog> getBacklog() {
        return backlog;
    }
   
    public String getSprint_name() {
        return sprint_name;
    }

    public String getSprint_goal() {
        return sprint_goal;
    }

    public String getSprint_status() {
        return sprint_status;
    }

    public String getRestrospective_notes() {
        return restrospective_notes;
    }

    public String getReview_notes() {
        return review_notes;
    }
    

    public LocalDate getSprint_start_date() {

    if (sprint_start_date == null || sprint_start_date.isEmpty()) {
        return null;
    }

    return LocalDate.parse(sprint_start_date);
}

    public LocalDate getSprint_end_date() {
        if (sprint_end_date == null || sprint_end_date.isEmpty()) {
        return null;
    }

    return LocalDate.parse(sprint_end_date);
    }
//
//    public LocalDateTime getSprint_created_at() {
//        return sprint_created_at;
//    }
//    
    
}
