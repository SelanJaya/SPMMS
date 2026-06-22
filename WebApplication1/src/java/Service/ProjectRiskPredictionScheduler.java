///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//
//package Service;
//
//import DAO.ProjectDAO;
//import beans.ProjectRiskScore_ML;
//import Service.MLPredictionService;
//import beans.RiskPredictionResponse;
//
//import java.util.List;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
//import javax.servlet.ServletContextEvent;
//import javax.servlet.ServletContextListener;
//import javax.servlet.annotation.WebListener;
//
///**
// *
// * @author HP
// */
//
//
//public class ProjectRiskPredictionScheduler implements ServletContextListener {
//
//    private ScheduledExecutorService scheduler;
//
//    @Override
//    public void contextInitialized(ServletContextEvent sce) {
//
//        scheduler = Executors.newSingleThreadScheduledExecutor();
//
//        // Run immediately when Tomcat starts
//        scheduler.execute(this::runMLPrediction);
//
//        // Run every hour
//        scheduler.scheduleAtFixedRate(
//                this::runMLPrediction,
//                1,
//                1,
//                TimeUnit.HOURS
//        );
//    }
//
//    private void runMLPrediction() {
//        try {
//            System.out.println("Running ML prediction...");
//
//            MLPredictionService mLPredictionService = new MLPredictionService();
//            ProjectDAO projectDAO = new ProjectDAO();
//
//            List<RiskPredictionResponse> predictions = mLPredictionService.ProjectRisk_MLService();
//
//            projectDAO.updateProjectRiskScores(predictions);
//
//            System.out.println("ML prediction completed.");
//
//        } catch (Exception e) {
//            System.err.println("ML prediction failed:");
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void contextDestroyed(ServletContextEvent sce) {
//        if (scheduler != null) {
//            scheduler.shutdown();
//        }
//    }
//}