package edu.byu.cs240.familymap;

import static org.junit.Assert.*;
import org.junit.Test;
import edu.byu.cs240.familymap.DataTransfer.ServerProxy;
import request.RegisterRequest;
import result.ClearResult;
import result.RegisterResult;

public class RegisterProxyTesting {

    @Test
    public void registerPass(){
        ServerProxy serverProxy = new ServerProxy();
        ClearResult clearResult = serverProxy.clear("localhost","8080");
        RegisterRequest request = new RegisterRequest("emitton","emit","emitton@gmail.com","Erika","Mitton","f");
        RegisterResult result = serverProxy.register("localhost","8080",request);

        assertNull(result.getMessage());
        assertNotNull(result.getAuthToken());
        assertNotNull(result.getPersonID());
        assertTrue(result.isSuccess());
    }

    @Test
    public void registerFail(){
        ServerProxy serverProxy = new ServerProxy();
        ClearResult clearResult = serverProxy.clear("localhost","8080");
        RegisterRequest request1 = new RegisterRequest("emitton","emit","emitton@gmail.com","Erika","Mitton","f");
        RegisterResult result1 = serverProxy.register("localhost","8080",request1);
        RegisterRequest request = new RegisterRequest("emitton","emit","emitton@gmail.com","Erika","Mitton","f");
        RegisterResult result = serverProxy.register("localhost","8080",request);

        assertNotNull(result.getMessage());
        assertNull(result.getAuthToken());
        assertNull(result.getPersonID());
        assertFalse(result.isSuccess());
    }
}
