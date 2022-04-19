package data;

/**
 * the class that holds the array of locations that are imported from the Json data
 */
public class LocationData {
    /**
     * location array
     */
    private Location [] data;

    /**
     * constructor for the LocationData
     */
    public LocationData(){}

    public Location[] getData() {
        return data;
    }

    public void setData(Location[] data) {
        this.data = data;
    }
}
