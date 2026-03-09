/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 *
 * @author HP
 */
public class SprintBacklogLink {
    int sprint_id, link_Backlogitem_story_points;
    ArrayList<Integer> backlog_item_id;
    String link_added_at;

    public SprintBacklogLink() {
    }

    public SprintBacklogLink(int sprint_id, ArrayList<Integer> link_backlog_item_id) {
        this.sprint_id = sprint_id;
        this.backlog_item_id = link_backlog_item_id;
    }

    public void setSprint_id(int link_sprint_id) {
        this.sprint_id = link_sprint_id;
    }

    public void setBacklog_item_id(ArrayList<Integer> link_backlog_item_id) {
        this.backlog_item_id = link_backlog_item_id;
    }
    
    public void setLink_added_at(String link_added_at) {
        this.link_added_at = link_added_at;
    }

    public int getSprint_id() {
        return sprint_id;
    }

    public ArrayList<Integer> getBacklog_item_id() {
        return backlog_item_id;
    }
    
    // retrurn in LOCALDATETIME
    public LocalDateTime getLink_added_at() {
        return LocalDateTime.parse(link_added_at);
    }
    
    
}
