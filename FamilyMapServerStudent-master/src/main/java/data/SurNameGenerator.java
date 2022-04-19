package data;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Random;

/**
 * class that generates one name from the json sur name file
 */
public class SurNameGenerator {

    public SurNameGenerator() {}

    /**
     * deserializes the sur name file
     * @return a SurNameData object
     * @throws IOException
     */
    public SurNameData deserializeSurNameFile() throws IOException {
        Gson gson = new Gson();
        Reader reader = new FileReader("json/snames.json");
        SurNameData surNameData = (SurNameData)gson.fromJson(reader,SurNameData.class);
        return surNameData;
    }

    /**
     * gets an array of sur names
     * @param surNameData
     * @return an array of sur names
     */
    public String [] getSurNamesArray(SurNameData surNameData) {
        String [] surNames = surNameData.getSurnameData();
        return surNames;
    }

    /**
     * gets a random sur name from the array
     * @param data the array being searched
     * @return one random sur name
     */
    public String getRandom(String[] data){
        Random randomNumber = new Random();
        return data[randomNumber.nextInt(data.length)];
    }
}
