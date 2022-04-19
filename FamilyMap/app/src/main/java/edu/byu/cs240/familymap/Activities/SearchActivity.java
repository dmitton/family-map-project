package edu.byu.cs240.familymap.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.byu.cs240.familymap.DataTransfer.DataCache;
import edu.byu.cs240.familymap.MainActivity;
import edu.byu.cs240.familymap.R;
import model.Event;
import model.Person;

public class SearchActivity extends AppCompatActivity {

    private static final int SEARCH_EVENTS_ITEM_VIEW_TYPE = 0;
    private static final int SEARCH_PEOPLE_ITEM_VIEW_TYPE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //set the title of the activity
        setTitle("Family Map: Search");

        //get the lists needed for the search
        DataCache dataCache = DataCache.getInstance();
        List<Event>modifiedEvents = dataCache.getModifiedList();
        List<Person>people = dataCache.getPeople();

        //initialize the recycler view
        RecyclerView recyclerView = findViewById(R.id.searchRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        //initialize the search view
        SearchView search = findViewById(R.id.searchView);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //perform a search on the event and the people and return those lists
                List<Event>searchEvents = searchEvents(modifiedEvents,s);
                List<Person>searchPeople = searchPeople(people,s);

                //place them into the adapter to be displayed
                SearchAdapter adapter = new SearchAdapter(searchPeople,searchEvents);
                recyclerView.setAdapter(adapter);
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)  {
            Intent intent= new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
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


    private class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder>{
        private final List<Event>searchEvents;
        private final List<Person>searchPeople;

        SearchAdapter(List<Person>searchPeople,List<Event>searchEvents){
            this.searchEvents = searchEvents;
            this.searchPeople = searchPeople;
        }

        @Override
        public int getItemViewType(int position) {
            return position < searchPeople.size() ? SEARCH_PEOPLE_ITEM_VIEW_TYPE : SEARCH_EVENTS_ITEM_VIEW_TYPE;
        }

        @NonNull
        @Override
        public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;

            if(viewType == SEARCH_PEOPLE_ITEM_VIEW_TYPE) {
                view = getLayoutInflater().inflate(R.layout.person_family_item, parent, false);
            } else {
                view = getLayoutInflater().inflate(R.layout.event_item, parent, false);
            }

            return new SearchViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
            if(position < searchPeople.size()) {
                holder.bind(searchPeople.get(position));
            } else {
                holder.bind(searchEvents.get(position - searchPeople.size()));
            }
        }

        @Override
        public int getItemCount() {
            return searchPeople.size() + searchEvents.size();
        }
    }


    private class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView personName;
        private final TextView eventDetails;
        private final TextView eventNameAssociated;

        private final int viewType;
        private Event searchEvent;
        private Person searchPerson;

        SearchViewHolder(View view, int viewType){
            super(view);
            this.viewType = viewType;

            itemView.setOnClickListener(this);

            if(viewType == SEARCH_PEOPLE_ITEM_VIEW_TYPE){
                personName = itemView.findViewById(R.id.familyNames);
                eventDetails = null;
                eventNameAssociated = null;

            } else{
                personName = null;
                eventDetails = itemView.findViewById(R.id.eventText);
                eventNameAssociated = itemView.findViewById(R.id.eventPersonNames);
            }
        }

        private void bind(Event searchEvent) {
            this.searchEvent = searchEvent;

            String eventType = searchEvent.getEventType().toUpperCase(Locale.ROOT);
            String country = searchEvent.getCountry();
            String city = searchEvent.getCity();
            int year = searchEvent.getYear();

            String eventDetailsText = eventType + ": " + city + ", " + country + " (" + year + ")";
            eventDetails.setText(eventDetailsText);

            Person eventPerson = returnPerson(searchEvent.getPersonID());

            String name = eventPerson.getFirstName() + " " + eventPerson.getLastName();

            eventNameAssociated.setText(name);

            ImageView eventImageView = itemView.findViewById(R.id.eventImageView);
            Drawable mapIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_map_marker).
                    colorRes(R.color.black).sizeDp(40);

            eventImageView.setImageDrawable(mapIcon);
        }

        private void bind(Person searchPerson) {
            this.searchPerson = searchPerson;

            String name = searchPerson.getFirstName() + " " + searchPerson.getLastName();

            personName.setText(name);

            ImageView familyImageView = (ImageView) itemView.findViewById(R.id.familyImageView);
            String gender = searchPerson.getGender();
            if(gender.equals("m")) {
                Drawable genderIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_male).colorRes(R.color.blue).sizeDp(40);
                familyImageView.setImageDrawable(genderIcon);
            }
            else{
                Drawable genderIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_female).colorRes(R.color.pink).sizeDp(40);
                familyImageView.setImageDrawable(genderIcon);
            }
        }

        @Override
        public void onClick(View view) {
            if(viewType == SEARCH_EVENTS_ITEM_VIEW_TYPE) {
                Intent intent = new Intent(SearchActivity.this,EventActivity.class);
                intent.putExtra("eventID",searchEvent.getEventID());
                startActivity(intent);
            } else {
                Intent intent2 = new Intent(SearchActivity.this,PersonActivity.class);
                intent2.putExtra("personID",searchPerson.getPersonID());
                startActivity(intent2);
            }
        }

        //returns a person when given a person ID
        public Person returnPerson(String personID){
            DataCache dataCache = DataCache.getInstance();
            List<Person> people = dataCache.getPeople();
            for(int i = 0;i < people.size();++i){
                if(personID.equals(people.get(i).getPersonID())){
                    return people.get(i);
                }
            }
            return null;
        }
    }


}