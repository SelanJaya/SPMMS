/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

/**
 *
 * @author HP
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import DAO.DBConnection;
import beans.user;
import java.sql.ResultSet;

public class projectDAO {

    private Connection connection;
    
    public projectDAO() {
    // Initialize the connection
    connection = DBConnection.getConnection();
    
    // --- Connectivity Test Logic ---
    try {
        if (connection != null && !connection.isClosed()) {
            System.out.println("[projectDAO] SUCCESS: Database connection is active.");
            // Optional: Print the driver implementation class name
            System.out.println("[projectDAO] Connection Details: " + connection.toString());
        } else {
            System.err.println("[projectDAO] FAILURE: Connection is null or closed.");
        }
    } catch (Exception e) {
        System.err.println("[projectDAO] ERROR: Exception occurred during connection test.");
        e.printStackTrace();
    }
}

    
    // Create an answer for a submission
    public boolean signUp(user user) {
        try {
            String sql = "INSERT INTO users (username, phone_number, email, password_hash, user_role) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPhone_number());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getUser_role());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Create an answer for a submission
    public boolean login(user user) {
        try {
            String sql = "SELECT email, password_hash FROM users WHERE email = ? AND password_hash = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPassword());
            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return true;
            }
            else{
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    
}
