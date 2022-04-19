package service;

import dataAccess.DataAccessException;
import dataAccess.Database;
import result.ClearResult;

/**
 * class to clear the tables from the database
 */
public class ClearService {
    /**
     * constructor for the clear service
     */
    public ClearService() {}
    /**
     * clear the tables in the database
     * @return the result of clearing the tables
     */
    public ClearResult clear() {
        Database db = new Database();
        try {
            //open the connection, clear the tables, and close the connection
            db.openConnection();
            db.clearTables();
            db.closeConnection(true);

            //return a positive clear result
            ClearResult result = new ClearResult(true,"Clear succeeded");
            return result;

        }catch(DataAccessException e){
            //print the stack trace, close the connection, and return an unsuccessful result
            e.printStackTrace();
            db.closeConnection(false);

            ClearResult result = new ClearResult(false,"ERROR: The clear request was unsuccessful");
            return result;
        }
    }

}
