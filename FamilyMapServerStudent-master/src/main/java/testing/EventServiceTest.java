package testing;

import dataAccess.*;
import model.Authtoken;
import model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import result.EventResult;
import service.ClearService;
import service.EventService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EventServiceTest {
    private EventService events;
    private ClearService clear;
    private Event bestEvent;
    private Event bestEvent2;
    private Authtoken bestAuthToken;

    /**
     * set up for before each test
     */
    @BeforeEach
    public void setUp(){
        events = new EventService();
        clear = new ClearService();

        bestEvent = new Event("Biking_123A", "johnsmith12", "Gale123A",
                35.9f, 140.1f, "Japan", "Ushiku",
                "Biking_Around", 2016);

        bestEvent2 = new Event("Biking_A321", "johnsmith12", "GaleA321",
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
     * passing test for getting a list of events
     * @throws DataAccessException
     */
    @Test
    public void eventsPass() throws DataAccessException {
        Database db = new Database();;
        EventDao eDao = new EventDao(db.getConnection());
        AuthtokenDao aDao = new AuthtokenDao(db.getConnection());
        aDao.insertAuthtoken(bestAuthToken);
        eDao.insertEvent(bestEvent);
        eDao.insertEvent(bestEvent2);
        db.closeConnection(true);

        EventResult result = events.events(bestAuthToken.getAuthToken());
        List<Event> eventList = result.getData();
        assertEquals(2,eventList.size());
        assertEquals(true,result.isSuccess());
    }

    /**
     * failed test for getting a list of events with an associated authtoken
     * @throws DataAccessException
     */
    @Test
    public void personsFail() throws DataAccessException{
        Database db = new Database();;
        EventDao eDao = new EventDao(db.getConnection());
        AuthtokenDao aDao = new AuthtokenDao(db.getConnection());
        aDao.insertAuthtoken(bestAuthToken);
        eDao.insertEvent(bestEvent);
        eDao.insertEvent(bestEvent2);
        db.closeConnection(true);

        EventResult result = events.events("372837hsnb");
        assertNotNull(result.getMessage());
        assertEquals(false,result.isSuccess());
    }


}
