package Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import result.EventIDResult;
import service.EventIDService;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;

/**
 * class that handles the EventID requests
 */
public class EventIDHandler extends Handler{
    /**
     * overriden handle function
     * @param exchange
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        try {
            //check to make sure the request is get
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {

                //grab the headers and check for the authorization header
                Headers reqHeaders = exchange.getRequestHeaders();
                if (reqHeaders.containsKey("Authorization")) {

                    //get the authtoken
                    String authtoken = reqHeaders.getFirst("Authorization");

                    //get the eventID from the URI string
                    String eventID = exchange.getRequestURI().toString();
                    eventID = eventID.substring(7);

                    //create a new service object
                    Gson gson = new Gson();
                    EventIDService service = new EventIDService();

                    //perform the service
                    EventIDResult result = service.event(eventID,authtoken);

                    //if the result is not successful send an error code
                    if(!(result.isSuccess())){
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
                        gson.toJson(result, resBody);
                        resBody.close();
                    }

                    //if the result is successful, send a success code
                    else{
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
                        gson.toJson(result, resBody);
                        resBody.close();
                        success = true;
                    }
                }
            }
        }catch(IOException e){
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}
