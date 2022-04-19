package data;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Random;

/**
 * class that generates one female name
 */
public class FemaleNameGenerator {

    public FemaleNameGenerator() {}

    /**
     * deserializes the female name json file
     * @return a FemaleNameData object
     * @throws IOException
     */
    public FemaleNameData deserializeFemaleNameFile() throws IOException {
        Gson gson = new Gson();
        Reader reader = new FileReader("json/fnames.json");
        FemaleNameData femaleNameData = gson.fromJson(reader,FemaleNameData.class);
        return femaleNameData;
    }

    /**
     * gets the array of names
     * @param femaleNameData the object to get the array from
     * @return
     */
    public String [] getFemaleNamesArray(FemaleNameData femaleNameData) {
        String [] femaleNames = femaleNameData.getFemaleNameData();
        return femaleNames;
    }

    /**
     * gets a random name from the array
     * @param data the array
     * @return a name
     */
    public String getRandom(String[] data){
        Random randomNumber = new Random();
        return data[randomNumber.nextInt(data.length)];
    }
}
