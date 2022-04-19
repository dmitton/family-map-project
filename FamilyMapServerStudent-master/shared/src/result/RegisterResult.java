package result;

/**
 * result class for the Register request
 */
public class RegisterResult {
    /**
     * authtoken given to register
     */
    private String authtoken;
    /**
     * username wanting to register with
     */
    private String username;
    /**
     * personID associated with the user
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
    public RegisterResult(String authtoken,String username,String personID,boolean success){
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
    public RegisterResult(boolean success,String message){
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
