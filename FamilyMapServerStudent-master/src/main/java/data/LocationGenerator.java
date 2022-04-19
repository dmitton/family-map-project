package data;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Random;

/**
 * class that generates one location
 */
public class LocationGenerator {

    public LocationGenerator() {}

    /**
     * deserializes the location json file
     * @return a LocationData object
     * @throws IOException
     */
    public LocationData deserializeLocationFile() throws IOException {
        Gson gson = new Gson();
        Reader reader = new FileReader("json/locations.json");
        LocationData locationData = (LocationData)gson.fromJson(reader,LocationData.class);
        return locationData;
    }

    /**
     * gets an array of locations
     * @param locationData
     * @return an array of locations
     */
    public static Location [] getLocationArray(LocationData locationData) {
        Location [] data = locationData.getData();
        return data;
    }

    /**
     * gets a random location from the array
     * @param data the array of locations
     * @return one random location
     */
    public Location getRandom(Location[] data){
        Random randomNumber = new Random();
        return data[randomNumber.nextInt(data.length)];
    }
}
