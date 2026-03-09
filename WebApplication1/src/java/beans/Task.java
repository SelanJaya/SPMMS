/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

import java.time.LocalDate;
import beans.TaskAssignment;

/**
 *
 * @author HP
 */
public class Task {

    private int task_id,sprint_Id;
    private Integer task_dependency;
    private String task_name, task_desc, task_status, task_start_date,task_end_date ;
    private TaskAssignment taskAssignment;

    public Task() {
    }

    public Task(int task_id, Integer task_dependency, int sprint_Id, String task_name, String task_desc, String task_status, String task_start_date, String task_end_date) {
        this.task_id = task_id;
        this.task_dependency = task_dependency;
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

    public void setTask_dependency(Integer task_dependency) {
        this.task_dependency = task_dependency;
    }

    public void setSprint_Id(int sprint_Id) {
        this.sprint_Id = sprint_Id;
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

    public void setTaskAssignment(TaskAssignment taskAssignment) {
        this.taskAssignment = taskAssignment;
    }
    
    public int getTask_id() {
        return task_id;
    }

    public Integer getTask_dependency() {
        return task_dependency;
    }

    public int getSprint_Id() {
        return sprint_Id;
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
        return  LocalDate.parse(task_start_date);
    }

    public LocalDate getTask_end_date() {
        return LocalDate.parse(task_end_date);
    }

    public TaskAssignment getTaskAssignment() {
        return taskAssignment;
    }
    
}
