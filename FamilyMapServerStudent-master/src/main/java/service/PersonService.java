package service;

import dataAccess.AuthtokenDao;
import dataAccess.DataAccessException;
import dataAccess.Database;
import dataAccess.PersonDao;
import model.Authtoken;
import model.Person;
import result.PersonResult;

import java.util.List;

public class PersonService {
    /**
     * constructor for the PersonService
     */
    public PersonService() {}

    /**
     * returns all the family members of the current user
     * @param authtoken authtoken to get the current user
     * @return result object
     */
    public PersonResult persons(String authtoken){
        Database db = new Database();
        try{
            //open the connection and check to see if the authtoken given is in the database
            db.openConnection();
            AuthtokenDao aDao = new AuthtokenDao(db.getConnection());
            Authtoken returningAuthtoken = aDao.findAuthToken(authtoken);

            //if it is not, close the connection and send back an error message
            if(returningAuthtoken == null){
                db.closeConnection(false);
                PersonResult result = new PersonResult("Error: AuthToken is not found",false);
                return result;
            }

            //get a list of people from the database that have the same associated username as the authtoken
            PersonDao pDao = new PersonDao(db.getConnection());
            String username = returningAuthtoken.getUsername();
            List<Person> persons = pDao.returnPeople(username);

            //close the connection and send back a success result
            db.closeConnection(true);
            PersonResult result = new PersonResult(persons,true);
            return result;
        } catch(DataAccessException e){
            //close the connection and send back an error message
            e.printStackTrace();
            db.closeConnection(false);

            PersonResult result = new PersonResult("Error: the request was unsuccessful",false);
            return result;
        }

    }
}
