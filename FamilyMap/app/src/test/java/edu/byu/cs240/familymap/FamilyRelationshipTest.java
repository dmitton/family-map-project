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

public class FamilyRelationshipTest {

    //calculate the family relationships for the user
    @Test
    public void calculateFamilyRelationshipPass(){
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

        List<Person>usersFamily = dataCache.getPersons(user.getPersonID(),user.getMotherID(),user.getFatherID(),user.getSpouseID());

        boolean fatherThere = false;
        boolean motherThere = false;
        boolean spouseThere = false;

        for(int i = 0;i < usersFamily.size();++i){
            if(usersFamily.get(i).getPersonID().equals(user.getFatherID())){
                fatherThere = true;
            }
            if(usersFamily.get(i).getPersonID().equals(user.getMotherID())){
                motherThere = true;
            }
            if(usersFamily.get(i).getPersonID().equals(user.getSpouseID())){
                spouseThere = true;
            }
        }

        assertTrue(fatherThere);
        assertTrue(motherThere);
        assertFalse(spouseThere);

    }

    //calculate the family relationships for the father
    @Test
    public void calculateFamilyRelationshipPass2(){
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
        Person father = dataCache.returnPerson(user.getFatherID());

        List<Person>fathersFamily = dataCache.getPersons(father.getPersonID(),father.getMotherID(),father.getFatherID(),father.getSpouseID());

        boolean fatherThere = false;
        boolean motherThere = false;
        boolean spouseThere = false;
        boolean childThere = false;

        for(int i = 0;i < fathersFamily.size();++i){
            if(fathersFamily.get(i).getPersonID().equals(father.getFatherID())){
                fatherThere = true;
            }
            if(fathersFamily.get(i).getPersonID().equals(father.getMotherID())){
                motherThere = true;
            }
            if(fathersFamily.get(i).getPersonID().equals(father.getSpouseID())){
                spouseThere = true;
            }
            if(fathersFamily.get(i).getPersonID().equals(user.getPersonID())){
                childThere = true;
            }
        }

        assertTrue(fatherThere);
        assertTrue(motherThere);
        assertTrue(spouseThere);
        assertTrue(childThere);

    }
}
