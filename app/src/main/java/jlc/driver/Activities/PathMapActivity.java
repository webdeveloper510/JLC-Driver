package jlc.driver.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.shitij.goyal.slidebutton.SwipeButton;
import com.shitij.goyal.slidebutton.Utils;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import jlc.driver.Model.ModelCancelRide;
import jlc.driver.Model.ModelCompleteTrip;
import jlc.driver.Model.ModelDriverArrived;
import jlc.driver.Model.ModelTripStart;
import jlc.driver.Model.ModelUpdateLocation;
import jlc.driver.R;
import jlc.driver.Retrofit.APIClient;
import jlc.driver.Retrofit.APIInterface;
import jlc.driver.Retrofit.Appcontroller;
import jlc.driver.Utils.Constants;
import jlc.driver.Utils.CsPrefrences;
import jlc.driver.Utils.CustomObject;
import jlc.driver.Utils.Event;
import jlc.driver.Utils.GoogleApis;
import jlc.driver.Utils.Utilss;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PathMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    Dialog progressDialog;
    private static final String TAG = Utilss.class.getSimpleName();
    LatLng currentlatlng, customer_source, customer_destination, lastlat, startPosition, endPosition, roadLatlng;
    CsPrefrences prefrences;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.txtdestaddress)
    TextView txtdestaddress;
    @BindView(R.id.navigate)
    ImageView navigate;
    @BindView(R.id.img_Loc)
    ImageView imgLoc;
    @BindView(R.id.swipe_startTrip)
    SwipeButton swipeStartTrip;
    @BindView(R.id.swipe_completeTrip)
    SwipeButton swipeCompleteTrip;
    @BindView(R.id.tv_beginTrip)
    TextView tvBeginTrip;
    @BindView(R.id.activity_path_map)
    RelativeLayout activityPathMap;
    GoogleMap map;
    CustomObject myData;
    Context context;
    LatLng startLatLng, destLatLng;
    Double sourceLatt, sourceLong, destLatt, destLong, lattitude, longitude;
    String pickupLocationName, dropLocationName, currentAddress, customerName, customerImage, customer_id, rideId, price, paymentType, navigateUri;
    Polyline greyPolyLine;
    PolylineOptions polylineOptions;
    ArrayList<LatLng> polyLineList;
    Marker stopMarker, startMarker;
    MarkerOptions stopOptions = new MarkerOptions();
    MarkerOptions carMarkerOptions = new MarkerOptions();
    boolean getCurrentLocation = false;
    Bundle bundle;
    int state = 1;
    @BindView(R.id.customerProfile)
    CircleImageView customerProfile;
    @BindView(R.id.txCustomerName)
    TextView txCustomerName;
    @BindView(R.id.txPaymentType)
    TextView txPaymentType;
    @BindView(R.id.llCustomerInfo)
    LinearLayout llCustomerInfo;
    @BindView(R.id.bottomLayout)
    RelativeLayout bottomLayout;
    APIInterface apiInterface;
    @BindView(R.id.txCancelRide)
    TextView txCancelRide;
    private boolean isFirstPosition = true;
    //state=1 for before arrived ,state 2= for arrived ,state=3 for begin trip ,state=4 for complete trip

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_map);
        ButterKnife.bind(this);
        context = this;
        getSupportMapFragment();
        getIntentData();
        swipeButtonListners();

    }

    private void swipeButtonListners() {
        swipeStartTrip.addOnSwipeCallback(new SwipeButton.Swipe() {
            @Override
            public void onButtonPress() {
                /*Toast.makeText(PathMapActivity.this, "Pressed!", Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onSwipeCancel() {

            }

            @Override
            public void onSwipeConfirm() {
                hitStartTripApi();
                // swipeStartTrip.setText("Trip Begin");
                //   Toast.makeText(PathMapActivity.this, "confirmd!", Toast.LENGTH_LONG).show();
            }
        });
        swipeCompleteTrip.addOnSwipeCallback(new SwipeButton.Swipe() {
            @Override
            public void onButtonPress() {
                /*Toast.makeText(PathMapActivity.this, "Pressed!", Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onSwipeCancel() {

            }

            @Override
            public void onSwipeConfirm() {
                hitStopTripApi();
                //   Toast.makeText(PathMapActivity.this, "confirmd!", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void getSupportMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(context, Locale.getDefault());
            try {
                if (lattitude != null && longitude != null)
                    addresses = geocoder.getFromLocation(lattitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }
            currentAddress = addresses.get(0).getAddressLine(0);
        }
        if (GoogleApis.polyLineList != null) {
            polyLineList = (ArrayList<LatLng>) GoogleApis.polyLineList;
            staticPolyLine();
        }

    }

    protected Marker createMarker(String locationName, LatLng latLng, boolean pickup) {
        int img;
        if (pickup) {
            img = R.mipmap.start_new_image;
        } else {
            img = R.mipmap.stop_marker_icon;
        }
        Marker marker = map.addMarker(new MarkerOptions().position(latLng).anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(context, img, ""))).title(locationName));
        myData = new CustomObject(locationName);
        marker.setTag(myData);

        return marker;
    }

    public static Bitmap createCustomMarker(Context context, @DrawableRes int resource, String _name) {
        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);
        CircleImageView markerImage = (CircleImageView) marker.findViewById(R.id.user_dp);
        markerImage.setImageResource(resource);
        TextView txt_name = (TextView) marker.findViewById(R.id.name);
        txt_name.setText(_name);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);
        return bitmap;
    }

    void staticPolyLine() {
        if (greyPolyLine != null) {
            greyPolyLine.remove();
        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : polyLineList) {
            builder.include(latLng);
        }
        polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.BLACK);
        polylineOptions.width(10);
        polylineOptions.startCap(new SquareCap());
        polylineOptions.endCap(new SquareCap());
        polylineOptions.jointType(JointType.ROUND);
        polylineOptions.addAll(polyLineList);
        greyPolyLine = map.addPolyline(polylineOptions);
        addStartMarker();
        addStopMarker();
    }

    private void addStartMarker() {
        if (startMarker != null)
            startMarker.remove();
        if (state == 1 || state == 2) {
            navigateUri = "http://maps.google.com/maps?daddr=" + sourceLatt + "," + sourceLong + " (" + "Pickup location" + ")";
            startLatLng = new LatLng(lattitude, longitude);
            carMarkerOptions.position(startLatLng);
            txtdestaddress.setText(pickupLocationName);
            Log.d("sourceLattitude", lattitude + "start Lattitude" + longitude + " start Longitude");
            startMarker = createMarker(currentAddress, startLatLng, true);

        } else if (state == 3 || state == 4) {
            navigateUri = "http://maps.google.com/maps?daddr=" + destLatt + "," + destLong + " (" + "Pickup location" + ")";
            startLatLng = new LatLng(sourceLatt, sourceLong);
            carMarkerOptions.position(startLatLng);
            txtdestaddress.setText(dropLocationName);
            startMarker = createMarker(pickupLocationName, startLatLng, true);
        }
    }

    private void addStopMarker() {
        if (stopMarker != null)
            stopMarker.remove();
        if (state == 1 || state == 2) {
            destLatLng = new LatLng(sourceLatt, sourceLong);
            Log.d("sourceLattitude", sourceLatt + "Source Lattitude" + sourceLong + " Source Longitude");
            stopOptions.position(destLatLng);
            stopMarker = createMarker(pickupLocationName, destLatLng, false);
        } else if (state == 3 || state == 4) {
            destLatLng = new LatLng(destLatt, destLong);
            stopOptions.position(destLatLng);
            stopMarker = createMarker(dropLocationName, destLatLng, false);
        }
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event event) {
        switch (event.getKey()) {
            case Constants.LOCATION_GET:
                //  Log.d("chkData", lattitude + "current Lat from service in Appcontroller" + longitude + " current Lng from service Appcontroller");
                lattitude = event.getLattitude();
                longitude = event.getLongitude();
                if (lattitude != null && longitude != null) {
                    hitUpdateDriverLocation(String.valueOf(lattitude), String.valueOf(longitude));
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

                if (state == 1 || state == 2) {

                    GoogleApis.hitDirectionApi(Utilss.directionApi(context, lattitude, longitude, sourceLatt, sourceLong));
                } else if (state == 3 || state == 4) {

                    GoogleApis.hitDirectionApi(Utilss.directionApi(context, lattitude, longitude, destLatt, destLong));
                }

                moveCarAnimation(lattitude, longitude);
                Log.d("AppLattitudeLongitude", lattitude + "application" + " " + longitude + "application");
                break;

            case Constants.DRAW_POLYLINE:
                polyLineList = (ArrayList<LatLng>) GoogleApis.polyLineList;
                if (polyLineList != null) {
                    staticPolyLine();
                } else {
                    Toast.makeText(context, " Google Direction Api Response is null", Toast.LENGTH_LONG).show();
                }
                break;
            case Constants.CANCEL_RIDE:
                Toast.makeText(context, "Customer Cancel the Ride", Toast.LENGTH_LONG).show();
                startActivity(new Intent(context, MainActivity.class));
                finish();
                break;

        }
    }

    private void getIntentData() {
        bundle = getIntent().getExtras();
        prefrences = new CsPrefrences(context);
        if (bundle != null) {
            if (bundle.containsKey("sourceLattitude")) {
                sourceLatt = bundle.getDouble("sourceLattitude");
                sourceLong = bundle.getDouble("sourceLongitude");
                destLatt = bundle.getDouble("destLattitude");
                destLong = bundle.getDouble("destLongitude");
                customerName = bundle.getString("customerName");
                customer_id = bundle.getString("customer_id");
                customerImage = bundle.getString("customerImage");
                rideId = bundle.getString("rideId");
                price = bundle.getString("price");
                pickupLocationName = bundle.getString("sourceName");
                dropLocationName = bundle.getString("destination");
                paymentType = bundle.getString("payment_mode");
            }
        }

        txCustomerName.setText(customerName);
        txPaymentType.setText(paymentType);
        if (!customerImage.equalsIgnoreCase("")) {
            Picasso.with(context).load(customerImage.replaceAll("\\s+", "")).placeholder(R.mipmap.user).into(customerProfile);
        }

    }

    @OnClick(R.id.navigate)
    public void onViewClicked() {

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(navigateUri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    @OnClick({R.id.swipe_startTrip, R.id.swipe_completeTrip, R.id.tv_beginTrip, R.id.txCancelRide})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_beginTrip:
                hitDriverArrivedApi();
                break;
            case R.id.txCancelRide:
                hitCancelRideApi();
                break;
        }
    }

    //*** Hit api when driver Arrived
    private void hitDriverArrivedApi() {
        if (progressDialog != null) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
        progressDialog = Utilss.progressDialogs(context);
        progressDialog.show();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ModelDriverArrived> call = apiInterface.hitDriverArrivedApi(prefrences.getSharedPref("driverID"), customer_id, rideId);
        call.enqueue(new Callback<ModelDriverArrived>() {
            @Override
            public void onResponse(Call<ModelDriverArrived> call, Response<ModelDriverArrived> response) {
                if (progressDialog != null) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    Toast.makeText(context, "Arrived to Customer Location", Toast.LENGTH_SHORT).show();
                    swipeStartTrip.setVisibility(View.VISIBLE);
                    state = 2;
                    txCancelRide.setVisibility(View.GONE);
                    tvBeginTrip.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ModelDriverArrived> call, Throwable t) {
                if (progressDialog != null) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            }

        });
    }
    //***End

    //*** Hit api when driver Arrived
    private void hitStartTripApi() {
        if (progressDialog != null) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
        progressDialog = Utilss.progressDialogs(context);
        progressDialog.show();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ModelTripStart> call = apiInterface.hitStartTripApi(customer_id, prefrences.getSharedPref("driverID"), rideId);
        call.enqueue(new Callback<ModelTripStart>() {
            @Override
            public void onResponse(Call<ModelTripStart> call, Response<ModelTripStart> response) {
                if (progressDialog != null) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    Toast.makeText(context, "Trip Started", Toast.LENGTH_SHORT).show();
                    swipeStartTrip.setVisibility(View.GONE);
                    state = 3;
                    swipeCompleteTrip.setVisibility(View.VISIBLE);

                    GoogleApis.hitDirectionApi(Utilss.directionApi(context, sourceLatt, sourceLong, destLatt, destLong));

                    tvBeginTrip.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ModelTripStart> call, Throwable t) {
                if (progressDialog != null) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            }

        });
    }
    //***End

    //*** Hit api when driver Arrived
    private void hitStopTripApi() {
        if (progressDialog != null) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
        progressDialog = Utilss.progressDialogs(context);
        progressDialog.show();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ModelCompleteTrip> call = apiInterface.hitCompleteTrip(customer_id, prefrences.getSharedPref("driverID"), rideId);
        call.enqueue(new Callback<ModelCompleteTrip>() {
            @Override
            public void onResponse(Call<ModelCompleteTrip> call, Response<ModelCompleteTrip> response) {
                if (progressDialog != null) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    Toast.makeText(context, "Trip Completed", Toast.LENGTH_SHORT).show();
                    swipeStartTrip.setVisibility(View.GONE);
                    state = 4;
                    swipeCompleteTrip.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(context, CashCollect.class);
                    intent.putExtra("rideId", rideId);
                    intent.putExtra("customerID", customer_id);
                    startActivity(intent);
                    finish();

                }
            }

            @Override
            public void onFailure(Call<ModelCompleteTrip> call, Throwable t) {
                if (progressDialog != null) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            }

        });
    }

    //*** Hit api when driver Cancel Ride
    private void hitCancelRideApi() {
        if (progressDialog != null) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
        progressDialog = Utilss.progressDialogs(context);
        progressDialog.show();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ModelCancelRide> call = apiInterface.hitCancelRideApi(prefrences.getSharedPref("driverID"), customer_id, rideId);
        call.enqueue(new Callback<ModelCancelRide>() {
            @Override
            public void onResponse(Call<ModelCancelRide> call, Response<ModelCancelRide> response) {
                if (progressDialog != null) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    startActivity(new Intent(context, MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ModelCancelRide> call, Throwable t) {
                if (progressDialog != null) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            }

        });
    }
    //***End

    //*** Hit api when driver Arrived
    private void hitUpdateDriverLocation(String lattitude, String longitude) {

        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ModelUpdateLocation> call = apiInterface.hitUpdateLocation(prefrences.getSharedPref("driverID"), lattitude, longitude);
        call.enqueue(new Callback<ModelUpdateLocation>() {
            @Override
            public void onResponse(Call<ModelUpdateLocation> call, Response<ModelUpdateLocation> response) {

                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    Log.d("updateLocationApiRes", "Location updated");
                }
            }

            @Override
            public void onFailure(Call<ModelUpdateLocation> call, Throwable t) {
                Log.d("updateLocationApiRes", "Location failure");

            }

        });
    }
    //***End

    private void moveCarAnimation(double lati, double lngi) {
        final LatLng latLng = new LatLng(lati, lngi);
        if (isFirstPosition) {
            startPosition = latLng;
            isFirstPosition = false;
        } else {
            endPosition = new LatLng(lati, lngi);
            Log.d(TAG, startPosition.latitude + "--" + endPosition.latitude + "--Check --" + startPosition.longitude + "--" + endPosition.longitude);
            if ((startPosition.latitude != endPosition.latitude) || (startPosition.longitude != endPosition.longitude)) {
                Log.e(TAG, "NOT SAME");
                Utilss.getInstance().startBikeAnimation(map, startMarker, startMarker.getPosition(), endPosition);
            } else {
                Log.e(TAG, "SAMME");
            }
        }
    }
}
