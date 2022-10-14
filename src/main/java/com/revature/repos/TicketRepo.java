package com.revature.repos;

import com.revature.models.Ticket;
import com.revature.models.User;
import com.revature.services.UserService;
import com.revature.utils.ConnectionManager;
import com.revature.utils.CrudDaoInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketRepo implements CrudDaoInterface<Ticket> {
    Connection con;
    List<Ticket> pendingtickets;
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
    public int createWithStatus(Ticket ticket) {
        if (ticket.getAmount() <= 0 || ticket.getDescription() == null) {
            if (ticket.getAmount() < 0)
                return -2;
            if (ticket.getAmount() == 0)
                return -4;
            if (ticket.getDescription() == null)
                return -3;
        } else {
            try {
                String sql = "INSERT INTO tickets(ticketid,amount,description,ticketstatus,userid) VALUES (?,?,?,?,?)";
                PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1,ticket.getId());
                pstmt.setDouble(2, ticket.getAmount());
                pstmt.setString(3, ticket.getDescription());
                pstmt.setString(4, ticket.getTicketStatus());
                pstmt.setInt(5, ticket.getUserid());

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

            String sql = "UPDATE tickets SET ticketstatus = ? WHERE ticketid = ?";
            try {
                PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, ticket.getTicketStatus());
                pstmt.setInt(2, ticket.getId());
                ResultSet rs = pstmt.executeQuery();
                System.out.println(ticket);
                return ticket;
            } catch (SQLException e) {
                LOGGER.error(e.getMessage());
                System.out.println(e.getMessage());
            }

        return null;
    }

    @Override
    public boolean delete(Ticket ticket) {
        // returns false is successful delete
        String sql = "DELETE FROM tickets WHERE ticketid = ?";
        try {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, ticket.getId());
            return pstmt.execute();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            System.out.println(e.getMessage());

        }
        return true;
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

    public List<Ticket> getAllTicketsThatArePending(User user) {
        //check if the user is a manager if not send error
        //get tickets that are pending only

        List<Ticket> allTickets = new ArrayList<>();
        String sql = "select * from tickets where ticketstatus = 'pending'";
        String sql2 = "select * from users where(username = ? and employeestatus = 'manager')";
        try {
            PreparedStatement pstmt = con.prepareStatement(sql);
            PreparedStatement pstmt2 = con.prepareStatement(sql2);
            pstmt2.setString(1, user.getUsername());
            ResultSet rs = pstmt.executeQuery();
            ResultSet rsu = pstmt2.executeQuery();
            if (rsu.next()) {

                while (rs.next()) {
                    Ticket ticket = new Ticket(rs.getInt("ticketid"),
                            rs.getDouble("amount"),
                            rs.getString("description"),
                            rs.getString("ticketstatus"),
                            rs.getInt("userid"));
                    System.out.println(ticket.toString());
                    allTickets.add(ticket);
                }
                return allTickets;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public int processTicket(User user, String ticketid, String decision) {
        String sql = "select * from tickets where ticketid = ?";
        String sql2 = "select * from users where(username = ? and employeestatus = 'manager')";
        try {
            PreparedStatement pstmt = con.prepareStatement(sql);
            PreparedStatement pstmt2 = con.prepareStatement(sql2);
            pstmt.setInt(1, Integer.parseInt(ticketid));
            pstmt2.setString(1, user.getUsername());
            ResultSet rs = pstmt.executeQuery();
            ResultSet rsu = pstmt2.executeQuery();

            if (rsu.next()) {
                System.out.println("manager was able to call this");
                while(rs.next()){
                    // ticket matched sql querry finding ticket
                    Ticket ticket = new Ticket(rs.getInt("ticketid"),
                            rs.getDouble("amount"),
                            rs.getString("description"),
                            rs.getString("ticketstatus"),
                            rs.getInt("userid"));
                    System.out.println(ticket.toString());
                    ticket.setTicketStatus(decision);
                    System.out.println(ticket.toString());
                    // make db reflect this ticket^
                    //this.update(ticket);
                    this.delete(ticket);
                    System.out.println(ticket.toString());
                    System.out.println(this.createWithStatus(ticket));

                    return ticket.getId();
                }
            } else {
                System.out.println("not a manager");
                return -1;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return -2;
        }
        return 0;
    }
}
