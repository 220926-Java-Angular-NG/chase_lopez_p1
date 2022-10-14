package com.revature;

import com.revature.controller.TicketController;
import com.revature.controller.UserController;
import io.javalin.Javalin;

public class Main
{
    public static void main(String[] args){
        Javalin app = Javalin.create().start(8080);
        UserController uc = new UserController();
        TicketController ticketController = new TicketController();
        //1 3
        app.post("/registerUser", uc.createNewUser);
        //2
        app.post("/userLogin", uc.logUserIn);
        //4 5
        app.post("/users/{id}/createTicket",ticketController.createTicket);
        //6 pending tickets in list for managers
        app.get("/users/{id}/managerTicketList",ticketController.getAllPendingTickets);
        //7 tickets can be processed


        app.get("/users/tickets",ticketController.getAllTickets);

        //8
        app.get("/users/{id}/tickets",ticketController.getAllTicketsByUserID);

    }
}
