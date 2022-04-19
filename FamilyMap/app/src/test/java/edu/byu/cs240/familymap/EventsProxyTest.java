package edu.byu.cs240.familymap;

import org.junit.Test;
import static org.junit.Assert.*;
import edu.byu.cs240.familymap.DataTransfer.ServerProxy;
import request.RegisterRequest;
import result.ClearResult;
import result.EventResult;
import result.RegisterResult;

public class EventsProxyTest {

    @Test
    public void eventPass(){
        ServerProxy serverProxy = new ServerProxy();
        ClearResult clearResult = serverProxy.clear("localhost","8080");
        RegisterRequest registerRequest = new RegisterRequest("dmitton","6asdf","davidbmitton@gmail.com","David","Mitton","m");
        RegisterResult register = serverProxy.register("localhost","8080",registerRequest);

        EventResult eventResult = serverProxy.events("localhost","8080",register.getAuthToken());


        int size = eventResult.getData().size();

        assertEquals(91,size);
        assertTrue(eventResult.isSuccess());
    }

    @Test
    public void eventFail(){
        ServerProxy serverProxy = new ServerProxy();
        ClearResult clearResult = serverProxy.clear("localhost","8080");
        RegisterRequest registerRequest = new RegisterRequest("dmitton","6asdf","davidbmitton@gmail.com","David","Mitton","m");
        RegisterResult register = serverProxy.register("localhost","8080",registerRequest);

        EventResult eventResult = serverProxy.events("localhost","8080","123456");

        assertNull(eventResult.getData());
        assertNotNull(eventResult.getMessage());
        assertFalse(eventResult.isSuccess());
    }
}
