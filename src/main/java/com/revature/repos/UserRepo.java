package com.revature.repos;

import com.revature.models.User;
import com.revature.utils.ConnectionManager;
import com.revature.utils.CrudDaoInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepo implements CrudDaoInterface<User> {

    Connection con;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRepo.class);

    public UserRepo() {
        try {
            con = ConnectionManager.getConnection();
            //System.out.println(con.getSchema());
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    @Override
    public int create(User user) {
        if (this.getByUserName(user.getUsername()) != null) {
            return -1;
        } else {
            try {
                String sql = "INSERT INTO users(userid,username,passcode,employeestatus) VALUES (default,?,?,default)";
                PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, user.getUsername());
                pstmt.setString(2, user.getPasscode());

                pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();

                rs.next();
                return rs.getInt("userid");

            } catch (SQLException e) {
                LOGGER.error(e.getMessage());
                // error not expected on creation
                return -2;
            }
        }
    }

    @Override
    public List<User> getAll() {

        List<User> users = new ArrayList<User>();

        try {
            String sql = "SELECT * FROM users";
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                User u = new User(rs.getInt("userid"),
                        rs.getString("username"),
                        rs.getString("passcode"), rs.getString("employeestatus")
                );

                users.add(u);
            }
            return users;
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public User getByID(int userid) {
        String sql = "SELECT * From users WHERE userid = ?";
        try {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, userid);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            User user = new User(rs.getInt("userid"),
                    rs.getString("username"),
                    rs.getString("passcode"),
                    rs.getString("employeestatus"));
            return user;
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            System.out.println(e.getMessage());
        }
        return null;
    }

    public User getByUserName(String username) {
        String sql = "SELECT * From users WHERE username = ?";
        try {
            PreparedStatement pstmt = con.prepareStatement(sql);

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            rs.next();

            User u = new User(rs.getInt("userid"),
                    rs.getString("username"),
                    rs.getString("passcode"),
                    rs.getString("employeestatus"));
            System.out.println(u.toString());
            return u;
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE users SET passcode = ? WHERE userid = ?";
        try {
            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = pstmt.executeQuery(sql);
            pstmt.setString(1, user.getPasscode());
            pstmt.setInt(2, user.getUserid());

            while (rs.next()) {
                user.setPasscode(rs.getString("passcode"));
            }
            return user;
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public boolean delete(User user) {
        String sql = "DELETE FROM users WHERE username = ?";
        try {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, user.getUsername());
            return pstmt.execute();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            System.out.println(e.getMessage());

        }
        return true;
    }

    public boolean deleteAllUsers() {

        List<User> allUsers = this.getAll();
        int sizeBefore;
        if (allUsers == null)
            sizeBefore = 0;
        else {
            sizeBefore = allUsers.size();
            for (User user : allUsers) {
                this.delete(user);
            }
        }
        allUsers = this.getAll();
        int sizeAfter = allUsers.size();
        if (sizeAfter >= 0)
            return false;
        return true;
    }

    public int login(User user) {
        User userInfoFromDatabase = this.getByUserName(user.getUsername());
        System.out.println(userInfoFromDatabase.toString());
        //this means there was a user with that username
        if (userInfoFromDatabase != null) {
            if (userInfoFromDatabase.getPasscode().equals(user.getPasscode())) {
                //passcode match the user passcode form the registered user in the database
                if (userInfoFromDatabase.getRole().equals("manager")) {
                    System.out.println("damn there a manager");
                    return userInfoFromDatabase.getUserid();
                }
                return userInfoFromDatabase.getUserid();
            }
            // passcode does not match
            return -1;
        }
        // user not found
        return 0;
    }
}
