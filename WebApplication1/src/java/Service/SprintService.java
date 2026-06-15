/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import DAO.SprintDAO;
import beans.Sprint;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HP
 */
public class SprintService {

    public List getCheckSprintValidity(int project_id) throws Exception {
        
        List<Sprint> sprintList = new ArrayList<>();
        
        try {
            SprintDAO sprintDao = new SprintDAO();
            sprintList = sprintDao.getSprintsData(project_id);
            
            LocalDate today = LocalDate.now();
            
            for (Sprint item : sprintList) {
                if(!today.isBefore(item.getSprint_end_date()) && !"Completed".equals(item.getSprint_status())){
                    item.setSprint_status("Completed");
                    sprintDao.updateSprintStatus(item);
                }
            }
            
            return sprintList;
        } catch(Exception e){
            System.out.println("Exception occured in getCheckSprintValidity = " + e);
            e.printStackTrace();
            throw e;
        }
    }
}
