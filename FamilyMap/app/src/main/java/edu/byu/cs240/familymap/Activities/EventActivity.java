package edu.byu.cs240.familymap.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import edu.byu.cs240.familymap.Fragments.LoginFragment;
import edu.byu.cs240.familymap.Fragments.MapFragment;
import edu.byu.cs240.familymap.MainActivity;
import edu.byu.cs240.familymap.R;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        String eventID = getIntent().getExtras().getString("eventID");
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment mapFragment = fragmentManager.findFragmentById(R.id.eventFrameLayout);
        if(mapFragment == null) {
            mapFragment = createMapFragment(eventID);
            fragmentManager.beginTransaction().add(R.id.eventFrameLayout, mapFragment).commit();
        }
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

    public Fragment createMapFragment(String eventID){
        MapFragment mapFragment = new MapFragment();
        Bundle bundle = new Bundle();
        bundle.putString("eventID",eventID);
        mapFragment.setArguments(bundle);
        return mapFragment;
    }
}