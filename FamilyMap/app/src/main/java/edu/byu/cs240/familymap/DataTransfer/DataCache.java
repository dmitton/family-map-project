package edu.byu.cs240.familymap.DataTransfer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import model.Event;
import model.Person;

public class DataCache {
    private static DataCache instance;

    public static DataCache getInstance(){
        if(instance == null){
            instance = new DataCache();
        }
        return instance;
    }

    private DataCache() {}

    private List<Person> people;
    private List<Event> events;
    private List<Event>motherSide;
    private List<Event>fatherSide;
    private List<Event>maleEvents;
    private List<Event>femaleEvents;
    private boolean settingsChanged = false;
    private boolean lifeStorySetting = true;
    private boolean familyTreeSetting = true;
    private boolean spouseLinesSetting = true;
    private boolean fatherSideSetting = true;
    private boolean motherSideSetting = true;
    private boolean maleEventsSetting = true;
    private boolean femaleEventsSetting = true;
    private String firstName;
    private String lastName;
    private List<Event>modifiedList;
    private Event clickedEvent;


    public List<Person> getPeople() {
        return people;
    }

    public void setPeople(List<Person> people) {
        this.people = people;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public List<Event> getMotherSide() {
        return motherSide;
    }

    public void setMotherSide(List<Event> motherSide) {
        this.motherSide = motherSide;
    }

    public List<Event> getFatherSide() {
        return fatherSide;
    }

    public void setFatherSide(List<Event> fatherSide) {
        this.fatherSide = fatherSide;
    }

    public List<Event> getMaleEvents() {
        return maleEvents;
    }

    public void setMaleEvents(List<Event> maleEvents) {
        this.maleEvents = maleEvents;
    }

    public List<Event> getFemaleEvents() {
        return femaleEvents;
    }

    public void setFemaleEvents(List<Event> femaleEvents) {
        this.femaleEvents = femaleEvents;
    }

    public boolean isLifeStorySetting() {
        return lifeStorySetting;
    }

    public void setLifeStorySetting(boolean lifeStorySetting) {
        this.lifeStorySetting = lifeStorySetting;
    }

    public boolean isFamilyTreeSetting() {
        return familyTreeSetting;
    }

    public void setFamilyTreeSetting(boolean familyTreeSetting) {
        this.familyTreeSetting = familyTreeSetting;
    }

    public boolean isSpouseLinesSetting() {
        return spouseLinesSetting;
    }

    public void setSpouseLinesSetting(boolean spouseLinesSetting) {
        this.spouseLinesSetting = spouseLinesSetting;
    }

    public boolean isFatherSideSetting() {
        return fatherSideSetting;
    }

    public void setFatherSideSetting(boolean fatherSideSetting) {
        this.fatherSideSetting = fatherSideSetting;
    }

    public boolean isMotherSideSetting() {
        return motherSideSetting;
    }

    public void setMotherSideSetting(boolean motherSideSetting) {
        this.motherSideSetting = motherSideSetting;
    }

    public boolean isMaleEventsSetting() {
        return maleEventsSetting;
    }

    public void setMaleEventsSetting(boolean maleEventsSetting) {
        this.maleEventsSetting = maleEventsSetting;
    }

    public boolean isFemaleEventsSetting() {
        return femaleEventsSetting;
    }

    public void setFemaleEventsSetting(boolean femaleEventsSetting) {
        this.femaleEventsSetting = femaleEventsSetting;
    }

    public boolean isSettingsChanged() {
        return settingsChanged;
    }

    public void setSettingsChanged(boolean settingsChanged) {
        this.settingsChanged = settingsChanged;
    }

    public List<Event> getModifiedList() {
        return modifiedList;
    }

    public void setModifiedList(List<Event> modifiedList) {
        this.modifiedList = modifiedList;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void clearDatacache(){
        instance = new DataCache();
    }

    public Event getClickedEvent() {
        return clickedEvent;
    }

    public void setClickedEvent(Event clickedEvent) {
        this.clickedEvent = clickedEvent;
    }

    //returns a person when given a person ID
    public Person returnPerson(String personID){
        List<Person>people = getPeople();
        for(int i = 0;i < people.size();++i){
            if(personID.equals(people.get(i).getPersonID())){
                return people.get(i);
            }
        }
        return null;
    }

    //get a list of events for a specific person
    public List<Event>getPersonEvents(List<Event>events,String personID){
        List<Event>personEvents = new ArrayList<>();
        for(int i = 0; i < events.size();++i){
            if(events.get(i).getPersonID().equals(personID)){
                personEvents.add(events.get(i));
            }
        }
        return personEvents;
    }

    //set the male events filter
    public void setMaleEventsFilter(List<Event>events){
        List<Event>maleEvents = new ArrayList<>();
        //get all of the male events and put them in a list
        for(int i = 0;i < events.size();++i){
            Person person = returnPerson(events.get(i).getPersonID());
            if(person.getGender().equals("m")){
                maleEvents.add(events.get(i));
            }
        }
        //put that list in the data cache
        setMaleEvents(maleEvents);
    }

    //set the female events filter
    public void setFemaleEventsFilter(List<Event>events){
        List<Event>femaleEvents = new ArrayList<>();
        //get all the female events in the database
        for(int i = 0;i < events.size();++i){
            Person person = returnPerson(events.get(i).getPersonID());
            if(person.getGender().equals("f")){
                femaleEvents.add(events.get(i));
            }

        }
        //set the female events list in data cache
        setFemaleEvents(femaleEvents);
    }

    //get the list of events for the mothers side of the family
    public void setMotherSideFilter(List<Event>events,List<Person>people){
        List<Event>motherSideEvents = new ArrayList<>();

        String firstname = getFirstName();
        String lastname = getLastName();

        String userPersonID = "";
        String userSpouseID = "";
        for(int i = 0;i < people.size();++i){
            if((people.get(i).getFirstName().equals(firstname)) && (people.get(i).getLastName().equals(lastname))){
                userPersonID = people.get(i).getPersonID();
                userSpouseID = people.get(i).getSpouseID();
            }
        }
        List<Event>userEvents = getPersonEvents(events,userPersonID);
        List<Event>spouseEvents = getPersonEvents(events,userSpouseID);

        for(int i = 0;i < userEvents.size();++i){
            motherSideEvents.add(userEvents.get(i));
        }
        for(int i = 0;i < spouseEvents.size();++i){
            motherSideEvents.add(spouseEvents.get(i));
        }


        Person mother = returnPerson(people.get(0).getMotherID());
        recursivePerson(mother, motherSideEvents, events);

        setMotherSide(motherSideEvents);
    }

    //get the list of events for the fathers side of the family
    public void setFatherSideFilter(List<Event>events,List<Person>people){
        List<Event>fatherSideEvents = new ArrayList<>();

        String firstname = getFirstName();
        String lastname = getLastName();

        String userPersonID = "";
        String userSpouseID = "";
        for(int i = 0;i < people.size();++i){
            if((people.get(i).getFirstName().equals(firstname)) && (people.get(i).getLastName().equals(lastname))){
                userPersonID = people.get(i).getPersonID();
                userSpouseID = people.get(i).getSpouseID();
            }
        }
        List<Event>userEvents = getPersonEvents(events,userPersonID);
        List<Event>spouseEvents = getPersonEvents(events,userSpouseID);

        for(int i = 0;i < userEvents.size();++i){
            fatherSideEvents.add(userEvents.get(i));
        }
        for(int i = 0;i < spouseEvents.size();++i){
            fatherSideEvents.add(spouseEvents.get(i));
        }

        Person father = returnPerson(people.get(0).getFatherID());
        recursivePerson(father,fatherSideEvents,events);

        setFatherSide(fatherSideEvents);
    }

    public void recursivePerson(Person person,List<Event>familySideEvents,List<Event>events){
        for(int i = 0;i < events.size();++i){
            if(events.get(i).getPersonID().equals(person.getPersonID())){
                familySideEvents.add(events.get(i));
            }
        }

        if(person.getMotherID() != null){
            Person mother = returnPerson(person.getMotherID());
            recursivePerson(mother, familySideEvents, events);
        }

        if(person.getFatherID() != null) {
            Person father = returnPerson(person.getFatherID());
            recursivePerson(father, familySideEvents, events);
        }
    }

    //get the list of events with that specific character
    public List<Event>searchEvents(List<Event>events,String characters){
        List<Event>returningEvents = new ArrayList<>();

        for(int i = 0; i < events.size();++i){
            String year =  String.valueOf(events.get(i).getYear());
            if(events.get(i).getEventID().toLowerCase(Locale.ROOT).contains(characters.toLowerCase(Locale.ROOT))){
                returningEvents.add(events.get(i));
            }
            else if(events.get(i).getCity().toLowerCase(Locale.ROOT).contains(characters.toLowerCase(Locale.ROOT))){
                returningEvents.add(events.get(i));
            }
            else if(events.get(i).getCountry().toLowerCase(Locale.ROOT).contains(characters.toLowerCase(Locale.ROOT))){
                returningEvents.add(events.get(i));
            }
            else if(year.contains(characters)){
                returningEvents.add(events.get(i));
            }
        }

        return returningEvents;
    }

    //get that list of people that contain the specific character
    public List<Person>searchPeople(List<Person>people,String characters){
        List<Person>returningPeople = new ArrayList<>();

        for(int i = 0; i < people.size();++i){
            if(people.get(i).getFirstName().toLowerCase(Locale.ROOT).contains(characters.toLowerCase(Locale.ROOT))){
                returningPeople.add(people.get(i));
            }
            else if(people.get(i).getLastName().toLowerCase(Locale.ROOT).contains(characters.toLowerCase(Locale.ROOT))){
                returningPeople.add(people.get(i));
            }
        }

        return returningPeople;
    }

    //get the list of people that includes the father,mother,and spouse
    public List<Person> getPersons(String personID, String motherID, String fatherID, String spouseID){
        List<Person> people = getPeople();
        List<Person> returningList = new ArrayList<>();

        if(fatherID != null) {
            //get the father
            for (int i = 0; i < people.size(); ++i) {
                if (fatherID.equals(people.get(i).getPersonID())) {
                    returningList.add(people.get(i));
                }
            }
        }

        if(motherID != null) {
            //get the mother
            for (int i = 0; i < people.size(); ++i) {
                if (motherID.equals(people.get(i).getPersonID())) {
                    returningList.add(people.get(i));
                }
            }
        }

        if(spouseID != null) {
            //get the spouse
            for (int i = 0; i < people.size(); ++i) {
                if (spouseID.equals(people.get(i).getPersonID())) {
                    returningList.add(people.get(i));
                }
            }
        }

        if(spouseID != null) {
            //get the child
            for (int i = 0; i < people.size(); ++i) {
                if((personID.equals(people.get(i).getFatherID())) || (personID.equals(people.get(i).getMotherID()))){
                    returningList.add(people.get(i));
                }
            }
        }
        return returningList;
    }


}
