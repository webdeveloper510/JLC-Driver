package jlc.driver.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import jlc.driver.Model.DriverStatus;
import jlc.driver.R;
import jlc.driver.Retrofit.APIClient;
import jlc.driver.Retrofit.APIInterface;
import jlc.driver.Retrofit.Appcontroller;
import jlc.driver.Utils.Constants;
import jlc.driver.Utils.CsPrefrences;
import jlc.driver.Utils.Event;
import jlc.driver.Utils.LocationService;
import jlc.driver.Utils.MainMenu;
import jlc.driver.Utils.Utilss;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
    DrawerLayout drawer;
    Context context;
    Dialog progressDialog;
    APIInterface apiInterface;
    Bundle bundle;
    TextView txDriverRating, txDriverName;
    Double lattitude, longitude;
    boolean getCurrentLocation = false;
    GoogleMap map;
    Switch mSwitch;
    CsPrefrences prefrences;
    CircleImageView driverProfilePic;
    String checkStatus = "", driverName, driverImage, driverContact;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        prefrences = new CsPrefrences(context);
        getDriverProfileInfo();
        initNavigationDrawer();
        inflateMainView();
        setToolbarDrawer();
        hitDriverStatusApi("1", prefrences.getSharedPref("driverID"));
        getSupportMapFragment();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();
    }

    private void getDriverProfileInfo() {
        driverName = prefrences.getSharedPref("driverName");
        driverImage = prefrences.getSharedPref("driverImage");
        driverContact = prefrences.getSharedPref("driverMobile");

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        MainMenu mai = new MainMenu(this, item);
        return true;
    }

    private void getSupportMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setToolbarDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("JLC");
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) {
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) {
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }

            }
        };

        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

 /*   private void getIntentData() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("comesFrom")) {
                checkStatus = bundle.getString("comesFrom");
                if(checkStatus.equalsIgnoreCase("CashCollect"))
                {
                    hitDriverStatusApi("1", prefrences.getSharedPref("driverID"));
                }

            }
        }
    }*/

    //***Inflate contentHome View in MainActivity
    private void inflateMainView() {
        LinearLayout dynamicContent = (LinearLayout) findViewById(R.id.drawer_layout_main);
        View wizardView = getLayoutInflater().inflate(R.layout.content_home, dynamicContent, false);
        dynamicContent.addView(wizardView);
    }

    //End Inflate
    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(context);
    }

    @Override
    protected void onStart() {
        EventBus.getDefault().register(context);
        super.onStart();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.jlc_toggle, menu);
        View count = menu.findItem(R.id.badge).getActionView();
        final Switch mSwitch = count.findViewById(R.id.simpleSwitch);
        this.mSwitch = mSwitch;
        int[][] states = new int[][]{
                new int[]{-android.R.attr.state_checked},
                new int[]{android.R.attr.state_checked},
        };
        int[] thumbColors = new int[]{
                Color.RED,
                Color.GREEN,
        };
        int[] trackColors = new int[]{
                Color.RED,
                Color.GREEN,
        };
        DrawableCompat.setTintList(DrawableCompat.wrap(mSwitch.getThumbDrawable()), new ColorStateList(states, thumbColors));
        DrawableCompat.setTintList(DrawableCompat.wrap(mSwitch.getTrackDrawable()), new ColorStateList(states, trackColors));
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mSwitch.isChecked()) {
                    hitDriverStatusApi("1", prefrences.getSharedPref("driverID"));
                } else if (!mSwitch.isChecked()) {
                    hitDriverStatusApi("0", prefrences.getSharedPref("driverID"));
                }
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.badge) {
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event event) {
        switch (event.getKey()) {
            case Constants.LOCATION_GET:
                //  Log.d("chkData", lattitude + "current Lat from service in Appcontroller" + longitude + " current Lng from service Appcontroller");
                lattitude = event.getLattitude();
                longitude = event.getLongitude();
                if (lattitude != null && longitude != null) {
                    if (!getCurrentLocation) {
                        if (lattitude != null && longitude != null) {
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lattitude, longitude), 14));
                            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            map.setMyLocationEnabled(true);
                        }
                    }
                    getCurrentLocation = true;
                }
                Log.d("AppLattitudeLongitude", lattitude + "application" + " " + longitude + "application");

                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        if (Appcontroller.lattitude != null && Appcontroller.longitude != null) {
            lattitude = Appcontroller.lattitude;
            longitude = Appcontroller.longitude;
            if (lattitude != null && longitude != null) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lattitude, longitude), 14));
                googleMap.setMyLocationEnabled(true);
            }
        }
    }

    private void hitDriverStatusApi(String status, String driverID) {
        if (progressDialog != null) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
        progressDialog = Utilss.progressDialogs(context);
        progressDialog.show();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<DriverStatus> call = apiInterface.driverStatusApi(driverID, status, Appcontroller.lattitude + "", Appcontroller.longitude + "");
        call.enqueue(new Callback<DriverStatus>() {
            @Override
            public void onResponse(Call<DriverStatus> call, Response<DriverStatus> response) {
                if (progressDialog != null) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    if (response.body().getData().equalsIgnoreCase("online")) {
                        Utilss.makeSnackBarSuccessfully(context, mSwitch, "Driver is Online");
                        /*Toast.makeText(context,"Online",Toast.LENGTH_SHORT).show();*/
                        mSwitch.setChecked(true);
                    } else if (response.body().getData().equalsIgnoreCase("offline")) {
                        /*Toast.makeText(context,"Offline",Toast.LENGTH_SHORT).show();*/
                        Utilss.validationMakeSnackBar(context, mSwitch, "Driver is Offline");
                        mSwitch.setChecked(false);
                    }
                }

            }

            @Override
            public void onFailure(Call<DriverStatus> call, Throwable t) {
                if (progressDialog != null) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            }

        });
    }

    public void initNavigationDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        driverProfilePic = (CircleImageView) navigationView.findViewById(R.id.nav_image);
        View header = navigationView.getHeaderView(0);
        txDriverName = (TextView) header.findViewById(R.id.customerName);
        LinearLayout linearLayout = (LinearLayout) header.findViewById(R.id.ll_layout1);
        driverProfilePic = (CircleImageView) header.findViewById(R.id.nav_image);
        txDriverRating = (TextView) header.findViewById(R.id.customerRating);
        txDriverRating.setText(driverContact);
        txDriverName.setText(driverName);
        if (!driverImage.equalsIgnoreCase("")) {
            Picasso.with(context).load(driverImage).placeholder(R.mipmap.user).into(driverProfilePic);
        }
        driverProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, EditProfileActivity.class));
            }
        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, EditProfileActivity.class));
            }
        });

    }
}
