/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

import java.io.Serializable;

/**
 *
 * @author HP
 */
public class TaskAssignment implements Serializable {

    private int task_id, task_assigned_to, task_assigned_by, task_assigned_at;
    private String task_assigned_to_Role, user_name, removal_reason;

    public TaskAssignment() {
    }

    public TaskAssignment(int task_id, int task_assigned_to, int task_assigned_at) {
        this.task_id = task_id;
        this.task_assigned_to = task_assigned_to;
        this.task_assigned_at = task_assigned_at;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public void setTask_assigned_to(int task_assigned_to) {
        this.task_assigned_to = task_assigned_to;
    }

    public void setTask_assigned_by(int task_assigned_by) {
        this.task_assigned_by = task_assigned_by;
    }

    public void setTask_assigned_at(int task_assigned_at) {
        this.task_assigned_at = task_assigned_at;
    }

    public void setTask_assigned_to_Role(String task_assigned_to_Role) {
        this.task_assigned_to_Role = task_assigned_to_Role;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setRemoval_reason(String removal_reason) {
        this.removal_reason = removal_reason;
    }

   

    public int getTask_id() {
        return task_id;
    }

    public int getTask_assigned_to() {
        return task_assigned_to;
    }

    public int getTask_assigned_at() {
        return task_assigned_at;
    }

    public String getTask_assigned_to_Role() {
        return task_assigned_to_Role;
    }

     public String getUser_name() {
        return user_name;
    }

    public int getTask_assigned_by() {
        return task_assigned_by;
    }

    public String getRemoval_reason() {
        return removal_reason;
    }
}