/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 *
 * @author HP
 */
public class ProjectAnalytics {
    
    int completedTask, totalSprint, totalTask, rejectedTask, approvedTask, totalApprovedTask, totalRejectedTask, sprintId, remainingTask;
    LocalDate taskEndDate, taskStartDate, sprintStartDate, sprintEndDate;
    double sprintSuccRate, AVGVelocity, CycleTime, RejectionRate;
    List<ProjectAnalytics> velocityGraphData, reworkGraphData, burnDownChartData;

    public ProjectAnalytics() {
    }

    public void setCompletedTask(int completedTask) {
        this.completedTask = completedTask;
    }

    public void setTotalSprint(int totalSprint) {
        this.totalSprint = totalSprint;
    }

    public void setTotalTask(int totalTask) {
        this.totalTask = totalTask;
    }

    public void setRejectedTask(int rejected_task) {
        this.rejectedTask = rejected_task;
    }

    public void setTotalApprovedTask(int totalApprovedTask) {
        this.totalApprovedTask = totalApprovedTask;
    }

    public void setTotalRejectedTask(int totalRejectedTask) {
        this.totalRejectedTask = totalRejectedTask;
    }

    public void setApprovedTask(int approved_task) {
        this.approvedTask = approved_task;
    }

    public void setSprintSuccRate(double sprintSuccRate) {
        this.sprintSuccRate = sprintSuccRate;
    }

    public void setAVGVelocity(double AVGVelocity) {
        this.AVGVelocity = AVGVelocity;
    }

    public void setCycleTime(double CycleTime) {
        this.CycleTime = CycleTime;
    }

    public void setRejectionRate(double RejectionRate) {
        this.RejectionRate = RejectionRate;
    }

    public void setSprintId(int sprintId) {
        this.sprintId = sprintId;
    }

    public void setRemainingTask(int remainingTask) {
        this.remainingTask = remainingTask;
    }

    public void setTaskEndDate(LocalDate taskEndDate) {
        this.taskEndDate = taskEndDate;
    }

    public void setTaskStartDate(LocalDate taskStartDate) {
        this.taskStartDate = taskStartDate;
    }

    public void setSprintStartDate(LocalDate sprintStartDate) {
        this.sprintStartDate = sprintStartDate;
    }

    public void setSprintEndDate(LocalDate sprintEndDate) {
        this.sprintEndDate = sprintEndDate;
    }
    
    public void setVelocityGraphData(List<ProjectAnalytics> velocityGraphData) {
        this.velocityGraphData = velocityGraphData;
    }

    public void setReworkGraphData(List<ProjectAnalytics> reworkGraphData) {
        this.reworkGraphData = reworkGraphData;
    }

    public void setBurnDownChartData(List<ProjectAnalytics> burnDownChartData) {
        this.burnDownChartData = burnDownChartData;
    }

    public int getTotalSprint() {
        return totalSprint;
    }

    public int getTotalTask() {
        return totalTask;
    }

    public int getRejectedTask() {
        return rejectedTask;
    }
    
    public int getCompletedTask() {
        return completedTask;
    }
    
    public int getTotalApprovedTask() {
        return totalApprovedTask;
    }

    public int getTotalRejectedTask() {
        return totalRejectedTask;
    }

    public int getApprovedTask() {
        return approvedTask;
    }

    public double getSprintSuccRate() {
        return sprintSuccRate;
    }

    public double getAVGVelocity() {
        return AVGVelocity;
    }

    public double getCycleTime() {
        return CycleTime;
    }

    public double getRejectionRate() {
        return RejectionRate;
    }

    public LocalDate getTaskEndDate() {
        return taskEndDate;
    }

    public LocalDate getTaskStartDate() {
        return taskStartDate;
    }

    public LocalDate getSprintStartDate() {
        return sprintStartDate;
    }

    public LocalDate getSprintEndDate() {
        return sprintEndDate;
    }

    public List<ProjectAnalytics> getVelocityGraphData() {
        return velocityGraphData;
    }

    public List<ProjectAnalytics> getReworkGraphData() {
        return reworkGraphData;
    }
    
    public int getSprintId() {
        return sprintId;
    }

    public int getRemainingTask() {
        return remainingTask;
    }

    public List<ProjectAnalytics> getBurnDownChartData() {
        return burnDownChartData;
    }
}
