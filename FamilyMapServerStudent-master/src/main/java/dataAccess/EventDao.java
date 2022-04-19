package dataAccess;

import model.Event;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * class that accesses events table from the database
 */
public class EventDao {
    /**
     * the connection to the database
     */
    private Connection conn;

    /**
     * constructor for EventDao
     * @param conn connection to the database
     */
    public EventDao(Connection conn){this.conn = conn;}

    /**
     * insert an event into the database
     * @param event event being inserted
     */
    public void insertEvent(Event event) throws DataAccessException{
        String sql = "INSERT INTO Event (eventID, associatedUsername, personID, latitude, longitude, " +
                "country, city, eventType, year) VALUES(?,?,?,?,?,?,?,?,?)";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getAssociatedUsername());
            stmt.setString(3, event.getPersonID());
            stmt.setFloat(4, event.getLatitude());
            stmt.setFloat(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());

            stmt.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting an event into the database");
        }
    }

    /**
     * find an event in the database
     * @param eventID eventID associated with the event
     * @return the event object
     */
    public Event findEvent(String eventID) throws DataAccessException{
        Event event;
        ResultSet rs;
        String sql = "SELECT * FROM Event WHERE eventID = ?;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1,eventID);
            rs = stmt.executeQuery();
            if(rs.next()){
                event = new Event(rs.getString("EventID"), rs.getString("AssociatedUsername"),
                        rs.getString("PersonID"), rs.getFloat("Latitude"), rs.getFloat("Longitude"),
                        rs.getString("Country"), rs.getString("City"), rs.getString("EventType"),
                        rs.getInt("Year"));
                return event;
            }else {
                return null;
            }
        }catch(SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding an event in the database");
        }
    }

    /**
     * returns all events for all family members of the current user
     * @param associatedUsername
     * @return
     */
    public List<Event> returnEvents(String associatedUsername) throws DataAccessException{
        List<Event>eventList = new ArrayList<>();
        Event event;
        ResultSet rs;
        String sql = "SELECT * FROM Event WHERE associatedUsername = ?;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1,associatedUsername);
            rs = stmt.executeQuery();
            while(rs.next()){
                event = new Event(rs.getString("EventID"), rs.getString("AssociatedUsername"),
                        rs.getString("PersonID"), rs.getFloat("Latitude"), rs.getFloat("Longitude"),
                        rs.getString("Country"), rs.getString("City"), rs.getString("EventType"),
                        rs.getInt("Year"));
                eventList.add(event);
            }
            if(eventList.size() == 0){
                return null;
            }
        }catch(SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding an event in the database");
        }
        return eventList;
    }

    /**
     * clears the event table
     */
    public void clearEventTable() throws DataAccessException{
        String sql = "DELETE FROM Event";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the event table");
        }
    }

    public void clearEventWithUsername(String username) throws DataAccessException {
        String sql = "DELETE FROM Event WHERE associatedUsername = ?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1,username);
            stmt.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the event table");
        }
    }
}
