/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import DAO.ProjectDAO;
import beans.DashboardInsight;
import beans.ProjectRiskScore_ML;
import beans.RiskPredictionResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HP
 */
public class DashboardInsightsService {

    public DashboardInsight getDashboardInsightService_PM(int user_id, String userRole) throws Exception {
        List<DashboardInsight> dashboardInsightsArr = new ArrayList<>();
        DashboardInsight dashboardInsights = new DashboardInsight();
        DashboardInsight dashboardInsightsTemp = new DashboardInsight();

        try {

            //Total project
            ProjectDAO projectDAO = new ProjectDAO();
            dashboardInsights.setTotalProject(projectDAO.getTotalProject(user_id));

            //number of task Overdue
            dashboardInsightsArr = projectDAO.getTotalOverduePerProject(user_id);
            dashboardInsights.setTasksOverdue(dashboardInsightsArr);

            int totalTaskOverdue = 0;
            for (DashboardInsight item : dashboardInsightsArr) {
                totalTaskOverdue += item.getTaskOverdue();
            }

            dashboardInsights.setSumTaskOverdue(totalTaskOverdue);

            //get pending recruitement
            dashboardInsightsTemp = projectDAO.getPendingRecruitement(user_id);

            dashboardInsights.setNoMissingRole(dashboardInsightsTemp.getNoMissingRole());
            dashboardInsights.setAssignmentPending_project(dashboardInsightsTemp.getAssignmentPending_project());

            if ("Project Manager".equalsIgnoreCase(userRole)) {
                //My Active Project
                dashboardInsights.setActiveProjects(projectDAO.getMYActiveProject(user_id));
            }

        } catch (Exception e) {
        }
        return dashboardInsights;
    }

    public DashboardInsight getDashboardInsightService_PO(int user_id) throws Exception {
        List<DashboardInsight> dashboardInsightsArr = new ArrayList<>();
        DashboardInsight dashboardInsights = new DashboardInsight();
        DashboardInsight dashboardInsightsTemp = new DashboardInsight();

        try {
            ProjectDAO projectDAO = new ProjectDAO();

            //My Active Project Summary
            dashboardInsights.setActiveProjects(projectDAO.getMYActiveProject(user_id));

        } catch (Exception e) {
        }
        return dashboardInsights;
    }

    public List ProjectRisk_MLService(int user_id) throws Exception {
        Map<Integer, ProjectRiskScore_ML> projectMap = new HashMap<>();

        DashboardInsight dashboardInsights = new DashboardInsight();
        try {
            ProjectDAO projectDAO = new ProjectDAO();

            //OverduePercentage
            projectMap = projectDAO.getOverduePercentagePerProject(user_id);
            projectMap = projectDAO.getTaskRejectionRatePerProject(projectMap, user_id);
            projectMap = projectDAO.getAVGStoryPointPerProject(projectMap, user_id);
            projectMap = projectDAO.getRemainingDatePerProject(projectMap, user_id);
            projectMap = projectDAO.getCompletionRatePerProject(projectMap, user_id);

            List<RiskPredictionResponse> predictions = new ArrayList<>();

            //MLPredictionService mLPredictionService = new MLPredictionService();
            //predictions = mLPredictionService.predictRisk(new ArrayList(projectMap.values()));

           
            return predictions;
        } catch (Exception e) {
            System.out.println("Exception occured at ProjectRisk_MLService : " + e);
            throw e;
        }
    }

}
