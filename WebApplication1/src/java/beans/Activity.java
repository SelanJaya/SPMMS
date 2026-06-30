/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

/**
 *
 * @author HP
 */
public class Activity {

    private int task_id, project_id, proj_assign_to, task_assigned_to, assigned_to;;
    private String activity, activityDate, activityType;

    public Activity() {
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public void setProj_assign_to(int proj_assign_to) {
        this.proj_assign_to = proj_assign_to;
    }

    public void setTask_assigned_to(int task_assigned_to) {
        this.task_assigned_to = task_assigned_to;
    }

    public void setAssigned_to(int assigned_to) {
        this.assigned_to = assigned_to;
    }
    
    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }
    
    public void setActivity(String activity) {
        this.activity = activity;
    }

    public void setActivityDate(String activityDate) {
        this.activityDate = activityDate;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public int getProject_id() {
        return project_id;
    }

    public int getProj_assign_to() {
        return proj_assign_to;
    }

    public int getTask_assigned_to() {
        return task_assigned_to;
    }

    public int getAssigned_to() {
        return assigned_to;
    }
    
    
    public int getTask_id() {
        return task_id;
    }

    public String getActivity() {
        return activity;
    }

    public String getActivityDate() {
        return activityDate;
    }

    public String getActivityType() {
        return activityType;
    }
}
