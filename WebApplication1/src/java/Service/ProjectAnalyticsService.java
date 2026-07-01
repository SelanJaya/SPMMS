/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import java.time.LocalDate;
import beans.ProjectAnalytics;
import beans.Project;
import DAO.ProjectAnalyticsDAO;
import DAO.ProjectDAO;
import beans.Task;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.sql.Date;
import org.apache.jasper.tagplugins.jstl.core.Catch;

/**
 *
 * @author HP
 */
public class ProjectAnalyticsService {

    public ProjectAnalytics getProjectAnalytics_All(int project_id) throws Exception {
        List<ProjectAnalytics> proAnalysisArr = new ArrayList<>();
        System.out.println("Service reached");
        try {

            ProjectAnalytics projectAnalytics = new ProjectAnalytics();

            projectAnalytics.setSprintSuccRate(calculateSprintSuccRate(project_id));
            projectAnalytics.setAVGVelocity(calculateAVGVelocity(project_id));
            projectAnalytics.setCycleTime(calculateCycleTime(project_id));
            projectAnalytics.setRejectionRate(calculateRejectionRate(project_id));

            //Velocity Graph 
            ProjectAnalyticsDAO projectAnalyticsDAO = new ProjectAnalyticsDAO();
            projectAnalytics.setVelocityGraphData(projectAnalyticsDAO.getAVGSpSuccRateData(project_id));

            ProjectAnalytics projectAnalyticsAR = new ProjectAnalytics();
            projectAnalyticsAR = calulateApproveRateChart(project_id);

            // Approve Rate 
            projectAnalytics.setTotalApprovedTask(projectAnalyticsAR.getTotalApprovedTask());
            projectAnalytics.setTotalRejectedTask(projectAnalyticsAR.getTotalRejectedTask());

            projectAnalytics.setReworkGraphData(projectAnalyticsDAO.getReworkGraphData(project_id));

            projectAnalytics.setBurnDownChartData(calculateBurnDownChart(null, project_id));
            return projectAnalytics;
        } catch (Exception e) {
            System.out.println("Exception occurs in service getProjectAnalytics_All : " + e);
            e.printStackTrace();
            throw e;
        }

    }

    public double calculateSprintSuccRate(int project_id) throws Exception {
        List<ProjectAnalytics> proAnalysisArr = new ArrayList<>();
        double totalSprintSuccRate = 0;
        try {
            ProjectAnalyticsDAO projectAnalyticsDAO = new ProjectAnalyticsDAO();
            proAnalysisArr = projectAnalyticsDAO.getAVGSpSuccRateData(project_id);

            for (ProjectAnalytics item : proAnalysisArr) {
                double sprintSucc;
                if (item.getTotalTask() == 0) {
                    sprintSucc = 0;
                } else {
                    sprintSucc = ((double) item.getCompletedTask() / item.getTotalTask()) * 100;
                }
                totalSprintSuccRate += sprintSucc;
            }

            if (proAnalysisArr.isEmpty()) {
                return 0;
            }

            return Math.round((totalSprintSuccRate / proAnalysisArr.size()) * 100.0) / 100.0;
        } catch (Exception e) {
            System.out.println("Exception occurs in service sprintSuccRate : " + e);
            e.printStackTrace();
            throw e;
        }
    }

    public double calculateAVGVelocity(int project_id) throws Exception {

        try {

            ProjectAnalyticsDAO projectAnalyticsDAO = new ProjectAnalyticsDAO();
            ProjectAnalytics projectAnalytics = projectAnalyticsDAO.getAVGVelocityData(project_id);

            int totalSprint = projectAnalytics.getTotalSprint();

            if (totalSprint == 0) {
                return 0;

            }
            double avgVelocity = (projectAnalytics.getCompletedTask() / totalSprint);

            return Math.round(avgVelocity * 100.0) / 100.0;

        } catch (Exception e) {
            System.out.println("Exception occurs in service AVGVelocity : " + e);
            e.printStackTrace();
            throw e;
        }
    }

    public double calculateCycleTime(int project_id) throws Exception {
        try {
            List<Task> taskArr = new ArrayList<>();
            ProjectAnalyticsDAO projectAnalyticsDAO = new ProjectAnalyticsDAO();
            taskArr = projectAnalyticsDAO.getCycleTimeData(project_id);

            double cycleTime = 0;
            for (Task item : taskArr) {
                System.out.println("Data" + item);
                if (item.getActual_startDate() != null && item.getActual_endDate() != null) {
                    cycleTime += ChronoUnit.DAYS.between(item.getActual_startDate(), item.getActual_endDate());
                }
            }
            double avarageCycleTime = 0;
            if (!taskArr.isEmpty()) {
                avarageCycleTime = cycleTime / taskArr.size();
            }
            return Math.round(avarageCycleTime * 100.0) / 100.0;
        } catch (Exception e) {
            System.out.println("Exception occurs in service calculateCycleTime : " + e);
            e.printStackTrace();
            throw e;
        }
    }

