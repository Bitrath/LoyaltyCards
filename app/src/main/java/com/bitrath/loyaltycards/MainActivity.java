package com.bitrath.loyaltycards;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bitrath.loyaltycards.model.LoyaltyCard;
import com.bitrath.loyaltycards.model.LoyaltyCardsDAO;
import com.bitrath.loyaltycards.model.LoyaltyCardsDB;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class MainActivity extends AppCompatActivity implements
        EasyPermissions.PermissionCallbacks
        /*GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<Status>*/ {

    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    FloatingActionButton addLoyaltyCard;

    LoyaltyCardsDAO lcDAO;
    private List<LoyaltyCard> cards;
    private List<Address> locations;
    private List<Geofence> geoFences;
    protected GoogleApiClient googleApiClient;

    protected static int RC_FINE_LOCATION_PERM = 110;
    protected static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = 12 * 60 * 60 * 1000;
    protected static final float GEOFENCE_RADIUS_IN_METERS = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lcDAO = LoyaltyCardsDB.getLoyaltyCardDB(this).loyaltyCardsDAO();

        // Empty Lists
        locations = new ArrayList<Address>();
        geoFences = new ArrayList<Geofence>();

        // Get Cards
        cards = lcDAO.getAllLoyaltyCards();

        // Request Permissions
        fineLocationPermissionTask();

        // Link Views
        tabLayout = (TabLayout) findViewById(R.id.cardsTabLayout);
        viewPager = (ViewPager) findViewById(R.id.cardsViewPager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        addLoyaltyCard = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        addLoyaltyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), InsertCardActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        // Add Fragments
        adapter.addFragment(new AllCardsFragment(), "All Cards");
        adapter.addFragment(new FavoriteCardsFragment(), "Favorites");

        // VP & TL setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        // TabLayout Images
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_white_credit_card);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_white_star);

        // Remove Shadow from Action Bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);
    }

    // --- LOCATIONS ---
    public void forwardGeocoding(){
        // Setup Geocoder
        Geocoder coder = new Geocoder(this);
        // Get best result for each Card.Address
        if(cards.size() != 0){
            for(LoyaltyCard card: cards){
                try {
                    List<Address> addresses = coder.getFromLocationName(card.getAddress(),5);
                    if(addresses != null) {
                        Address loc = addresses.get(0);
                        Log.d("Forward Geocoding", card.getAddress() + " : " + loc.getLatitude() + " , " + loc.getLongitude());
                        locations.add(loc);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // --- GEO FENCES ---
    public void populateGeofenceList() {
        if( locations.size() != 0){
            for( Address location: locations){
                geoFences.add(new Geofence.Builder()
                        .setRequestId(location.getLocality())
                        .setCircularRegion(
                                location.getLatitude(),
                                location.getLongitude(),
                                GEOFENCE_RADIUS_IN_METERS
                        )
                        .setExpirationDuration(GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                                Geofence.GEOFENCE_TRANSITION_EXIT)
                        .build());
            }
        }
    }

    // --- PERMISSIONS ---
    public void fineLocationPermissionTask(){
        String[] perms = { Manifest.permission.ACCESS_FINE_LOCATION };
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Have permissions, do the thing!
            Toast.makeText(this, "TODO: Location and GeoFences things", Toast.LENGTH_LONG).show();
        } else {
            // Ask for both permissions
            EasyPermissions.requestPermissions(this, getString(R.string.location_rationale),
                    RC_FINE_LOCATION_PERM, perms);
            /*
            EasyPermissions.requestPermissions(
                    new PermissionRequest.Builder(this, RC_LOCATION_PERM, perms)
                            .setRationale(R.string.location_rationale)
                            .setPositiveButtonText(R.string.ask_ok_rationale)
                            .setNegativeButtonText(R.string.ask_cancel_rationale)
                            .build());
             */
        }
    }

    // --- EasyPermission Interface ---
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        Toast.makeText(this, "Permission Granted! Request: " + requestCode, Toast.LENGTH_LONG).show();
        //buildGoogleApiClient();
        forwardGeocoding();
        populateGeofenceList();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        Toast.makeText(this, "Permission Denied! Request: " + requestCode, Toast.LENGTH_LONG).show();
    }

    // --- ( Google API + Location Service + Geofence Transition ) Interface ---
    /*
    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void addActualGeoFences(View view) {
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(this, "Google API Client not connected!", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    getGeofencingRequest(),
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
        }
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(this, GeofenceTransitionController.class );
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling addgeoFences()
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Do something with result.getErrorCode());
    }
    @Override
    public void onConnectionSuspended(int cause) {
        googleApiClient.connect();
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (!googleApiClient.isConnecting() || !googleApiClient.isConnected()) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient.isConnecting() || googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    public void onResult(Status status) {
        if (status.isSuccess()) {
            Toast.makeText(
                    this,
                    "GeoFences Added",
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            Toast.makeText(this,"No geo fence :(",Toast.LENGTH_SHORT).show();
        }
    }
    */
}