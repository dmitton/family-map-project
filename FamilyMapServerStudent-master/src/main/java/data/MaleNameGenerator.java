package data;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Random;

/**
 * generates one male name from the json file
 */
public class MaleNameGenerator {

    public MaleNameGenerator() {}

    /**
     * deserializes the male names json file
     * @return a MaleNameData object
     * @throws IOException
     */
    public MaleNameData deserializeMaleNameFile() throws IOException {
        Gson gson = new Gson();
        Reader reader = new FileReader("json/mnames.json");
        MaleNameData maleNameData = (MaleNameData)gson.fromJson(reader,MaleNameData.class);
        return maleNameData;
    }

    /**
     * gets an array of male names
     * @param maleNameData
     * @return an array of male names
     */
    public String [] getMaleNamesArray(MaleNameData maleNameData) {
        String [] maleNames = maleNameData.getMaleNameData();
        return maleNames;
    }

    /**
     * gets a random name from the array
     * @param data the array
     * @return one random male name
     */
    public String getRandom(String[] data){
        Random randomNumber = new Random();
        return data[randomNumber.nextInt(data.length)];
    }
}
