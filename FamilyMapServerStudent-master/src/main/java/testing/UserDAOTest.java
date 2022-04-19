package testing;

import dataAccess.DataAccessException;
import dataAccess.Database;
import dataAccess.UserDao;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;

/**
 * testing class for data access to the user table in the database
 */
public class UserDAOTest {
    private Database db;
    private User bestUser;
    private UserDao uDao;

    /**
     * set up for before each test
     * @throws DataAccessException
     */
    @BeforeEach
    public void setUp()throws DataAccessException {
        db = new Database();
        bestUser = new User("johnsmith12","password","johnsmith@gmail.com"
                ,"John","Smith","m","12345");
        Connection conn = db.getConnection();
        db.clearTables();
        uDao = new UserDao(conn);
    }

    /**
     * close the connection after each test
     */
    @AfterEach
    public void tearDown() {
        db.closeConnection(false);
    }

    /**
     * testing insertion into the table successfully
     * @throws DataAccessException
     */
    @Test
    public void insertPass() throws DataAccessException{
        uDao.insertUser(bestUser);
        User compareTest = uDao.findUser(bestUser.getUsername());
        assertNotNull(compareTest);
        assertEquals(bestUser,compareTest);
    }

    /**
     * testing insertion into the table unsuccessfully
     * @throws DataAccessException
     */
    @Test
    public void insertFail() throws DataAccessException{
        uDao.insertUser(bestUser);
        assertThrows(DataAccessException.class, () -> uDao.insertUser(bestUser));
    }

    /**
     * finding a user in the database successfully
     * @throws DataAccessException
     */
    @Test
    public void findPass() throws DataAccessException{
        uDao.insertUser(bestUser);
        User compareTest = uDao.findUser(bestUser.getUsername());
        assertNotNull(compareTest);
        assertEquals(bestUser,compareTest);
    }

    /**
     * finding a user in the database unsuccessfully
     * @throws DataAccessException
     */
    @Test
    public void findFail() throws DataAccessException{
        User compareTest = uDao.findUser(bestUser.getUsername());
        assertEquals(null,compareTest);
    }

    /**
     * testing clearing the user table
     * @throws DataAccessException
     */
    @Test
    public void clearTest() throws DataAccessException{
        uDao.insertUser(bestUser);
        uDao.clearUserTable();
        User compareTest = uDao.findUser(bestUser.getUsername());
        assertEquals(null,compareTest);
    }


}
