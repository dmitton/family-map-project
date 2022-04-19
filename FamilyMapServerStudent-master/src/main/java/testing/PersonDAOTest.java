package testing;

import dataAccess.DataAccessException;
import dataAccess.Database;
import dataAccess.PersonDao;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * class for testing data access to the person table in the database
 */
public class PersonDAOTest {
    private Database db;
    private Person bestPerson;
    private PersonDao pDao;

    /**
     * set up for before each test
     * @throws DataAccessException
     */
    @BeforeEach
    public void setUp()throws DataAccessException {
        db = new Database();
        bestPerson = new Person("person123","johnsmith12","James"
                ,"Larson","m","father123","mother123","spouse123");
        Connection conn = db.getConnection();
        db.clearTables();
        pDao = new PersonDao(conn);
    }

    /**
     * closing the connection after each test
     */
    @AfterEach
    public void tearDown() {
        db.closeConnection(false);
    }

    /**
     * testing insertion into the person table successfully
     * @throws DataAccessException
     */
    @Test
    public void insertPass() throws DataAccessException{
        pDao.addPerson(bestPerson);
        Person compareTest = pDao.findPerson(bestPerson.getPersonID());
        assertNotNull(compareTest);
        assertEquals(bestPerson,compareTest);
    }

    /**
     * testing insertion into the person table unsuccessfully
     * @throws DataAccessException
     */
    @Test
    public void insertFail() throws DataAccessException{
        pDao.addPerson(bestPerson);
        assertThrows(DataAccessException.class, () -> pDao.addPerson(bestPerson));
    }

    /**
     * testing finding a person in the database successfully
     * @throws DataAccessException
     */
    @Test
    public void findPass() throws DataAccessException{
        pDao.addPerson(bestPerson);
        Person compareTest = pDao.findPerson(bestPerson.getPersonID());
        assertNotNull(compareTest);
        assertEquals(bestPerson,compareTest);
    }

    /**
     * testing finding a person in the database unsuccessfully
     * @throws DataAccessException
     */
    @Test
    public void findFail() throws DataAccessException{
        Person compareTest = pDao.findPerson(bestPerson.getPersonID());
        assertEquals(null,compareTest);
    }

    /**
     * testing clearing the person table in the database
     * @throws DataAccessException
     */
    @Test
    public void clearTest() throws DataAccessException{
        pDao.addPerson(bestPerson);
        pDao.clearPersonTable();
        Person compareTest = pDao.findPerson(bestPerson.getPersonID());
        assertEquals(null,compareTest);
    }

    /**
     * test for if you can clear the table with a specific username
     * @throws DataAccessException
     */
    @Test
    public void clearUsernameTestPass() throws DataAccessException{
        pDao.addPerson(bestPerson);
        Person testPerson = new Person("1234","barb12","Barbara","Wilding",
                "f","11","12","13");
        pDao.addPerson(testPerson);
        pDao.clearPersonWithUsername(bestPerson.getAssociatedUsername());
        Person compareTest1 = pDao.findPerson(bestPerson.getPersonID());
        Person compareTest2 = pDao.findPerson(testPerson.getPersonID());
        assertEquals(null,compareTest1);
        assertNotNull(compareTest2);
    }

    /**
     * unsuccessful test for clearing the table with a specific username
     * @throws DataAccessException
     */
    @Test
    public void clearUsernameTestFail() throws DataAccessException{
        pDao.addPerson(bestPerson);
        Person testPerson = new Person("1234","barb12","Barbara","Wilding",
                "f","11","12","13");
        pDao.addPerson(testPerson);
        pDao.clearPersonWithUsername("john12345");
        Person compareTest1 = pDao.findPerson(bestPerson.getPersonID());
        Person compareTest2 = pDao.findPerson(testPerson.getPersonID());
        assertNotNull(compareTest1);
        assertNotNull(compareTest2);
    }

    /**
     * passing test for returning a list of person with an associated username
     * @throws DataAccessException
     */
    @Test
    public void eventListPass() throws DataAccessException{
        pDao.addPerson(bestPerson);
        Person testPerson = new Person("person321","johnsmith12","James"
                ,"Lanyard","m","father321","mother321","spouse321");
        pDao.addPerson(testPerson);
        List<Person> personList = pDao.returnPeople(bestPerson.getAssociatedUsername());
        assertEquals(2,personList.size());
    }

    /**
     * failed test for retrieving persons with an associated username
     * @throws DataAccessException
     */
    @Test
    public void eventListFail() throws DataAccessException{
        pDao.addPerson(bestPerson);
        Person testPerson = new Person("person321","johnsmith12","James"
                ,"Lanyard","m","father321","mother321","spouse321");
        pDao.addPerson(testPerson);
        List<Person> personList = pDao.returnPeople("johnsmth12");
        assertNull(personList);
    }
}
