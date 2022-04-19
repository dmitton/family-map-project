package data;

/**
 * model class for a location
 */
public class Location {
    /**
     * location's country
     */
    private String country;
    /**
     * location's city
     */
    private String city;
    /**
     * location's latitude
     */
    private String latitude;
    /**
     * location's longitude
     */
    private String longitude;

    /**
     * constructor for a location
     * @param country
     * @param city
     * @param latitude
     * @param longitude
     */
    public Location(String country,String city,String latitude,String longitude){
        this.country = country;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }


}
