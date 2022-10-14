package com.revature.services;

import com.revature.models.Ticket;
import com.revature.repos.TicketRepo;

import java.util.List;

public class TicketService {
    private TicketRepo ticketRepo;

    public TicketService() {
        ticketRepo = new TicketRepo();
    }

    public List<Ticket> getAllTicketsByUserID(String userID) {
        return ticketRepo.getAllTicketsByUserID(userID);
    }

    public List<Ticket> getAllTickets() {
        return ticketRepo.getAllTickets();
    }

    public void deleteAllTickets() {
    }

    public int createTicketWithID(Ticket ticket, String userID) {
        return ticketRepo.createWithUserIDInPath(ticket, userID);
    }
    public int createTicket(Ticket ticket) {
        return ticketRepo.create(ticket);
    }

    public Ticket getByID(int ticketCode) {
        return ticketRepo.getByID(ticketCode);
    }
}
