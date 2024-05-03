package com.example.AntenatalReminders;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks { // Remove GpsStatus.Listener if not used

    BottomNavigationView bottomNavigation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_form); // Use the correct layout
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        loadFragment(new RegistrationFragment());

        bottomNavigation = findViewById(R.id.navigationView);
        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                loadFragment(new RegistrationFragment());
                return true;
            } else if (itemId == R.id.navigation_capture) {
                loadFragment(new CaptureFragment());
                return true;
            }
            return false;
        });

        // Remove location code if not used (commented out)
        // easyWayLocation = new EasyWayLocation(this, false, false, this);
        // getPermissionsAndLocation();
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("TAG", "Google Places API connected.");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
