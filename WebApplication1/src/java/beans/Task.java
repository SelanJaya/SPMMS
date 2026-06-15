/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

import java.time.LocalDate;
import beans.TaskAssignment;
import beans.TaskApproval;
import java.util.List;

/**
 *
 * @author HP
 */
public class Task {

    private int task_id, sprint_Id;
    private Integer backlog_id;
    //private Integer task_dependency;
    private String task_name, task_desc, task_status, task_start_date, task_end_date, actual_endDate, actual_startDate, taskApproval_status;
    private TaskAssignment taskAssignment;
    private List<TaskDependency> taskDependencies;
   
    Backlog backlog;
    TaskApproval taskApproval;

    public Task() {
    }

    public Task(int task_id, int sprint_Id, String task_name, String task_desc, String task_status, String task_start_date, String task_end_date) {
        this.task_id = task_id;
        //this.task_dependency = task_dependency;
        this.sprint_Id = sprint_Id;
        this.task_name = task_name;
        this.task_desc = task_desc;
        this.task_status = task_status;
        this.task_start_date = task_start_date;
        this.task_end_date = task_end_date;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

//    public void setTask_dependency(Integer task_dependency) {
//        this.task_dependency = task_dependency;
//    }
    public void setBacklog(Backlog backlog) {
        this.backlog = backlog;
    }

    public void setTaskApproval(TaskApproval taskApproval) {
        this.taskApproval = taskApproval;
    }

    public void setSprint_Id(int sprint_Id) {
        this.sprint_Id = sprint_Id;
    }

    public void setBacklog_id(Integer backlog_id) {
        this.backlog_id = backlog_id;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public void setTask_desc(String task_desc) {
        this.task_desc = task_desc;
    }

    public void setTask_status(String task_status) {
        this.task_status = task_status;
    }

    public void setTask_start_date(String task_start_date) {
        this.task_start_date = task_start_date;
    }

    public void setTask_end_date(String task_end_date) {
        this.task_end_date = task_end_date;
    }

    public void setActual_endDate(String actual_endDate) {
        this.actual_endDate = actual_endDate;
    }

    public void setActual_startDate(String actual_startDate) {
        this.actual_startDate = actual_startDate;
    }

    public void setTaskAssignment(TaskAssignment taskAssignment) {
        this.taskAssignment = taskAssignment;
    }

    public void setTaskDepedencies(List<TaskDependency> taskDependencies) {
        this.taskDependencies = taskDependencies;
    }

    public int getTask_id() {
        return task_id;
    }

    public Backlog getBacklog() {
        return backlog;
    }

    public TaskApproval getTaskApproval() {
        return taskApproval;
    }

//    public Integer getTask_dependency() {
//        return task_dependency;
//    }
    public int getSprint_Id() {
        return sprint_Id;
    }

    public int getBacklog_id() {
        return backlog_id;
    }

    public String getTask_name() {
        return task_name;
    }

    public String getTask_desc() {
        return task_desc;
    }

    public String getTask_status() {
        return task_status;
    }

    public LocalDate getTask_start_date() {
        return LocalDate.parse(task_start_date);
    }

    public LocalDate getTask_end_date() {
        return LocalDate.parse(task_end_date);
    }

    public LocalDate getActual_endDate() {
        return LocalDate.parse(actual_endDate) ;
    }

    public LocalDate getActual_startDate() {
        return LocalDate.parse(actual_startDate) ;
    }
    
    public TaskAssignment getTaskAssignment() {
        return taskAssignment;
    }

    public List<TaskDependency> getTaskDepedencies() {
        return taskDependencies;
    }
}
