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
import result.PersonIDResult;
import service.ClearService;
import service.PersonIDService;
import static org.junit.jupiter.api.Assertions.*;

/**
 * testing class for the personID service
 */
public class PersonIDServiceTest {
    private PersonIDService person;
    private ClearService clear;
    private Person bestPerson;
    private Authtoken bestAuthToken;

    /**
     * set up for before each test
     */
    @BeforeEach
    public void setUp(){
        person = new PersonIDService();
        clear = new ClearService();

        bestPerson = new Person("person123","johnsmith12","James"
                ,"Larson","m","father123","mother123","spouse123");

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
     * passing test for the personID service class
     * @throws DataAccessException
     */
    @Test
    public void personIDPass() throws DataAccessException {
        Database db = new Database();;
        PersonDao pDao = new PersonDao(db.getConnection());
        AuthtokenDao aDao = new AuthtokenDao(db.getConnection());
        aDao.insertAuthtoken(bestAuthToken);
        pDao.addPerson(bestPerson);
        db.closeConnection(true);

        PersonIDResult result = person.person(bestPerson.getPersonID(),bestAuthToken.getAuthToken());
        assertEquals(bestPerson.getAssociatedUsername(),result.getAssociatedUsername());
        assertEquals(bestPerson.getPersonID(),result.getPersonID());
        assertEquals(bestPerson.getFirstName(),result.getFirstName());
        assertEquals(bestPerson.getLastName(),result.getLastName());
        assertEquals(bestPerson.getGender(),result.getGender());
        assertEquals(true,result.isSuccess());
    }

    /**
     * failed test due to a bad person ID
     * @throws DataAccessException
     */
    @Test
    public void personIDFailBadPersonID() throws DataAccessException {
        Database db = new Database();;
        PersonDao pDao = new PersonDao(db.getConnection());
        AuthtokenDao aDao = new AuthtokenDao(db.getConnection());
        aDao.insertAuthtoken(bestAuthToken);
        pDao.addPerson(bestPerson);
        db.closeConnection(true);

        PersonIDResult result = person.person("165286",bestAuthToken.getAuthToken());
        assertNotNull(result.getMessage());
        assertEquals(false,result.isSuccess());
    }

    /**
     * failed test due to a failed authtoken
     * @throws DataAccessException
     */
    @Test
    public void personIDFailBadAuthToken() throws DataAccessException {
        Database db = new Database();;
        PersonDao pDao = new PersonDao(db.getConnection());
        AuthtokenDao aDao = new AuthtokenDao(db.getConnection());
        aDao.insertAuthtoken(bestAuthToken);
        pDao.addPerson(bestPerson);
        db.closeConnection(true);

        PersonIDResult result = person.person(bestPerson.getPersonID(), "9562");
        assertNotNull(result.getMessage());
        assertEquals(false,result.isSuccess());
    }

}
