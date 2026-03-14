/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import DAO.DBConnection;
import DAO.TaskDAO;
import DAO.TaskAssignmentDAO;
import DAO.TaskDependencyDAO;
import beans.Task;
import beans.TaskAssignment;
import beans.TaskDependency;
import java.sql.Connection;
import java.util.List;

/**
 *
 * @author HP
 */
public class TaskServices {

    public int insertTaskDetails_Assignment(Task task, List<Integer> dependencyArr) throws Exception {

        Connection con = DBConnection.getConnection();
        con.setAutoCommit(false);

        try {
            //Task Details
            TaskDAO taskDAO = new TaskDAO();
            int task_id = taskDAO.insertTask(task);

            System.out.println("Task Id return" + task_id);
            
            //Task dependences
            TaskDependencyDAO taskDependencyDAO = new TaskDependencyDAO();
            if ( dependencyArr != null) {
                for (int item : dependencyArr) {
                    TaskDependency taskDependency = new TaskDependency(task_id, item);

                    System.out.println("This id :" + taskDependency.getTask_id() + " depends on " + taskDependency.getDepend_on_task_id());

                    taskDependencyDAO.insertTaskDependency(con, taskDependency);
                }
            }

            //Task Assignment
            TaskAssignment taskAssignment = new TaskAssignment();
            taskAssignment = task.getTaskAssignment();
            taskAssignment.setTask_id(task_id);

            TaskAssignmentDAO taskAssignmentDAO = new TaskAssignmentDAO();
            taskAssignmentDAO.insertTaskAssignment(taskAssignment);

            con.commit();
            return task_id;
        } catch (Exception e) {
            con.rollback();
            throw e;
        }
    }

    public void updateTaskDetails_Assignment(Task task, List<Integer> dependencyArr) throws Exception {

        Connection con = DBConnection.getConnection();
        con.setAutoCommit(false);

        try {

            TaskDAO taskDAO = new TaskDAO();
            taskDAO.updateTaskDetails(con, task);

            TaskAssignment taskAssignment = new TaskAssignment();
            taskAssignment = task.getTaskAssignment();

            TaskAssignmentDAO taskAssignmentDAO = new TaskAssignmentDAO();
            taskAssignmentDAO.updateTaskAssignment(con, taskAssignment);
            
            // Delete task dependecy 
            TaskDependencyDAO taskDependencyDAO = new TaskDependencyDAO();
            taskDependencyDAO.deleteTaskDependency(con, task.getTask_id());
            
            //form New Dependecy
            for (int item : dependencyArr) {
                TaskDependency taskDependency = new TaskDependency(task.getTask_id(), item);
                taskDependencyDAO.insertTaskDependency(con, taskDependency);
            }
            con.commit();

        } catch (Exception e) {
            con.rollback();
            System.out.println("Exception Occurs" + e);
            throw e;
        } finally {
            con.close();
        }
    }

    public void deleteTaskDetails_Assignment(int task_id) throws Exception {

        Connection con = DBConnection.getConnection();
        con.setAutoCommit(false);

        try {
            
            TaskDAO taskDAO = new TaskDAO();
            taskDAO.deleteTask(con, task_id);

            TaskAssignmentDAO taskAssignmentDAO = new TaskAssignmentDAO();
            taskAssignmentDAO.deleteTaskAssignment(con, task_id);

            con.commit();
        } catch (Exception e) {
            con.rollback();
            System.out.println("Exception Occurs" + e);
            throw e;
        } finally {
            con.close();
        }
    }
}
