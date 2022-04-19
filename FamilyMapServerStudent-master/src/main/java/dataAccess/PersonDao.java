package dataAccess;

import model.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * accesses the database of the Person table
 */
public class PersonDao {
    /**
     * the connection to the database
     */
    private Connection conn;

    /**
     * PersonDao constructor
     * @param conn connection
     */
    public PersonDao(Connection conn){this.conn = conn;}

    /**
     * add person to the database
     * @param person person being added
     */
    public void addPerson(Person person) throws DataAccessException{
        String sql = "INSERT INTO Person (personID, associatedUsername, firstName, lastName, gender, " +
                "fatherID, motherID, spouseID) VALUES(?,?,?,?,?,?,?,?)";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getAssociatedUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFatherID());
            stmt.setString(7, person.getMotherID());
            stmt.setString(8, person.getSpouseID());

            stmt.executeUpdate();
        }catch(SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting a person into the database");
        }
    }

    /**
     * find person in the database
     * @param personID personID for person being found
     * @return the person trying to be found
     */
    public Person findPerson(String personID) throws DataAccessException{
        Person person;
        ResultSet rs;
        String sql = "SELECT * FROM Person WHERE personID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person(rs.getString("personID"), rs.getString("associatedUsername"),
                        rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender"),
                        rs.getString("fatherID"), rs.getString("motherID"), rs.getString("spouseID"));
                return person;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding a person in the database");
        }
    }

    /**
     * return a list of all the people in the database associated with a username
     * @param associatedUsername username associated with who is being returned
     * @return list of people
     */
    public List<Person> returnPeople( String associatedUsername) throws DataAccessException{
        List<Person>peopleList = new ArrayList<>();
        Person person;
        ResultSet rs;
        String sql = "SELECT * FROM Person WHERE associatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, associatedUsername);
            rs = stmt.executeQuery();
            while(rs.next()) {
                person = new Person(rs.getString("personID"), rs.getString("associatedUsername"),
                        rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender"),
                        rs.getString("fatherID"), rs.getString("motherID"), rs.getString("spouseID"));
                peopleList.add(person);
            }
            if(peopleList.size() == 0){
                return null;
            }
            return peopleList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding a person in the database");
        }
    }

    /**
     * clear the event table
     */
    public void clearPersonTable() throws DataAccessException {
        String sql = "DELETE FROM Person";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the person table");
        }
    }

    public void clearPersonWithUsername(String username) throws DataAccessException {
        String sql = "DELETE FROM Person WHERE associatedUsername = ?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1,username);
            stmt.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the person table");
        }
    }

}
