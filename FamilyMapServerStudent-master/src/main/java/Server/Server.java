package Server;
import java.io.*;
import java.net.*;
import Handlers.*;
import com.sun.net.httpserver.*;

/**
 * class for running the server
 */
public class Server {
    private static final int MAX_WAITING_CONNECTIONS = 12;
    private HttpServer server;

    /**
     * running the server
     * @param portNumber port number that the server is running on
     */
    private void run(String portNumber) {
        System.out.println("Initializing HTTP Server");
        try {
            server = HttpServer.create(
                    new InetSocketAddress(Integer.parseInt(portNumber)),
                    MAX_WAITING_CONNECTIONS);
        }
        catch(IOException e) {
            e.printStackTrace();
            return;
        }
        server.setExecutor(null);
        System.out.println("Creating contexts");

        //all the API calls for the project
        server.createContext("/clear",new ClearHandler());
        server.createContext("/user/register", new RegisterHandler());
        server.createContext("/user/login", new LoginHandler());
        server.createContext("/fill/", new FillHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/person/", new PersonaIDHandler());
        server.createContext("/person", new PersonHandler());
        server.createContext("/event/", new EventIDHandler());
        server.createContext("/event", new EventHandler());
        server.createContext("/", new FileHandler());

        //starts the server
        System.out.println("Starting server");
        server.start();
        System.out.println("Server running on port " + portNumber);
    }

    public static void main(String[] args) {
        //runs the server
        String portNumber = args[0];
        new Server().run(portNumber);
    }
}
