package dataAccess;

import dataAccess.DataAccessException;
import java.sql.*;

/**
 * open, and closes connection the database
 */
public class Database {
    private Connection conn;

    /**
     * open connection to the database
     * @return the connection
     * @throws DataAccessException if it fails to connectio the database
     */
    public Connection openConnection() throws DataAccessException {
        try{
            final String CONNECTION_URL = "jdbc:sqlite:ServerDatabase.sqlite";
            conn = DriverManager.getConnection(CONNECTION_URL);
            conn.setAutoCommit(false);
        } catch(SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Unable to open connection to database");
        }
        return conn;
    }

    /**
     * get connection if you dont have one
     * @return the connection
     * @throws DataAccessException if the connection fails to connect to the database
     */
    public Connection getConnection() throws DataAccessException {
        if(conn == null){
            return openConnection();
        }
        else{
            return conn;
        }
    }

    /**
     * close the connection
     * @param commit true or false to closing the database
     */
    public void closeConnection(boolean commit){
        try {
            if(commit){
                conn.commit();
            }
            else{
                conn.rollback();
            }
            conn.close();
            conn = null;
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * clear the tables in the database
     * @throws DataAccessException if it fails to clear the tables
     */
    public void clearTables() throws DataAccessException{
        try(Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM User";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM Person";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM Event";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM AuthToken";
            stmt.executeUpdate(sql);
        }catch(SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the tables");
        }
    }
}
