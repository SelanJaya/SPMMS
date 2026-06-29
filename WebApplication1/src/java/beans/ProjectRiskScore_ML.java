/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

import java.util.List;

/**
 *
 * @author HP
 */
public class ProjectRiskScore_ML {

    private int projectId;
    private int  total_story_points;
    private double completionRate;
    private double overduePercentage;
    private double rejectionRate;
    private double avgStoryPoints;
    private int projectDurationDays;
    private double riskScore;
    
    public ProjectRiskScore_ML() {
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public void setTotal_story_points(int total_story_points) {
        this.total_story_points = total_story_points;
    }

    public void setCompletionRate(double completionRate) {
        this.completionRate = completionRate;
    }

    public void setOverduePercentage(double overduePercentage) {
        this.overduePercentage = overduePercentage;
    }

    public void setRejectionRate(double rejectionRate) {
        this.rejectionRate = rejectionRate;
    }

    public void setAvgStoryPoints(double avgStoryPoints) {
        this.avgStoryPoints = avgStoryPoints;
    }

    public void setProjectDurationDays(int projectDurationDays) {
        this.projectDurationDays = projectDurationDays;
    }

    public void setRiskScore(double riskScore) {
        this.riskScore = riskScore;
    }

    public int getProjectId() {
        return projectId;
    }

    public int getTotal_story_points() {
        return total_story_points;
    }

    public double getCompletionRate() {
        return completionRate;
    }

    public double getOverduePercentage() {
        return overduePercentage;
    }

    public double getRejectionRate() {
        return rejectionRate;
    }

    public double getAvgStoryPoints() {
        return avgStoryPoints;
    }

    public int getProjectDurationDays() {
        return projectDurationDays;
    }

    public double getRiskScore() {
        return riskScore;
    }
}
