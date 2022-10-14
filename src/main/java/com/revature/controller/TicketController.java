package com.revature.controller;

import com.revature.models.Ticket;
import com.revature.services.TicketService;
import io.javalin.http.Handler;

public class TicketController {

    private TicketService ticketService;

    public TicketController() {
        ticketService = new TicketService();
    }

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
        String userID = context.pathParam("id");

        // do this unless v userid doesnt refer to a manager
        context.json(ticketService.getAllTicketsThatArePending(userID)).status(200);
    };

    public Handler getAllTickets = context -> {
        if (ticketService.getAllTickets() == null) {
            context.result("No tickets were returned").status(400);
        } else {
            context.json(ticketService.getAllTickets()).status(200);
        }

    };

}
