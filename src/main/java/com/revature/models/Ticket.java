package com.revature.models;

import java.util.Objects;

public class Ticket {
    private int id;
    private double amount;
    private String description;
    private String ticketStatus;
    private int userid;

    public Ticket() {
    }

    public Ticket(double amount, String description, int userid) {
        this.amount = amount;
        this.description = description;
        this.userid = userid;
    }

    public Ticket(int id, double amount, String description, String ticketStatus, int userid) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.ticketStatus = ticketStatus;
        this.userid = userid;
    }

    public Ticket(String description, int userid) {
        this.description = description;
        this.userid = userid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", ticketStatus=" + ticketStatus +
                ", userid=" + userid +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return id == ticket.id && Double.compare(ticket.amount, amount) == 0 && userid == ticket.userid && Objects.equals(description, ticket.description) && Objects.equals(ticketStatus, ticket.ticketStatus);
    }
}
