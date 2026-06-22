/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;
import beans.Project;
import beans.Task;
import beans.Activity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 *
 * @author HP
 */
public class DashboardInsight {
    
    private String completedBacklog, activeBacklog, pendingBacklog, totalBacklog, project_name;
    private Map <String, ArrayList<String>> assignmentPending_project;
    private List<DashboardInsight> activeProjects, activeTasks, tasksOverdue;
    private int taskOverdue, sumTaskOverdue, totalProject, noMissingRole, taskDone, taskInProgress, taskToDo;
    List<Activity> activitys;
    //private Project project;

    public DashboardInsight() {
    }

    public void setCompletedBacklog(String completedBacklog) {
        this.completedBacklog = completedBacklog + "/" + getTotalBacklog();
    }

    public void setActiveBacklog(String activeBacklog) {
        this.activeBacklog = activeBacklog + "/" + getTotalBacklog();
    }

    public void setPendingBacklog(String pendingBacklog) {
        this.pendingBacklog = pendingBacklog + "/" + getTotalBacklog();
    }

    public void setTotalBacklog(String totalBacklog) {
        this.totalBacklog = totalBacklog;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public void setNoMissingRole(int noMissingRole) {
        this.noMissingRole = noMissingRole;
    }

    public void setAssignmentPending_project(Map<String, ArrayList<String>> assignmentPending_project) {
        this.assignmentPending_project = assignmentPending_project;
    }
    
    public void setActiveProjects(List<DashboardInsight> activeProjects) {
        this.activeProjects = activeProjects;
    }

    public void setActiveTasks(List<DashboardInsight> activeTasks) {
        this.activeTasks = activeTasks;
    }

    public void setTasksOverdue(List<DashboardInsight> tasksOverdue) {
        this.tasksOverdue = tasksOverdue;
    }

    public void setTaskOverdue(int taskOverdue) {
        this.taskOverdue = taskOverdue;
    }

    public void setSumTaskOverdue(int sumTaskOverdue) {
        this.sumTaskOverdue = sumTaskOverdue;
    }

    public void setTotalProject(int totalProject) {
        this.totalProject = totalProject;
    }

    public void setTaskDone(int taskDone) {
        this.taskDone = taskDone;
    }

    public void setTaskInProgress(int taskInProgress) {
        this.taskInProgress = taskInProgress;
    }

    public void setTaskToDo(int taskToDo) {
        this.taskToDo = taskToDo;
    }

    public void setActivitys(List<Activity> activitys) {
        this.activitys = activitys;
    }
    
//    public void setProject(Project project) {
//        this.project = project;
//    }

    public String getCompletedBacklog() {
        return completedBacklog;
    }

    public String getActiveBacklog() {
        return activeBacklog;
    }

    public String getPendingBacklog() {
        return pendingBacklog;
    }

    public String getTotalBacklog() {
        return totalBacklog;
    }

    public List<DashboardInsight> getActiveProjects() {
        return activeProjects;
    }

    public List<DashboardInsight> getActiveTask() {
        return activeTasks;
    }

    public List<DashboardInsight> getTasksOverdue() {
        return tasksOverdue;
    }

    public String getProject_name() {
        return project_name;
    }

    public int getNoMissingRole() {
        return noMissingRole;
    }
    
    public Map<String, ArrayList<String>> getAssignmentPending_project() {
        return assignmentPending_project;
    }
    

//    public Project getProject() {
//        return project;
//    }

    public int getTaskOverdue() {
        return taskOverdue;
    }

    public int getSumTaskOverdue() {
        return sumTaskOverdue;
    }

    public int getTotalProject() {
        return totalProject;
    }

    public int getTaskDone() {
        return taskDone;
    }

    public int getTaskInProgress() {
        return taskInProgress;
    }

    public int getTaskToDo() {
        return taskToDo;
    }

    public List<Activity> getActivitys() {
        return activitys;
    }
}
