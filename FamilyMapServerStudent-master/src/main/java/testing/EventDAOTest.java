package testing;

import dataAccess.DataAccessException;
import dataAccess.Database;
import dataAccess.EventDao;
import model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * class for testing data access to the Event table in the database
 */
public class EventDAOTest {
    private Database db;
    private Event bestEvent;
    private EventDao eDao;

    /**
     * set up before each test
     * @throws DataAccessException
     */
    @BeforeEach
    public void setUp() throws DataAccessException
    {
        db = new Database();
        bestEvent = new Event("Biking_123A", "Gale", "Gale123A",
                35.9f, 140.1f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        Connection conn = db.getConnection();
        db.clearTables();
        eDao = new EventDao(conn);
    }

    /**
     * closing the connection after each test
     * @throws DataAccessException
     */
    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    /**
     * testing insertion into the database successfully
     * @throws DataAccessException
     */
    @Test
    public void insertPass() throws DataAccessException {
        eDao.insertEvent(bestEvent);
        Event compareTest = eDao.findEvent(bestEvent.getEventID());
        assertNotNull(compareTest);
        assertEquals(bestEvent, compareTest);
    }

    /**
     * testing insertion into the database unsuccessfully
     * @throws DataAccessException
     */
    @Test
    public void insertFail() throws DataAccessException {
        eDao.insertEvent(bestEvent);
        assertThrows(DataAccessException.class, ()-> eDao.insertEvent(bestEvent));
    }

    /**
     * testing finding an event in the database successfully
     * @throws DataAccessException
     */
    @Test
    public void findPass() throws DataAccessException{
        eDao.insertEvent(bestEvent);
        Event compareTest = eDao.findEvent(bestEvent.getEventID());
        assertNotNull(compareTest);
        assertEquals(bestEvent,compareTest);
    }

    /**
     * testing finding an event in the database unsuccessfully
     * @throws DataAccessException
     */
    @Test
    public void findFail() throws DataAccessException{
        Event compareTest = eDao.findEvent(bestEvent.getEventID());
        assertEquals(null,compareTest);
    }

    /**
     * testing clearing the Event table in the database
     * @throws DataAccessException
     */
    @Test
    public void clearTest() throws DataAccessException{
        eDao.insertEvent(bestEvent);
        eDao.clearEventTable();
        Event compareTest = eDao.findEvent(bestEvent.getPersonID());
        assertEquals(null,compareTest);
    }

    /**
     * test for if you can clear the table with a specific username
     * @throws DataAccessException
     */
    @Test
    public void clearUsernameTestPass() throws DataAccessException{
        eDao.insertEvent(bestEvent);
        Event testEvent = new Event("9876","barb13","9845",234,564,
                "Canada","Toronto","Death",1957);
        eDao.insertEvent(testEvent);
        eDao.clearEventWithUsername(bestEvent.getAssociatedUsername());
        Event compareTest1 = eDao.findEvent(bestEvent.getEventID());
        Event compareTest2 = eDao.findEvent(testEvent.getEventID());
        assertEquals(null,compareTest1);
        assertNotNull(compareTest2);
    }

    /**
     * unsuccessful test for clearing the table with a specific username
     * @throws DataAccessException
     */
    @Test
    public void clearUsernameTestFail() throws DataAccessException{
        eDao.insertEvent(bestEvent);
        Event testEvent = new Event("9876","barb13","9845",234,564,
                "Canada","Toronto","Death",1957);
        eDao.insertEvent(testEvent);
        eDao.clearEventWithUsername("john1524");
        Event compareTest1 = eDao.findEvent(bestEvent.getEventID());
        Event compareTest2 = eDao.findEvent(testEvent.getEventID());
        assertNotNull(compareTest1);
        assertNotNull(compareTest2);
    }

    /**
     * passing test for returning a list of events with an associated username
     * @throws DataAccessException
     */
    @Test
    public void eventListPass() throws DataAccessException{
        eDao.insertEvent(bestEvent);
        Event testEvent = new Event("9876","Gale","9845",234,564,
                "Canada","Toronto","Death",1957);
        eDao.insertEvent(testEvent);
        List<Event> eventList = eDao.returnEvents(bestEvent.getAssociatedUsername());
        assertEquals(2,eventList.size());
    }

    /**
     * failed test for retrieving events with an associated username
     * @throws DataAccessException
     */
    @Test
    public void eventListFail() throws DataAccessException{
        eDao.insertEvent(bestEvent);
        Event testEvent = new Event("9876","Gale","9845",234,564,
                "Canada","Toronto","Death",1957);
        eDao.insertEvent(testEvent);
        List<Event> eventList = eDao.returnEvents("gale");
        assertNull(eventList);
    }

}

