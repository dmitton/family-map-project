package edu.byu.cs240.familymap.Fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import edu.byu.cs240.familymap.DataTransfer.DataCache;
import edu.byu.cs240.familymap.DataTransfer.ServerProxy;
import edu.byu.cs240.familymap.R;
import model.Person;
import request.LoginRequest;
import request.RegisterRequest;
import result.EventResult;
import result.LoginResult;
import result.PersonResult;
import result.RegisterResult;


public class LoginFragment extends Fragment {
    private static final String MESSAGE_SUCCESS = "Request Success";
    private Listener listener;
    private static final String LOG_TAG = "Login fragment";
    private EditText serverHostEditText;
    private EditText serverPortEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private Button registerButton;
    private Button signInButton;
    private RadioGroup radioGroup;


    public interface Listener{
        void notifyDone();
    }

    public void registerListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        //get the info put into the login interface
        serverHostEditText = view.findViewById(R.id.serverHost);
        serverPortEditText = view.findViewById(R.id.serverPort);
        usernameEditText = view.findViewById(R.id.userName);
        passwordEditText = view.findViewById(R.id.pword);
        firstNameEditText = view.findViewById(R.id.firstName);
        lastNameEditText = view.findViewById(R.id.lastName);
        emailEditText = view.findViewById(R.id.em);

        //get the radio group buttons
        radioGroup = view.findViewById(R.id.genderButtons);


        //get the register and the sign in buttons
        registerButton = view.findViewById(R.id.registerButton);
        signInButton = view.findViewById(R.id.signInButton);

        //set both buttons to disabled
        registerButton.setEnabled(false);
        signInButton.setEnabled(false);


        //listener that goes to a function that checks the enabling aspect
        radioGroup.setOnCheckedChangeListener((v,a)-> enableButtons());

        //text watcher for checking the buttons text inputs to disable and enable the buttons
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                enableButtons();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        //add the text watcher to text change listeners
        serverHostEditText.addTextChangedListener(textWatcher);
        serverPortEditText.addTextChangedListener(textWatcher);
        usernameEditText.addTextChangedListener(textWatcher);
        passwordEditText.addTextChangedListener(textWatcher);
        firstNameEditText.addTextChangedListener(textWatcher);
        lastNameEditText.addTextChangedListener(textWatcher);
        emailEditText.addTextChangedListener(textWatcher);


        //sign in button on click
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    String serverHost = serverHostEditText.getText().toString();
                    String serverPort = serverPortEditText.getText().toString();
                    String username = usernameEditText.getText().toString();
                    String password = passwordEditText.getText().toString();

                    Handler uiThreadMessageHandler = new Handler(Looper.getMainLooper()) {
                        @Override
                        public void handleMessage(Message message) {
                            Bundle bundle = message.getData();

                            DataCache dataCache = DataCache.getInstance();
                            String firstName = dataCache.getFirstName();
                            String lastName = dataCache.getLastName();
                            Boolean success = bundle.getBoolean(MESSAGE_SUCCESS, false);
                            if(success){
                                Toast.makeText(getContext(),firstName + " " + lastName + " logged in",Toast.LENGTH_LONG).show();
                                listener.notifyDone();
                            }
                            else{
                                Toast.makeText(getContext(),"Login Failed",Toast.LENGTH_LONG).show();
                            }
                        }
                    };

