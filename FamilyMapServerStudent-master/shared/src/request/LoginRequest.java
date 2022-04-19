package request;

/**
 * generates a request to login
 */
public class LoginRequest {
    /**
     * username being used to login
     */
    private String username;
    /**
     * password being used to login
     */
    private String password;

    /**
     * constructor for a login request
     * @param username username associated with the user trying to log in
     * @param password password associated with the user trying to log in
     */
    public LoginRequest(String username,String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
