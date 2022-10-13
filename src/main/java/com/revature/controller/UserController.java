package com.revature.controller;

import com.revature.models.User;
import com.revature.services.UserService;
import io.javalin.http.Handler;

import java.util.List;

public class UserController {

    UserService userService;

    public UserController() {
        userService = new UserService();
    }

    public UserController(UserService userService) {
        this.userService = userService;
    }


    public Handler createNewUser = context -> {
        User user = context.bodyAsClass(User.class);// change json from postman to an object
        // the passed in user must contain a username and passcode
        System.out.println(user.getUsername() != null && user.getPasscode() == null);
        if (user.getUsername() == null || user.getPasscode() == null) {
            if (user.getUsername() != null && user.getPasscode() == null)
                context.result("You must enter a passcode").status(400);
            if (user.getUsername() == null && user.getPasscode() != null)
                context.result("You must enter a username").status(400);
        } else {
            //checks to see if the @user already exist
            User userFromDataBase = userService.getByUserName(user.getUsername());
            if (userFromDataBase != null) {
                context.result("User already exist").status(400);
            } else {
                //try's to create user
                int idOfCreatedUser = userService.createUser(user);
                // -2 is returned when an unaccounted for sql error occurs
                if (idOfCreatedUser == -2) {
                    context.result("User not created").status(401);
                } else {
                    //@user was made successfully and returns info from database indexed by @idOfCreatedUser
                    context.json(userService.getByID(idOfCreatedUser)).status(201);
                }
            }
        }
    };
    public Handler logUserIn = context -> {
        User user = context.bodyAsClass(User.class);// change json from postman to an object
        // the passed in user must contain a username and passcode to log in

        if (user.getUsername() == null || user.getPasscode() == null) {
            if (user.getUsername() != null && user.getPasscode() == null)
                context.result("You must enter a passcode").status(400);
            if (user.getUsername() == null && user.getPasscode() != null)
                context.result("You must enter a username").status(400);
        }else {
            int loginCode = userService.login(user);
            switch (loginCode) {
                case -1:

                    context.result("Your passcode does not match!").status(400);
                    break;
                case 0:
                    context.result("User does not exist, please register user").status(401);
                    break;
                case 1:

                    context.result("Successful login!").status(200);
                    break;
            }
        }
    };
}