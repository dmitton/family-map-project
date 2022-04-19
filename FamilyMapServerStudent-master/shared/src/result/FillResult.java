package result;

/**
 * result of filling the specific user's profile
 */
public class FillResult {
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
    public FillResult(boolean success,String message){
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
