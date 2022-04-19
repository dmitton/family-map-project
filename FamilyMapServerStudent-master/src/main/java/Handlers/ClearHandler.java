package Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import result.ClearResult;
import service.ClearService;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;

/**
 * handler class for clearing the database
 */
public class ClearHandler extends Handler{
    /**
     * override the handle function
     * @param exchange
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException{
        boolean success = false;
        try{
            //check to see if the request method is post and if it is perform the clear service
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                ClearService service = new ClearService();
                ClearResult result = service.clear();

                //send the results of the clear back
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
                Gson gson = new Gson();
                gson.toJson(result,resBody);
                resBody.close();
                success = true;
            }
        }catch(IOException e){
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}
