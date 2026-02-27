/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

import java.time.LocalDateTime;

/**
 *
 * @author HP
 */
public class SprintBacklogLink {
    int link_sprint_id, link_backlog_item_id, link_Backlogitem_story_points;
    String link_added_at;

    public SprintBacklogLink() {
    }

    public SprintBacklogLink(int link_sprint_id, int link_backlog_item_id) {
        this.link_sprint_id = link_sprint_id;
        this.link_backlog_item_id = link_backlog_item_id;
    }

    public void setLink_sprint_id(int link_sprint_id) {
        this.link_sprint_id = link_sprint_id;
    }

    public void setLink_backlog_item_id(int link_backlog_item_id) {
        this.link_backlog_item_id = link_backlog_item_id;
    }

    public void setLink_Backlogitem_story_points(int link_Backlogitem_story_points) {
        this.link_Backlogitem_story_points = link_Backlogitem_story_points;
    }

    public void setLink_added_at(String link_added_at) {
        this.link_added_at = link_added_at;
    }

    public int getLink_sprint_id() {
        return link_sprint_id;
    }

    public int getLink_backlog_item_id() {
        return link_backlog_item_id;
    }

    public int getLink_Backlogitem_story_points() {
        return link_Backlogitem_story_points;
    }
    
    // retrurn in LOCALDATETIME
    public LocalDateTime getLink_added_at() {
        return LocalDateTime.parse(link_added_at);
    }
    
    
}
