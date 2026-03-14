package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author HP
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Database URL, user, and password for MySQL

    private static final String URL = "jdbc:mysql://localhost:3307/spmms";  // Your database name
//    private static final String URL = "jdbc:mysql://localhost:3307/quizsystem";  // Your database name

    private static final String USER = "root";  // MySQL username
//    private static final String PASSWORD = "";  // MySQL password (empty password for root)
    private static final String PASSWORD = "admin";  // MySQL password 

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Explicitly load the MySQL JDBC driver class
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Attempt to establish the connection to the database
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Check if connection was successful
            if (connection != null) {
                //System.out.println("Database connection successful.");
            } else {
                System.out.println("Database connection failed: Connection object is null.");
            }
        } catch (ClassNotFoundException e) {
            // This exception is thrown if the driver class is not found
            System.out.println("JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            // Print out the error message if connection fails
            System.out.println("Failed to connect to the database: " + e.getMessage());
        }
        return connection; // Return the connection object
    }
}
