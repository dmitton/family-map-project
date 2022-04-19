package service;

import dataAccess.AuthtokenDao;
import dataAccess.DataAccessException;
import dataAccess.Database;
import dataAccess.UserDao;
import model.Authtoken;
import model.User;
import request.LoginRequest;
import result.LoginResult;

import java.util.Objects;
import java.util.UUID;

/**
 * class to perform the log in process
 */
public class LoginService {
    /**
     * constructor for the LoginService
     */
    public LoginService() {}
    /**
     * method for login
     * @param r the login request
     * @return the result of the request
     */
    public LoginResult login(LoginRequest r){
        Database db = new Database();
        try{
            //open the connection and get the user and authtoken data access connections
            db.openConnection();
            UserDao userDao = new UserDao(db.getConnection());
            AuthtokenDao authtokenDao = new AuthtokenDao(db.getConnection());

            //get the suername from the request and find the user in the database
            String username = r.getUsername();
            User foundUser = userDao.findUser(username);

            //if the user is not found, close the connection and return an error result
            if(foundUser == null){
                db.closeConnection(false);
                LoginResult result = new LoginResult(false,"ERROR: This user is not found");
                return result;
            }

            //get the password and check to see if the password from the user matches the request
            else{
                String password = foundUser.getPassword();

                //if it does not equal close the connection and send back an error result
                if(!(Objects.equals(password, r.getPassword()))){
                    db.closeConnection(false);
                    LoginResult result = new LoginResult(false,"ERROR: The password is invalid");
                    return result;
                }

                //if it does equal create an authtoken
                else{
                    UUID randomNumber = UUID.randomUUID();
                    String authTokenNumber = randomNumber.toString();

                    //insert the authtoken into the database
                    Authtoken authtoken = new Authtoken(authTokenNumber,r.getUsername());
                    authtokenDao.insertAuthtoken(authtoken);

                    //close the connection and return a success result
                    db.closeConnection(true);
                    LoginResult result = new LoginResult(authTokenNumber,foundUser.getUsername(), foundUser.getPersonID(), true);
                    return result;
                }
            }
        }catch(DataAccessException e){
            //close the connection and send back an error result
            e.printStackTrace();
            db.closeConnection(false);
            LoginResult result = new LoginResult(false,"ERROR: The request was unsuccessful");
            return result;
        }
    }

}
