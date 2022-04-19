package testing;

import dataAccess.AuthtokenDao;
import dataAccess.DataAccessException;
import dataAccess.Database;
import dataAccess.PersonDao;
import model.Authtoken;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import result.PersonResult;
import service.ClearService;
import service.PersonService;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * testing class for the person service
 */
public class PersonServiceTest {
    private PersonService persons;
    private ClearService clear;
    private Person bestPerson;
    private Person bestPerson2;
    private Authtoken bestAuthToken;

    /**
     * set up for before each test
     */
    @BeforeEach
    public void setUp(){
        persons = new PersonService();
        clear = new ClearService();

        bestPerson = new Person("person123","johnsmith12","James"
                ,"Larson","m","father123","mother123","spouse123");

        bestPerson2 = new Person("person321","johnsmith12","Jaden"
                ,"Smithy","m","father321","mother321","spouse321");

        bestAuthToken = new Authtoken("1234","johnsmith12");

        clear.clear();
    }

    /**
     * clearing the database after each test
     */
    @AfterEach
    public void tearDown() {
        clear.clear();
    }

    /**
     * passing test for getting a list of people
     * @throws DataAccessException
     */
    @Test
    public void personsPass() throws DataAccessException {
        Database db = new Database();;
        PersonDao pDao = new PersonDao(db.getConnection());
        AuthtokenDao aDao = new AuthtokenDao(db.getConnection());
        aDao.insertAuthtoken(bestAuthToken);
        pDao.addPerson(bestPerson);
        pDao.addPerson(bestPerson2);
        db.closeConnection(true);

        PersonResult result = persons.persons(bestAuthToken.getAuthToken());
        List<Person> personList = result.getData();
        assertEquals(2,personList.size());
        assertEquals(true,result.isSuccess());
    }

    /**
     * failed test for getting a list of people with an associated authtoken
     * @throws DataAccessException
     */
    @Test
    public void personsFail() throws DataAccessException{
        Database db = new Database();;
        PersonDao pDao = new PersonDao(db.getConnection());
        AuthtokenDao aDao = new AuthtokenDao(db.getConnection());
        aDao.insertAuthtoken(bestAuthToken);
        pDao.addPerson(bestPerson);
        pDao.addPerson(bestPerson2);
        db.closeConnection(true);

        PersonResult result = persons.persons("728380230");
        assertNotNull(result.getMessage());
        assertEquals(false,result.isSuccess());
    }
}
