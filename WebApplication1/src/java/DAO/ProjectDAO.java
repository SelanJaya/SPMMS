/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import beans.User;
import beans.Project;
import beans.DashboardInsight;
import beans.ProjectRiskScore_ML;
import beans.RiskPredictionResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HP
 */
public class ProjectDAO {

    // Get projects info to disply in the dashboard
    public List<Project> projectInfo(int user_id) throws Exception {
        // FIX 1: Corrected SQL syntax (added missing comma after project_id)
        // FIX 2: Filter by 'proj_created_by' instead of 'project_id' to get ALL user projects
        String sql = """
                     SELECT p.project_id, p.project_name, p.project_desc, 
                     p.project_status, p.proj_start_date, p.proj_end_date, p.project_risk_score
                     FROM projects p 
                     JOIN users u ON p.proj_created_by = u.user_id 
                     WHERE u.user_id = ? AND p.project_status != 'Archive'
                     """;

        // FIX 3: Initialize the list properly (Do NOT set to null)
        List<Project> projectArr = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, user_id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Project project = new Project();

                // FIX 4: Map the correct columns to the correct setters
                project.setProjectId(rs.getInt("project_id"));
                project.setProjectName(rs.getString("project_name")); // Use correct column
                project.setProjectDesc(rs.getString("project_desc")); // Use description setter
                project.setProjectStatus(rs.getString("project_status"));
                project.setProject_risk_score(rs.getDouble("project_risk_score"));

                // FIX 5: Use distinct setters for Start and End dates
                project.setProjStartDate(rs.getDate("proj_start_date").toString());
                project.setProjEndDate(rs.getDate("proj_end_date").toString());

                projectArr.add(project);
            }
        } catch (SQLException e) {
            System.out.println("Exception occured in projectInfo : " + e);
            e.printStackTrace();
            throw e;
        }
        return projectArr;
    }

    // Get projects info to disply in the dashboard
    public List<Project> getProjectStatusInfo_check() throws Exception {
        // FIX 1: Corrected SQL syntax (added missing comma after project_id)
        // FIX 2: Filter by 'proj_created_by' instead of 'project_id' to get ALL user projects
        String sql = """
                     SELECT p.project_id, p.proj_start_date, p.proj_end_date
                     FROM projects p  
                     WHERE p.project_status != 'Archive'
                     """;

        // FIX 3: Initialize the list properly (Do NOT set to null)
        List<Project> projectArr = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Project project = new Project();

                // FIX 4: Map the correct columns to the correct setters
                project.setProjectId(rs.getInt("project_id"));

                // FIX 5: Use distinct setters for Start and End dates
                project.setProjStartDate(rs.getDate("proj_start_date").toString());
                project.setProjEndDate(rs.getDate("proj_end_date").toString());

                projectArr.add(project);
            }
        } catch (SQLException e) {
            System.out.println("Exception occured in projectInfo : " + e);
            e.printStackTrace();
            throw e;
        }
        return projectArr;
    }

    public List<Project> getProjectsByNonPMUserId(int userId) throws Exception {
        List<Project> projects = new ArrayList<>();
        String query = """
                       SELECT p.project_id, p.project_name, p.project_risk_score 
                       FROM projects p
                       JOIN project_assignments pa ON p.project_id = pa.project_id 
                       WHERE pa.proj_assign_to = ?
                       """;

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Project proj = new Project();
                proj.setProjectId(rs.getInt("project_id"));
                proj.setProjectName(rs.getString("project_name"));
                proj.setProject_risk_score(rs.getDouble("project_risk_score"));
                projects.add(proj);
            }
        } catch (SQLException e) {
            System.out.println("Exception occured in getProjectsByNonPMUserId : " + e);
            e.printStackTrace();
            throw e;
        }
        return projects;
    }

    public int createProject(Project project) throws Exception {
        // Correct table name from your schema is 'project' (singular)
        String sql = "INSERT INTO projects (project_name, project_desc, project_status, proj_type, proj_client"
                + ",proj_start_date, proj_end_date, proj_created_by) "
                + "VALUES (?, ?, ?, ?, ?,?, ?, ?)";

        // Initialize with -1 to indicate failure
        int generatedId = -1;

        // Pass RETURN_GENERATED_KEYS flag to the connection
        try (Connection connection = DBConnection.getConnection(); PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, project.getProjectName()); //
            ps.setString(2, project.getProjectDesc()); //
            ps.setString(3, project.getProjectStatus()); //
            ps.setString(4, project.getProjectType()); //
            ps.setString(5, project.getProjectClient()); //
            ps.setObject(6, project.getProjStartDate()); //
            ps.setObject(7, project.getProjEndDate()); //

            ps.setInt(8, project.getProjCreatedBy()); //

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                // Retrieve the generated key
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating project: " + e.getMessage());
            e.printStackTrace();
            throw new Error("Project Creation Unsuccessfull");
        }
        return generatedId;
    }

    // get  project info tp display in the projectPage
    public Project ProjectInfoById(int projectId) throws Exception {
        System.out.println("project id " + projectId);
        String sql = "SELECT * FROM projects WHERE project_id = ?";
        Project project = null;

        try (Connection connection = DBConnection.getConnection(); PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, projectId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                project = new Project();
                project.setProjectId(rs.getInt("project_id"));
                project.setProjectName(rs.getString("project_name"));
                project.setProjectDesc(rs.getString("project_desc"));
                project.setProjectStatus(rs.getString("project_status"));
                project.setProjectType(rs.getString("proj_type"));
                project.setProjectClient(rs.getString("proj_client"));
                project.setProject_risk_score(rs.getDouble("project_risk_score"));

                // Modern Date Mapping
                project.setProjStartDate(rs.getDate("proj_start_date").toString());
                project.setProjEndDate(rs.getDate("proj_end_date").toString());
                project.setProjCreatedAt(rs.getString("proj_created_at"));
            }
        } catch (SQLException e) {
            System.out.println("Exception occured in ProjectInfoById : " + e);
            e.printStackTrace();
            throw e;
        }
        return project;
    }

    public void updateProject(Project project) throws Exception {
        String sql = "UPDATE projects SET project_name = ?, project_desc = ?, "
                + "proj_type = ?, proj_client =?, proj_start_date = ?, proj_end_date = ? "
                + "WHERE project_id = ?";

        try (Connection connection = DBConnection.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, project.getProjectName());
            ps.setString(2, project.getProjectDesc());
            ps.setString(3, project.getProjectType());
            ps.setString(4, project.getProjectClient());
            // Mapping LocalDates to SQL
            ps.setObject(5, project.getProjStartDate());
            ps.setObject(6, project.getProjEndDate());

            // The WHERE clause ID
            ps.setInt(7, project.getProjectId());

            int rowsUpdated = ps.executeUpdate();

            if (!(rowsUpdated > 0)) {
                throw new Exception("Project Update Failed");
            }

        } catch (SQLException e) {
            System.out.println("Exception occured in updateProject : " + e);
            e.printStackTrace();
            throw e;
        }
    }

    public void updateProjectEndData(Project project) throws Exception {
        String sql = """
                     UPDATE projects SET proj_end_date = ?, project_status = ?
                     WHERE project_id = ?
                     """;

        try (Connection connection = DBConnection.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setObject(1, project.getProjEndDate());
            ps.setString(2, "Active");

            // The WHERE clause ID
            ps.setInt(3, project.getProjectId());

            int rowsUpdated = ps.executeUpdate();

            if (!(rowsUpdated > 0)) {
                throw new Exception("Project Update Failed");
            }

        } catch (SQLException e) {
            System.out.println("Exception occured in updateProject : " + e);
            e.printStackTrace();
            throw e;
        }
    }

    public void deleteProject(int projectId) throws Exception {
        System.out.println("Delet Project DAO EXECUTED");

        String sql = "DELETE FROM projects WHERE project_id = ?";

        try (Connection conn = DBConnection.getConnection(); // Your DB connection method
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
            System.out.println("project id " + projectId);
            pstmt.setInt(1, projectId);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Row Affexted");
            if (!(rowsAffected > 0)) {
                throw new Error();
            }
            System.out.println("Done");
        } catch (SQLException e) {
            System.out.println("Exception occured in deleteProject : " + e);
            e.printStackTrace();
            throw new Error("Project Deletion Failed");
        }
    }

    public List<Project> getArchivedProjectsByUserId(int userId) throws Exception {
        List<Project> archivedList = new ArrayList<>();
        String sql = "SELECT project_id, project_name, project_desc, proj_created_at, proj_end_date FROM projects WHERE proj_created_by = ? AND "
                + "project_status = 'Archive'";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Project p = new Project();
                p.setProjectId(rs.getInt("project_id"));
                p.setProjectName(rs.getString("project_name"));
                p.setProjectDesc(rs.getString("project_desc"));
                p.setProjCreatedAt(rs.getString("proj_created_at"));
                p.setProjEndDate(rs.getString("proj_end_date"));
                archivedList.add(p);

                System.out.println("----------------------------------------");
                System.out.println("Project ID   : " + rs.getInt("project_id"));
                System.out.println("Name         : " + rs.getString("project_name"));
                System.out.println("Description  : " + rs.getString("project_desc"));

            }

        } catch (SQLException e) {
            System.out.println("Exception occured in getProjectsByNonPMUserId : " + e);
            e.printStackTrace();
            throw e;
        }
        return archivedList;
    }

    public void updateProjectStatus(String projectStatus, int project_id) throws Exception {
        // Standard SQL for updating a specific column
        String sql = "UPDATE projects SET project_status = ? WHERE project_id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            // Set the ID to target the specific row
            ps.setString(1, projectStatus);
            ps.setInt(2, project_id);

            // executeUpdate returns the number of rows affected
            int rowsAffected = ps.executeUpdate();

            if (!(rowsAffected > 0)) {
                throw new Error();
            }

        } catch (Exception e) {
            System.err.println("Error updating project status: " + e.getMessage());
            e.printStackTrace();
            throw new Error("Project Update Failed");
        }
    }

    //For Email
    public String getProjectNameById(int project_id) throws Exception {

        String projectName = null;

        String sql = """
                 SELECT project_name
                 FROM projects
                 WHERE project_id = ?
                 """;

        try (Connection connection = DBConnection.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, project_id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                projectName = rs.getString("project_name");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception occurred in getProjectNameById: " + e);
            throw e;
        }

        return projectName;
    }

    // Project Dashboard DAO
    public int getTotalProject(int user_id) throws Exception {
        int activeProject = -1;
        String sql = """
                     SELECT COUNT(project_id) AS ActiveProject
                     FROM projects p 
                     LEFT JOIN users u 
                     ON p.proj_created_by = u.user_id
                     WHERE u.user_id = ? AND p.project_status != 'Archive';
                     """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, user_id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                activeProject = rs.getInt("ActiveProject");
            }

        } catch (Exception e) {
            throw e;
        }
        return activeProject;
    }

    public List getMYActiveProject(int user_id) throws Exception {

        List<DashboardInsight> dashboardInsightsArr = new ArrayList<>();
        String sql = """
                    SELECT p.project_name,
                        COUNT(DISTINCT b.backlog_item_id) AS total_backlog,
                    
                        COUNT(DISTINCT CASE
                            WHEN s.sprint_status = 'Completed'
                            THEN b.backlog_item_id
                        END) AS completed,
                    
                        COUNT(DISTINCT CASE
                            WHEN s.sprint_status = 'Active'
                            THEN b.backlog_item_id
                        END) AS active,
                    
                        COUNT(DISTINCT CASE
                            WHEN s.sprint_status NOT IN ('Active', 'Completed')
                            THEN b.backlog_item_id
                        END) AS pending
                    
                    FROM projects p
                    JOIN backlog_items b
                        ON p.project_id = b.project_id
                    LEFT JOIN sprint_backlog_links sbl
                        ON b.backlog_item_id = sbl.backlog_item_id
                    LEFT JOIN sprints s
                        ON sbl.sprint_id = s.sprint_id
                    WHERE p.proj_created_by = ?
                    GROUP BY p.project_id;
                    """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, user_id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DashboardInsight dashboardInsight = new DashboardInsight();
                dashboardInsight.setProject_name(rs.getString("project_name"));
                dashboardInsight.setTotalBacklog(rs.getString("total_backlog"));
                dashboardInsight.setActiveBacklog(rs.getString("active"));
                dashboardInsight.setPendingBacklog(rs.getString("pending"));
                dashboardInsight.setCompletedBacklog(rs.getString("completed"));
                dashboardInsightsArr.add(dashboardInsight);
            }
        } catch (Exception e) {
            System.out.println("Exception ocurred in getActiveProject : " + e);
            e.printStackTrace();
            throw e;
        }
        return dashboardInsightsArr;
    }

    public List getMYActiveProject_PO(int user_id) throws Exception {

        List<DashboardInsight> dashboardInsightsArr = new ArrayList<>();
        String sql = """
                    SELECT p.project_name,
                                            COUNT(DISTINCT b.backlog_item_id) AS total_backlog,
                                        
                                            COUNT(DISTINCT CASE
                                                WHEN s.sprint_status = 'Completed'
                                                THEN b.backlog_item_id
                                            END) AS completed,
                                        
                                            COUNT(DISTINCT CASE
                                                WHEN s.sprint_status = 'Active'
                                                THEN b.backlog_item_id
                                            END) AS active,
                                        
                                            COUNT(DISTINCT CASE
                                                WHEN s.sprint_status NOT IN ('Active', 'Completed')
                                                THEN b.backlog_item_id
                                            END) AS pending
                                        
                                        FROM projects p
                                        LEFT JOIN project_assignments pa 
                                        USING(project_id)
                                        JOIN backlog_items b
                                            ON p.project_id = b.project_id
                                        LEFT JOIN sprint_backlog_links sbl
                                            ON b.backlog_item_id = sbl.backlog_item_id
                                        LEFT JOIN sprints s
                                            ON sbl.sprint_id = s.sprint_id
                                        WHERE pa.proj_assign_to = ?
                                        GROUP BY p.project_id;
                    """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, user_id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DashboardInsight dashboardInsight = new DashboardInsight();
                dashboardInsight.setProject_name(rs.getString("project_name"));
                dashboardInsight.setTotalBacklog(rs.getString("total_backlog"));
                dashboardInsight.setActiveBacklog(rs.getString("active"));
                dashboardInsight.setPendingBacklog(rs.getString("pending"));
                dashboardInsight.setCompletedBacklog(rs.getString("completed"));
                dashboardInsightsArr.add(dashboardInsight);
            }
        } catch (Exception e) {
            System.out.println("Exception ocurred in getActiveProject : " + e);
            e.printStackTrace();
            throw e;
        }
        return dashboardInsightsArr;
    }

    public List getTotalOverduePerProject(int user_id) throws Exception {
        List<DashboardInsight> dashboardInsightsArr = new ArrayList<>();
        String sql = """
                     SELECT COUNT(*) AS overdue_tasks, project_name
                     FROM projects p
                     LEFT JOIN users u
                         ON p.proj_created_by = u.user_id
                     LEFT JOIN sprints s
                         USING (project_id)
                     LEFT JOIN tasks t
                         USING (sprint_id)
                     WHERE u.user_id = ?
                       AND t.task_end_date < NOW()
                       AND t.task_status != 'DONE';
                     """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, user_id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                DashboardInsight dashboardInsight = new DashboardInsight();
                dashboardInsight.setProject_name(rs.getString("project_name"));
                dashboardInsight.setTaskOverdue(rs.getInt("overdue_tasks"));

                dashboardInsightsArr.add(dashboardInsight);
            }
        } catch (Exception e) {
            System.out.println("Exception ocurred in getTotalOverduePerProject : " + e);
            e.printStackTrace();
            throw e;
        }

        return dashboardInsightsArr;
    }

    public DashboardInsight getPendingRecruitement(int user_id) throws Exception {

        Map<String, ArrayList<String>> assignmentPending_project = new HashMap<>();
        DashboardInsight dashboardInsight = new DashboardInsight();

        String sql = """
                 SELECT
                       p.project_name,

                       CASE
                           WHEN SUM(CASE WHEN u.user_role = 'Product Owner' THEN 1 ELSE 0 END) = 0
                           THEN 'Product Owner'
                       END AS missing_po,

                       CASE
                           WHEN SUM(CASE WHEN u.user_role = 'Scrum Master' THEN 1 ELSE 0 END) = 0
                           THEN 'Scrum Master'
                       END AS missing_sm,

                       CASE
                           WHEN SUM(CASE WHEN u.user_role = 'Developer' THEN 1 ELSE 0 END) = 0
                           THEN 'Developer'
                       END AS missing_dev

                   FROM projects p

                   LEFT JOIN project_assignments pa
                       ON p.project_id = pa.project_id
                       AND pa.proj_assign_status = 'ACTIVE'

                   LEFT JOIN users u
                       ON pa.proj_assign_to = u.user_id

                   WHERE p.proj_created_by = ?
                     AND p.project_status NOT IN ('Archive')

                   GROUP BY p.project_id, p.project_name

                   HAVING
                          missing_po IS NOT NULL
                       OR missing_sm IS NOT NULL
                       OR missing_dev IS NOT NULL;
                 """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, user_id);

            try (ResultSet rs = ps.executeQuery()) {

                int noMissingRole = 0;

                while (rs.next()) {

                    ArrayList<String> positionName = new ArrayList<>();

                    String missing_po = rs.getString("missing_po");
                    String missing_sm = rs.getString("missing_sm");
                    String missing_dev = rs.getString("missing_dev");

                    if (missing_po != null || missing_sm != null || missing_dev != null) {
                        noMissingRole++;
                    }

                    if (missing_po != null) {
                        positionName.add(missing_po);
                    }

                    if (missing_sm != null) {
                        positionName.add(missing_sm);
                    }

                    if (missing_dev != null) {
                        positionName.add(missing_dev);
                    }

                    assignmentPending_project.put(
                            rs.getString("project_name"),
                            positionName
                    );
                }

                dashboardInsight.setAssignmentPending_project(assignmentPending_project);
                dashboardInsight.setNoMissingRole(noMissingRole);
            }

        } catch (Exception e) {
            System.out.println("Exception occurred in getPendingRecruitement : " + e);
            e.printStackTrace();
            throw e;
        }

        return dashboardInsight;
    }

    public Map getOverduePercentagePerProject(int user_id) throws Exception {

        Map<Integer, ProjectRiskScore_ML> projectMap = new HashMap<>();

        String sql = """
                     SELECT
                         p.project_id,
                         COALESCE(
                             AVG(
                                 CASE
                                     WHEN t.task_end_date < NOW()
                                          AND t.task_status <> 'DONE'
                                     THEN 1
                                     ELSE 0
                                 END
                             ),
                             0
                         ) AS overduePercentage
                     FROM projects p
                     LEFT JOIN sprints s
                         USING(project_id)
                     LEFT JOIN tasks t
                         USING(sprint_id)
                     WHERE EXISTS (
                         SELECT 1
                         FROM project_assignments pa
                         WHERE pa.project_id = p.project_id
                           AND (
                                 pa.proj_assign_by = ?
                              OR pa.proj_assign_to = ?
                           )
                     )
                     GROUP BY p.project_id, p.project_name;
                     """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, user_id);
            ps.setInt(2, user_id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ProjectRiskScore_ML projectRiskScore = new ProjectRiskScore_ML();
                projectRiskScore.setProjectId(rs.getInt("project_id"));
                projectRiskScore.setOverduePercentage(rs.getDouble("overduePercentage"));
                projectMap.put(rs.getInt("project_id"), projectRiskScore);
            }

            return projectMap;
        } catch (Exception e) {
            System.out.println("Exception ocurred in getOverduePercentagePerProject : " + e);
            e.printStackTrace();
            throw e;
        }
    }

    public Map getTaskRejectionRatePerProject(Map<Integer, ProjectRiskScore_ML> projectMap, int user_id) throws Exception {

        String sql = """
                        SELECT
                            p.project_id,
                            COALESCE(
                            AVG(
                                CASE
                                    WHEN ta.taskApproval_status = 'reject'
                                    THEN 1
                                    ELSE 0
                                END
                            ),
                            0
                        ) AS rejection_rate
                        FROM projects p
                        LEFT JOIN sprints s USING(project_id)
                        LEFT JOIN tasks t USING(sprint_id)
                        LEFT JOIN task_approvals ta USING(task_id)
                        WHERE EXISTS (
                            SELECT 1
                            FROM project_assignments pa
                            WHERE pa.project_id = p.project_id
                              AND (pa.proj_assign_by = ? OR pa.proj_assign_to = ?)
                        )
                        GROUP BY p.project_id;
                     """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, user_id);
            ps.setInt(2, user_id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int projectId = rs.getInt("project_id");

                ProjectRiskScore_ML project = projectMap.get(projectId);

                if (project != null) {
                    project.setRejectionRate(
                            rs.getDouble("rejection_rate")
                    );
                }
            }

            return projectMap;
        } catch (Exception e) {
            System.out.println("Exception ocurred in getTaskRejectionRatePerProject : " + e);
            e.printStackTrace();
            throw e;
        }
    }

    public Map getAVGStoryPointPerProject(Map<Integer, ProjectRiskScore_ML> projectMap, int user_id) throws Exception {

        String sql = """
                       SELECT
                           p.project_id,
                           COALESCE(AVG(b.story_points), 0) AS avgStoryPoint
                       FROM projects p
                       LEFT JOIN backlog_items b USING(project_id)
                       WHERE EXISTS (
                           SELECT 1
                           FROM project_assignments pa
                           WHERE pa.project_id = p.project_id
                             AND (pa.proj_assign_by = ? OR pa.proj_assign_to = ?)
                       )
                       GROUP BY p.project_id;
                     """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, user_id);
            ps.setInt(2, user_id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int projectId = rs.getInt("project_id");

                ProjectRiskScore_ML project = projectMap.get(projectId);

                if (project != null) {
                    project.setAvgStoryPoints(
                            rs.getDouble("avgStoryPoint")
                    );
                }
            }

            return projectMap;
        } catch (Exception e) {
            System.out.println("Exception ocurred in getAVGStoryPointPerProject : " + e);
            e.printStackTrace();
            throw e;
        }
    }
    
    public Map getTotalStoryPointAllProject(Map<Integer, ProjectRiskScore_ML> projectMap) throws Exception {

        String sql = """
                       SELECT
                           p.project_id,
                           COALESCE(SUM(b.story_points), 0) AS totalStoryPoint
                       FROM projects p
                       LEFT JOIN backlog_items b USING(project_id)
                       GROUP BY p.project_id;
                     """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int projectId = rs.getInt("project_id");

                ProjectRiskScore_ML project = projectMap.get(projectId);

                if (project != null) {
                    project.setTotal_story_points(
                            rs.getInt("totalStoryPoint")
                    );
                }
            }

            return projectMap;
        } catch (Exception e) {
            System.out.println("Exception ocurred in getAVGStoryPointPerProject : " + e);
            e.printStackTrace();
            throw e;
        }
    }

    public Map getCompletionRatePerProject(Map<Integer, ProjectRiskScore_ML> projectMap, int user_id) throws Exception {

        String sql = """
                      SELECT
                          p.project_id,
                          COALESCE(
                              AVG(
                                  CASE
                                      WHEN t.task_status = 'DONE' THEN 1
                                      ELSE 0
                                  END
                              ),
                              0
                          ) AS completion_rate
                      FROM projects p
                      LEFT JOIN sprints s USING(project_id)
                      LEFT JOIN tasks t USING(sprint_id)
                      WHERE EXISTS (
                          SELECT 1
                          FROM project_assignments pa
                          WHERE pa.project_id = p.project_id
                            AND (pa.proj_assign_by = ? OR pa.proj_assign_to = ?)
                      )
                      GROUP BY p.project_id;
                     """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, user_id);
            ps.setInt(2, user_id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int projectId = rs.getInt("project_id");

                ProjectRiskScore_ML project = projectMap.get(projectId);

                if (project != null) {
                    project.setCompletionRate(
                            rs.getDouble("completion_rate")
                    );
                }
            }

            return projectMap;
        } catch (Exception e) {
            System.out.println("Exception ocurred in getCompletionRatePerProject : " + e);
            e.printStackTrace();
            throw e;
        }
    }

    public Map getRemainingDatePerProject(Map<Integer, ProjectRiskScore_ML> projectMap, int user_id) throws Exception {

        String sql = """
                      SELECT
                                                p.project_id,
                                                GREATEST(
                                                    DATEDIFF(p.proj_end_date, CURDATE()),
                                                    0
                                                ) AS remaining_days
                                            FROM projects p
                                            LEFT JOIN project_assignments pa 
                                            USING(project_id)
                                            WHERE EXISTS (
                                               SELECT 1
                                               FROM project_assignments pa
                                               WHERE pa.project_id = p.project_id
                                                 AND (
                                                       pa.proj_assign_by = ?
                                                    OR pa.proj_assign_to = ?
                                                 )
                                           )
                                           GROUP BY project_id;
                     """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, user_id);
            ps.setInt(2, user_id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int projectId = rs.getInt("project_id");

                ProjectRiskScore_ML project = projectMap.get(projectId);

                if (project != null) {
                    project.setProjectDurationDays(
                            rs.getInt("remaining_days")
                    );
                }
            }

            return projectMap;
        } catch (Exception e) {
            System.out.println("Exception ocurred in getRemainingDatePerProject : " + e);
            e.printStackTrace();
            throw e;
        }
    }

    public Map getOverduePercentageAllProject() throws Exception {

        Map<Integer, ProjectRiskScore_ML> projectMap = new HashMap<>();

        String sql = """
                     SELECT
                         p.project_id,
                         COALESCE(
                             AVG(
                                 CASE
                                     WHEN t.task_end_date < NOW()
                                          AND t.task_status <> 'DONE'
                                     THEN 1
                                     ELSE 0
                                 END
                             ),
                             0
                         ) AS overduePercentage
                     FROM projects p
                     LEFT JOIN sprints s
                         USING(project_id)
                     LEFT JOIN tasks t
                         USING(sprint_id)
                     GROUP BY p.project_id, p.project_name;
                     """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ProjectRiskScore_ML projectRiskScore = new ProjectRiskScore_ML();
                projectRiskScore.setProjectId(rs.getInt("project_id"));
                projectRiskScore.setOverduePercentage(rs.getDouble("overduePercentage") * 100);
                projectMap.put(rs.getInt("project_id"), projectRiskScore);
            }

            return projectMap;
        } catch (Exception e) {
            System.out.println("Exception ocurred in getOverduePercentagePerProject : " + e);
            e.printStackTrace();
            throw e;
        }
    }

    public Map getTaskRejectionRateAllProject(Map<Integer, ProjectRiskScore_ML> projectMap) throws Exception {

        String sql = """
                        SELECT
                            p.project_id,
                            COALESCE(
                            AVG(
                                CASE
                                    WHEN ta.taskApproval_status = 'reject'
                                    THEN 1
                                    ELSE 0
                                END
                            ),
                            0
                        ) AS rejection_rate
                        FROM projects p
                        LEFT JOIN sprints s USING(project_id)
                        LEFT JOIN tasks t USING(sprint_id)
                        LEFT JOIN task_approvals ta USING(task_id)
                        GROUP BY p.project_id;
                     """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int projectId = rs.getInt("project_id");

                ProjectRiskScore_ML project = projectMap.get(projectId);

                if (project != null) {
                    project.setRejectionRate(
                            rs.getDouble("rejection_rate") * 100
                    );
                }
            }

            return projectMap;
        } catch (Exception e) {
            System.out.println("Exception ocurred in getTaskRejectionRatePerProject : " + e);
            e.printStackTrace();
            throw e;
        }
    }

    public Map getAVGStoryPointAllProject(Map<Integer, ProjectRiskScore_ML> projectMap) throws Exception {

        String sql = """
                       SELECT
                           p.project_id,
                           COALESCE(AVG(b.story_points), 0) AS avgStoryPoint
                       FROM projects p
                       LEFT JOIN backlog_items b USING(project_id)
                       GROUP BY p.project_id;
                     """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int projectId = rs.getInt("project_id");

                ProjectRiskScore_ML project = projectMap.get(projectId);

                if (project != null) {
                    project.setAvgStoryPoints(
                            rs.getDouble("avgStoryPoint")
                    );
                }
            }

            return projectMap;
        } catch (Exception e) {
            System.out.println("Exception ocurred in getAVGStoryPointPerProject : " + e);
            e.printStackTrace();
            throw e;
        }
    }

    public Map getCompletionRateAllProject(Map<Integer, ProjectRiskScore_ML> projectMap) throws Exception {

        String sql = """
                      SELECT
                          p.project_id,
                          COALESCE(
                              AVG(
                                  CASE
                                      WHEN t.task_status = 'DONE' THEN 1
                                      ELSE 0
                                  END
                              ),
                              0
                          ) AS completion_rate
                      FROM projects p
                      LEFT JOIN sprints s USING(project_id)
                      LEFT JOIN tasks t USING(sprint_id)
                      GROUP BY p.project_id;
                     """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int projectId = rs.getInt("project_id");

                ProjectRiskScore_ML project = projectMap.get(projectId);

                if (project != null) {
                    project.setCompletionRate(
                            rs.getDouble("completion_rate") * 100
                    );
                }
            }

            return projectMap;
        } catch (Exception e) {
            System.out.println("Exception ocurred in getCompletionRatePerProject : " + e);
            e.printStackTrace();
            throw e;
        }
    }

    public Map getRemainingDateAllProject(Map<Integer, ProjectRiskScore_ML> projectMap) throws Exception {

        String sql = """
                      SELECT
                                                p.project_id,
                                                GREATEST(
                                                    DATEDIFF(p.proj_end_date, CURDATE()),
                                                    0
                                                ) AS remaining_days
                                            FROM projects p
                                            LEFT JOIN project_assignments pa 
                                            USING(project_id)
                                           GROUP BY project_id;
                     """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int projectId = rs.getInt("project_id");

                ProjectRiskScore_ML project = projectMap.get(projectId);

                if (project != null) {
                    project.setProjectDurationDays(
                            rs.getInt("remaining_days")
                    );
                }
            }

            return projectMap;
        } catch (Exception e) {
            System.out.println("Exception ocurred in getRemainingDatePerProject : " + e);
            e.printStackTrace();
            throw e;
        }
    }

    public void updateProjectRiskScores(List<RiskPredictionResponse> predictions) throws Exception {
        String sql = "UPDATE projects SET project_risk_score = ? WHERE project_id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            System.out.println("IN DOA :" + predictions.getFirst().getProjectId() + " " + predictions.getFirst().getRiskScore());

            for (RiskPredictionResponse riskScore : predictions) {
                ps.setDouble(1, riskScore.getRiskScore()); // adjust type if needed
                System.out.println("1 " + riskScore.getRiskScore());
                ps.setInt(2, riskScore.getProjectId());
                System.out.println("2 " + riskScore.getProjectId());
                ps.addBatch();
            }

            int[] results = ps.executeBatch();

            for (int rowsAffected : results) {
                if (rowsAffected <= 0) {
                    throw new Exception("One or more project updates failed.");
                }
            }

        } catch (Exception e) {
            System.err.println("Error updating project risk scores: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Batch project update failed", e);
        }
    }

}
