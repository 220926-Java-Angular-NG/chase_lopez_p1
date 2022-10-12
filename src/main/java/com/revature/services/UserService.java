package com.revature.services;

import com.revature.models.User;
import com.revature.repos.UserRepo;

import java.util.List;

public class UserService {
    UserRepo ur;

    public UserService() {
        ur = new UserRepo();
    }

    public UserService(UserRepo ur) {
        this.ur = ur;
    }

    public int createUser(User user) {
        return ur.create(user);
    }

    public List<User> getAllUsers() {
        return ur.getAll();
    }

    public User getByID(int i) {
        return ur.getByID(i);
    }

    public User updateUser(User user) {
        return ur.update(user);
    }

    public boolean deleteUser(User user) {
        return ur.delete(user);
    }

    public User getByUserName(String username) {
        return ur.getByUserName(username);
    }

    public void deleteAllUsers() {
        ur.deleteAllUsers();
    }

    public int login(User user){
        return ur.login(user);
    }
}
