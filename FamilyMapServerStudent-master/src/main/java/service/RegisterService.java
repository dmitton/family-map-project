package service;

import dataAccess.AuthtokenDao;
import dataAccess.DataAccessException;
import dataAccess.Database;
import dataAccess.UserDao;
import model.Authtoken;
import model.User;
import request.LoginRequest;
import request.RegisterRequest;
import result.FillResult;
import result.LoginResult;
import result.RegisterResult;
import java.util.*;

/**
 * class to do the registration process
 */
public class RegisterService {
    /**
     * constructor for the register service
     */
    public RegisterService(){}
    /**
     * method to register a user in the database
     * @return a register result of successful or not
     */
    public RegisterResult register(RegisterRequest r){
        Database db = new Database();
        try{
            //open a database connection
            db.openConnection();

            //create a user and authorization token data access object
            UserDao uDao = new UserDao(db.getConnection());
            AuthtokenDao aDao = new AuthtokenDao(db.getConnection());

            //generate a random number for the personID;
            UUID randomNumber = UUID.randomUUID();
            String personID = randomNumber.toString();

            //create a user and insert into the database
            User user = new User(r.getUsername(),r.getPassword(),r.getEmail(),r.getFirstName(),r.getLastName(),r.getGender(),personID);
            uDao.insertUser(user);

            //create a random number for the authorization token
            randomNumber = UUID.randomUUID();
            String authTokenNumber = randomNumber.toString();

            //create the authToken and insert into the database in the authToken table
            Authtoken authtoken = new Authtoken(authTokenNumber,r.getUsername());
            aDao.insertAuthtoken(authtoken);

            //close the connection
            db.closeConnection(true);

            //fill in four generations of family data
            FillService fill = new FillService();
            FillResult fillResult = fill.fill(r.getUsername(),4);

            //log the recently created user in
            LoginRequest loginRequest = new LoginRequest(r.getUsername(),r.getPassword());
            LoginService login = new LoginService();
            LoginResult loginResult = login.login(loginRequest);

            //create a new object that has the result with the return message
            RegisterResult result = new RegisterResult(loginResult.getAuthToken(),r.getUsername(),personID,true);
            return result;
        }catch(DataAccessException e){
            //close the connection and send back an error message
            e.printStackTrace();
            db.closeConnection(false);

            RegisterResult result = new RegisterResult(false,"ERROR: The request was unsuccessful");
            return result;
        }
    }
}
