package result;

/**
 * result of the PersonID request
 */
public class PersonIDResult {
    /**
     * username associated with the person
     */
    private String associatedUsername;
    /**
     * id for the person
     */
    private String personID;
    /**
     * first name of the person
     */
    private String firstName;
    /**
     * last name of the person
     */
    private String lastName;
    /**
     * person's gender
     */
    private String gender;
    /**
     * person's father's id
     */
    private String fatherID;
    /**
     * person's mother's id
     */
    private String motherID;
    /**
     * person's spouse's id
     */
    private String spouseID;
    /**
     * success of the request
     */
    private boolean success;
    /**
     * error message if unsuccessful
     */
    private String message;

    /**
     * constructor of the PersonIDResult if successful
     * @param associatedUsername associatedUsername for the person
     * @param personID personID for the person getting returned
     * @param firstName first name for the person
     * @param lastName last name for the person
     * @param gender gender of the person
     * @param fatherID persons father
     * @param motherID persons mother
     * @param spouseID persons spouse
     * @param success true success
     */
    public PersonIDResult(String associatedUsername,String personID,String firstName,String lastName,String gender,String fatherID,String motherID,
                          String spouseID,boolean success){
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
        this.success = success;
    }

    /**
     * constructor for the PersonIDResult if not successful
     * @param message Error message
     * @param success false unsuccessful
     */
    public PersonIDResult(String message, boolean success){
        this.message = message;
        this.success = success;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public String getPersonID() {
        return personID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getFatherID() {
        return fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