    public double calculateRejectionRate(int project_id) throws Exception {

        List<ProjectAnalytics> projectAnalyticArr = new ArrayList<>();

        try {
            ProjectAnalyticsDAO projectAnalyticsDAO = new ProjectAnalyticsDAO();
            projectAnalyticArr = projectAnalyticsDAO.getRejectionRateData(project_id);

            double rejectionRate = 0;

            for (ProjectAnalytics item : projectAnalyticArr) {

                if (item.getTotalTask() > 0) {

                    if (item.getTotalTask() != 0) {
                        rejectionRate += ((double) item.getRejectedTask() / item.getTotalTask()) * 100;
                    } else {
                        rejectionRate = 0;
                    }

                }
            }
            double avgRejectionRate=0;
            if (!projectAnalyticArr.isEmpty()) {
                 avgRejectionRate = rejectionRate / projectAnalyticArr.size();
            }
            return Math.round(avgRejectionRate * 100.0) / 100.0;

        } catch (Exception e) {
            System.out.println("Exception occurs in service calculateRejectionRate : " + e);
            e.printStackTrace();
            throw e;
        }
    }

    public ProjectAnalytics calulateApproveRateChart(int project_id) throws Exception {
        List<ProjectAnalytics> projectAnalyticsArr = new ArrayList<>();
        try {
            ProjectAnalyticsDAO projectAnalyticsDAO = new ProjectAnalyticsDAO();
            projectAnalyticsArr = projectAnalyticsDAO.getRejectedRateChart(project_id);

            int appovedTaskSum = 0;
            int rejectedTaskSum = 0;

            for (ProjectAnalytics item : projectAnalyticsArr) {
                appovedTaskSum += item.getApprovedTask();
                rejectedTaskSum += item.getRejectedTask();
            }

            ProjectAnalytics projectAnalytics = new ProjectAnalytics();
            projectAnalytics.setTotalApprovedTask(appovedTaskSum);
            projectAnalytics.setTotalRejectedTask(rejectedTaskSum);

            return projectAnalytics;
        } catch (Exception e) {
            System.out.println("Exception occurs in service calulateApproveRateChart : " + e);
            e.printStackTrace();
            throw e;
        }
    }

    public List<ProjectAnalytics> calculateBurnDownChart(Integer sprint_id, Integer project_id) throws Exception {

        List<ProjectAnalytics> rawData = new ArrayList<>();
        List<ProjectAnalytics> burnDownChart = new ArrayList<>();

        try {

            ProjectAnalyticsDAO projectAnalyticsDAO
                    = new ProjectAnalyticsDAO();

            // Fetch sprint data
            if (sprint_id != null) {
                rawData = projectAnalyticsDAO.getBurnDownData(sprint_id);
            } else if (project_id != null) {
                rawData = projectAnalyticsDAO.getBurnDownData_Default(project_id);
            }

            if (rawData.isEmpty()) {
                return burnDownChart;
            }

            // Sprint info
            LocalDate sprintStartDate = rawData.get(0).getSprintStartDate();

            LocalDate sprintEndDate = rawData.get(0).getSprintEndDate();

            LocalDate today = LocalDate.now();

            // Stop at today OR sprint end date
            LocalDate chartEndDate = today.isBefore(sprintEndDate) ? today : sprintEndDate;

            // Generate every day in sprint timeline
            for (LocalDate currentDate = sprintStartDate;
                    !currentDate.isAfter(chartEndDate);
                    currentDate = currentDate.plusDays(1)) {

                int activeTask = 0;
                int completedTask = 0;

                // Calculate task state for this day
                for (ProjectAnalytics task : rawData) {
                    // Task has started
                    if (task.getTaskStartDate() != null
                            && !task.getTaskStartDate().isAfter(currentDate)) {

                        activeTask++;
                    }

                    // Task has actually been completed
                    if (task.getActual_endDate() != null
                            && !task.getActual_endDate().isAfter(currentDate)) {

                        completedTask++;
                    }
                }

                int remainingTask
                        = activeTask - completedTask;

                // Chart point
                ProjectAnalytics chartData = new ProjectAnalytics();

                chartData.setSprintId(rawData.getFirst().getSprintId());
                chartData.setSprintStartDate(sprintStartDate);

                chartData.setSprintEndDate(sprintEndDate);

                chartData.setTaskStartDate(currentDate);

                chartData.setRemainingTask(remainingTask);

                burnDownChart.add(chartData);
            }

        } catch (Exception e) {
            throw e;
        }
        return burnDownChart;
    }

    public void projectStatusCheckingService() throws Exception {
        System.out.println("projectStatusCheckingService Executed \n");
        List<Project> projectsList = new ArrayList<>();
        try {
            ProjectDAO projectDAO = new ProjectDAO();

            LocalDate today = LocalDate.now();
            projectsList = projectDAO.getProjectStatusInfo_check();

            for (Project item : projectsList) {

                if (today.equals(item.getProjEndDate()) || today.isAfter(item.getProjEndDate())) {
                    System.out.println("");
                    projectDAO.updateProjectStatus("Delayed", item.getProjectId());
                }
            }
        } catch (Exception e) {
            System.out.println("Exception occurs in service calulateApproveRateChart : " + e);
            e.printStackTrace();
            throw e;
        }
    }

}
