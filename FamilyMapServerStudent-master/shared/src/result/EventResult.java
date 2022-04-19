package result;

import model.Event;

import java.util.List;

/**
 * result of the EventRequest
 */
public class EventResult {
    /**
     * the data holding all the events
     */
    private List<Event> data;
    /**
     * success of the request
     */
    private boolean success;
    /**
     * error message if unsuccessful
     */
    private String message;

    /**
     * constructor for a unsuccessful or successful request
     * @param success if the request was successful
     * @param message result of the action
     */
    public EventResult(String message,boolean success){
        this.success = success;
        this.message = message;
    }

    public EventResult(List<Event>data,boolean success){
        this.data = data;
        this.success = success;
    }

    public List<Event> getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
