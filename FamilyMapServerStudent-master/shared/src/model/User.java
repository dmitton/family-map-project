package model;

/**
 * class that is the block for a user
 */
public class User {
    /**
     * username of the user
     */
    private String username;
    /**
     * user's password
     */
    private String password;
    /**
     * user's email
     */
    private String email;
    /**
     * user's first name
     */
    private String firstName;
    /**
     * user's last name
     */
    private String lastName;
    /**
     * user's gender
     */
    private String gender;
    /**
     * user's personID
     */
    private String personID;

    /**
     * creates a user
     * @param username the created username
     * @param password the password chosen for the new user
     * @param email email associated with the user
     * @param firstName first name of the user
     * @param lastName last name of the user
     * @param gender gender of the user
     * @param personID personID associated with the user
     */
    public User(String username,String password,String email,String firstName,String lastName,String gender,String personID){
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personID = personID;
    }


    /**
     * get username
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * set username
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * get password
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * set password
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * get email
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * set email
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * get firstName
     * @return firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * set firstName
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * get lastName
     * @return lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * set lastName
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * get gender
     * @return gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * set gender
     * @param gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * get personID
     * @return personID
     */
    public String getPersonID() {
        return personID;
    }

    /**
     * set personID
     * @param personID
     */
    public void setPersonID(String personID) {
        this.personID = personID;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if(o instanceof User) {
            User oUser = (User)o;
            return oUser.getUsername().equals(getUsername()) &&
                    oUser.getPassword().equals(getPassword()) &&
                    oUser.getEmail().equals(getEmail()) &&
                    oUser.getFirstName().equals(getFirstName()) &&
                    oUser.getLastName().equals(getLastName()) &&
                    oUser.getGender().equals(getGender()) &&
                    oUser.getPersonID().equals(getPersonID());
        } else{
            return false;
        }
    }

}
