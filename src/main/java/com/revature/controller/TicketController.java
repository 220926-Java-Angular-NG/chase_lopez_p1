package com.revature.controller;

import com.revature.models.Ticket;
import com.revature.models.User;
import com.revature.services.TicketService;
import io.javalin.http.Handler;

public class TicketController {


    private TicketService ticketService;

    public TicketController() {
        ticketService = new TicketService();
    }

    public Handler processTicket = context -> {
        String userID = context.pathParam("id");
        String ticketid = context.pathParam("ticketid");
        String decision = context.body();
        int resultOfProcessRequest = ticketService.processTicket(userID, ticketid, decision);
        // create handels
        switch (resultOfProcessRequest) {
            case -1:
                context.result("Ticket status changed to denied").status(200);
                break;
            case 0:
                context.result("Ticket status not changed unknown error").status(401);
                break;
            case 1:
                context.result("Ticket status changed to accepted").status(200);
                break;
        }

    };
    public Handler createTicket = context -> {
        Ticket ticket = context.bodyAsClass(Ticket.class);
        String userID = context.pathParam("id");

        int ticketCode = ticketService.createTicketWithID(ticket, userID);
        if (ticketCode <= 0) {
            switch (ticketCode) {
                case -4:
                    context.result("Ticket must have a amount").status(400);
                    break;
                case -3:
                    context.result("Ticket must have a description").status(400);
                    break;
                case -2:
                    context.result("Ticket amount must be a positive number").status(400);
                    break;
                case -1:
                    context.result("Ticket not created sql error").status(400);
                    break;
                case 0:
                    context.result("Ticket not created unknown error").status(401);
                    break;
            }
        } else {
            context.json(ticketService.getByID(ticketCode)).status(200);

        }
    };
    public Handler getAllTicketsByUserID = context -> {
        String userID = context.pathParam("id");

        context.json(ticketService.getAllTicketsByUserID(userID)).status(200);

    };
    public Handler getAllPendingTickets = context -> {

        User user = context.bodyAsClass(User.class);
        if (user.getUsername() == null || user.getPasscode() == null) {
            if (user.getUsername() != null && user.getPasscode() == null)
                context.result("You must enter a passcode").status(400);
            if (user.getUsername() == null && user.getPasscode() != null)
                context.result("You must enter a username").status(400);
        }else {
            // do this unless v userid doesnt refer to a manager
            System.out.println(user.toString());
            context.json(ticketService.getAllTicketsThatArePending(user)).status(200);
        }
    };

    public Handler getAllTickets = context -> {
        if (ticketService.getAllTickets() == null) {
            context.result("No tickets were returned").status(400);
        } else {
            context.json(ticketService.getAllTickets()).status(200);
        }

    };

}
