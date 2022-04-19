package result;

/**
 * result of the EventIDRequest
 */
public class EventIDResult {
    /**
     * username associated with the event
     */
    private String associatedUsername;
    /**
     * specific ID for the event
     */
    private String eventID;
    /**
     * personID associated with the event
     */
    private String personID;
    /**
     * latitude of the event
     */
    private String latitude;
    /**
     * longitude of the event
     */
    private String longitude;
    /**
     * country of the event
     */
    private String country;
    /**
     * city of the event
     */
    private String city;
    /**
     * event type
     */
    private String eventType;
    /**
     * year the event happened in
     */
    private String year;
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
     * @param associatedUsername username associated with the event
     * @param eventID Id for the event
     * @param personID personID associated with the event
     * @param latitude latitude of the event
     * @param longitude longitude of the event
     * @param country country of the event
     * @param city city of the event
     * @param eventType eventType for the event
     * @param year events year
     * @param success successful
     */
    public EventIDResult(String associatedUsername,String eventID, String personID,String latitude,String longitude,String country,String city,String eventType,
                          String year,boolean success){
        this.associatedUsername = associatedUsername;
        this.eventID = eventID;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
        this.success = success;
    }

    /**
     * unsuccessful request
     * @param message error message
     * @param success unsuccessful
     */
    public EventIDResult(String message,boolean success){
        this.message = message;
        this.success = success;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public String getEventID() {
        return eventID;
    }

    public String getPersonID() {
        return personID;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getEventType() {
        return eventType;
    }

    public String getYear() {
        return year;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
