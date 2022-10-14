package com.revature.models;

import java.util.Objects;

public class User {
    private int userid;
    private String username;
    private String passcode;

    private String employeestatus;

    public User() {
    }

    public User(String username, String passcode) {
        this.username = username;
        this.passcode = passcode;
    }

    public User(int id, String username, String passcode, String employeestatus) {
        this.userid = id;
        this.username = username;
        this.passcode = passcode;
        this.employeestatus = employeestatus;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }

    public String getRole() {
        return employeestatus;
    }

    public void setEmployeestatus(String role) {
        this.employeestatus = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + userid +
                ", username='" + username + '\'' +
                ", passcode='" + passcode + '\'' +
                ", employeestatus='" + employeestatus + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userid == user.userid && Objects.equals(username, user.username) && Objects.equals(passcode, user.passcode) && Objects.equals(employeestatus, user.employeestatus);
    }
}
