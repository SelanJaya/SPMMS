package beans;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.time.LocalDateTime;
import javax.swing.text.PasswordView;

/**
 *
 * @author HP
 */
public class user {

    int user_id;
    String username, email, password, user_role, phone_number;
    LocalDateTime crated_at;

    public user() {
    }

    // constructor for login
    public user(String email, String password) {
        this.email = email;
        this.password = password;
    }

    //constructor for sign up
    public user(String username, String email, String phone_number, String password, String user_role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.user_role = user_role;
        this.phone_number = phone_number;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setCrated_at(LocalDateTime crated_at) {
        this.crated_at = crated_at;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUser_role() {
        return user_role;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public LocalDateTime getCrated_at() {
        return crated_at;
    }

    public String toString() {
        return "------------------------------\n"
                + "[User Object Content]\n"
                + "Username: " + username + "\n"
                + "Email:    " + email + "\n"
                + "Phone:    " + phone_number + "\n"
                + "Role:     " + user_role + "\n"
                + "Password: " + (password != null ? "********" : "null") + "\n"
                + // Security best practice: don't print actual password
                "------------------------------";
    }
}
