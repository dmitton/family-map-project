package testing;

import dataAccess.*;
import model.Authtoken;
import model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import result.EventIDResult;
import service.ClearService;
import service.EventIDService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * testing class for the event ID service
 */
public class EventIDServiceTest {
    private EventIDService event;
    private ClearService clear;
    private Event bestEvent;
    private Authtoken bestAuthToken;

    /**
     * set up for before each test
     */
    @BeforeEach
    public void setUp(){
        event = new EventIDService();
        clear = new ClearService();

        bestEvent = new Event("Biking_123A", "johnsmith12", "Gale123A",
                35.9f, 140.1f, "Japan", "Ushiku",
                "Biking_Around", 2016);

        bestAuthToken = new Authtoken("1234","johnsmith12");

        clear.clear();
    }

    /**
     * clearing the database after each test
     */
    @AfterEach
    public void tearDown(){
        clear.clear();
    }

    /**
     * passing test for the eventID service class
     * @throws DataAccessException
     */
    @Test
    public void eventIDPass() throws DataAccessException {
        Database db = new Database();;
        EventDao eDao = new EventDao(db.getConnection());
        AuthtokenDao aDao = new AuthtokenDao(db.getConnection());
        aDao.insertAuthtoken(bestAuthToken);
        eDao.insertEvent(bestEvent);
        db.closeConnection(true);

        EventIDResult result = event.event(bestEvent.getEventID(),bestAuthToken.getAuthToken());
        assertEquals(bestEvent.getAssociatedUsername(),result.getAssociatedUsername());
        assertEquals(bestEvent.getEventID(),result.getEventID());
        assertEquals(bestEvent.getPersonID(),result.getPersonID());
        assertEquals(Float.toString(bestEvent.getLatitude()),result.getLatitude());
        assertEquals(Float.toString(bestEvent.getLongitude()),result.getLongitude());
        assertEquals(bestEvent.getCountry(),result.getCountry());
        assertEquals(bestEvent.getCity(),result.getCity());
        assertEquals(bestEvent.getEventType(),result.getEventType());
        assertEquals(Float.toString(bestEvent.getYear()),result.getYear());
        assertEquals(true,result.isSuccess());
    }

    /**
     * failed test due to a bad event ID
     * @throws DataAccessException
     */
    @Test
    public void eventIDFailBadPersonID() throws DataAccessException {
        Database db = new Database();;
        EventDao eDao = new EventDao(db.getConnection());
        AuthtokenDao aDao = new AuthtokenDao(db.getConnection());
        aDao.insertAuthtoken(bestAuthToken);
        eDao.insertEvent(bestEvent);
        db.closeConnection(true);

        EventIDResult result = event.event("165286",bestAuthToken.getAuthToken());
        assertNotNull(result.getMessage());
        assertEquals(false,result.isSuccess());
    }

    /**
     * failed test due to a failed authtoken
     * @throws DataAccessException
     */
    @Test
    public void eventIDFailBadAuthToken() throws DataAccessException {
        Database db = new Database();;
        EventDao eDao = new EventDao(db.getConnection());
        AuthtokenDao aDao = new AuthtokenDao(db.getConnection());
        aDao.insertAuthtoken(bestAuthToken);
        eDao.insertEvent(bestEvent);
        db.closeConnection(true);

        EventIDResult result = event.event(bestEvent.getEventID(),"asdasdf");
        assertNotNull(result.getMessage());
        assertEquals(false,result.isSuccess());
    }



}
