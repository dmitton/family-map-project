package request;

/**
 * class to send a request to register a user
 */
public class RegisterRequest {
    /**
     * username chosen to register with
     */
    private String username;
    /**
     * password chosen to register with
     */
    private String password;
    /**
     * email associated with the user
     */
    private String email;
    /**
     * user's first name to register with
     */
    private String firstName;
    /**
     * user's last name to register with
     */
    private String lastName;
    /**
     * gender of the user
     */
    private String gender;

    /**
     * constructor class for a RegisterRequest object
     * @param username username associated with the user registering
     * @param password password associated with the user registering
     * @param email email associated with the user registering
     * @param firstName firstName associated with the user registering
     * @param lastName lastName associated with the user registering
     * @param gender gender associated with the user registering
     */
    public RegisterRequest(String username,String password,String email,String firstName,String lastName, String gender){
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
