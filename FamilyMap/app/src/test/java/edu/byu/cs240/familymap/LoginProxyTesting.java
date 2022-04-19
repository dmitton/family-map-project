package edu.byu.cs240.familymap;

import static org.junit.Assert.*;
import org.junit.Test;
import edu.byu.cs240.familymap.DataTransfer.ServerProxy;
import request.LoginRequest;
import request.RegisterRequest;
import result.ClearResult;
import result.LoginResult;
import result.RegisterResult;

public class LoginProxyTesting {

    @Test
    public void loginPass(){
        ServerProxy serverProxy = new ServerProxy();
        ClearResult clearResult = serverProxy.clear("localhost","8080");
        RegisterRequest registerRequest = new RegisterRequest("dmitton","6asdf","davidbmitton@gmail.com","David","Mitton","m");
        RegisterResult register = serverProxy.register("localhost","8080",registerRequest);

        LoginRequest request = new LoginRequest("dmitton","6asdf");
        LoginResult loginResult = serverProxy.login("localhost","8080",request);

        assertNotNull(loginResult.getAuthToken());
        assertNotNull(loginResult.getPersonID());
        assertNull(loginResult.getMessage());
        assertTrue(loginResult.isSuccess());
    }

    @Test
    public void loginFail() {
        ServerProxy serverProxy = new ServerProxy();
        ClearResult clearResult = serverProxy.clear("localhost","8080");
        RegisterRequest registerRequest = new RegisterRequest("dmitton","6asdf","davidbmitton@gmail.com","David","Mitton","m");
        RegisterResult register = serverProxy.register("localhost","8080",registerRequest);

        LoginRequest request = new LoginRequest("dmitton4","6asdf");
        LoginResult loginResult = serverProxy.login("localhost","8080",request);

        assertNull(loginResult.getAuthToken());
        assertNull(loginResult.getPersonID());
        assertNotNull(loginResult.getMessage());
        assertFalse(loginResult.isSuccess());
    }
}
