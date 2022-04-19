package service;

import dataAccess.*;
import model.Authtoken;
import model.Event;
import result.EventIDResult;
import java.util.Objects;

/**
 * class for returning an event based on the event ID
 */
public class EventIDService {
    /**
     * constructor for EventIDService
     */
    public EventIDService(){}
    /**
     * return the Event object associated with the specified ID;
     * @param eventID eventID to get the request
     * @param authtoken to verify the user
     * @return result of the request
     */
    public EventIDResult event(String eventID,String authtoken){
        Database db = new Database();
        try{
            //open a connection and get the authtoken from the authtoken data table
            db.openConnection();
            AuthtokenDao aDao = new AuthtokenDao(db.getConnection());
            Authtoken returningAuthtoken = aDao.findAuthToken(authtoken);

            //if the authtoken is null, close the connection and send back an unsuccessful result
            if(returningAuthtoken == null){
                db.closeConnection(false);
                EventIDResult result = new EventIDResult("Error: authtoken is not found",false);
                return result;
            }

            //find the event that has the specific event ID
            EventDao eDao = new EventDao(db.getConnection());
            Event returnedEvent = eDao.findEvent(eventID);

            //if the event is null, close the connection and return an unsuccessful result
            if(returnedEvent == null){
                db.closeConnection(false);
                EventIDResult result = new EventIDResult("Error: eventID is not found",false);
                return result;
            }

            //if it is successful check to make sure the authorization token's username is the same as the returned event
            else{
                String associatedUsername = returningAuthtoken.getUsername();

                //if it is not, close the connection and send back an unsuccessful result
                if(!Objects.equals(associatedUsername, returnedEvent.getAssociatedUsername())){
                    db.closeConnection(false);
                    EventIDResult result = new EventIDResult("Error: authtoken and the returning event's username are not the same",false);
                    return result;
                }

                //close the connection and send back a successful result
                db.closeConnection(true);
                EventIDResult result = new EventIDResult(returnedEvent.getAssociatedUsername(),returnedEvent.getEventID(),returnedEvent.getPersonID(),
                        Float.toString(returnedEvent.getLatitude()),Float.toString(returnedEvent.getLongitude()),returnedEvent.getCountry(),returnedEvent.getCity(),
                        returnedEvent.getEventType(),Float.toString(returnedEvent.getYear()),true);
                return result;
            }
        }catch(DataAccessException e){
            //close the connection and send an error result
            e.printStackTrace();
            db.closeConnection(false);

            EventIDResult result = new EventIDResult("Error: the request was unsuccessful",false);
            return result;
        }

    }
}
