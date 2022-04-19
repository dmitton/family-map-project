package testing;

import dataAccess.DataAccessException;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;
import result.RegisterResult;
import service.ClearService;
import service.RegisterService;
import static org.junit.jupiter.api.Assertions.*;

/**
 * class for testing the register service
 */
public class RegisterServiceTest {
    private User bestUser;
    private RegisterService service;
    private ClearService clear;

    /**
     * set up before each test
     * @throws DataAccessException
     */
    @BeforeEach
    public void setUp() throws DataAccessException{
        clear = new ClearService();
        bestUser = new User("johnsmith12","password","johnsmith@gmail.com"
                ,"John","Smith","m","12345");
        service = new RegisterService();
        clear.clear();
    }

    /**
     * clear the database after each attempt
     * @throws DataAccessException
     */
    @AfterEach
    public void tearDown() throws DataAccessException{
        clear.clear();
    }

    /**
     * testing for registering a user in the database successfully
     * @throws DataAccessException
     */
    @Test
    public void registerPass() throws DataAccessException{
        RegisterRequest request = new RegisterRequest(bestUser.getUsername(),bestUser.getPassword(),bestUser.getEmail(),
                bestUser.getFirstName(),bestUser.getLastName(),bestUser.getGender());
        RegisterResult result = service.register(request);
        assertNotNull(result.getAuthToken());
        assertNotNull(result.getPersonID());
        assertEquals(bestUser.getUsername(),result.getUsername());
        assertEquals(true, result.isSuccess());
    }

    /**
     * testing for registering a user into the database unsuccessfully
     * @throws DataAccessException
     */
    @Test
    public void registerFail() throws DataAccessException{
        RegisterRequest request = new RegisterRequest(bestUser.getUsername(),bestUser.getPassword(),bestUser.getEmail(),
                bestUser.getFirstName(),bestUser.getLastName(),bestUser.getGender());
        RegisterResult result = service.register(request);
        result = service.register(request);
        assertEquals(null, result.getAuthToken());
        assertEquals(null,result.getPersonID());
        assertEquals("ERROR: The request was unsuccessful",result.getMessage());
        assertEquals(false,result.isSuccess());

    }



}
