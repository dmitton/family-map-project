package Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.*;
import request.RegisterRequest;
import result.RegisterResult;
import service.RegisterService;
import java.io.*;
import java.net.HttpURLConnection;

/**
 * handles all the API request for registering in the database
 */
public class RegisterHandler extends Handler {

    /**
     * override handle function
     * @param exchange
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        try{
            //check to make sure the request is post
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                //get the request body and print it out, so it is visible
                String reqData = readString(exchange.getRequestBody());
                System.out.println(reqData);

                //take the Json request and convert to Gson and send a request to register in the service class
                Gson gson = new Gson();
                RegisterRequest request = gson.fromJson(reqData,RegisterRequest.class);
                RegisterService service = new RegisterService();
                RegisterResult result = service.register(request);

                //if the service is not successful send back an error code
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
