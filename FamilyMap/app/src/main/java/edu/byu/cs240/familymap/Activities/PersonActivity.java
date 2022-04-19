package edu.byu.cs240.familymap.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import edu.byu.cs240.familymap.DataTransfer.DataCache;
import edu.byu.cs240.familymap.DataTransfer.EventComparator;
import edu.byu.cs240.familymap.MainActivity;
import edu.byu.cs240.familymap.R;
import model.Event;
import model.Person;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

public class PersonActivity extends AppCompatActivity {

    private Person person;
    private TextView firstNameText;
    private TextView lastNameText;
    private TextView genderText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        //set the title of the activity
        setTitle("Family Map: Person Details");

        //set all the first and last name text views as well as the gender text view
        firstNameText = (TextView) findViewById(R.id.firstNamePA);
        lastNameText = (TextView) findViewById(R.id.lastNamePA);
        genderText = (TextView) findViewById(R.id.genderPA);

        //get the person ID and get the specific person from our data cache
        person = null;
        DataCache dataCache = DataCache.getInstance();
        String clickedPersonId = getIntent().getExtras().getString("personID");
        List<Event>modifiedList = dataCache.getModifiedList();
        List<Event>events = dataCache.getEvents();
        List<Person> people = dataCache.getPeople();
        for(int i = 0;i < people.size();++i){
            if(clickedPersonId.equals(people.get(i).getPersonID())){
                person = people.get(i);
            }
        }

        //set the texts for our layout view for first and last name and gender
        firstNameText.setText(person.getFirstName());
        lastNameText.setText(person.getLastName());
        if(person.getGender().equals("m")){
            genderText.setText(R.string.male_gender);
        }
        else{
            genderText.setText(R.string.female_gender);
        }

        //get the family and the events that you need
        Map<String,String>relationMap = new HashMap<>();
        List<Event> personEvents = getEvents(clickedPersonId,modifiedList);
        List<Person> personFamily = getPersons(clickedPersonId,person.getMotherID(),person.getFatherID(),person.getSpouseID(),dataCache, relationMap);

        ExpandableListView expandableListView = findViewById(R.id.expandableListView);
        expandableListView.setAdapter(new ExpandableListAdapter(personEvents,personFamily,person,relationMap));

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

    //get the list of events associated with that person
    public List<Event> getEvents(String personID,List<Event>modifiedList){
        List<Event> returningList = new ArrayList<>();

        for(int i = 0;i < modifiedList.size();++i){
            if(personID.equals(modifiedList.get(i).getPersonID())){
                returningList.add(modifiedList.get(i));
            }
        }

        if(modifiedList.size() != 0) {
            returningList.sort(new EventComparator());
        }

        return returningList;
    }


    //get the list of people that includes the father,mother,and spouse
    public List<Person> getPersons(String personID,String motherID,String fatherID,String spouseID,DataCache dataCache,Map<String,String>relationMap){
        List<Person> people = dataCache.getPeople();
        List<Person> returningList = new ArrayList<>();

        if(fatherID != null) {
            //get the father
            for (int i = 0; i < people.size(); ++i) {
                if (fatherID.equals(people.get(i).getPersonID())) {
                    returningList.add(people.get(i));
                    relationMap.put("Father",people.get(i).getPersonID());
                }
            }
        }

        if(motherID != null) {
            //get the mother
            for (int i = 0; i < people.size(); ++i) {
                if (motherID.equals(people.get(i).getPersonID())) {
                    returningList.add(people.get(i));
                    relationMap.put("Mother",people.get(i).getPersonID());
                }
            }
        }

        if(spouseID != null) {
            //get the spouse
            for (int i = 0; i < people.size(); ++i) {
                if (spouseID.equals(people.get(i).getPersonID())) {
                    returningList.add(people.get(i));
                    relationMap.put("Spouse",people.get(i).getPersonID());
                }
            }
        }

        if(spouseID != null) {
            //get the child
            for (int i = 0; i < people.size(); ++i) {
                if((personID.equals(people.get(i).getFatherID())) || (personID.equals(people.get(i).getMotherID()))){
                    returningList.add(people.get(i));
                    relationMap.put("Child",people.get(i).getPersonID());
                }
            }
        }


        return returningList;
    }

    private class ExpandableListAdapter extends BaseExpandableListAdapter {
        private static final int PERSON_EVENT_GROUP_POSITION = 0;
        private static final int PERSON_FAMILY_GROUP_POSITION = 1;

