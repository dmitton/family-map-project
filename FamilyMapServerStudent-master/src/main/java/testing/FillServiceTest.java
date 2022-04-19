package testing;

import dataAccess.DataAccessException;
import dataAccess.Database;
import dataAccess.EventDao;
import dataAccess.PersonDao;
import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;
import result.FillResult;
import result.RegisterResult;
import service.ClearService;
import service.FillService;
import service.RegisterService;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * testing class for the fill service
 */
public class FillServiceTest {
    private RegisterService register;
    private FillService fill;
    private ClearService clear;
    private User bestUser;
    private RegisterRequest request;

    /**
     * set up for before each test
     */
    @BeforeEach
    public void setUp() {
        register = new RegisterService();
        fill = new FillService();
        clear = new ClearService();
        bestUser = new User("johnsmith12","password","johnsmith@gmail.com"
                ,"John","Smith","m","12345");
        request = new RegisterRequest(bestUser.getUsername(),bestUser.getPassword(),bestUser.getEmail(),
                bestUser.getFirstName(),bestUser.getLastName(),bestUser.getGender());

        clear.clear();
    }

    /**
     * clear the database after each test
     */
    @AfterEach
    public void tearDown() {clear.clear();}

    /**
     * test for successfully filling the database
     * @throws DataAccessException
     */
    @Test
    public void fillPass() throws DataAccessException {
        RegisterResult result = register.register(request);
        FillResult fillResult = fill.fill(bestUser.getUsername(),4);
        Database db = new Database();;
        PersonDao pDao = new PersonDao(db.getConnection());
        EventDao eDao = new EventDao(db.getConnection());
        List<Person> returnedPeople = pDao.returnPeople(bestUser.getUsername());
        List<Event> returnedEvents = eDao.returnEvents(bestUser.getUsername());
        db.closeConnection(true);
        assertEquals(31,returnedPeople.size());
        assertEquals(91,returnedEvents.size());
    }

    /**
     * failed test due to invalid username
     * @throws DataAccessException
     */
    @Test
    public void fillFailInvalidUsername() throws DataAccessException {
        RegisterResult result = register.register(request);
        FillResult fillResult = fill.fill("johnny",4);
        assertEquals(false,fillResult.isSuccess());
        assertNotNull(fillResult.getMessage());
    }

}
