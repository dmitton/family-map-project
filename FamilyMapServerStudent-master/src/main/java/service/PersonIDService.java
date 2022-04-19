package service;


import dataAccess.AuthtokenDao;
import dataAccess.DataAccessException;
import dataAccess.Database;
import dataAccess.PersonDao;
import model.Authtoken;
import model.Person;
import result.PersonIDResult;

import java.util.Objects;

/**
 * using a personID, get the person from the Person object associated with it in the database
 */
public class PersonIDService {
    /**
     * constructor for the PersonIDService
     */
    public PersonIDService() {}
    /**
     * return the Person object associated with the personID;
     * @param personID personID requesting
     * @return result of the request
     */
    public PersonIDResult person(String personID, String authtoken){
        Database db = new Database();
        try{
            //open the connection and get the authtoken given in the parameter, from the database
            db.openConnection();
            AuthtokenDao aDao = new AuthtokenDao(db.getConnection());
            Authtoken returningAuthtoken = aDao.findAuthToken(authtoken);

            //if the authtoken does not exist, close the connection and send back an error message
            if(returningAuthtoken == null){
                db.closeConnection(false);
                PersonIDResult result = new PersonIDResult("Error: authtoken is not found",false);
                return result;
            }

            //find the person in the database using the person ID given in the parameters
            PersonDao pDao = new PersonDao(db.getConnection());
            Person returningPerson = pDao.findPerson(personID);

            //if the person is null, close the connection and send back an error message
            if(returningPerson == null){
                db.closeConnection(false);
                PersonIDResult result = new PersonIDResult("Error: personID is not found",false);
                return result;
            }

            //if it does exist, check to make sure the authorization token username matches the person found
            else{
                String associatedUsername = returningAuthtoken.getUsername();

                //if it does not, close the connection and send back an error message
                if(!Objects.equals(associatedUsername, returningPerson.getAssociatedUsername())){
                    db.closeConnection(false);
                    PersonIDResult result = new PersonIDResult("Error: authtoken and the returning person's username are not the same",false);
                    return result;
                }

                //close the connection and send back a success result
                db.closeConnection(true);
                PersonIDResult result = new PersonIDResult(returningPerson.getAssociatedUsername(),returningPerson.getPersonID(),
                        returningPerson.getFirstName(),returningPerson.getLastName(),returningPerson.getGender(),
                        returningPerson.getFatherID(),returningPerson.getMotherID(),returningPerson.getSpouseID(),true);
                return result;
            }
        }catch(DataAccessException e){
            //close the connection and send back an error result
            e.printStackTrace();
            db.closeConnection(false);

            PersonIDResult result = new PersonIDResult("Error: the request was unsuccessful",false);
            return result;
        }
    }

}
