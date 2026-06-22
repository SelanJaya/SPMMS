/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

/**
 *
 * @author HP
 */
public class Activity {

    private String activity, activityDate, activityType;

    public Activity() {
    }
    
    public void setActivity(String activity) {
        this.activity = activity;
    }

    public void setActivityDate(String activityDate) {
        this.activityDate = activityDate;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivity() {
        return activity;
    }

    public String getActivityDate() {
        return activityDate;
    }

    public String getActivityType() {
        return activityType;
    }
}
