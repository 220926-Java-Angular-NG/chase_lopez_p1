package com.revature;

import com.revature.controller.UserController;
import com.revature.services.UserService;
import io.javalin.Javalin;

public class Main
{
    public static void main(String[] args){
        Javalin app = Javalin.create().start(8080);
        UserController uc = new UserController();
        app.post("/registerUser", uc.createNewUser);
        app.post("/userLogin", uc.logUserIn);

    }
}
