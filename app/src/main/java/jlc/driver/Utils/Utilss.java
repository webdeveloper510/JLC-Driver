package jlc.driver.Utils;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import jlc.driver.R;

import static jlc.driver.Retrofit.Appcontroller.TAG;


public class Utilss {
    private float v;
    private LatLng startPosition;
    public static float bearing;
    private static Utilss modelManager;
    private static final long ANIMATION_TIME_PER_ROUTE = 2000;
    private double lat, lng;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static Dialog progressDialogs(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        ((CircleProgressBar) dialog.findViewById(R.id.progressBar)).setColorSchemeColors(R.color.blue_btn_bg_color);
        return dialog;
    }
    public static Dialog progressDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        ((CircleProgressBar) dialog.findViewById(R.id.progressBar)).setColorSchemeColors(R.color.yellow);
       // dialog.setColorSchemeColors(android.R.color.holo_red_light);
        return dialog;
    }


    public static void makeSnackBar(Context context, View view, String message) {
        Snackbar snackbar;
        snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.grey));

        snackbar.show();
    }
    public static boolean checkLocationPermission(final Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(context).setTitle("Allow location").setMessage("Location get").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                    }
                }).create().show();

            } else {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    public static void makeSnackBarSuccessfully(Context context, View view, String message) {
        Snackbar snackbar;
        snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGreen));
        snackbar.show();
    }

    public static void validationMakeSnackBar(Context context, View view, String message) {
        Snackbar snackbar;
        snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorred));

        snackbar.show();
    }

    public static String directionApi(Context context, double lat, double lng, double destination_lat, double dest_long) {

        String parms = "https://maps.googleapis.com/maps/api/directions/json?" +
                "mode=driving&"
                + "transit_routing_preference=less_driving&"
                + "origin=" + String.valueOf(lat) + "," + String.valueOf(lng) + "&"
                + "destination=" + String.valueOf(destination_lat) + "," + String.valueOf(dest_long) + "&"
                + "key=" + context.getResources().getString(R.string.google_maps_key);
        Log.d("chkDirectionApi", parms);
        // String parms = Config.nearestroadurl + latlng + "&key=" + "AIzaSyD0f9QMvoH2EBf0qEyHO-afhPX3yluriu4";
        return parms;
    }
    public void startBikeAnimation(final GoogleMap googleMap, final Marker carMarker, final LatLng start, final LatLng end) {

        Log.i(TAG, "startBikeAnimation called...");

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(ANIMATION_TIME_PER_ROUTE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                //LogMe.i(TAG, "Car Animation Started...");
                v = valueAnimator.getAnimatedFraction();
                lng = v * end.longitude + (1 - v)
                        * start.longitude;
                lat = v * end.latitude + (1 - v)
                        * start.latitude;

                LatLng newPos = new LatLng(lat, lng);
                carMarker.setPosition(newPos);
                carMarker.setAnchor(0.5f, 0.45f);
                carMarker.setRotation(getHeadingForDirection(start, end));
                bearing = getHeadingForDirection(start, end);
                // todo : Shihab > i can delay here
//                googleMap.moveCamera(CameraUpdateFactory
//                        .newCameraPosition
//                                (new CameraPosition.Builder()
//                                        .target(newPos)
//                                        .zoom(15.5f)
//                                        .build()));

                startPosition = carMarker.getPosition();

            }

        });
        valueAnimator.start();
    }

    public static float getHeadingForDirection(LatLng begin, LatLng end) {

        float beginLat = (float) Math.toRadians(begin.latitude);
        float beginLong = (float) Math.toRadians(begin.longitude);
        float endLat = (float) Math.toRadians(end.latitude);
        float endLong = (float) Math.toRadians(end.longitude);
        return (float) Math.toDegrees(Math.atan2(Math.sin(endLong - beginLong) * Math.cos(endLat),
                Math.cos(beginLat) * Math.sin(endLat) - Math.sin(beginLat) * Math.cos(endLat) *
                        Math.cos(endLong - beginLong)));


    }
    public static Utilss getInstance() {
        if (modelManager == null)
            return modelManager = new Utilss();
        else
            return modelManager;
    }
}
