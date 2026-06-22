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
import java.sql.SQLIntegrityConstraintViolationException;
import org.apache.jasper.tagplugins.jstl.core.ForEach;

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
            if (dependencyArr != null) {
                for (int item : dependencyArr) {
                    TaskDependency taskDependency = new TaskDependency(task_id, item);

                    System.out.println("This id :" + taskDependency.getTask_id() + " depends on " + taskDependency.getDepend_on_task_id());

                    taskDependencyDAO.insertTaskDependency(con, taskDependency);
                }
            }

            //Task Assignment
            TaskAssignmentDAO taskAssignmentDAO = new TaskAssignmentDAO();
            task.getTaskAssignment().setTask_id(task_id);
            taskAssignmentDAO.insertTaskAssignment(task.getTaskAssignment());

//            for (TaskAssignment item : task.getNewAssignment()) {
//                item.setTask_id(task_id);
//                taskAssignmentDAO.insertTaskAssignment(item);
//            }
//            TaskAssignment taskAssignment = new TaskAssignment();
//            taskAssignment = task.getTaskAssignment();
//            taskAssignment.setTask_id(task_id);
//
//            TaskAssignmentDAO taskAssignmentDAO = new TaskAssignmentDAO();
//            taskAssignmentDAO.insertTaskAssignment(taskAssignment);
            con.commit();
            return task_id;
        } catch (Exception e) {
            con.rollback();
            throw new Exception("Task insertion Failed", e);
        } finally {
            con.close();
        }
    }

    public void updateTaskDetails_Assignment(Task task, List<Integer> dependencyArr) throws Exception {
        Connection con = DBConnection.getConnection();
        con.setAutoCommit(false);

        try {

            TaskDAO taskDAO = new TaskDAO();
            taskDAO.updateTaskDetails(con, task);

            TaskAssignmentDAO taskAssignmentDAO = new TaskAssignmentDAO();

            // Set task id for removed assignment
            if (task.getRemovedAssignment() != null) {
                task.getRemovedAssignment().setTask_id(task.getTask_id());

                taskAssignmentDAO.removeTaskAssignment(con, task.getRemovedAssignment());
                System.out.println("Removed ");
            }

            // Set task id for new assignment
            if (task.getTaskAssignment() != null) {
                task.getTaskAssignment().setTask_id(task.getTask_id());
                System.out.println("Task ID : " + task.getTaskAssignment().getTask_id());
                System.out.println("Assigned To : " + task.getTaskAssignment().getTask_assigned_to());

                boolean assignmentStatus = taskAssignmentDAO.checkTaskAssignmentStatus(task.getTaskAssignment());

                System.out.println("Status : " + assignmentStatus);

                if (assignmentStatus) {
                    taskAssignmentDAO.reactivateTaskAssignment(con, task.getTaskAssignment());
                } else {
                    taskAssignmentDAO.insertTaskAssignment(con, task.getTaskAssignment());
                }
            }

            // Delete old dependencies
            TaskDependencyDAO taskDependencyDAO = new TaskDependencyDAO();

            taskDependencyDAO.deleteTaskDependency(con, task.getTask_id());

            // Insert new dependencies
            if (dependencyArr != null
                    && !dependencyArr.isEmpty()) {

                for (int item : dependencyArr) {
                    TaskDependency taskDependency = new TaskDependency(task.getTask_id(), item);
                    taskDependencyDAO.insertTaskDependency(con, taskDependency);
                }
            }

            con.commit();
        } catch (Exception e) {
            con.rollback();
            System.out.println("Exception Occurs : " + e);

            throw new Exception("Task Update Unsuccessful", e);

        } finally {
            con.close();
        }
    }
//
//    public void updateTaskDetails_Assignment(Task task, List<Integer> dependencyArr) throws Exception {
//
//        Connection con = DBConnection.getConnection();
//        con.setAutoCommit(false);
//
//        try {
//
//            TaskDAO taskDAO = new TaskDAO();
//            taskDAO.updateTaskDetails(con, task);
//
//            TaskAssignment taskAssignment = new TaskAssignment();
//            TaskAssignmentDAO taskAssignmentDAO = new TaskAssignmentDAO();
//
//            task.getRemovedAssignment().setTask_id(task.getTask_id());
//            taskAssignmentDAO.removeTaskAssignment(con, task.getRemovedAssignment());
//
//            boolean assignmentStatus = taskAssignmentDAO.checkTaskAssignmentStatus(taskAssignment);
//            System.out.println("Status : " + assignmentStatus);
//
//            if (assignmentStatus) {
//                taskAssignmentDAO.reactivateTaskAssignment(con, task.getTaskAssignment());
//            } else if (!assignmentStatus) {
//                System.out.println("Status : " + task.getTaskAssignment().getTask_id() + " " + task.getTaskAssignment().getTask_assigned_to());
//                taskAssignmentDAO.insertTaskAssignment(con, task.getTaskAssignment());
//            } else {
//                throw new Error("Task Assignment Failes");
//            }
//
//            // Delete task dependecy 
//            TaskDependencyDAO taskDependencyDAO = new TaskDependencyDAO();
//            taskDependencyDAO.deleteTaskDependency(con, task.getTask_id());
//
//            if (dependencyArr != null && !dependencyArr.isEmpty()) {
//                //form New Dependecy
//                for (int item : dependencyArr) {
//                    System.out.println(task.getTask_id() + " " + item);
//                    TaskDependency taskDependency = new TaskDependency(task.getTask_id(), item);
//                    taskDependencyDAO.insertTaskDependency(con, taskDependency);
//                }
//            }
//
//            con.commit();
//        } catch (Exception e) {
//            con.rollback();
//            System.out.println("Exception Occurs" + e);
//            throw new Exception("Task Update Unsucessfull", e);
//        } finally {
//            con.close();
//        }
//    }

    public void deleteTaskDetails_Assignment(int task_id) throws Exception {

        Connection con = DBConnection.getConnection();
        con.setAutoCommit(false);

        try {

            TaskDAO taskDAO = new TaskDAO();
            taskDAO.deleteTask(con, task_id);

            TaskAssignmentDAO taskAssignmentDAO = new TaskAssignmentDAO();
            taskAssignmentDAO.deleteTaskAssignment(con, task_id);

            con.commit();
        } catch (SQLIntegrityConstraintViolationException e) {
            con.rollback();
            throw new Exception("Cannot delete this task because other tasks depend on it.");
        } catch (Exception e) {
            con.rollback();
            System.out.println("Exception Occurs" + e);
            throw new Exception("Task Deletion Failed", e);
        } finally {
            con.close();
        }
    }
}
