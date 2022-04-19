package model;

/**
 * person associated with a user that can be a spouse, father, mother, grandparents, etc...
 */
public class Person {
    /**
     * ID for the person
     */
    private String personID;
    /**
     * username associated with the person
     */
    private String associatedUsername;
    /**
     * person's first name
     */
    private String firstName;
    /**
     * person's last name
     */
    private String lastName;
    /**
     * person's gender
     */
    private String gender;
    /**
     * person's father's ID
     */
    private String fatherID;
    /**
     * person's mother's ID
     */
    private String motherID;
    /**
     * person's spouse's ID
     */
    private String spouseID;

    /**
     * person constructor
     * @param personID id associated with the person
     * @param associatedUsername username that the person is attached too
     * @param firstName first name of the person
     * @param lastName last name of the person
     * @param gender gender of the person
     * @param fatherID father of the person
     * @param motherID mother of the person
     * @param spouseID spouse of the person
     */
    public Person(String personID,String associatedUsername,String firstName,String lastName,String gender,String fatherID,String motherID,String spouseID){
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }


    /**
     * get personID
     * @return personID
     */
    public String getPersonID() {
        return personID;
    }

    /**
     * set person ID
     * @param personID
     */
    public void setPersonID(String personID) {
        this.personID = personID;
    }

    /**
     * get associatedUsername
     * @return associated username
     */
    public String getAssociatedUsername() {
        return associatedUsername;
    }

    /**
     * set associatedUsername
     * @param associatedUsername
     */
    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    /**
     * get first name
     * @return first name
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
     * get last name
     * @return last name
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
     * get father
     * @return father
     */
    public String getFatherID() {
        return fatherID;
    }

    /**
     * set fatherID
     * @param fatherID
     */
    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    /**
     * get mother
     * @return mother
     */
    public String getMotherID() {
        return motherID;
    }

    /**
     * set motherID
     * @param motherID
     */
    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    /**
     * get spouse
     * @return spouse
     */
    public String getSpouseID() {
        return spouseID;
    }

    /**
     * set spouseID
     * @param spouseID
     */
    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if(o instanceof Person) {
            Person oPerson = (Person)o;
            return oPerson.getPersonID().equals(getPersonID()) &&
                    oPerson.getAssociatedUsername().equals(getAssociatedUsername()) &&
                    oPerson.getFirstName().equals(getFirstName()) &&
                    oPerson.getLastName().equals(getLastName()) &&
                    oPerson.getGender().equals(getGender()) &&
                    oPerson.getFatherID().equals(getFatherID()) &&
                    oPerson.getMotherID().equals(getMotherID()) &&
                    oPerson.getSpouseID().equals(getSpouseID());
        } else{
            return false;
        }
    }
}
