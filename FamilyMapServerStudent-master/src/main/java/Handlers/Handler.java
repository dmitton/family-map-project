package Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;

/**
 * parent class for the handlers
 */
public abstract class Handler implements HttpHandler {
    /**
     * virtual handle function
     * @param exchange
     * @throws IOException
     */
    public abstract void handle(HttpExchange exchange) throws IOException;

    /**
     * virtual read string function to read in the request data
     * @param is
     * @return
     * @throws IOException
     */
    public String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    /**
     * function for writing the string to the result body
     * @param str
     * @param os
     * @throws IOException
     */
    public void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }



}
