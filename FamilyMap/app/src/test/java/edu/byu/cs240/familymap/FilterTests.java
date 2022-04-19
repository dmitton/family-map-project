package edu.byu.cs240.familymap;

import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;
import edu.byu.cs240.familymap.DataTransfer.DataCache;
import edu.byu.cs240.familymap.DataTransfer.ServerProxy;
import model.Event;
import model.Person;
import request.RegisterRequest;
import result.ClearResult;
import result.EventResult;
import result.PersonResult;
import result.RegisterResult;

public class FilterTests {

    //tests the male filters on all events
    @Test
    public void maleFilterPass() {
        ServerProxy serverProxy = new ServerProxy();
        ClearResult clearResult = serverProxy.clear("localhost","8080");
        RegisterRequest registerRequest = new RegisterRequest("dmitton","6asdf","davidbmitton@gmail.com","David","Mitton","m");
        RegisterResult register = serverProxy.register("localhost","8080",registerRequest);

        EventResult eventResult = serverProxy.events("localhost","8080",register.getAuthToken());
        PersonResult personResult = serverProxy.people("localhost","8080",register.getAuthToken());

        List<Event> events = eventResult.getData();
        DataCache dataCache = DataCache.getInstance();
        dataCache.setPeople(personResult.getData());

        dataCache.setMaleEventsFilter(events);
        List<Event>maleEvents = dataCache.getMaleEvents();

        int size = maleEvents.size();
        boolean noFemaleEvents = true;

        for(int i = 0;i < events.size();++i){
            Person person = dataCache.returnPerson(events.get(i).getPersonID());
            if(person.getGender().equals("f")){
                noFemaleEvents = false;
            }
        }

        assertNotNull(maleEvents);
        assertFalse(noFemaleEvents);

    }

    //tests the male filters on the mother side
    @Test
    public void maleFilterPass2() {
        ServerProxy serverProxy = new ServerProxy();
        ClearResult clearResult = serverProxy.clear("localhost","8080");
        RegisterRequest registerRequest = new RegisterRequest("dmitton","6asdf","davidbmitton@gmail.com","David","Mitton","m");
        RegisterResult register = serverProxy.register("localhost","8080",registerRequest);

        EventResult eventResult = serverProxy.events("localhost","8080",register.getAuthToken());
        PersonResult personResult = serverProxy.people("localhost","8080",register.getAuthToken());

        List<Event> events = eventResult.getData();
        List<Person> people = personResult.getData();
        DataCache dataCache = DataCache.getInstance();
        dataCache.setFirstName("David");
        dataCache.setLastName("Mitton");
        dataCache.setPeople(people);
        dataCache.setEvents(events);

        dataCache.setMotherSideFilter(events,people);

        List<Event>motherEvents = dataCache.getMotherSide();

        dataCache.setMaleEventsFilter(motherEvents);
        List<Event>maleEvents = dataCache.getMaleEvents();


        boolean noFemaleEvents = true;

        for(int i = 0;i < maleEvents.size();++i){
            Person person = dataCache.returnPerson(maleEvents.get(i).getPersonID());
            if(person.getGender().equals("f")){
                noFemaleEvents = false;
            }
        }

        assertNotNull(maleEvents);
        assertTrue(noFemaleEvents);

    }

    //tests the female filter
    @Test
    public void femaleFilterPass() {
        ServerProxy serverProxy = new ServerProxy();
        ClearResult clearResult = serverProxy.clear("localhost","8080");
        RegisterRequest registerRequest = new RegisterRequest("dmitton","6asdf","davidbmitton@gmail.com","David","Mitton","m");
        RegisterResult register = serverProxy.register("localhost","8080",registerRequest);

        EventResult eventResult = serverProxy.events("localhost","8080",register.getAuthToken());
        PersonResult personResult = serverProxy.people("localhost","8080",register.getAuthToken());

        List<Event> events = eventResult.getData();
        DataCache dataCache = DataCache.getInstance();
        dataCache.setPeople(personResult.getData());

        dataCache.setFemaleEventsFilter(events);
        List<Event>femaleEvents = dataCache.getFemaleEvents();

        boolean noMaleEvents = true;

        for(int i = 0;i < events.size();++i){
            Person person = dataCache.returnPerson(events.get(i).getPersonID());
            if(person.getGender().equals("m")){
                noMaleEvents = false;
            }
        }

        assertNotNull(femaleEvents);
        assertFalse(noMaleEvents);

    }

    //tests the female filter on the mothers side
    @Test
    public void femaleFilterPass2() {
        ServerProxy serverProxy = new ServerProxy();
        ClearResult clearResult = serverProxy.clear("localhost","8080");
        RegisterRequest registerRequest = new RegisterRequest("dmitton","6asdf","davidbmitton@gmail.com","David","Mitton","m");
        RegisterResult register = serverProxy.register("localhost","8080",registerRequest);

        EventResult eventResult = serverProxy.events("localhost","8080",register.getAuthToken());
        PersonResult personResult = serverProxy.people("localhost","8080",register.getAuthToken());

        List<Event> events = eventResult.getData();
        List<Person> people = personResult.getData();
        DataCache dataCache = DataCache.getInstance();
        dataCache.setFirstName("David");
        dataCache.setLastName("Mitton");
        dataCache.setPeople(people);
        dataCache.setEvents(events);

        dataCache.setMotherSideFilter(events,people);

        List<Event>motherEvents = dataCache.getMotherSide();

        dataCache.setFemaleEventsFilter(motherEvents);
        List<Event>femaleEvents = dataCache.getFemaleEvents();

        boolean noMaleEvents = true;

        for(int i = 0;i < femaleEvents.size();++i){
            Person person = dataCache.returnPerson(femaleEvents.get(i).getPersonID());
            if(person.getGender().equals("m")){
                noMaleEvents = false;
            }
        }

        assertNotNull(femaleEvents);
        assertTrue(noMaleEvents);

    }

