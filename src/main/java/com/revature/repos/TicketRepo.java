package com.revature.repos;

import com.revature.models.Ticket;
import com.revature.utils.ConnectionManager;
import com.revature.utils.CrudDaoInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketRepo implements CrudDaoInterface<Ticket> {
    Connection con;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRepo.class);

    public TicketRepo() {
        try {
            con = ConnectionManager.getConnection();
            //System.out.println(con.getSchema());
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    @Override
    public int create(Ticket ticket) {
        if (ticket.getAmount() <= 0 || ticket.getDescription() == null) {
            if (ticket.getAmount() < 0)
                return -2;
            if (ticket.getAmount() == 0)
                return -4;
            if (ticket.getDescription() == null)
                return -3;
        } else {
            try {
                String sql = "INSERT INTO tickets(amount,description,userid) VALUES (?,?,?)";
                PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pstmt.setDouble(1, ticket.getAmount());
                pstmt.setString(2, ticket.getDescription());
                pstmt.setInt(3, ticket.getUserid());

                pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();

                rs.next();
                return rs.getInt("ticketid");

            } catch (SQLException e) {
                LOGGER.error(e.getMessage());
                // error not expected on creation
                return -1;
            }
        }
        return 0;
    }

    public int createWithUserIDInPath(Ticket ticket, String userid) {
        try {
            ticket.setUserid(Integer.parseInt(userid));
            return this.create(ticket);
        } catch (NumberFormatException nfe) {
            LOGGER.error(nfe.getMessage());
            // error not expected on creation
            return -2;
        }
    }

    @Override
    public List<Ticket> getAll() {
        return null;
    }

    @Override
    public Ticket getByID(int id) {
        String sql = "SELECT * From tickets WHERE ticketid = ?";
        try {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            Ticket ticket = new Ticket(rs.getInt("ticketid"),
                    rs.getDouble("amount"),
                    rs.getString("description"),
                    rs.getString("ticketstatus"),
                    rs.getInt("userid"));
            return ticket;
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Ticket update(Ticket ticket) {
        return null;
    }

    @Override
    public boolean delete(Ticket ticket) {

        return false;
    }

    public List<Ticket> getAllTicketsByUserID(String userID) {
        List<Ticket> allTicketsFromSpecificUser = new ArrayList<>();
        String sql = "SELECT * From tickets where userid = ?";
        try {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(userID));

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Ticket ticket = new Ticket(rs.getInt("ticketid"),
                        rs.getDouble("amount"),
                        rs.getString("description"),
                        rs.getString("ticketstatus"),
                        rs.getInt("userid"));
                allTicketsFromSpecificUser.add(ticket);
            }
            return allTicketsFromSpecificUser;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Ticket> getAllTickets() {
        List<Ticket> allTickets = new ArrayList<>();
        String sql = "SELECT * From tickets";
        try {
            PreparedStatement pstmt = con.prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Ticket ticket = new Ticket(rs.getInt("ticketid"),
                        rs.getDouble("amount"),
                        rs.getString("description"),
                        rs.getString("ticketstatus"),
                        rs.getInt("userid"));
                allTickets.add(ticket);
            }
            return allTickets;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
