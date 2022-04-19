package Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import result.FillResult;
import service.FillService;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;

/**
 * class that handles the fill requests
 */
public class FillHandler extends Handler{

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        try{
            //check to see if the request method is a post request
            if(exchange.getRequestMethod().toLowerCase().equals("post")){

                //create a new service object
                FillService service = new FillService();

                //get the URI substring
                String URI = exchange.getRequestURI().toString();
                URI = URI.substring(6);
                Gson gson = new Gson();

                //check to see if there is a generation parameter
                if(URI.contains("/")){
                    int index = getIndexOfGenerations(URI);
                    String username = URI.substring(0,index);
                    int generations = Integer.parseInt(URI.substring(index + 1));
                    FillResult result = service.fill(username,generations);

                    //check to see if the result is successful
                    //if not successful send an error code back
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

                //do the same thing except have the default generation parameter as 4
                else{
                    FillResult result = service.fill(URI,4);
                    if(!(result.isSuccess())){
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
                        gson.toJson(result, resBody);
                        resBody.close();
                    }
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

    /**
     * gets the index of where the generation starts in the URI
     * @param usernameString the string being checked
     * @return
     */
    public int getIndexOfGenerations(String usernameString){
        for(int i = 0; i < usernameString.length();++i){
            if(usernameString.charAt(i) == '/'){
                return i;
            }
        }
        return 0;
    }
}
