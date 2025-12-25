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
import beans.User;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private Connection connection;

    public UserDAO() {
    }

    public int signUp(User user) {
        int generatedId = -1;

        String sql = "INSERT INTO users (username, phone_number, email, password_hash, user_role) VALUES (?, ?, ?, ?, ?)";

        try ( Connection connection = DBConnection.getConnection(); PreparedStatement ps = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPhone_number());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getUser_role());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                // Retrieve the generated keys
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1); // Get the first column 
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return generatedId;
    }

//    // Create an answer for a submission
//    public boolean signUp(User user) {
//        try {
//            String sql = "INSERT INTO users (username, phone_number, email, password_hash, user_role) VALUES (?, ?, ?, ?, ?)";
//            PreparedStatement ps = connection.prepareStatement(sql);
//            ps.setString(1, user.getUsername());
//            ps.setString(2, user.getPhone_number());
//            ps.setString(3, user.getEmail());
//            ps.setString(4, user.getPassword());
//            ps.setString(5, user.getUser_role());
//
//            return ps.executeUpdate() > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
    // Create an answer for a submission
    public int login(User user) {
        int user_id = -1;
        String sql = "SELECT user_id, email, password_hash FROM users WHERE email = ? AND password_hash = ?";

        try (Connection connection = DBConnection.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                user_id = resultSet.getInt("user_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return user_id;
        }
        return user_id;
    }

    // Create an answer for a submission
    public User profileInformation(int user_id) {
        User user = null;
        String sql = "SELECT username, phone_number, email, user_role, created_at FROM users WHERE user_id = ? ";

        try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, user_id);

            ResultSet resultSet = preparedStatement.executeQuery();

            user = new User();
            if (resultSet.next()) {
                user.setUsername(resultSet.getString("username"));
                user.setPhone_number(resultSet.getString("phone_number"));
                user.setEmail(resultSet.getString("email"));
                user.setUser_role(resultSet.getString("user_role"));
                user.setCreated_at(resultSet.getObject("created_at", java.time.LocalDateTime.class));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public boolean updateProfile(User user) {
        boolean rowUpdated = false;
        // We don't update created_at because that should stay the same forever
        String sql = "UPDATE users SET username = ?, phone_number = ?, email = ?, user_role = ? WHERE user_id = ?";

        try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPhone_number());
            preparedStatement.setString(3, user.getEmail()); 
            preparedStatement.setString(4, user.getUser_role());
            preparedStatement.setInt(5, user.getUser_id());

            // executeUpdate returns the number of rows affected
            rowUpdated = preparedStatement.executeUpdate() > 0;

            
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rowUpdated;
    }
    
    public boolean deleteProfile(int user_Id) {
        boolean rowUpdated = false;
        // We don't update created_at because that should stay the same forever
        String sql = "DELETE FROM users WHERE user_id = ?";

        try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, user_Id);

            // executeUpdate returns the number of rows affected
            rowUpdated = preparedStatement.executeUpdate() > 0;    
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rowUpdated;
    }

}
