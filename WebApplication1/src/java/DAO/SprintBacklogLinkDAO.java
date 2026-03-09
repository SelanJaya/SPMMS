/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import beans.User;
import beans.Project;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import beans.Backlog;
import beans.SprintBacklogLink;
import javax.servlet.ServletException;

/**
 *
 * @author HP
 */
public class SprintBacklogLinkDAO {

    public void addSprintBacklogLink(int link_sprint_id, int link_backlog_item_id) throws Exception {
        String sql = """
                     INSERT INTO sprint_backlog_links (sprint_id, backlog_item_id, 
                     link_added_at) VALUES 
                     (?,?,?)
                     """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, link_sprint_id);
            ps.setInt(2, link_backlog_item_id);
            ps.setTimestamp(3, new java.sql.Timestamp(System.currentTimeMillis()));

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(" Exception Occue in linking" + e);
            e.printStackTrace();
        }
    }

    public void addSprintBacklogLink(Connection con, int link_sprint_id, int link_backlog_item_id) throws Exception {
        String sql = """
                     INSERT INTO sprint_backlog_links (sprint_id, backlog_item_id, 
                     link_added_at) VALUES 
                     (?,?,?)
                     """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, link_sprint_id);
            ps.setInt(2, link_backlog_item_id);
            ps.setTimestamp(3, new java.sql.Timestamp(System.currentTimeMillis()));

            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    public Backlog getLinkedBacklog(int backlogI_id) throws Exception {

        Backlog backlog = new Backlog();

        String sql = """
                     SELECT b.backlog_item_id, b.backlog_item_title, b.story_points 
                     FROM sprint_backlog_links sbl
                     LEFT JOIN backlog_items b
                     USING(backlog_item_id)
                     WHERE b.backlog_item_id = ?;
                     """;

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, backlogI_id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                backlog.setBacklogI_id(rs.getInt("backlog_item_id"));
                backlog.setBacklogI_title(rs.getString("backlog_item_title"));
                backlog.setStory_point(rs.getInt("story_points"));
            }
            return backlog;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Backlog getLinkedBacklog(Connection con, int backlogI_id) throws Exception {

        Backlog backlog = new Backlog();

        String sql = """
                        SELECT b.backlog_item_id, b.backlog_item_title, b.story_points
                        FROM sprint_backlog_links sbl
                        LEFT JOIN backlog_items b
                        USING(backlog_item_id)
                        WHERE b.backlog_item_id = ?
                     """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, backlogI_id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                backlog.setBacklogI_id(rs.getInt("backlog_item_id"));
                backlog.setBacklogI_title(rs.getString("backlog_item_title"));
                backlog.setStory_point(rs.getInt("story_points"));
            }
        }

        return backlog;
    }

    public void deleteBacklogLink(Connection con, int sprint_id) throws Exception {

        String sql = """
                     DELETE FROM sprint_backlog_links WHERE sprint_id = ?
                     """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, sprint_id);

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<Backlog> updateBacklog(SprintBacklogLink sbl) throws Exception {
        Connection con = DBConnection.getConnection();
        con.setAutoCommit(false);   // start transaction
        List<Backlog> backlogArr = new ArrayList<>();
        try {

            deleteBacklogLink(con, sbl.getSprint_id());

            for (int item : sbl.getBacklog_item_id()) {
                addSprintBacklogLink(con, sbl.getSprint_id(), item);
                backlogArr.add(getLinkedBacklog(con, item));
            }

            con.commit();           // save everything
            return backlogArr;
        } catch (Exception e) {

            con.rollback();         // undo everything

        } finally {

            con.setAutoCommit(true);
            con.close();
        }
        return null;
    }

}
