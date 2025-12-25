/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author HP
 */
public class Project {

    private int projectId, projCreatedBy;
    private String projectName, projectDesc, projectStatus;
    private LocalDate projStartDate;
    private LocalDate projEndDate;
    private LocalDateTime projCreatedAt;

    public Project() {
    }
    
    //Contructor for create project
    public Project(int projCreatedBy, String projectName, String projectDesc, String projectStatus, LocalDate projStartDate, LocalDate projEndDate) {
        this.projCreatedBy = projCreatedBy;
        this.projectName = projectName;
        this.projectDesc = projectDesc;
        this.projectStatus = projectStatus;
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

    public void setProjStartDate(LocalDate projStartDate) {
        this.projStartDate = projStartDate;
    }

    public void setProjEndDate(LocalDate projEndDate) {
        this.projEndDate = projEndDate;
    }

    public void setProjCreatedAt(LocalDateTime projCreatedAt) {
        this.projCreatedAt = projCreatedAt;
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

    public LocalDate getProjStartDate() {
        return projStartDate;
    }

    public LocalDate getProjEndDate() {
        return projEndDate;
    }

    public String getProjCreatedAt() {
        if (this.projCreatedAt == null) {
            return "";
        }

        // Define the pattern: DD MM YY HH MM
        // dd = Day, MM = Month, yy = Year (2 digits), HH = 24hr, mm = minutes
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yy  HH:mm");

        return this.projCreatedAt.format(formatter);
    }

}
