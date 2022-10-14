import com.revature.models.Ticket;
import com.revature.models.User;
import com.revature.services.TicketService;
import com.revature.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MoneyTestPrePopulatedTables {

    // Declares a static variable that will be referenced by all test cases
    public static UserService userService;
    public static TicketService ticketService;
    int userid1;
    int userid2;
    int userid3;

    @BeforeAll
    public static void setup() {
        // Creates UserService object to make a connection to the database and facilitate data querying, transfer, and manipulation
        userService = new UserService();
        ticketService = new TicketService();
    }

    @BeforeEach
    public void setUpTables() {
        //All tables are cleared before each test to ensure test are not reliant on other test
        userService.deleteAllUsers();
        ticketService.deleteAllTickets();
        userid1 = userService.createUser(new User("tony", "passcode"));
        userid2 = userService.createUser(new User("chase", "passcode"));
        userid3 = userService.createUser(new User("greg", "passcode"));
        // add multpiple tickets
        ticketService.createTicket(new Ticket(1, "soda", userid1));
        ticketService.createTicket(new Ticket(1.1, "soda with tax", userid1));
        ticketService.createTicket(new Ticket(1.20, "soda in 10 years", userid1));
        ticketService.createTicket(new Ticket(9999999.99, "bought a country", userid2));
        ticketService.createTicket(new Ticket(150.85, "cpu", userid2));
        ticketService.createTicket(new Ticket(1420.70, "gpu", userid2));
        ticketService.createTicket(new Ticket(200.37, "motherboard", userid2));
        ticketService.createTicket(new Ticket(100.70, "solid state drive", userid2));
        ticketService.createTicket(new Ticket(100, "a", userid3));
        ticketService.createTicket(new Ticket(100, "a", userid3));

    }

    //pending tickets are in que/ list can be seen by managers
    //tickets can be processed by managers
    @Test
    public void employeeCanSeePastSubmittedReimbursementTickets() {
        int totalTickets = ticketService.getAllTickets().size();
        int u1Tickets = ticketService.getAllTicketsByUserID(userid1 + "").size();
        int u2Tickets = ticketService.getAllTicketsByUserID(userid2 + "").size();
        int u3Tickets = ticketService.getAllTicketsByUserID(userid3 + "").size();
        Assertions.assertEquals(5, u2Tickets);
    }



}
