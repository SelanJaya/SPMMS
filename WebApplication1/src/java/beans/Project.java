/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author HP
 */
public class Project implements Serializable {

    private int projectId, projCreatedBy;
    private String projectName, projectDesc, projectStatus, projectType, projectClient, projStartDate, projEndDate, projCreatedAt;
    private Double project_risk_score;

    public Project() {
    }

    //Contructor for update project
    public Project(String projectName, String projectDesc, String projectStatus, String projectType, String projectClient, String projStartDate, String projEndDate, int createdBy) {
        this.projectName = projectName;
        this.projectDesc = projectDesc;
        this.projectStatus = projectStatus;
        this.projectType = projectType;
        this.projectClient = projectClient;
        this.projStartDate = projStartDate;
        this.projEndDate = projEndDate;
        this.projCreatedBy = createdBy;
    }

    //Contructor for update project
    public Project(int projectId, String projectName, String projectDesc, String projectStatus, String projectType, String projectClient, String projStartDate, String projEndDate) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectDesc = projectDesc;
        this.projectStatus = projectStatus;
        this.projectType = projectType;
        this.projectClient = projectClient;
        this.projStartDate = projStartDate;
        this.projEndDate = projEndDate;
    }

    //Contructor for update project
    public Project(int projectId, String projectName, String projectDesc, String projectType, String projectClient, String projStartDate, String projEndDate) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectDesc = projectDesc;
        this.projectType = projectType;
        this.projectClient = projectClient;
        this.projStartDate = projStartDate;
        this.projEndDate = projEndDate;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public void setProjCreatedBy(int projCreatedBy) {
        this.projCreatedBy = projCreatedBy;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setProjectDesc(String projectDesc) {
        this.projectDesc = projectDesc;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public void setProjectClient(String projectClient) {
        this.projectClient = projectClient;
    }

    public void setProjStartDate(String projStartDate) {
        this.projStartDate = projStartDate;
    }

    public void setProjEndDate(String projEndDate) {
        this.projEndDate = projEndDate;
    }

    public void setProjCreatedAt(String projCreatedAt) {
        this.projCreatedAt = projCreatedAt;
    }

    public void setProject_risk_score(Double project_risk_score) {
        this.project_risk_score = project_risk_score;
    }

    public int getProjectId() {
        return projectId;
    }

    public int getProjCreatedBy() {
        return projCreatedBy;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getProjectDesc() {
        return projectDesc;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public String getProjectType() {
        return projectType;
    }

    public String getProjectClient() {
        return projectClient;
    }

    public LocalDate getProjStartDate() {
        return LocalDate.parse(projStartDate);
    }

    public LocalDate getProjEndDate() {
        return LocalDate.parse(projEndDate);
    }
    
    public String getProjCreatedAt() {
        if (projCreatedAt == null || projCreatedAt.isEmpty()) {
            return "";
        }

        LocalDateTime dateTime = LocalDateTime.parse(projCreatedAt);

        DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern("dd MM yy HH:mm");

        return dateTime.format(formatter);
    }

    public Double getProject_risk_score() {
        return project_risk_score;
    }
}
