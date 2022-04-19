package Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import result.EventResult;
import service.EventService;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;

/**
 * class that handles event requests
 */
public class EventHandler extends Handler{
    /**
     * overriden handle function
     * @param exchange
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        try {
            //check to make sure the request is for get
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {

                //check the headers for an authorization column and grab the authtoken
                Headers reqHeaders = exchange.getRequestHeaders();
                if (reqHeaders.containsKey("Authorization")) {
                    String authtoken = reqHeaders.getFirst("Authorization");

                    //create a new service object
                    Gson gson = new Gson();
                    EventService service = new EventService();

                    //perform the service
                    EventResult result = service.events(authtoken);

                    //if the request is not successful, send back an error code
                    if(!(result.isSuccess())){
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
                        gson.toJson(result, resBody);
                        resBody.close();
                    }
                    //if it is successful send back a success code
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
