package dataAccess;
import model.Authtoken;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * class that is responsible accessing the Authtoken table in the database
 */
public class AuthtokenDao {
    /**
     * the connection to the database
     */
    private Connection conn;

    /**
     * constructor for AuthtokenDao
     * @param conn connection to the database
     */
    public AuthtokenDao(Connection conn){this.conn = conn;}

    /**
     * insert authtoken into the database
     * @param authtoken authtoken being inserted
     */
    public void insertAuthtoken(Authtoken authtoken) throws DataAccessException{
        String sql = "INSERT INTO AuthToken (authtoken, username) VALUES(?,?)";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, authtoken.getAuthToken());
            stmt.setString(2, authtoken.getUsername());
            stmt.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting an authtoken into the database");
        }
    }

    /**
     * find an authToken in the database
     * @param authtoken authorization token we are looking for
     * @return Authtoken returns authToken
     */
    public Authtoken findAuthToken(String authtoken) throws DataAccessException{
        Authtoken authToken;
        ResultSet rs;
        String sql = "SELECT * FROM AuthToken WHERE authtoken = ?;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, authtoken);
            rs = stmt.executeQuery();
            if(rs.next()){
                authToken = new Authtoken(rs.getString("authtoken"),rs.getString("username"));
                return authToken;
            }else {
                return null;
            }
        }catch(SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding an event in the database");
        }
    }

    /**
     * return all authtokens associated with a username
     * @param username username associate with an authtoken
     * @return the list of authtokens
     */
    public List<Authtoken> returnAuthTokens(String username){return null;}

    /**
     * clear the authToken table
     */
    public void clearAuthtokenTable() throws DataAccessException{
        String sql = "DELETE FROM AuthToken";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the AuthToken table");
        }
    }
}
