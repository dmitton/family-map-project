package edu.byu.cs240.familymap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import edu.byu.cs240.familymap.Fragments.GoogleMapsFragment;
import edu.byu.cs240.familymap.Fragments.LoginFragment;
import edu.byu.cs240.familymap.Fragments.MapFragment;
import edu.byu.cs240.familymap.R;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class MainActivity extends AppCompatActivity implements LoginFragment.Listener{

    private static final String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Iconify.with(new FontAwesomeModule());

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment loginFragment = fragmentManager.findFragmentById(R.id.fragmentFrameLayout);
        if(loginFragment == null) {
            loginFragment = createLoginFragment();

            fragmentManager.beginTransaction().add(R.id.fragmentFrameLayout, loginFragment).commit();
        } else {
            // If the fragment is not null, the MainActivity was destroyed and recreated
            // so we need to reset the listener to the new instance of the fragment
            if(loginFragment instanceof LoginFragment) {
                ((LoginFragment) loginFragment).registerListener(this);
            }
        }
    }

    private Fragment createLoginFragment() {
        LoginFragment fragment= new LoginFragment();
        fragment.registerListener(this);
        return fragment;
    }

    @Override
    public void notifyDone() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment mapFragment = new MapFragment();
        Fragment googleMapFragment = new GoogleMapsFragment();
        fragmentManager.beginTransaction().replace(R.id.fragmentFrameLayout, mapFragment).commit();
    }



}