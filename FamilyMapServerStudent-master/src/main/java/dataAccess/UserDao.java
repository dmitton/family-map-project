package dataAccess;
import model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * class to access the user table from the database
 */
public class UserDao {
    /**
     * the connection to the database
     */
    private final Connection conn;

    /**
     * UserDao constructor
     * @param conn connection to the database
     */
    public UserDao(Connection conn){
        this.conn = conn;
    }

    /**
     * insert user into the database
     * @param user user being inserted
     */
    public void insertUser(User user) throws DataAccessException{
        String sql = "INSERT INTO User (username, password, email, firstName, lastName, " +
                "gender, personID) VALUES(?,?,?,?,?,?,?)";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getPersonID());

            stmt.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting a user into the database");
        }
    }
    /**
     * find a user
     * @param username username to find
     * @return the user
     */
    public User findUser(String username) throws DataAccessException {
        User user;
        ResultSet rs;
        String sql = "SELECT * FROM User WHERE username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("username"), rs.getString("password"),
                        rs.getString("email"), rs.getString("firstName"), rs.getString("lastName"),
                        rs.getString("gender"), rs.getString("PersonID"));
                return user;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding a user in the database");
        }

    }

    /**
     * return list of all the users in database
     * @return list of users
     */
    public List<User> returnUsers() {return null;}

    /**
     * clear the users table from the database
     */
    public void clearUserTable() throws DataAccessException{
        String sql = "DELETE FROM User";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the user table");
        }
    }

}
