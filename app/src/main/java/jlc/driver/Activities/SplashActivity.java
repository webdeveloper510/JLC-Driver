package jlc.driver.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import jlc.driver.R;
import jlc.driver.Utils.CsPrefrences;
import jlc.driver.Utils.LocationService;
import jlc.driver.Utils.Utilss;


public class SplashActivity extends Activity implements GoogleApiClient.ConnectionCallbacks {

    /**
     * Duration of wait
     **/

    private final int SPLASH_DISPLAY_LENGTH = 2000;
    final static int REQUEST_LOACTION = 199;
    Context context;
    LocationManager mLocationManager;
    public static Double lattitude, longitude;
    private GoogleApiClient googleApiClient;
    CsPrefrences csPrefrences;

    @Override
    public void onCreate(Bundle icicle) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(icicle);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        context = SplashActivity.this;
        csPrefrences = new CsPrefrences(context);
        if (Build.VERSION.SDK_INT <= 22) {
            if (!Utilss.isMyServiceRunning(LocationService.class, context))
                context.startService(new Intent(context, LocationService.class));
            handleEvent();

        } else {
            chkPermissions();
        }
    }


    private void chkPermissions() {
        if (Utilss.checkLocationPermission(context)) {
            if (!Utilss.isMyServiceRunning(LocationService.class, context))
                context.startService(new Intent(context, LocationService.class));
            handleEvent();

        } else {
            getLocations();
        }

    }

    private void handleEvent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (csPrefrences.getSharedPref("driverID").equalsIgnoreCase("")) {
                        Intent loginIntent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(loginIntent);
                        finish();
                        ;
                    } else {
                        Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(loginIntent);
                        finish();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    //if GPS location off then it on GPS loc else go to next Activity
    private void getLocations() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.e("if called", "");
            enableLoc();
        } else {
            askPermissions();
            Log.e("if called", "");
        }
    }

    public void askPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {//Can add more as per requirement
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
        } else {
            if (!Utilss.isMyServiceRunning(LocationService.class, context))
                context.startService(new Intent(context, LocationService.class));
            handleEvent();

        }
    }

    private void enableLoc() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(ConnectionResult connectionResult) {
                    Log.e("Location error ", "" + connectionResult.getErrorCode());
                }
            }).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);
            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult((Activity) context, REQUEST_LOACTION);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        switch (requestCode) {
            case 99:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (!Utilss.isMyServiceRunning(LocationService.class, context))
                        context.startService(new Intent(context, LocationService.class));
                    handleEvent();
                    //  Toast.makeText(context,"allow Afetr in 99 Case",Toast.LENGTH_LONG).show();
                    Log.d("locationCheck", "allow Afetr in 99 Case");
                    mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                }
                break;
            case 123:
                Log.d("locationCheck", "");
                //  Toast.makeText(context,"Deny Afetr in 123 Case",Toast.LENGTH_LONG).show();
                //askPermissions();
                // enableLoc();
                //if(!Utils.isMyServiceRunning(LocationService.class,context))
                //   context.startService(new Intent(context,LocationService.class));
                // handleEvent();
                //  mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                break;

            case REQUEST_LOACTION:
                if (!Utilss.isMyServiceRunning(LocationService.class, context))
                    context.startService(new Intent(context, LocationService.class));
                handleEvent();

                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
