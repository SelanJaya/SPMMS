/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author HP
 */
public class RiskPredictionResponse {
    
    @SerializedName("projectId")
    private int projectId;
    private double riskScore;

    public RiskPredictionResponse() {
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public void setRiskScore(double riskScore) {
        this.riskScore = riskScore;
    }

    public int getProjectId() {
        return projectId;
    }

    public double getRiskScore() {
        return riskScore;
    }
    
    
    
}
