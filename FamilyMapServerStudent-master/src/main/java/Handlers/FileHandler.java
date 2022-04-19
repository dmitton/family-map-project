package Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.nio.file.Files;

/**
 * default file handler to take you to the main page
 */
public class FileHandler implements HttpHandler {
    /**
     * override handle function
     * @param exchange exchange
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException{
        boolean success = false;
        try{
            //check to make sure the request method is get
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {

                //get the urlPath and if it is null or a backslash, reroute to index.html
                String urlPath = exchange.getRequestURI().toString();
                if((urlPath == null) || (urlPath.equals("/"))){
                    urlPath = "/index.html";
                }

                //add web to the url path and check to see if the file exists
                String filePath = "web" + urlPath;
                File file = new File(filePath);

                //if the file does not exist reroute to the error code 404 file
                if(!(file.exists())){
                    filePath = "web/HTML/404.html";
                    file = new File(filePath);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                    OutputStream respBody = exchange.getResponseBody();
                    Files.copy(file.toPath(), respBody);
                    exchange.getResponseBody().close();
                }

                //if it does exist send back a success code
                else{
                    OutputStream respBody = exchange.getResponseBody();
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    Files.copy(file.toPath(), respBody);
                    respBody.close();
                    success = true;
                }
            }
            //if the request is not a get send an error code
            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 0);
                exchange.getResponseBody().close();
            }
        }catch(IOException e){
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }

}
