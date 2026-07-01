/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Scheduler;

import Service.ProjectAnalyticsService;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ProjectStatusScheduler implements ServletContextListener {

    private ScheduledExecutorService scheduler;
    private final ProjectAnalyticsService service = new ProjectAnalyticsService();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        stop();
    }

    public void start() {

        try {
            // Run immediately after deployment
            service.projectStatusCheckingService();

            scheduler = Executors.newSingleThreadScheduledExecutor();

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime nextMidnight = now.plusDays(1)
                                            .toLocalDate()
                                            .atStartOfDay();

            long initialDelay = Duration.between(now, nextMidnight).toMillis();

            scheduler.scheduleAtFixedRate(
                    () -> {
                        try {
                            service.projectStatusCheckingService();
                            System.out.println(
                                    "Project status check executed at: "
                                    + LocalDateTime.now());
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Exception Occured : " + e);
                        }
                    },
                    initialDelay,
                    TimeUnit.DAYS.toMillis(1),
                    TimeUnit.MILLISECONDS
            );

            System.out.println("ProjectStatusScheduler started.");

        } catch (Exception e) {
            System.err.println("Failed to start ProjectStatusScheduler");
            e.printStackTrace();
        }
    }

    public void stop() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
            System.out.println("ProjectStatusScheduler stopped.");
        }
    }
}