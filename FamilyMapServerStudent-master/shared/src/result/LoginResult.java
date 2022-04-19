package result;

/**
 * result of the login request
 */
public class LoginResult {
    /**
     * authtoken used to login with
     */
    private String authtoken;
    /**
     * username attempt for the login
     */
    private String username;
    /**
     * personID for the login
     */
    private String personID;
    /**
     * success of the request
     */
    private boolean success;
    /**
     * error message if unsuccessful
     */
    private String message;

    /**
     * constructor for a successful request
     * @param authtoken authoken
     * @param username username
     * @param personID personID
     * @param success if the request was successful
     */
    public LoginResult(String authtoken,String username,String personID,boolean success){
        this.authtoken = authtoken;
        this.username = username;
        this.personID = personID;
        this.success = success;
    }

    /**
     * constructor for a unsuccessful request
     * @param success if the request was successful
     * @param message error message
     */
    public LoginResult(boolean success,String message){
        this.success = success;
        this.message = message;
    }

    public String getAuthToken() {
        return authtoken;
    }

    public String getUsername() {
        return username;
    }

    public String getPersonID() {
        return personID;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
