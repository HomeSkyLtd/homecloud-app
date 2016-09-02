package com.homesky.homecloud;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends SingleFragmentActivity {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 1;


    @Override
    protected Fragment createFragment() {
        return MainActivityFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkGoogleApiAvailability();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        checkGoogleApiAvailability();
        super.onResume();
    }

    private void checkGoogleApiAvailability(){
        GoogleApiAvailability gApi = GoogleApiAvailability.getInstance();
        int result = gApi.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS){
            if(gApi.isUserResolvableError(result));
            gApi.getErrorDialog(this, result,
                    PLAY_SERVICES_RESOLUTION_REQUEST).show();
        }
    }
}

