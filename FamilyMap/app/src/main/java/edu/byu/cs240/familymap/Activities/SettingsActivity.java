package edu.byu.cs240.familymap.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import edu.byu.cs240.familymap.DataTransfer.DataCache;
import edu.byu.cs240.familymap.MainActivity;
import edu.byu.cs240.familymap.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Family Map: Settings");

        DataCache dataCache = DataCache.getInstance();
        dataCache.setSettingsChanged(false);


        //get all of the switches
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch lifeStory = (Switch) findViewById(R.id.lifeStorySwitch);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch familyTree = (Switch) findViewById(R.id.familyTreeSwitch);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch spouseLines = (Switch) findViewById(R.id.spouseSwitch);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch fatherLine = (Switch) findViewById(R.id.fatherSwitch);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch motherLine = (Switch) findViewById(R.id.motherSwitch);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch maleEventsSwitch = (Switch) findViewById(R.id.maleSwitch);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch femaleEventsSwitch = (Switch) findViewById(R.id.femaleSwitch);

        //get all of the booleans from the datacache
        boolean lifeStorySwitch = dataCache.isLifeStorySetting();
        boolean familyTreeSwitch = dataCache.isFamilyTreeSetting();
        boolean spouseLineSwitch = dataCache.isSpouseLinesSetting();
        boolean fatherSwitch = dataCache.isFatherSideSetting();
        boolean motherSwitch = dataCache.isMotherSideSetting();
        boolean maleSwitch = dataCache.isMaleEventsSetting();
        boolean femaleSwitch = dataCache.isFemaleEventsSetting();

        //set all of the booleans in the datacache
        lifeStory.setChecked(lifeStorySwitch);
        familyTree.setChecked(familyTreeSwitch);
        spouseLines.setChecked(spouseLineSwitch);
        fatherLine.setChecked(fatherSwitch);
        motherLine.setChecked(motherSwitch);
        maleEventsSwitch.setChecked(maleSwitch);
        femaleEventsSwitch.setChecked(femaleSwitch);

        //listener for the life story setting
        lifeStory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                boolean lifeStorySetting = lifeStory.isChecked();
                dataCache.setSettingsChanged(true);
                dataCache.setLifeStorySetting(lifeStorySetting);
                System.out.println(dataCache.isLifeStorySetting());
            }
        });

        //listener for the family tree switch
        familyTree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                boolean familyTreeSetting = familyTree.isChecked();
                dataCache.setSettingsChanged(true);
                dataCache.setFamilyTreeSetting(familyTreeSetting);
                System.out.println(dataCache.isFamilyTreeSetting());
            }
        });

        //listener for the spouse lines switch
        spouseLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                boolean spouseSetting = spouseLines.isChecked();
                dataCache.setSettingsChanged(true);
                dataCache.setSpouseLinesSetting(spouseSetting);
                System.out.println(dataCache.isSpouseLinesSetting());
            }
        });

        //listener for the father side switch
        fatherLine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                boolean fatherSetting = fatherLine.isChecked();
                dataCache.setSettingsChanged(true);
                dataCache.setFatherSideSetting(fatherSetting);
                System.out.println(dataCache.isFatherSideSetting());
            }
        });

        //listener for the mother side switch
        motherLine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                boolean motherSetting = motherLine.isChecked();
                dataCache.setSettingsChanged(true);
                dataCache.setMotherSideSetting(motherSetting);
                System.out.println(dataCache.isMotherSideSetting());
            }
        });

        //listener for the male events switch
        maleEventsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                boolean maleEventsChecked = maleEventsSwitch.isChecked();
                dataCache.setSettingsChanged(true);
                dataCache.setMaleEventsSetting(maleEventsChecked);
                System.out.println(dataCache.isMaleEventsSetting());
            }
        });

        //listener for the female events switch
        femaleEventsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                boolean femaleEventsChecked = femaleEventsSwitch.isChecked();
                dataCache.setSettingsChanged(true);
                dataCache.setFemaleEventsSetting(femaleEventsChecked);
                System.out.println(dataCache.isFemaleEventsSetting());
            }
        });

        LinearLayout logout = findViewById(R.id.logoutLayout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataCache.clearDatacache();
                Intent intent = new Intent(SettingsActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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

}