package edu.byu.cs240.familymap.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.w3c.dom.Text;

import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import edu.byu.cs240.familymap.Activities.PersonActivity;
import edu.byu.cs240.familymap.Activities.SearchActivity;
import edu.byu.cs240.familymap.Activities.SettingsActivity;
import edu.byu.cs240.familymap.DataTransfer.DataCache;
import edu.byu.cs240.familymap.DataTransfer.EventComparator;
import edu.byu.cs240.familymap.MainActivity;
import edu.byu.cs240.familymap.R;
import model.Event;
import model.Person;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback{
    private GoogleMap map;
    private TextView markerTextView;
    private ImageView genderImageView;
    private Person clickedPerson;
    private DataCache dataCache;
    private List<Event>events;
    private List<Person>people;
    private List<Event>modifiedList;
    private List<Polyline>polylines;
    private List<PolylineOptions>polylineOptions;
    private List<PolylineOptions>tempPolylineOptions;
    private Map<Float,String> colorMap;
    private String firstname;
    private String lastname;
    private String eventActivityID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.googleMapFragment);
        mapFragment.getMapAsync(this);

        markerTextView = (TextView)view.findViewById(R.id.markerText);
        genderImageView = (ImageView) view.findViewById(R.id.imageView);

        //get the people and the events from the data cache
        dataCache = DataCache.getInstance();
        events = dataCache.getEvents();
        people = dataCache.getPeople();

        //initialize the first and last name of the user
        firstname = dataCache.getFirstName();
        lastname = dataCache.getLastName();

        //set all of the unique filters
        setMaleEvents(events,dataCache);
        setFemaleEvents(events,dataCache);
        setMotherSide(events,people,dataCache);
        setFatherSide(events,people,dataCache);

        polylines = new ArrayList<>();
        polylineOptions = new ArrayList<>();
        colorMap = new HashMap<>();

        Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_android).colorRes(R.color.green).sizeDp(40);
        genderImageView.setImageDrawable(genderIcon);

        Bundle bundle = getArguments();
        if(bundle != null){
            eventActivityID = getArguments().getString("eventID");
            setHasOptionsMenu(false);
        }
        else {
            eventActivityID = null;
            setHasOptionsMenu(true);
        }

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        map = googleMap;
        map.setOnMapLoadedCallback(this);

        modifiedList = new ArrayList<>();

        //get all of the lists that are filters from the data cache
        List<Event>motherSideEvents = dataCache.getMotherSide();
        List<Event>fatherSideEvents = dataCache.getFatherSide();
        List<Event>maleEvents = dataCache.getMaleEvents();
        List<Event>femaleEvents = dataCache.getFemaleEvents();

        //set the right markers on the map according to the filters
        getRightFilters(dataCache,fatherSideEvents,motherSideEvents,events,maleEvents,femaleEvents);

        //check to see if the event sent from the event activity is null or set and if it is then set the event banner
        if(eventActivityID != null){
            setEventBanner(events,people, dataCache);
        }

        dataCache.setModifiedList(modifiedList);

        //set an on click listener to the markers
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                //remove all of the previous poly lines
                for(int i = 0; i < polylines.size();++i){
                    polylines.get(i).remove();
                }
                polylineOptions.clear();
                polylines.clear();


                clickedPerson = null;
                Event clickedEvent = null;

                //search through the events and get the event associated with the marker
                for(int i = 0; i < events.size();++i){
                    if(events.get(i).getEventID().equals(marker.getTag())){
                        clickedEvent = events.get(i);
                        dataCache.setClickedEvent(clickedEvent);
                    }
                }

                //search through the people and get the person associated with the event
                clickedPerson = returnPerson(clickedEvent.getPersonID());


                //set the strings needed for the person popup
                setBanner(clickedEvent,people);

                setPolyLines(clickedEvent);
                tempPolylineOptions = new ArrayList<>(polylineOptions);

                return false;
            }
        });

        //put a listener onto the textview
        markerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clickedPerson == null){
                    Toast.makeText(getContext(),"Click on a person",Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent = new Intent(getActivity(), PersonActivity.class);
                    dataCache.setModifiedList(modifiedList);
                    intent.putExtra("personID",clickedPerson.getPersonID());;
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onMapLoaded() {
        // You probably don't need this callback. It occurs after onMapReady and I have seen
        // cases where you get an error when adding markers or otherwise interacting with the map in
        // onMapReady(...) because the map isn't really all the way ready. If you see that, just
        // move all code where you interact with the map (everything after
        // map.setOnMapLoadedCallback(...) above) to here.
    }

    @Override
    public void onResume() {
        super.onResume();
        if(map != null) {
            map.clear();
            onMapReady(map);
            if(clickedPerson != null) {
                setPolyLines(dataCache.getClickedEvent());
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.map_menu, menu);
        MenuItem searchMenuItem= menu.findItem(R.id.searchMenu);
        MenuItem settingsMenuItem = menu.findItem(R.id.settingsMenu);

        searchMenuItem.setIcon(new IconDrawable(getActivity(), FontAwesomeIcons.fa_search).colorRes(R.color.white)
                .actionBarSize());
        settingsMenuItem.setIcon(new IconDrawable(getActivity(),FontAwesomeIcons.fa_gear).colorRes(R.color.white)
        .actionBarSize());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        switch(menu.getItemId()) {
            case R.id.searchMenu:
                Toast.makeText(getActivity(), getString(R.string.search_test), Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent2);
                return true;
            case R.id.settingsMenu:
                Toast.makeText(getActivity(), getString(R.string.settings_test), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(menu);
        }
    }

    //set the banner at the bottom of the screen
    public void setBanner(Event clickedEvent,List<Person>people){
        String firstName = clickedPerson.getFirstName();
        String lastName = clickedPerson.getLastName();
        String gender = clickedPerson.getGender();
        String eventType = clickedEvent.getEventType();
        String city = clickedEvent.getCity();
        String country = clickedEvent.getCountry();
        int year = clickedEvent.getYear();
        String text = firstName + " " + lastName + "\n" + eventType.toUpperCase(Locale.ROOT)
                + ": " + city + ", " + country + " (" + year + ")";

        if(gender.equals("m")) {
            Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).colorRes(R.color.blue).sizeDp(40);
            genderImageView.setImageDrawable(genderIcon);
        }
        else{
            Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).colorRes(R.color.pink).sizeDp(40);
            genderImageView.setImageDrawable(genderIcon);
        }

        //set the text in the textview
        markerTextView.setText(text);
    }

    //returns a person when given a person ID
    public Person returnPerson(String personID){

        for(int i = 0;i < people.size();++i){
            if(personID.equals(people.get(i).getPersonID())){
                return people.get(i);
            }
        }
        return null;
    }

    //set the male events in data cache
    public void setMaleEvents(List<Event>events, DataCache dataCache){
        List<Event>maleEvents = new ArrayList<>();
        //get all of the male events and put them in a list
        for(int i = 0;i < events.size();++i){
            Person person = returnPerson(events.get(i).getPersonID());
            if(person.getGender().equals("m")){
                maleEvents.add(events.get(i));
            }
        }
        //put that list in the data cache
        dataCache.setMaleEvents(maleEvents);
    }

    //set the female events in dataCache
    public void setFemaleEvents(List<Event>events, DataCache dataCache){
        List<Event>femaleEvents = new ArrayList<>();
        //get all the female events in the database
        for(int i = 0;i < events.size();++i){
            Person person = returnPerson(events.get(i).getPersonID());
            if(person.getGender().equals("f")){
                femaleEvents.add(events.get(i));
            }

        }
        //set the female events list in data cache
        dataCache.setFemaleEvents(femaleEvents);
    }

    //get the list of events for the mothers side of the family
    public void setMotherSide(List<Event>events,List<Person>people, DataCache dataCache){
        List<Event>motherSideEvents = new ArrayList<>();

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
        recursivePerson(mother, motherSideEvents, events, dataCache);

        dataCache.setMotherSide(motherSideEvents);
    }

    //get the list of events for the fathers side of the family
    public void setFatherSide(List<Event>events,List<Person>people, DataCache dataCache){
        List<Event>fatherSideEvents = new ArrayList<>();

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
        recursivePerson(father,fatherSideEvents,events,dataCache);

        dataCache.setFatherSide(fatherSideEvents);
    }

    //function for putting male event markers on the map
    public void setMarkers(List<Event>eventsMarked){
        Float hue = 30.0F;
        Float add = 30.0F;
        //put markers on the map for all of the male events
        for(int i = 0;i < eventsMarked.size();++i){
            modifiedList.add(eventsMarked.get(i));
            Float latitude = eventsMarked.get(i).getLatitude();
            Float longitude = eventsMarked.get(i).getLongitude();
            LatLng marker = new LatLng(latitude,longitude);
            if(eventsMarked.get(i).getEventType().toUpperCase(Locale.ROOT).equals("DEATH")) {
                map.addMarker(new MarkerOptions().position(marker).icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED))).setTag(eventsMarked.get(i).getEventID());
            }
            else if(eventsMarked.get(i).getEventType().toUpperCase(Locale.ROOT).equals("BIRTH")){
                map.addMarker(new MarkerOptions().position(marker)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))).setTag(eventsMarked.get(i).getEventID());
            }
            else if(eventsMarked.get(i).getEventType().toUpperCase(Locale.ROOT).equals("MARRIAGE")){
                map.addMarker(new MarkerOptions().position(marker)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))).setTag(eventsMarked.get(i).getEventID());
            }
            else{
                hue += add;
                if((hue == 60.0F) || (hue == 180.0F)){
                    hue += add;
                }
                setMarkersIfTheSame(hue,add,marker,eventsMarked.get(i));
            }
        }
    }

    //check the count to see if there is another event in the database
    public void setMarkersIfTheSame(Float hue,Float add, LatLng marker,Event eventBeingMarked){
        if(colorMap.size() == 0){
            colorMap.put(hue,eventBeingMarked.getEventType().toLowerCase(Locale.ROOT));
            map.addMarker(new MarkerOptions().position(marker)
                    .icon(BitmapDescriptorFactory.defaultMarker(hue))).setTag(eventBeingMarked.getEventID());
        }
        else {
            for (Map.Entry<Float, String> m : colorMap.entrySet()) {
                if (m.getValue().equals(eventBeingMarked.getEventType().toLowerCase(Locale.ROOT))) {
                    map.addMarker(new MarkerOptions().position(marker)
                            .icon(BitmapDescriptorFactory.defaultMarker(m.getKey()))).setTag(eventBeingMarked.getEventID());
                    return;
                }
            }
            colorMap.put(hue, eventBeingMarked.getEventType().toLowerCase(Locale.ROOT));
            map.addMarker(new MarkerOptions().position(marker)
                    .icon(BitmapDescriptorFactory.defaultMarker(hue))).setTag(eventBeingMarked.getEventID());

        }
    }


    //recursive function to get the events for a side of the family
    public void recursivePerson(Person person,List<Event>familySideEvents,List<Event>events,DataCache dataCache){
        for(int i = 0;i < events.size();++i){
            if(events.get(i).getPersonID().equals(person.getPersonID())){
                familySideEvents.add(events.get(i));
            }
        }

        if(person.getMotherID() != null){
            Person mother = returnPerson(person.getMotherID());
            recursivePerson(mother, familySideEvents, events, dataCache);
        }

        if(person.getFatherID() != null) {
            Person father = returnPerson(person.getFatherID());
            recursivePerson(father, familySideEvents, events, dataCache);
        }
    }

    //function that provides the right filters based on the settings
    public void getRightFilters(DataCache dataCache,List<Event>fatherSide,List<Event>motherSide, List<Event>events, List<Event>male,List<Event>female){
        boolean fatherSideSetting = dataCache.isFatherSideSetting();
        boolean motherSideSetting = dataCache.isMotherSideSetting();
        boolean femaleSetting = dataCache.isFemaleEventsSetting();
        boolean maleSetting = dataCache.isMaleEventsSetting();

        //info for the user and his spouse
        List<Person>people = dataCache.getPeople();
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

        if(fatherSideSetting && motherSideSetting){
            if(maleSetting && femaleSetting){
                setMarkers(events);
            }
            else if((maleSetting) && (!femaleSetting)){
                setMarkers(male);
            }
            else if((!maleSetting) && (femaleSetting)){
                setMarkers(female);
            }
        }
        else if((fatherSideSetting) && (!motherSideSetting)){
            if(maleSetting && femaleSetting){
                setMarkers(fatherSide);
            }
            else if((maleSetting) && (!femaleSetting)){
                setMaleEvents(fatherSide,dataCache);
                List<Event>newMaleEvents = dataCache.getMaleEvents();
                setMarkers(newMaleEvents);
            }
            else if((!maleSetting) && (femaleSetting)){
                setFemaleEvents(fatherSide,dataCache);
                List<Event>newFemaleEvents = dataCache.getFemaleEvents();
                setMarkers(newFemaleEvents);
            }
        }

        else if((!fatherSideSetting) && (motherSideSetting)){
            if(maleSetting && femaleSetting){
                setMarkers(motherSide);
            }
            else if((maleSetting) && (!femaleSetting)){
                setMaleEvents(motherSide,dataCache);
                List<Event>newMaleEvents = dataCache.getMaleEvents();
                setMarkers(newMaleEvents);
            }
            else if((!maleSetting) && (femaleSetting)){
                setFemaleEvents(motherSide,dataCache);
                List<Event>newFemaleEvents = dataCache.getFemaleEvents();
                setMarkers(newFemaleEvents);
            }
        }
        else{
            if(maleSetting && femaleSetting){
                setMarkers(userEvents);
                setMarkers(spouseEvents);
            }
            else if((maleSetting) && (!femaleSetting)){
                for(int i = 0;i < userEvents.size();++i){
                    spouseEvents.add(userEvents.get(i));
                }
                setMaleEvents(spouseEvents,dataCache);
                List<Event>newMaleEvents = dataCache.getMaleEvents();
                setMarkers(newMaleEvents);
            }
            else if((!maleSetting) && (femaleSetting)){
                for(int i = 0;i < userEvents.size();++i){
                    spouseEvents.add(userEvents.get(i));
                }
                setFemaleEvents(spouseEvents,dataCache);
                List<Event>newFemaleEvents = dataCache.getFemaleEvents();
                setMarkers(newFemaleEvents);
            }
        }
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

    //add poly markers to whoever the clicked person is
    public void addPolyMarkers(List<Event>events){
        for(int i = 0;i < events.size() - 1;++i) {
            if (events.size() > 1) {
                Float latitude = events.get(i).getLatitude();
                Float longitude = events.get(i).getLongitude();
                Float latitude2 = events.get(i + 1).getLatitude();
                Float longitude2 = events.get(i + 1).getLongitude();
                LatLng end = new LatLng(latitude2, longitude2);
                LatLng start = new LatLng(latitude, longitude);
                PolylineOptions options = new PolylineOptions().add(start).add(end).color(Color.GREEN).width(20);
                Polyline line = map.addPolyline(options);
                polylines.add(line);
                polylineOptions.add(options);
            }
        }
    }

    //add poly lines for the spouse lines setting
    public void setSpouseLines(Event clickedEvent,List<Event>events) {
        if(clickedPerson.getSpouseID() != null) {
            List<Event> spouseEvents = getPersonEvents(events, clickedPerson.getSpouseID());
            spouseEvents.sort(new EventComparator());
            if(spouseEvents.size() != 0) {
                LatLng start = new LatLng(clickedEvent.getLatitude(), clickedEvent.getLongitude());
                LatLng end = new LatLng(spouseEvents.get(0).getLatitude(), spouseEvents.get(0).getLongitude());

                PolylineOptions options = new PolylineOptions().add(start).add(end).color(Color.RED).width(20);
                Polyline line = map.addPolyline(options);
                polylines.add(line);
                polylineOptions.add(options);
            }
        }
    }

    public void setFamilyTreeLines(Person person,Event startEvent, List<Event>events,int width,DataCache dataCache){
        if(person.getFatherID() != null){
            Person father = returnPerson(person.getFatherID());
            List<Event>fathersEvents = getPersonEvents(events,person.getFatherID());
            if(fathersEvents.size() != 0) {
                fathersEvents.sort(new EventComparator());
                LatLng start = new LatLng(startEvent.getLatitude(), startEvent.getLongitude());
                LatLng end = new LatLng(fathersEvents.get(0).getLatitude(), fathersEvents.get(0).getLongitude());
                PolylineOptions options = new PolylineOptions().add(start).add(end).color(Color.BLUE).width(width);
                Polyline line = map.addPolyline(options);
                polylines.add(line);
                polylineOptions.add(options);
                setFamilyTreeLines(father, fathersEvents.get(0),events,width - 8,dataCache );
            }
        }
        if(person.getMotherID() != null){
            Person mother = returnPerson(person.getMotherID());
            List<Event>mothersEvents = getPersonEvents(events,person.getMotherID());
            if(mothersEvents.size() != 0) {
                mothersEvents.sort(new EventComparator());
                LatLng start = new LatLng(startEvent.getLatitude(), startEvent.getLongitude());
                LatLng end = new LatLng(mothersEvents.get(0).getLatitude(), mothersEvents.get(0).getLongitude());
                PolylineOptions options = new PolylineOptions().add(start).add(end).color(Color.BLUE).width(width);
                Polyline line = map.addPolyline(options);
                polylines.add(line);
                polylineOptions.add(options);
                setFamilyTreeLines(mother, mothersEvents.get(0),events,width - 8,dataCache );
            }
        }
    }


    public void setEventBanner(List<Event>events, List<Person>people,DataCache dataCache){
        boolean lifeStorySetting = dataCache.isLifeStorySetting();
        boolean familyTreeSetting = dataCache.isFamilyTreeSetting();
        boolean spouseLineSetting = dataCache.isSpouseLinesSetting();
        for(int i = 0;i < events.size();++i){
            if(events.get(i).getEventID().equals(eventActivityID)){
                Event eventClicked = events.get(i);
                clickedPerson = returnPerson(eventClicked.getPersonID());
                setBanner(eventClicked,people);

                //if the life story setting is true then put the poly lines on the map
                if(lifeStorySetting){
                    List<Event> personEvents = getPersonEvents(modifiedList,clickedPerson.getPersonID());
                    personEvents.sort(new EventComparator());
                    addPolyMarkers(personEvents);
                }

                //if the spouse line setting is true then put the poly lines on the map
                if(spouseLineSetting){
                    setSpouseLines(eventClicked,modifiedList);
                }

                //if the family tree line setting is true then put the poly lines on the map
                if(familyTreeSetting){
                    setFamilyTreeLines(clickedPerson,eventClicked,modifiedList,32,dataCache);
                }


                LatLng camera = new LatLng(events.get(i).getLatitude(),events.get(i).getLongitude());
                map.animateCamera(CameraUpdateFactory.newLatLng(camera));
            }
        }
    }

    public void setPolyLines(Event clickedEvent){
        boolean isInModifiedList = false;
        for(int i = 0;i < modifiedList.size();++i){
            if(clickedEvent.getEventID().equals(modifiedList.get(i).getEventID())){
                isInModifiedList = true;
            }
        }
        if(!isInModifiedList){
            return;
        }
        //get the boolean for the relationship setting
        boolean lifeStorySetting = dataCache.isLifeStorySetting();
        boolean familyTreeSetting = dataCache.isFamilyTreeSetting();
        boolean spouseLineSetting = dataCache.isSpouseLinesSetting();

        //if the life story setting is true then put the poly lines on the map
        if(lifeStorySetting){
            List<Event> personEvents = getPersonEvents(modifiedList,clickedPerson.getPersonID());
            personEvents.sort(new EventComparator());
            addPolyMarkers(personEvents);
        }

        //if the spouse line setting is true then put the poly lines on the map
        if(spouseLineSetting){
            setSpouseLines(clickedEvent,modifiedList);
        }

        //if the family tree line setting is true then put the poly lines on the map
        if(familyTreeSetting){
            setFamilyTreeLines(clickedPerson,clickedEvent,modifiedList,32,dataCache);
        }
    }
}