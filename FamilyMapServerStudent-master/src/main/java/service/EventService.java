package service;

import dataAccess.*;
import model.Authtoken;
import model.Event;
import result.EventResult;

import java.util.List;

/**
 * return all events associated with the current user
 */
public class EventService {
    /**
     * constructor for EventService
     */
    public EventService() {}

    /**
     * returns all events for all family members of the current user
     * @param authtoken the authtoken to make the request
     * @return the result of the request
     */
    public EventResult events(String authtoken){
        Database db = new Database();
        try{
            //open the connection and get the authorization token from the database
            db.openConnection();
            AuthtokenDao aDao = new AuthtokenDao(db.getConnection());
            Authtoken returningAuthtoken = aDao.findAuthToken(authtoken);

            //if the authtoken is null, then close the connection and send back an unsuccessful result
            if(returningAuthtoken == null){
                db.closeConnection(false);
                EventResult result = new EventResult("Error: AuthToken is not found",false);
                return result;
            }

            //get the list of events that correspond with a given username
            EventDao eDao = new EventDao(db.getConnection());
            String username = returningAuthtoken.getUsername();
            List<Event> events = eDao.returnEvents(username);

            //close the connection and return a successful result
            db.closeConnection(true);
            EventResult result = new EventResult(events,true);
            return result;
        } catch(DataAccessException e){
            //close the connection and return an error result
            e.printStackTrace();
            db.closeConnection(false);

            EventResult result = new EventResult("Error: the request was unsuccessful",false);
            return result;
        }
    }
}
