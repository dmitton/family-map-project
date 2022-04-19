package data;

import dataAccess.DataAccessException;
import dataAccess.Database;
import dataAccess.EventDao;
import dataAccess.PersonDao;
import model.Event;
import model.Person;
import java.io.IOException;
import java.util.*;

/**
 * class for generating the json data
 */
public class DataGenerator {
    /**
     * constructor for data generator
     */
    public DataGenerator(){}

    /**
     * generating a family tree
     * @param username username associated with the tree
     * @param person Person being added
     * @param generations number of generations
     * @param birthYear birthyear of the user
     */
    public void generatePerson(String username,Person person, int generations, int birthYear) {
        Database db = new Database();
        try {
            Person mother = null;
            Person father = null;
            if(generations >= 1) {
                //get the personId, then female first name, and then the random last name for the mother
                UUID randomPersonID = UUID.randomUUID();
                String motherPersonID = randomPersonID.toString();
                String motherFirstName = returnFemaleName();
                String motherLastName = returnSurName();
                mother = new Person(motherPersonID, username, motherFirstName, motherLastName, "f", null, null, null);

                //get the personId, then male first name, and then the random last name for the father
                UUID randomPersonID2 = UUID.randomUUID();
                String fatherPersonID = randomPersonID2.toString();
                String fatherFirstName = returnMaleName();
                String fatherLastName = returnSurName();
                father = new Person(fatherPersonID, username, fatherFirstName, fatherLastName, "m", null, null, null);

                //set the mother and father spouse ID and the person's mother and father IDs
                String fatherSpouseID = motherPersonID;
                String motherSpouseID = fatherPersonID;
                father.setSpouseID(fatherSpouseID);
                mother.setSpouseID(motherSpouseID);
                person.setMotherID(motherPersonID);
                person.setFatherID(fatherPersonID);

                //open and get the connection
                db.openConnection();
                EventDao eventDao = new EventDao(db.getConnection());

                //create the events for mom
                Location location = returnLocation();
                Event birth = createBirthEvent(mother, birthYear);
                Event marriage = createMarriageEvent(mother, birthYear, location);
                Event death = createDeathEvent(mother, birthYear);

                //add Mom's events to the database
                eventDao.insertEvent(birth);
                eventDao.insertEvent(marriage);
                eventDao.insertEvent(death);

                //create events for dad
                birth = createBirthEvent(father, birthYear);
                marriage = createMarriageEvent(father, birthYear, location);
                death = createDeathEvent(father, birthYear);

                //add Dad's events to the database
                eventDao.insertEvent(birth);
                eventDao.insertEvent(marriage);
                eventDao.insertEvent(death);
            }

            //add the person to the database
            PersonDao personDao = new PersonDao(db.getConnection());
            personDao.addPerson(person);

            //close the database
            db.closeConnection(true);


            //recursively go through the tree
            if (generations > 0) {
                generatePerson(username, mother, generations - 1, birthYear - 30);
                generatePerson(username, father, generations - 1, birthYear - 30);
            }

        }catch(DataAccessException e){
            e.printStackTrace();
            db.closeConnection(false);
        }

    }

    /**
     * returns a female name from the json list
     * @return a female name
     */
    public String returnFemaleName(){
        try {
            FemaleNameGenerator femaleNameGenerator = new FemaleNameGenerator();
            FemaleNameData femaleNameData = femaleNameGenerator.deserializeFemaleNameFile();
            String[] femaleNames = femaleNameGenerator.getFemaleNamesArray(femaleNameData);
            String femaleName = femaleNameGenerator.getRandom(femaleNames);
            return femaleName;

        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * returns a sur name from the json list
     * @return the sur name
     */
    public String returnSurName(){
        try {
            SurNameGenerator surNameGenerator = new SurNameGenerator();
            SurNameData surNameData = surNameGenerator.deserializeSurNameFile();
            String[] surNames = surNameGenerator.getSurNamesArray(surNameData);
            String surName = surNameGenerator.getRandom(surNames);
            return surName;

        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * returns the male name from the json list
     * @return a male name
     */
    public String returnMaleName(){
        try {
            MaleNameGenerator maleNameGenerator = new MaleNameGenerator();
            MaleNameData maleNameData = maleNameGenerator.deserializeMaleNameFile();
            String[] maleNames = maleNameGenerator.getMaleNamesArray(maleNameData);
            String maleName = maleNameGenerator.getRandom(maleNames);
            return maleName;

        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * returns a location from a json list
     * @return a location
     */
    public Location returnLocation(){
        try {
            LocationGenerator locationGenerator = new LocationGenerator();
            LocationData locationData = locationGenerator.deserializeLocationFile();
            Location [] locations = LocationGenerator.getLocationArray(locationData);
            Location location = locationGenerator.getRandom(locations);
            return location;

        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }


    /**
     * create a birth event for a person
     * @param person person the event is being created for
     * @param birthYear the year that the person was born
     * @return an event
     */
    public Event createBirthEvent(Person person, int birthYear){
        //get a location
        Location location = returnLocation();

        UUID randomEventID = UUID.randomUUID();
        String eventID = randomEventID.toString();
        Event birthEvent = new Event(eventID,person.getAssociatedUsername(),person.getPersonID(),Float.parseFloat(location.getLatitude()),
                Float.parseFloat(location.getLongitude()),location.getCountry(),location.getCity(),"Birth",birthYear - 30);

        return birthEvent;
    }

    /**
     * creates a death event for a user
     * @param person person the event is being created for
     * @param birthYear year that the person was born in
     * @return an event
     */
    public Event createDeathEvent(Person person, int birthYear){
        //get a location
        Location location = returnLocation();

        UUID randomEventID = UUID.randomUUID();
        String eventID = randomEventID.toString();
        Event deathEvent = new Event(eventID,person.getAssociatedUsername(),person.getPersonID(),Float.parseFloat(location.getLatitude()),
                Float.parseFloat(location.getLongitude()),location.getCountry(),location.getCity(),"Death",birthYear + 15);

        return deathEvent;
    }

    /**
     * creates a marriage event for the person
     * @param person person the event is for
     * @param birthYear year of birth
     * @param location location of the marriage
     * @return an event
     */
    public Event createMarriageEvent(Person person, int birthYear, Location location){
        UUID randomEventID = UUID.randomUUID();
        String eventID = randomEventID.toString();

        Event marriageEvent = new Event(eventID,person.getAssociatedUsername(),person.getPersonID(),Float.parseFloat(location.getLatitude()),
                Float.parseFloat(location.getLongitude()),location.getCountry(),location.getCity(),"Marriage",birthYear - 5);

        return marriageEvent;
    }

}
