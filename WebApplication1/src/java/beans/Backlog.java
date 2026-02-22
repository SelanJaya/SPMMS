/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

/**
 *
 * @author HP
 */
public class Backlog {
   
   private String backlogI_title, backlogI_desc, acceptance_cri;
   private int project_id, backlogI_id, story_point, mandays, backlogI_priority;

    public Backlog() {
    }

    public Backlog(int project_id, int backlogI_id, String backlogI_title, String backlogI_desc, String acceptance_cri, int story_point, int mandays, int backlogI_priority) {
        this.project_id = project_id;
        this.backlogI_id = backlogI_id;
        this.backlogI_title = backlogI_title;
        this.backlogI_desc = backlogI_desc;
        this.acceptance_cri = acceptance_cri;
        this.story_point = story_point;
        this.mandays = mandays;
        this.backlogI_priority = backlogI_priority;
    }
    
    

    public Backlog(int project_id, String backlogI_title, String backlogI_desc, String acceptance_cri, int story_point, int mandays, int backlogI_priority) {
        this.project_id = project_id;
        this.backlogI_title = backlogI_title;
        this.backlogI_desc = backlogI_desc;
        this.acceptance_cri = acceptance_cri;
        this.story_point = story_point;
        this.mandays = mandays;
        this.backlogI_priority = backlogI_priority;
    }

   
    
    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public void setBacklogI_id(int backlogI_id) {
        this.backlogI_id = backlogI_id;
    }
    
    public void setBacklogI_title(String backlogI_title) {
        this.backlogI_title = backlogI_title;
    }

    public void setBacklogI_desc(String backlogI_desc) {
        this.backlogI_desc = backlogI_desc;
    }

    public void setAcceptance_cri(String acceptance_cri) {
        this.acceptance_cri = acceptance_cri;
    }

    public void setStory_point(int story_point) {
        this.story_point = story_point;
    }

    public void setMandays(int mandays) {
        this.mandays = mandays;
    }

    public void setBacklogI_priority(int backlogI_priority) {
        this.backlogI_priority = backlogI_priority;
    }
    
    public int getProject_id() {
        return project_id;
    }
    
    public int getBacklogI_id() {
        return backlogI_id;
    }
    
    public String getBacklogI_title() {
        return backlogI_title;
    }

    public String getBacklogI_desc() {
        return backlogI_desc;
    }

    public String getAcceptance_cri() {
        return acceptance_cri;
    }

    public int getStory_point() {
        return story_point;
    }

    public int getMandays() {
        return mandays;
    }

    public int getBacklogI_priority() {
        return backlogI_priority;
    }
   
   
}
