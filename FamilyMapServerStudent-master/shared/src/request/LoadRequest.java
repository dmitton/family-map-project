package request;

import model.Event;
import model.Person;
import model.User;
import java.util.List;

/**
 * request to load the users,person,and events data into the database
 */
public class LoadRequest {
    /**
     * list of users
     */
    private List<User>users;
    /**
     * list of person that is returned
     */
    private List<Person>persons;
    /**
     * list of events
     */
    private List<Event>events;

    /**
     * constructor for the request
     * @param users list of users to load
     * @param persons list of persons to load
     * @param events list of events to load
     */
    public LoadRequest(List<User>users,List<Person>persons,List<Event>events){
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
