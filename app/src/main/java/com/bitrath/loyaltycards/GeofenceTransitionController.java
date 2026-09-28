package com.bitrath.loyaltycards;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.location.GeofencingEvent;

public class GeofenceTransitionController extends IntentService {

    protected static final String TAG = "GeofenceTransitionCTR";

    public GeofenceTransitionController(){
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        if (event.hasError()) {
            Log.e(TAG, "GeofencingEvent Error: " + event.getErrorCode());
        }
        // GET TRANSITION DETAILS
        // SEND NOTIFICATION WITH DETAILS
    }

    // -- GET DETAILS METHOD

    // -- SEND NOTIFICATION WITH DETAILS METHOD
}
