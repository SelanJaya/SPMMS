/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DAO.DBConnection;
import beans.Backlog;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import beans.Backlog;

/**
 *
 * @author HP
 */
public class BacklogDAO {

    public int insertBacklogItem(Backlog backlog) {
        String sql = "INSERT INTO backlog_items (project_id, backlog_item_title, backlog_item_desc, "
                + "acceptance_criteria, story_points, mandays, backlog_item_priority, "
                + "backlog_item_added_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, backlog.getProject_id());
            ps.setString(2, backlog.getBacklogI_title());
            ps.setString(3, backlog.getBacklogI_desc());
            ps.setString(4, backlog.getAcceptance_cri());
            ps.setInt(5, backlog.getStory_point());
            ps.setInt(6, backlog.getMandays());
            ps.setInt(7, backlog.getBacklogI_priority());
            // Sets the current timestamp for the added_at field
            ps.setTimestamp(8, new java.sql.Timestamp(System.currentTimeMillis()));

            if (ps.executeUpdate() > 0) {
                // 2. Retrieve the generated keys
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int newId = rs.getInt(1); // This is your backlog_item_id
                        System.out.println("Inserted ID: " + newId);
                        return newId;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List getBacklogItem(int project_id) {

        String sql = "SELECT backlog_item_id, backlog_item_title, backlog_item_desc, "
                + "acceptance_criteria, story_points, mandays, backlog_item_priority, "
                + "backlog_item_added_at FROM backlog_items WHERE project_id = ? ORDER BY backlog_item_priority ASC";

        List<Backlog> backlogItemArr = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, project_id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Backlog backlog = new Backlog();

                backlog.setBacklogI_id(rs.getInt("backlog_item_id"));
                backlog.setBacklogI_title(rs.getString("backlog_item_title"));
                backlog.setBacklogI_desc(rs.getString("backlog_item_desc"));
                backlog.setAcceptance_cri(rs.getString("acceptance_criteria"));
                backlog.setStory_point(rs.getInt("story_points"));
                backlog.setMandays(rs.getInt("mandays"));
                backlog.setBacklogI_priority(rs.getInt("backlog_item_priority"));

                backlogItemArr.add(backlog);
            }

            return backlogItemArr;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void updateBacklogItem(Backlog backlog) {
        String sql = "UPDATE backlog_items SET backlog_item_title = ?, backlog_item_desc = ?, "
                + "acceptance_criteria = ?, story_points = ?, mandays = ?,  "
                + "where backlog_item_id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, backlog.getBacklogI_title());
            ps.setString(2, backlog.getBacklogI_desc());
            ps.setString(3, backlog.getAcceptance_cri());
            ps.setInt(4, backlog.getStory_point());
            ps.setInt(5, backlog.getMandays());
            //ps.setInt(6, backlog.getBacklogI_priority());
            ps.setInt(6, backlog.getBacklogI_id());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void reorderBacklogItem(int backlogI_priority, int backlogI_id) {
        boolean status = false;
        String sql = "UPDATE backlog_items SET backlog_item_priority = ? "
                + "where backlog_item_id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, backlogI_priority);
            ps.setInt(2, backlogI_id);

            status = ps.executeUpdate() > 1;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Exception from DAO :" + e);
        }

        System.out.println("Status from DAO " + status);
    }

    public void deleteBacklogItem(int backlogId) {
        String sql = "DELETE FROM backlog_items WHERE backlog_item_id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, backlogId);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public List getHighPriorityBacklog(int project_id) {
        List<Backlog> backlogList = new ArrayList<>();

        String sql = """
            SELECT b.backlog_item_id, b.backlog_item_title
            FROM backlog_items b
            LEFT JOIN sprint_backlog_links sbl
                ON b.backlog_item_id = sbl.backlog_item_id
            WHERE b.project_id = ?
            AND sbl.backlog_item_id IS NULL
            ORDER BY b.backlog_item_priority ASC
            LIMIT 5
            """;

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, project_id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Backlog backlog = new Backlog();
                backlog.setBacklogI_id(rs.getInt("backlog_item_id"));
                backlog.setBacklogI_title(rs.getString("backlog_item_title"));
                backlogList.add(backlog);
            }
            return backlogList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List getHighPriorityBacklog_Edit(int project_id, int sprint_id) {
        List<Backlog> backlogList = new ArrayList<>();

        String sql = """
                        SELECT b.backlog_item_id, b.backlog_item_title
                        FROM backlog_items b
                        LEFT JOIN sprint_backlog_links sbl
                            ON b.backlog_item_id = sbl.backlog_item_id
                        WHERE b.project_id = ?
                        AND (
                                sbl.backlog_item_id IS NULL
                                OR sbl.sprint_id = ?
                            )
                        ORDER BY b.backlog_item_priority ASC
                        LIMIT 5;
                     """;

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, project_id);
            ps.setInt(2, sprint_id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Backlog backlog = new Backlog();
                backlog.setBacklogI_id(rs.getInt("backlog_item_id"));
                backlog.setBacklogI_title(rs.getString("backlog_item_title"));
                backlogList.add(backlog);
            }
            return backlogList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