                    LoginTask loginTask = new LoginTask(uiThreadMessageHandler, serverHost,serverPort,username, password);
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.submit(loginTask);

                }
            }
        });


        //register button on click
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(listener != null){
                    String serverHost = serverHostEditText.getText().toString();
                    String serverPort = serverPortEditText.getText().toString();
                    String username = usernameEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    String firstName = firstNameEditText.getText().toString();
                    String lastName = lastNameEditText.getText().toString();
                    String email = emailEditText.getText().toString();
                    String gender = "";

                    DataCache dataCache = DataCache.getInstance();
                    dataCache.setFirstName(firstName);
                    dataCache.setLastName(lastName);

                    int genderSelectedID = radioGroup.getCheckedRadioButtonId();

                    switch (genderSelectedID) {
                        case R.id.female:
                            gender = "f";
                            break;
                        case R.id.male:
                            gender = "m";
                            break;
                    }



                    Handler uiThreadMessageHandler = new Handler(Looper.getMainLooper()) {
                        @Override
                        public void handleMessage(Message message) {
                            Bundle bundle = message.getData();
                            Boolean success = bundle.getBoolean(MESSAGE_SUCCESS, false);
                            if(success){
                                Toast.makeText(getContext(),firstName + " " + lastName + " logged in",Toast.LENGTH_LONG).show();
                                listener.notifyDone();
                            }
                            else{
                                Toast.makeText(getContext(),"Register Failed",Toast.LENGTH_LONG).show();
                            }
                        }
                    };

                    RegisterTask registerTask = new RegisterTask(uiThreadMessageHandler,serverHost,serverPort,username,password,firstName,lastName,
                            email,gender);
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.submit(registerTask);
                }
            }
        });

        return view;
    }


    private static class LoginTask implements Runnable{
        private final Handler messageHandler;
        private String serverHost;
        private String serverPort;
        private String username;
        private String password;

        public LoginTask(Handler messageHandler,String serverHost,String serverPort,String username, String password) {
            this.messageHandler = messageHandler;
            this.serverHost = serverHost;
            this.serverPort = serverPort;
            this.username = username;
            this.password = password;
        }

        @Override
        public void run(){
            //put those in a loginRequest
            LoginRequest loginRequest = new LoginRequest(username,password);

            //get an instance of serverProxy
            ServerProxy serverProxy = new ServerProxy();

            //get a loginResult back after accessing the server with the info
            LoginResult loginResult = serverProxy.login(serverHost,serverPort,loginRequest);


            if(loginResult.isSuccess()){
                PersonResult personResult = serverProxy.people(serverHost,serverPort,loginResult.getAuthToken());
                EventResult eventResult = serverProxy.events(serverHost,serverPort,loginResult.getAuthToken());
                DataCache dataCache = DataCache.getInstance();
                dataCache.setPeople(personResult.getData());
                dataCache.setEvents(eventResult.getData());
                String firstName = "";
                String lastName = "";
                List<Person>people = personResult.getData();
                for(int i = 0;i < people.size();++i){
                    if(people.get(i).getPersonID().equals(loginResult.getPersonID())){
                        firstName = people.get(i).getFirstName();
                        lastName = people.get(i).getLastName();
                    }
                }
                dataCache.setFirstName(firstName);
                dataCache.setLastName(lastName);

                sendMessage(true);
            }
            else{
                sendMessage(false);
            }
        }

        private void sendMessage(boolean isSuccessful){
            Message message = Message.obtain();

            Bundle messageBundle = new Bundle();
            messageBundle.putBoolean(MESSAGE_SUCCESS,isSuccessful);
            message.setData(messageBundle);

            messageHandler.sendMessage(message);
        }

    }

    private static class RegisterTask implements Runnable{
        private final Handler messageHandler;
        private String serverHost;
        private String serverPort;
        private String username;
        private String password;
        private String firstName;
        private String lastName;
        private String email;
        private String gender;

        public RegisterTask(Handler messageHandler,String serverHost,String serverPort,String username, String password,String firstName,String lastName,String email,
                            String gender) {

            this.messageHandler = messageHandler;
            this.serverHost = serverHost;
            this.serverPort = serverPort;
            this.username = username;
            this.password = password;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.gender = gender;
        }

        @Override
        public void run(){
            RegisterRequest registerRequest = new RegisterRequest(username,password,email,firstName,lastName,gender);

            ServerProxy serverProxy = new ServerProxy();

            RegisterResult registerResult = serverProxy.register(serverHost,serverPort,registerRequest);

            if(registerResult.isSuccess()){
                PersonResult personResult = serverProxy.people(serverHost,serverPort,registerResult.getAuthToken());
                EventResult eventResult = serverProxy.events(serverHost,serverPort,registerResult.getAuthToken());
                DataCache dataCache = DataCache.getInstance();
                dataCache.setPeople(personResult.getData());
                dataCache.setEvents(eventResult.getData());
                sendMessage(true);
            }
            else{
                sendMessage(false);
            }
        }

        private void sendMessage(boolean isSuccessful){
            Message message = Message.obtain();

            Bundle messageBundle = new Bundle();
            messageBundle.putBoolean(MESSAGE_SUCCESS,isSuccessful);
            message.setData(messageBundle);

            messageHandler.sendMessage(message);
        }

    }

    public void enableButtons(){
        if ((serverHostEditText.getText().toString().isEmpty()) ||
                (serverPortEditText.getText().toString().isEmpty()) ||
                (usernameEditText.getText().toString().isEmpty()) ||
                (passwordEditText.getText().toString().isEmpty()) ||
                (firstNameEditText.getText().toString().isEmpty()) ||
                (lastNameEditText.getText().toString().isEmpty()) ||
                (emailEditText.getText().toString().isEmpty()) ||
                ((radioGroup.getCheckedRadioButtonId() != R.id.male) && ((radioGroup.getCheckedRadioButtonId() != R.id.female)))){

            registerButton.setEnabled(false);

        }
        else{
            registerButton.setEnabled(true);
        }
        if ((serverHostEditText.getText().toString().isEmpty()) ||
                (serverPortEditText.getText().toString().isEmpty()) ||
                (usernameEditText.getText().toString().isEmpty()) ||
                (passwordEditText.getText().toString().isEmpty())){

            signInButton.setEnabled(false);
        }
        else{
            signInButton.setEnabled(true);
        }
    }

}