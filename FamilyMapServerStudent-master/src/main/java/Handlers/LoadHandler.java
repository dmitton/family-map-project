package Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import request.LoadRequest;
import result.LoadResult;
import service.LoadService;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;

/**
 * class that handles the load requests
 */
public class LoadHandler extends Handler {
    /**
     * override the handle function
     * @param exchange
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException{
        boolean success = false;
        try {
            //check to see if the request method is a post request
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                //get the request body
                String reqData = readString(exchange.getRequestBody());
                System.out.println(reqData);

                //put the request data into a LoadRequest object
                Gson gson = new Gson();
                LoadRequest request = gson.fromJson(reqData, LoadRequest.class);

                //create a LoadService object and perform the service
                LoadService service = new LoadService();
                LoadResult result = service.load(request);

                //if the result is not successful send back an error code
                if(!(result.isSuccess())) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
                    gson.toJson(result, resBody);
                    resBody.close();
                }

                //if the result is successful send back a success code
                else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
                    gson.toJson(result, resBody);
                    resBody.close();
                    success = true;
                }
            }
        }catch(IOException e){
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }


}
