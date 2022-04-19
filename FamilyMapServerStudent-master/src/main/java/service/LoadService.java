package service;

import dataAccess.*;
import model.Event;
import model.Person;
import model.User;
import request.LoadRequest;
import result.LoadResult;

import java.util.List;

/**
 * clears the database
 * loads the designated users, persons, and events into the database
 */
public class LoadService {
    /**
     * constructor for the LoadService
     */
    public LoadService() {}
    /**
     * load the designated users,persons, and events into the database
     * @param l LoadRequest
     * @return return a result object
     */
    public LoadResult load(LoadRequest l){
        //initialize the counter
        int userCount = 0;
        int personCount = 0;
        int eventCount = 0;


        Database db = new Database();
        try{
            //first clear all the tables
            db.openConnection();
            db.clearTables();
            db.closeConnection(true);

            db.openConnection();
            //open all the data access connections
            UserDao uDao = new UserDao(db.getConnection());
            PersonDao pDao = new PersonDao(db.getConnection());
            EventDao eDao = new EventDao(db.getConnection());

            //get all the lists from the request
            List<User> users = l.getUsers();
            List<Person> persons = l.getPersons();
            List<Event> events = l.getEvents();

            //next insert all the users into the database
            for(int i = 0;i < users.size();++i){
                uDao.insertUser(users.get(i));
                userCount += 1;
            }

            //next insert all the persons into the database
            for(int i = 0;i < persons.size();++i){
                pDao.addPerson(persons.get(i));
                personCount += 1;
            }

            //next insert all the events into the database
            for(int i = 0;i < events.size();++i){
                eDao.insertEvent(events.get(i));
                eventCount += 1;
            }

            db.closeConnection(true);

            //send back a success result
            LoadResult result = new LoadResult(true,"Successfully added " + userCount + " users, " + personCount +
                    " persons, and " + eventCount + " events to the database.");

            return result;

        }catch(DataAccessException e){
            //close the connection and send back and error result
            e.printStackTrace();
            db.closeConnection(false);

            LoadResult result = new LoadResult(false,"ERROR: The input was invalid or missing");
            return result;
        }
    }
}
