package testing;

import dataAccess.DataAccessException;
import dataAccess.Database;
import dataAccess.AuthtokenDao;
import model.Authtoken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;

/**
 * class for testing data access to the AuthToken table in the database
 */
public class AuthtokenDAOTest {
    private Database db;
    private Authtoken bestAuthToken;
    private AuthtokenDao aDao;

    /**
     * setup for before each test
     * @throws DataAccessException
     */
    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();
        bestAuthToken = new Authtoken("12345","johnSmith13");
        Connection conn = db.getConnection();
        db.clearTables();
        aDao = new AuthtokenDao(conn);
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
        aDao.insertAuthtoken(bestAuthToken);
        Authtoken compareTest = aDao.findAuthToken(bestAuthToken.getAuthToken());
        assertNotNull(compareTest);
        assertEquals(bestAuthToken, compareTest);
    }

    /**
     * testing insertion into the database unsuccessfully
     * @throws DataAccessException
     */
    @Test
    public void insertFail() throws DataAccessException {
        aDao.insertAuthtoken(bestAuthToken);
        assertThrows(DataAccessException.class, ()-> aDao.insertAuthtoken(bestAuthToken));
    }

    /**
     * testing finding an authtoken in the database successfully
     * @throws DataAccessException
     */
    @Test
    public void findPass() throws DataAccessException{
        aDao.insertAuthtoken(bestAuthToken);
        Authtoken compareTest = aDao.findAuthToken(bestAuthToken.getAuthToken());
        assertNotNull(compareTest);
        assertEquals(bestAuthToken,compareTest);
    }

    /**
     * testing finding an authtoken in the database unsuccessfully
     * @throws DataAccessException
     */
    @Test
    public void findFail() throws DataAccessException{
        Authtoken compareTest = aDao.findAuthToken(bestAuthToken.getAuthToken());
        assertEquals(null,compareTest);
    }

    /**
     * testing clearing the AuthToken table in the database
     * @throws DataAccessException
     */
    @Test
    public void clearTest() throws DataAccessException{
        aDao.insertAuthtoken(bestAuthToken);
        aDao.clearAuthtokenTable();
        Authtoken compareTest = aDao.findAuthToken(bestAuthToken.getUsername());
        assertEquals(null,compareTest);
    }
}
