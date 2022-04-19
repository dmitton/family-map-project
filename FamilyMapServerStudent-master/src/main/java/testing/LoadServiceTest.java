package testing;

import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoadRequest;
import result.LoadResult;
import service.ClearService;
import service.LoadService;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * testing class for the load service
 */
public class LoadServiceTest {
    private LoadService load;
    private ClearService clear;
    private List<User>users;
    private List<Person>persons;
    private List<Event> events;
    private User bestUser;
    private Person bestPerson;
    private Event bestEvent;
    private User bestUser2;
    private Person bestPerson2;
    private Event bestEvent2;

    /**
     * set up that happens before each test
     */
    @BeforeEach
    public void setUp() {
        load = new LoadService();
        clear = new ClearService();
        users = new ArrayList<>();
        persons = new ArrayList<>();
        events = new ArrayList<>();

        clear.clear();

        bestUser = new User("johnsmith12","password","johnsmith@gmail.com"
                ,"John","Smith","m","12345");
        bestUser2 = new User("jadensmith12","jsmith","jadensmith@gmail.com"
                ,"Jaden","Smith","m","54321");

        users.add(bestUser);
        users.add(bestUser2);

        bestPerson = new Person("person123","johnsmith12","James"
                ,"Larson","m","father123","mother123","spouse123");

        bestPerson2 = new Person("person321","jadensmith12","Jaden"
                ,"Smithy","m","father321","mother321","spouse321");

        persons.add(bestPerson);
        persons.add(bestPerson2);

        bestEvent = new Event("Biking_123A", "Gale", "Gale123A",
                35.9f, 140.1f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        bestEvent2 = new Event("Biking_A321", "jadensmith", "GaleA321",
                40.1f, 141.1f, "ukraine", "kiev",
                "Baptism", 2019);

        events.add(bestEvent);
        events.add(bestEvent2);
    }

    /**
     * clearing the database after each test
     */
    @AfterEach
    public void tearDown() {
        clear.clear();
    }

    /**
     * passing test for loading data into the database
     */
    @Test
    public void loadPass(){
        LoadRequest request = new LoadRequest(users,persons,events);
        LoadResult result = load.load(request);
        assertEquals(true,result.isSuccess());
        assertEquals("Successfully added 2 users, 2 persons, and 2 events to the database.",result.getMessage());
    }

    /**
     * failed test for loading data into the database
     */
    @Test
    public void loadFailInvalidValue(){
        bestUser2 = new User("johnsmith12","password","johnsmith25@gmail.com"
                ,"John","Smith","m","32541");
        users.add(bestUser2);
        LoadRequest request = new LoadRequest(users,persons,events);
        LoadResult result = load.load(request);
        assertEquals(false,result.isSuccess());
        assertNotNull(result.getMessage());

    }

    /**
     * failed test due to missing information
     */
    @Test
    public void loadFailMissingValue(){
        bestUser2 = new User(null,"password","johnsmith25@gmail.com"
                ,"John","Smith","m","32541");
        users.add(bestUser2);
        LoadRequest request = new LoadRequest(users,persons,events);
        LoadResult result = load.load(request);
        assertEquals(false,result.isSuccess());
        assertNotNull(result.getMessage());
    }
}
