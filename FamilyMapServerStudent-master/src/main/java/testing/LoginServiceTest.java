package testing;

import dataAccess.DataAccessException;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.RegisterResult;
import service.ClearService;
import service.LoginService;
import service.RegisterService;
import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceTest {
    private User bestUser;
    private LoginService login;
    private RegisterService register;
    private RegisterRequest request;
    private ClearService clear;

    @BeforeEach
    public void setUp() throws DataAccessException{
        clear = new ClearService();
        bestUser = new User("johnsmith12","password","johnsmith@gmail.com"
                ,"John","Smith","m","12345");
        request = new RegisterRequest(bestUser.getUsername(),bestUser.getPassword(),bestUser.getEmail(),
                bestUser.getFirstName(),bestUser.getLastName(),bestUser.getGender());
        register = new RegisterService();
        login = new LoginService();
        clear.clear();
    }

    @AfterEach
    public void tearDown() throws DataAccessException{
        clear.clear();
    }

    @Test
    public void loginPass() throws DataAccessException{
        RegisterResult result = register.register(request);
        LoginRequest loginRequest = new LoginRequest(bestUser.getUsername(),bestUser.getPassword());
        LoginResult loginResult = login.login(loginRequest);
        assertNotNull(loginResult.getAuthToken());
        assertEquals(bestUser.getUsername(),loginResult.getUsername());
        assertEquals(result.getPersonID(),loginResult.getPersonID());
        assertEquals(true,loginResult.isSuccess());
    }

    @Test
    public void loginFailWrongUsername() throws DataAccessException{
        RegisterResult result = register.register(request);
        LoginRequest loginRequest = new LoginRequest("johnsmith",bestUser.getPassword());
        LoginResult loginResult = login.login(loginRequest);
        assertNull(loginResult.getAuthToken());
        assertNull(loginResult.getUsername());
        assertNull(loginResult.getPersonID());
        assertEquals(false,loginResult.isSuccess());
        assertNotNull(loginResult.getMessage());

    }

    @Test
    public void loginFailWrongPassword() throws DataAccessException{
        RegisterResult result = register.register(request);
        LoginRequest loginRequest = new LoginRequest(bestUser.getUsername(),"pasword");
        LoginResult loginResult = login.login(loginRequest);
        assertNull(loginResult.getAuthToken());
        assertNull(loginResult.getUsername());
        assertNull(loginResult.getPersonID());
        assertEquals(false,loginResult.isSuccess());
        assertNotNull(loginResult.getMessage());

    }


}
