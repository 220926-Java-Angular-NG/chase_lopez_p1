import com.revature.models.Ticket;
import com.revature.models.User;
import com.revature.services.TicketService;
import com.revature.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MoneyTest {

    // Declares a static variable that will be referenced by all test cases
    public static UserService userService;
    public static TicketService ticketService;

    @BeforeAll
    public static void setup() {
        // Creates UserService object to make a connection to the database and facilitate data querying, transfer, and manipulation
        userService = new UserService();
        ticketService = new TicketService();
    }

    @BeforeEach
    public void clearTables() {
        //All tables are cleared before each test to ensure test are not reliant on other test
        userService.deleteAllUsers();
        ticketService.deleteAllTickets();
    }
    @Test
    public void canCreateNewUserWithUsernameAndPasscode() {
        // Ability to register an account, should register with at least a username and password(passcode)
        int idOfCreatedUser = userService.createUser(new User("toony", "passcode"));
        int idOfLookUp = userService.getByID(idOfCreatedUser).getUserid();
        Assertions.assertEquals(idOfLookUp, idOfCreatedUser);
    }
    @Test
    public void userCanLogIn() {
        userService.createUser(new User("tony", "passcode"));
        Assertions.assertEquals(1, userService.login(new User("tony", "passcode")));
    }
    @Test
    public void cantCreateNewUserWithSameUserName() {
        // Make sure new username can not be created if it is already registered
        userService.createUser(new User("tony", "passcode"));
        Assertions.assertEquals(-1, userService.createUser(new User("tony", "passcode")));
    }
    @Test
    public void createTicket() {
        int userID = userService.createUser(new User("tony", "passcode"));

        Ticket ticket = new Ticket(120.20, "description", userID);

        int idOfCreatedTicket = ticketService.createTicket(ticket);
        int idOfLookUp = ticketService.getByID(idOfCreatedTicket).getId();
        Assertions.assertEquals(idOfLookUp, idOfCreatedTicket);

    }
    @Test
    public void ticketDoesNotHaveDescription() {
        int userID = userService.createUser(new User("tony", "passcode"));
        Ticket ticket = new Ticket(120.20, null, userID);
        Assertions.assertEquals(-3, ticketService.createTicket(ticket));
    }
    @Test
    public void ticketDoesNoHaveAmount() {
        int userID = userService.createUser(new User("tony", "passcode"));
        ticketService.createTicket(new Ticket("only description and userid",userID));
        Assertions.assertEquals(-4, ticketService.createTicket(new Ticket( "description", 2)));
    }

    @Test
    public void ticketMustHavePositiveAmount() {
        int userID = userService.createUser(new User("tony", "passcode"));
        Assertions.assertEquals(-2, ticketService.createTicket(new Ticket(-120.20, "description", userID)));
    }

    @Test
    public void defaultRoleIsEmployee() {
        // Check default role is employee
        String username = "tony";
        userService.createUser(new User(username, "passcode"));
        Assertions.assertEquals("employee", userService.getByUserName(username).getRole());
    }
    @Test
    public void userCantLogInPasscodeNoMatch() {
        userService.createUser(new User("tony", "passcode"));
        Assertions.assertEquals(-1, userService.login(new User("tony", "p")));
    }

    @Test
    public void userCantLogInUsernameMisMatch() {
        userService.createUser(new User("tony", "passcode"));
        Assertions.assertEquals(0, userService.login(new User("onion", "passcode")));
    }

    @Test
    public void ticketSQLError() {
        Assertions.assertEquals(-1, ticketService.createTicket(new Ticket(120.20, "description", -2)));
    }
    @Test
    public void deleteAllTickets() {
        //delete tickets is called in the @before
        Assertions.assertEquals(0, ticketService.getAllTickets().size());
    }
    @Test
    public void deleteUser() {
        //delete tickets is called in the @before
        userService.createUser(new User("toony", "passcode"));
        Assertions.assertEquals(false, userService.deleteUser(new User("toony","passcode")));
    }

}