        private List<Event>personEvents;
        private List<Person> personsFamily;
        private Person clickedPerson;
        private Map<String,String> relationMap;
        private LinearLayout familyLayouts;
        private LinearLayout eventLayout;

        ExpandableListAdapter(List<Event>personEvents, List<Person>personsFamily,Person clickedPerson, Map<String,String>relationMap){
            this.clickedPerson = clickedPerson;
            this.personEvents = personEvents;
            this.personsFamily = personsFamily;
            this.relationMap = relationMap;
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition){
            switch(groupPosition){
                case PERSON_EVENT_GROUP_POSITION:
                    return personEvents.size();
                case PERSON_FAMILY_GROUP_POSITION:
                    return personsFamily.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            // Not used
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            // Not used
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_group, parent, false);
            }

            TextView titleView = convertView.findViewById(R.id.listTitle);

            switch (groupPosition) {
                case PERSON_EVENT_GROUP_POSITION:
                    titleView.setText(R.string.event_title);
                    break;
                case PERSON_FAMILY_GROUP_POSITION:
                    titleView.setText(R.string.family_title);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView;

            switch(groupPosition) {
                case PERSON_EVENT_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.event_item, parent, false);
                    initializeEventView(itemView, childPosition);
                    break;
                case PERSON_FAMILY_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.person_family_item, parent, false);
                    initializeFamilyView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return itemView;
        }

        private void initializeEventView(View eventView, final int childPosition){
            TextView eventText = (TextView) eventView.findViewById(R.id.eventText);
            TextView eventNameAssociated = (TextView) eventView.findViewById(R.id.eventPersonNames);

            String eventName = clickedPerson.getFirstName() + " " + clickedPerson.getLastName();
            String eventTextInsert = setEventText(childPosition);

            eventText.setText(eventTextInsert);
            eventNameAssociated.setText(eventName);

            ImageView eventImageView = eventView.findViewById(R.id.eventImageView);
            Drawable mapIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_map_marker).
                    colorRes(R.color.black).sizeDp(40);

            eventImageView.setImageDrawable(mapIcon);

            eventLayout = eventView.findViewById(R.id.eventLayout);
            eventLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(PersonActivity.this,EventActivity.class);
                    intent.putExtra("eventID",personEvents.get(childPosition).getEventID());
                    startActivity(intent);
                }
            });

        }

        private void initializeFamilyView(View familyView, final int childPosition){
            TextView familyNameText = (TextView) familyView.findViewById(R.id.familyNames);
            TextView relationText = (TextView) familyView.findViewById(R.id.relation);

            String familyName = personsFamily.get(childPosition).getFirstName() + " " + personsFamily.get(childPosition).getLastName();
            String relation = getRelation(childPosition);

            familyNameText.setText(familyName);
            relationText.setText(relation);

            ImageView familyImageView = (ImageView) familyView.findViewById(R.id.familyImageView);
            String gender = personsFamily.get(childPosition).getGender();
            if(gender.equals("m")) {
                Drawable genderIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_male).colorRes(R.color.blue).sizeDp(40);
                familyImageView.setImageDrawable(genderIcon);
            }
            else{
                Drawable genderIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_female).colorRes(R.color.pink).sizeDp(40);
                familyImageView.setImageDrawable(genderIcon);
            }

            familyLayouts = familyView.findViewById(R.id.familyLayout);
            familyLayouts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String relation = (String) relationText.getText();
                    String personId = getPersonFromRelation(relation);

                    Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
                    intent.putExtra("personID", personId);
                    startActivity(intent);
                    System.out.println("I have clicked a person in the relations");
                }
            });

        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        public String setEventText(int childPosition){
            String eventType = personEvents.get(childPosition).getEventType().toUpperCase(Locale.ROOT);
            String city = personEvents.get(childPosition).getCity();
            String country = personEvents.get(childPosition).getCountry();
            int year = personEvents.get(childPosition).getYear();

            String text = eventType + ": " + city + ", " + country + " (" + year + ")";

            return text;
        }

        public String getRelation(int childPosition){
            String personID = personsFamily.get(childPosition).getPersonID();
            String relation = "";
            for (Map.Entry<String, String> entry : relationMap.entrySet()) {
                if (personID.equals(entry.getValue())) {
                    relation = entry.getKey();
                }
            }
            return relation;
        }

        public String getPersonFromRelation(String relation){
            for (Map.Entry<String, String> entry : relationMap.entrySet()) {
                if (relation.equals(entry.getKey())) {
                    return entry.getValue();
                }
            }
            return null;
        }
    }
}