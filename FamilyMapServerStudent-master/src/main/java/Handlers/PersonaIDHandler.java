package Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import result.PersonIDResult;
import service.PersonIDService;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;

/**
 * class that handles the requests for personID requests
 */
public class PersonaIDHandler extends Handler{
    /**
     * override the handle function
     * @param exchange
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        try {
            //check to make sure that the request method is a get request
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {

                //get the request headers and check to see if it has authorization
                Headers reqHeaders = exchange.getRequestHeaders();
                if (reqHeaders.containsKey("Authorization")) {

                    //get the authtoken
                    String authtoken = reqHeaders.getFirst("Authorization");

                    //get the personID from the URI
                    String personID = exchange.getRequestURI().toString();
                    personID = personID.substring(8);

                    //create a new service object
                    Gson gson = new Gson();
                    PersonIDService service = new PersonIDService();

                    //perform the service
                    PersonIDResult result = service.person(personID,authtoken);

                    //if the service is not successful send back an error code
                    if(!(result.isSuccess())){
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
                        gson.toJson(result, resBody);
                        resBody.close();
                    }

                    //if the service is successful send back a success code
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
