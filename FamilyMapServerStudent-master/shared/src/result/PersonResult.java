package result;
import model.Person;
import java.util.List;

/**
 * result class for PersonService
 */
public class PersonResult {
    /**
     * list of persons for a user
     */
    private List<Person> data;
    /**
     * success of the request
     */
    private boolean success;
    /**
     * error message if unsuccessful
     */
    private String message;

    /**
     * success constructor
     * @param data list of persons that are associated with the current user
     * @param success successful
     */
    public PersonResult(List<Person>data,boolean success){
        this.data = data;
        this.success = success;
    }

    /**
     * unsuccessful constructor
     * @param message error message
     * @param success unsuccessful
     */
    public PersonResult(String message,boolean success){
        this.message = message;
        this.success = success;
    }

    public List<Person> getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
