package model;

import java.util.Objects;

/**
 * class that holds data for events
 */
public class Event {
    /**
     * ID for that specific event
     */
    private String eventID;
    /**
     * username associated with the ID
     */
    private String associatedUsername;
    /**
     * ID for the person that is associated with the event
     */
    private String personID;
    /**
     * latitude for the event
     */
    private float latitude;
    /**
     * longitude for the event
     */
    private float longitude;
    /**
     * event's country
     */
    private String country;
    /**
     * event's city
     */
    private String city;
    /**
     * type of event
     */
    private String eventType;
    /**
     * year the event happened in
     */
    private int year;

    /**
     * constructor for an event
     * @param eventID ID for the event
     * @param associatedUsername username associated with event
     * @param personID person associated with event
     * @param latitude latitude of event
     * @param longitude longitude of event
     * @param country event's country
     * @param city event's city
     * @param eventType type of event
     * @param year event's year
     */
    public Event(String eventID,String associatedUsername,String personID,float latitude,float longitude,String country,String city,String eventType,int year){
        this.eventID = eventID;
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    /**
     * get eventID
     * @return eventID
     */
    public String getEventID() {
        return eventID;
    }

    /**
     * set eventID
     * @param eventID
     */
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    /**
     * get associatedUsername
     * @return associatedUsername
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
     * get personID
     * @return person ID
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

    /**
     * get latitude
     * @return latitude
     */
    public float getLatitude() {
        return latitude;
    }

    /**
     * set latitude
     * @param latitude
     */
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    /**
     * get longitude
     * @return longitude
     */
    public float getLongitude() {
        return longitude;
    }

    /**
     * set longitude
     * @param longitude
     */
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    /**
     * get country
     * @return country
     */
    public String getCountry() {
        return country;
    }

    /**
     * set country
     * @param country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * get city
     * @return city
     */
    public String getCity() {
        return city;
    }

    /**
     * set city
     * @param city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * get eventType
     * @return eventType
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * set eventType
     * @param eventType
     */
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    /**
     * get year
     * @return year
     */
    public int getYear() {
        return year;
    }

    /**
     * set year
     * @param year
     */
    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(eventID, event.eventID) && Objects.equals(associatedUsername, event.associatedUsername) && Objects.equals(personID, event.personID) && Objects.equals(latitude, event.latitude) && Objects.equals(longitude, event.longitude) && Objects.equals(country, event.country) && Objects.equals(city, event.city) && Objects.equals(eventType, event.eventType) && Objects.equals(year, event.year);
    }
}
