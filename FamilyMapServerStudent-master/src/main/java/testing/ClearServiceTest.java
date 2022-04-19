package testing;

import dataAccess.DataAccessException;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;
import result.RegisterResult;
import service.ClearService;
import service.RegisterService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * class for testing clearing the database
 */
public class ClearServiceTest {
    private User bestUser;
    private ClearService clear;
    private RegisterService service;

    /**
     * clearing the database after the test
     * @throws DataAccessException
     */
    @AfterEach
    public void tearDown() throws DataAccessException{
        clear.clear();
    }

    /**
     * testing the clearing service for the database
     * @throws DataAccessException
     */
    @Test
    public void clearTest() throws DataAccessException{
        clear = new ClearService();
        service = new RegisterService();
        bestUser = new User("johnsmith12","password","johnsmith@gmail.com"
                ,"John","Smith","m","12345");

        RegisterRequest request = new RegisterRequest(bestUser.getUsername(),bestUser.getPassword(),bestUser.getEmail(),
                bestUser.getFirstName(),bestUser.getLastName(),bestUser.getGender());
        RegisterResult result = service.register(request);
        clear.clear();
        result = service.register(request);
        assertNotNull(result.getAuthToken());
        assertNotNull(result.getPersonID());
        assertEquals(bestUser.getUsername(),result.getUsername());
        assertEquals(true, result.isSuccess());
    }


}
