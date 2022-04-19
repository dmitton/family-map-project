package edu.byu.cs240.familymap.DataTransfer;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import request.EventRequest;
import request.LoginRequest;
import request.PersonRequest;
import request.RegisterRequest;
import result.ClearResult;
import result.EventResult;
import result.LoginResult;
import result.PersonResult;
import result.RegisterResult;

public class ServerProxy {

    public ServerProxy(){}

    public ClearResult clear(String serverHost, String serverPort){
        try{
            Gson gson = new Gson();
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/clear");

            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("POST");

            http.setDoOutput(true);

            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Route successfully claimed.");

                InputStream respBody = http.getInputStream();

                String respData = readString(respBody);

                ClearResult result = gson.fromJson(respData,ClearResult.class);

                return result;
            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());

                InputStream respBody = http.getErrorStream();

                String respData = readString(respBody);

                ClearResult result = gson.fromJson(respData,ClearResult.class);

                System.out.println(respData);

                return result;
            }
        }
        catch(IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
            ClearResult result = new ClearResult(true,"ERROR: the request was invalid");
            return result;
        }
    }

    public RegisterResult register(String serverHost, String serverPort, RegisterRequest request){
        try{
            Gson gson = new Gson();
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");

            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("POST");

            http.setDoOutput(true);

            http.connect();

            String reqData = gson.toJson(request);

            OutputStream reqBody = http.getOutputStream();

            writeString(reqData, reqBody);

            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Route successfully claimed.");

                InputStream respBody = http.getInputStream();

                String respData = readString(respBody);

                RegisterResult result = gson.fromJson(respData,RegisterResult.class);

                return result;
            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());

                InputStream respBody = http.getErrorStream();

                String respData = readString(respBody);

                RegisterResult result = gson.fromJson(respData,RegisterResult.class);

                System.out.println(respData);

                return result;
            }
        }
        catch(IOException e) {
        // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
            RegisterResult result = new RegisterResult(true,"ERROR: the request was invalid");
            return result;
        }
    }

    public LoginResult login(String serverHost, String serverPort, LoginRequest request){
        try{
            Gson gson = new Gson();

            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");

            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("POST");

            http.setDoOutput(true);

            http.connect();

            String reqData = gson.toJson(request);

            OutputStream reqBody = http.getOutputStream();

            writeString(reqData, reqBody);

            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Route successfully claimed.");

                InputStream respBody = http.getInputStream();

                String respData = readString(respBody);

                LoginResult result = gson.fromJson(respData,LoginResult.class);

                return result;
            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());

                InputStream respBody = http.getErrorStream();

                String respData = readString(respBody);

                LoginResult result = gson.fromJson(respData,LoginResult.class);

                System.out.println(respData);

                return result;
            }
        }
        catch(IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
            LoginResult result = new LoginResult(false,"ERROR: the request was invalid");
            return result;
        }
    }

    public PersonResult people(String serverHost, String serverPort, String authtoken){
        try {
            Gson gson = new Gson();
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person");

            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("GET");

            http.setDoOutput(false);

            http.addRequestProperty("Authorization", authtoken);

            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Route successfully claimed.");

                InputStream respBody = http.getInputStream();

                String respData = readString(respBody);

                PersonResult result = gson.fromJson(respData,PersonResult.class);

                return result;
            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());

                InputStream respBody = http.getErrorStream();

                String respData = readString(respBody);

                PersonResult result = gson.fromJson(respData,PersonResult.class);

                System.out.println(respData);

                return result;
            }
        }catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
            PersonResult result = new PersonResult("ERROR: the request is invalid", true);
            return result;
        }
    }

    public EventResult events(String serverHost, String serverPort, String authtoken){
        try {
            Gson gson = new Gson();
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/event");

            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("GET");

            http.setDoOutput(false);

            http.addRequestProperty("Authorization", authtoken);

            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Route successfully claimed.");

                InputStream respBody = http.getInputStream();

                String respData = readString(respBody);

                EventResult result = gson.fromJson(respData,EventResult.class);

                return result;
            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());

                InputStream respBody = http.getErrorStream();

                String respData = readString(respBody);

                EventResult result = gson.fromJson(respData,EventResult.class);

                System.out.println(respData);

                return result;
            }
        }catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
            EventResult result = new EventResult("ERROR: the request is invalid", true);
            return result;
        }
    }



    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}

