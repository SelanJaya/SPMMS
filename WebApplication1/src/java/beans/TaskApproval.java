/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

/**
 *
 * @author HP
 */
public class TaskApproval {

    private int task_id, approved_by;
    private Integer approval_id;
    private String taskApproval_status, approved_at, remarks ;

    public TaskApproval() {
    }

    public void setApproval_id(int approval_id) {
        this.approval_id = approval_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public void setApproved_by(Integer approved_by) {
        this.approved_by = approved_by;
    }

    public void setTaskApproval_status(String taskApproval_status) {
        this.taskApproval_status = taskApproval_status;
    }

    public void setApproved_at(String approved_at) {
        this.approved_at = approved_at;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getApproval_id() {
        return approval_id;
    }

    public int getTask_id() {
        return task_id;
    }

    public int getApproved_by() {
        return approved_by;
    }

    public String getTaskApproval_status() {
        return taskApproval_status;
    }

    public String getApproved_at() {
        return approved_at;
    }

    public String getRemarks() {
        return remarks;
    }
    
    

}
