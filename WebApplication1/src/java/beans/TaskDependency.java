/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

/**
 *
 * @author HP
 */
public class TaskDependency {
    
    private int task_id, depend_on_task_id;
    String depend_on_task_Name;

    public TaskDependency() {
    }

    public TaskDependency(Integer task_id, int depend_on_task_id) {
        this.task_id = task_id;
        this.depend_on_task_id = depend_on_task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public void setDepend_on_task_id(int depend_on_task_id) {
        this.depend_on_task_id = depend_on_task_id;
    }

    public void setDepend_on_task_Name(String depend_on_task_Name) {
        this.depend_on_task_Name = depend_on_task_Name;
    }

    public int getTask_id() {
        return task_id;
    }

    public int getDepend_on_task_id() {
        return depend_on_task_id;
    }

    public String getDepend_on_task_Name() {
        return depend_on_task_Name;
    }
    
}
