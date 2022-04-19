package Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import request.LoginRequest;
import result.LoginResult;
import service.LoginService;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;

/**
 * class for handling the login requests
 */
public class LoginHandler extends Handler{
    /**
     * override the handle function
     * @param exchange
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        try{
            //check to make sure the request is a post request
            if(exchange.getRequestMethod().toLowerCase().equals("post")){

                //read in the request body into a string
                String reqData = readString(exchange.getRequestBody());
                System.out.println(reqData);

                //convert the request data into a LoginRequest object
                Gson gson = new Gson();
                LoginRequest request = gson.fromJson(reqData,LoginRequest.class);

                //create a LoginService object and perform the request
                LoginService service = new LoginService();
                LoginResult result = service.login(request);

                //if the result is not successful, send back an error code
                if(!(result.isSuccess())){
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
                    gson.toJson(result, resBody);
                    resBody.close();
                }

                //if the result is successful, send back a success code
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
