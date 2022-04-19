package edu.byu.cs240.familymap;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import edu.byu.cs240.familymap.DataTransfer.DataCache;
import edu.byu.cs240.familymap.DataTransfer.EventComparator;
import edu.byu.cs240.familymap.DataTransfer.ServerProxy;
import model.Event;
import model.Person;
import request.RegisterRequest;
import result.ClearResult;
import result.EventResult;
import result.PersonResult;
import result.RegisterResult;

public class ChronologicalTest {

    //sorting a persons events pass
    @Test
    public void sortingPass(){
        ServerProxy serverProxy = new ServerProxy();
        ClearResult clearResult = serverProxy.clear("localhost","8080");
        RegisterRequest registerRequest = new RegisterRequest("dmitton","6asdf","davidbmitton@gmail.com","David","Mitton","m");
        RegisterResult register = serverProxy.register("localhost","8080",registerRequest);

        EventResult eventResult = serverProxy.events("localhost","8080",register.getAuthToken());
        PersonResult personResult = serverProxy.people("localhost","8080",register.getAuthToken());

        DataCache dataCache = DataCache.getInstance();
        dataCache.setEvents(eventResult.getData());
        dataCache.setPeople(personResult.getData());
        Person user = dataCache.returnPerson(register.getPersonID());
        List<Event> userEvents = dataCache.getPersonEvents(eventResult.getData(),user.getFatherID());
        userEvents.sort(new EventComparator());

        assertEquals("Birth",userEvents.get(0).getEventType());
        assertEquals("Marriage",userEvents.get(1).getEventType());

    }

    //sorting two peoples events together pass
    @Test
    public void sortPass2(){
        ServerProxy serverProxy = new ServerProxy();
        ClearResult clearResult = serverProxy.clear("localhost","8080");
        RegisterRequest registerRequest = new RegisterRequest("dmitton","6asdf","davidbmitton@gmail.com","David","Mitton","m");
        RegisterResult register = serverProxy.register("localhost","8080",registerRequest);

        EventResult eventResult = serverProxy.events("localhost","8080",register.getAuthToken());
        PersonResult personResult = serverProxy.people("localhost","8080",register.getAuthToken());

        DataCache dataCache = DataCache.getInstance();
        dataCache.setEvents(eventResult.getData());
        dataCache.setPeople(personResult.getData());
        Person user = dataCache.returnPerson(register.getPersonID());
        List<Event> fatherEvents = dataCache.getPersonEvents(eventResult.getData(),user.getFatherID());
        List<Event>motherEvents = dataCache.getPersonEvents(eventResult.getData(),user.getMotherID());

        for(int i = 0;i < motherEvents.size();++i){
            fatherEvents.add(motherEvents.get(i));
        }

        fatherEvents.sort(new EventComparator());

        assertEquals("Birth",fatherEvents.get(0).getEventType());
        assertEquals("Birth",fatherEvents.get(1).getEventType());

    }
}
