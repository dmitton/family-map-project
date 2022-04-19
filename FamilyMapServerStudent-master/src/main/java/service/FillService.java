package service;

import data.DataGenerator;
import data.Location;
import dataAccess.*;
import model.Event;
import model.Person;
import model.User;
import result.FillResult;
import java.util.List;
import java.util.UUID;


/**
 *fills a users profile with data regarding generations
 */
public class FillService {
    /**
     * constructor for the fill service
     */
    public FillService() {}
    /**
     * fills the users profile with data regarding generations
     * @param username the request to fill
     * @return the result of the request
     */
    public FillResult fill(String username,int generations) {
        //check to make sure that the generations is a valid number
        if(generations != (int)generations){
            FillResult result = new FillResult(false,"ERROR: Generations not valid");
            return result;
        }

        Database db = new Database();
        try{
            db.openConnection();

            //clear the tables with an associated username
            PersonDao personDao = new PersonDao(db.getConnection());
            EventDao eventDao = new EventDao(db.getConnection());
            personDao.clearPersonWithUsername(username);
            eventDao.clearEventWithUsername(username);

            //get the user from the database and check to see if it exists
            UserDao userDao = new UserDao(db.getConnection());
            User user = userDao.findUser(username);

            //if it does not exist then close the connection and send back an error message
            if(user == null){
                db.closeConnection(false);
                FillResult result = new FillResult(false,"ERROR: Invalid username");
                return result;
            }

            //generate a person using the user information and close the connection
            Person userPerson = new Person(user.getPersonID(),username,user.getFirstName(),user.getLastName(),user.getGender(),
                    null,null,null);
            db.closeConnection(true);


            //generate the tree starting with the person
            DataGenerator dataGenerator = new DataGenerator();
            dataGenerator.generatePerson(username,userPerson,generations,1999);

            //create a birth event to the user
            UUID randomEventID = UUID.randomUUID();
            String eventID = randomEventID.toString();
            Location location = dataGenerator.returnLocation();
            Event userBirthEvent = new Event(eventID,username,userPerson.getPersonID(),Float.parseFloat(location.getLatitude()),
                    Float.parseFloat(location.getLongitude()),location.getCountry(),location.getCity(),"Birth",1999);

            //add the users birth event to the database
            db.openConnection();
            eventDao = new EventDao(db.getConnection());
            PersonDao pDao = new PersonDao(db.getConnection());
            EventDao eDao = new EventDao(db.getConnection());
            eventDao.insertEvent(userBirthEvent);

            //check to see how many events and people were added
            List<Person> returnedPeople = pDao.returnPeople(username);
            List<Event> returnedEvents = eDao.returnEvents(username);
            db.closeConnection(true);

            //send back a successful result
            FillResult result = new FillResult(true,"Successfully added " + returnedPeople.size() + " persons and "
            + returnedEvents.size() + " events to the database.");
            return result;
        }catch(DataAccessException e){
            //close the connection and return an error result
            e.printStackTrace();
            db.closeConnection(false);
            FillResult result = new FillResult(false,"ERROR: The request was unsuccessful");
            return result;
        }
    }
}
