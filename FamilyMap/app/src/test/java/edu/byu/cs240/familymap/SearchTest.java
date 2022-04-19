package edu.byu.cs240.familymap;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import edu.byu.cs240.familymap.DataTransfer.DataCache;
import edu.byu.cs240.familymap.DataTransfer.ServerProxy;
import model.Event;
import model.Person;
import request.RegisterRequest;
import result.ClearResult;
import result.EventResult;
import result.PersonResult;
import result.RegisterResult;

public class SearchTest {

    //correct search for people
    @Test
    public void searchPeoplePass(){
        ServerProxy serverProxy = new ServerProxy();
        ClearResult clearResult = serverProxy.clear("localhost","8080");
        RegisterRequest registerRequest = new RegisterRequest("dmitton","6asdf","davidbmitton@gmail.com","David","Mitton","m");
        RegisterResult register = serverProxy.register("localhost","8080",registerRequest);

        EventResult eventResult = serverProxy.events("localhost","8080",register.getAuthToken());
        PersonResult personResult = serverProxy.people("localhost","8080",register.getAuthToken());

        DataCache dataCache = DataCache.getInstance();
        dataCache.setEvents(eventResult.getData());
        dataCache.setPeople(personResult.getData());

        String characters = "S";
        List<Person>searchPeople= dataCache.searchPeople(personResult.getData(),characters);

        assertNotNull(searchPeople);
        assertNotEquals(31,searchPeople.size());
    }

    //correct search for people where it returns none
    @Test
    public void searchPeoplePass2(){
        ServerProxy serverProxy = new ServerProxy();
        ClearResult clearResult = serverProxy.clear("localhost","8080");
        RegisterRequest registerRequest = new RegisterRequest("dmitton","6asdf","davidbmitton@gmail.com","David","Mitton","m");
        RegisterResult register = serverProxy.register("localhost","8080",registerRequest);

        EventResult eventResult = serverProxy.events("localhost","8080",register.getAuthToken());
        PersonResult personResult = serverProxy.people("localhost","8080",register.getAuthToken());

        DataCache dataCache = DataCache.getInstance();
        dataCache.setEvents(eventResult.getData());
        dataCache.setPeople(personResult.getData());

        String characters = "@";
        List<Person>searchPeople= dataCache.searchPeople(personResult.getData(),characters);

        assertEquals(0,searchPeople.size());
    }

    //correct search for events
    @Test
    public void searchEventPass(){
        ServerProxy serverProxy = new ServerProxy();
        ClearResult clearResult = serverProxy.clear("localhost","8080");
        RegisterRequest registerRequest = new RegisterRequest("dmitton","6asdf","davidbmitton@gmail.com","David","Mitton","m");
        RegisterResult register = serverProxy.register("localhost","8080",registerRequest);

        EventResult eventResult = serverProxy.events("localhost","8080",register.getAuthToken());
        PersonResult personResult = serverProxy.people("localhost","8080",register.getAuthToken());

        DataCache dataCache = DataCache.getInstance();
        dataCache.setEvents(eventResult.getData());
        dataCache.setPeople(personResult.getData());

        String characters = "S";
        List<Event>searchEvents = dataCache.searchEvents(eventResult.getData(),characters);

        assertNotNull(searchEvents);
        assertNotEquals(91,searchEvents.size());
    }

    //correct search for events where it returns none
    @Test
    public void searchEventPass2(){
        ServerProxy serverProxy = new ServerProxy();
        ClearResult clearResult = serverProxy.clear("localhost","8080");
        RegisterRequest registerRequest = new RegisterRequest("dmitton","6asdf","davidbmitton@gmail.com","David","Mitton","m");
        RegisterResult register = serverProxy.register("localhost","8080",registerRequest);

        EventResult eventResult = serverProxy.events("localhost","8080",register.getAuthToken());
        PersonResult personResult = serverProxy.people("localhost","8080",register.getAuthToken());

        DataCache dataCache = DataCache.getInstance();
        dataCache.setEvents(eventResult.getData());
        dataCache.setPeople(personResult.getData());

        String characters = "@";
        List<Event>searchEvents = dataCache.searchEvents(eventResult.getData(),characters);

        assertEquals(0,searchEvents.size());
    }

}
