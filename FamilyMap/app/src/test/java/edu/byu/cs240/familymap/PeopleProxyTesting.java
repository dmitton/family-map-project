package edu.byu.cs240.familymap;

import org.junit.Test;
import static org.junit.Assert.*;
import edu.byu.cs240.familymap.DataTransfer.ServerProxy;
import request.RegisterRequest;
import result.ClearResult;
import result.PersonResult;
import result.RegisterResult;

public class PeopleProxyTesting {

    @Test
    public void peoplePass(){
        ServerProxy serverProxy = new ServerProxy();
        ClearResult clearResult = serverProxy.clear("localhost","8080");
        RegisterRequest registerRequest = new RegisterRequest("dmitton","6asdf","davidbmitton@gmail.com","David","Mitton","m");
        RegisterResult register = serverProxy.register("localhost","8080",registerRequest);

        PersonResult personResult = serverProxy.people("localhost","8080",register.getAuthToken());

        int size = personResult.getData().size();

        assertEquals(31,size);
        assertTrue(personResult.isSuccess());
    }

    @Test
    public void peopleFail(){
        ServerProxy serverProxy = new ServerProxy();
        ClearResult clearResult = serverProxy.clear("localhost","8080");
        RegisterRequest registerRequest = new RegisterRequest("dmitton","6asdf","davidbmitton@gmail.com","David","Mitton","m");
        RegisterResult register = serverProxy.register("localhost","8080",registerRequest);

        PersonResult personResult = serverProxy.people("localhost","8080","123456");

        assertNull(personResult.getData());
        assertNotNull(personResult.getMessage());
        assertFalse(personResult.isSuccess());
    }

}
