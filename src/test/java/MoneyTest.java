import com.revature.models.User;
import com.revature.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class MoneyTest {

    // Declares a static variable that will be referenced by all test cases
    public static UserService userService;
    @BeforeAll
    public static void setup(){
        // Creates UserService object to make a connection to the database and facilitate data querying, transfer, and manipulation
        userService = new UserService();
    }
    @BeforeEach
    public void clearTables(){
        //All tables are cleared before each test to ensure test are not reliant on other test
        userService.deleteAllUsers();
    }
    @Test
    public void canCreateNewUserWithUsernameAndPasscode() {
        // Ability to register an account, should register with at least a username and password(passcode)
        int idOfCreatedUser = userService.createUser(new User("tony", "passcode"));
        int idOfLookUp = userService.getByID(idOfCreatedUser).getId();
        Assertions.assertEquals(idOfLookUp, idOfCreatedUser);
    }
    @Test
    public void defaultRoleIsEmployee() {
        // Check default role is employee
        String username = "tony";
        userService.createUser(new User(username, "passcode"));
        Assertions.assertEquals("employee",userService.getByUserName(username).getRole());
    }

    @Test
    public void cantCreateNewUserWithSameUserName() {
        // Make sure new username can not be created if it is already registered
        userService.createUser(new User("tony", "passcode"));
        Assertions.assertEquals(-1,userService.createUser(new User("tony", "passcode")));
    }
    @Test public void userCanLogIn() {
        userService.createUser(new User("tony", "passcode"));
        Assertions.assertEquals(1,userService.login(new User("tony", "passcode")));

    }
    @Test public void userCantLogInPasscodeNoMatch() {
        userService.createUser(new User("tony", "passcode"));
        Assertions.assertEquals(-1,userService.login(new User("tony", "p")));
    }
    @Test public void userCantLogInUsernameMisMatch() {
        userService.createUser(new User("tony", "passcode"));
        Assertions.assertEquals(0,userService.login(new User("onion", "passcode")));
    }

    //@Test public void cantCreateNewUserWithSameId() {}
    //@Test public void checkIfKnownUserIsInDataBase() {}
    //@Test public void noDuplicateUserNames() {}
    //@Test public void employeeCanSubmitReimbursementTicket() {}
    //@Test public void reimbursementTicketHasDescriptionAndAmount() {}
    //@Test public void pendingReimbursementTicketsStoredAndSeenByManagers() {}
    //@Test public void employeeCanSeePastSubmittedReimbursementTickets() { }
}