    //tests the mother side filter on all events
    @Test
    public void motherSideFilterPass(){
        ServerProxy serverProxy = new ServerProxy();
        ClearResult clearResult = serverProxy.clear("localhost","8080");
        RegisterRequest registerRequest = new RegisterRequest("dmitton","6asdf","davidbmitton@gmail.com","David","Mitton","m");
        RegisterResult register = serverProxy.register("localhost","8080",registerRequest);

        EventResult eventResult = serverProxy.events("localhost","8080",register.getAuthToken());
        PersonResult personResult = serverProxy.people("localhost","8080",register.getAuthToken());

        List<Event> events = eventResult.getData();
        List<Person> people = personResult.getData();
        DataCache dataCache = DataCache.getInstance();
        dataCache.setFirstName("David");
        dataCache.setLastName("Mitton");
        dataCache.setPeople(people);
        dataCache.setEvents(events);

        dataCache.setMotherSideFilter(events,people);

        List<Event>motherEvents = dataCache.getMotherSide();

        assertNotNull(motherEvents);
        assertEquals(46,motherEvents.size());

    }

    //tests the mother side filters on filtered female events
    @Test
    public void motherSideFilterPass2() {
        ServerProxy serverProxy = new ServerProxy();
        ClearResult clearResult = serverProxy.clear("localhost","8080");
        RegisterRequest registerRequest = new RegisterRequest("dmitton","6asdf","davidbmitton@gmail.com","David","Mitton","m");
        RegisterResult register = serverProxy.register("localhost","8080",registerRequest);

        EventResult eventResult = serverProxy.events("localhost","8080",register.getAuthToken());
        PersonResult personResult = serverProxy.people("localhost","8080",register.getAuthToken());

        List<Event> events = eventResult.getData();
        List<Person> people = personResult.getData();
        DataCache dataCache = DataCache.getInstance();
        dataCache.setFirstName("David");
        dataCache.setLastName("Mitton");
        dataCache.setPeople(people);
        dataCache.setEvents(events);

        dataCache.setMotherSideFilter(events,people);

        List<Event>motherEvents = dataCache.getMotherSide();

        dataCache.setFemaleEventsFilter(motherEvents);
        List<Event>femaleEvents = dataCache.getFemaleEvents();


        boolean isMotherSide = true;
        for(int i = 0;i < femaleEvents.size();++i){
            if(femaleEvents.get(i).getPersonID().equals("087170aa-8347-41ae-8c75-d90bb87728f3")){
                isMotherSide = false;
            }

        }

        assertNotNull(femaleEvents);
        assertTrue(isMotherSide);
    }

    //tests the father side filters on all events
    @Test
    public void fatherSideFilterPass(){
        ServerProxy serverProxy = new ServerProxy();
        ClearResult clearResult = serverProxy.clear("localhost","8080");
        RegisterRequest registerRequest = new RegisterRequest("dmitton","6asdf","davidbmitton@gmail.com","David","Mitton","m");
        RegisterResult register = serverProxy.register("localhost","8080",registerRequest);

        EventResult eventResult = serverProxy.events("localhost","8080",register.getAuthToken());
        PersonResult personResult = serverProxy.people("localhost","8080",register.getAuthToken());

        List<Event> events = eventResult.getData();
        List<Person> people = personResult.getData();
        DataCache dataCache = DataCache.getInstance();
        dataCache.setFirstName("David");
        dataCache.setLastName("Mitton");
        dataCache.setPeople(people);
        dataCache.setEvents(events);

        dataCache.setFatherSideFilter(events,people);

        List<Event>fatherEvents = dataCache.getFatherSide();

        assertNotNull(fatherEvents);
        assertEquals(46,fatherEvents.size());

    }

    //tests the father side filter on female events
    @Test
    public void fatherSideFilterPass2() {
        ServerProxy serverProxy = new ServerProxy();
        ClearResult clearResult = serverProxy.clear("localhost","8080");
        RegisterRequest registerRequest = new RegisterRequest("dmitton","6asdf","davidbmitton@gmail.com","David","Mitton","m");
        RegisterResult register = serverProxy.register("localhost","8080",registerRequest);

        EventResult eventResult = serverProxy.events("localhost","8080",register.getAuthToken());
        PersonResult personResult = serverProxy.people("localhost","8080",register.getAuthToken());

        List<Event> events = eventResult.getData();
        List<Person> people = personResult.getData();
        DataCache dataCache = DataCache.getInstance();
        dataCache.setFirstName("David");
        dataCache.setLastName("Mitton");
        dataCache.setPeople(people);
        dataCache.setEvents(events);

        dataCache.setFatherSideFilter(events,people);

        List<Event>fatherEvents = dataCache.getFatherSide();

        dataCache.setFemaleEventsFilter(fatherEvents);
        List<Event>femaleEvents = dataCache.getFemaleEvents();


        boolean isFatherSide = true;
        for(int i = 0;i < femaleEvents.size();++i){
            if(femaleEvents.get(i).getPersonID().equals("58548fb4-933b-45a4-bf09-6fea0807456b")){
                isFatherSide = false;
            }

        }

        assertNotNull(femaleEvents);
        assertTrue(isFatherSide);
    }
}
