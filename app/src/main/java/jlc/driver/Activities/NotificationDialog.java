package jlc.driver.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.nio.file.Path;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.iwgang.countdownview.CountdownView;
import de.hdodenhof.circleimageview.CircleImageView;
import jlc.driver.Model.ModelDriverAccept;
import jlc.driver.R;
import jlc.driver.Retrofit.APIClient;
import jlc.driver.Retrofit.APIInterface;
import jlc.driver.Retrofit.Appcontroller;
import jlc.driver.Utils.Constants;
import jlc.driver.Utils.CsPrefrences;
import jlc.driver.Utils.Event;
import jlc.driver.Utils.GoogleApis;
import jlc.driver.Utils.Utilss;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationDialog extends AppCompatActivity implements CountdownView.OnCountdownEndListener {
    boolean isActiveDialog = false;
    String price, customerName, customer_id, customerImage, distance, rideId, paymentType, destinationName, driverID;
    Bundle bundle;
    String pickupLocation;
    Double sourceLattitude, sourceLongitude, destLongitude, destLattitude;
    @BindView(R.id.imagevieww)
    CircleImageView imagevieww;
    @BindView(R.id.txtcustomerName)
    TextView txtcustomerName;
    @BindView(R.id.txtrequestid)
    TextView txtrequestid;
    @BindView(R.id.view_)
    View view;
    @BindView(R.id.image2)
    ImageView image2;
    @BindView(R.id.txtdistance)
    TextView txtdistance;
    @BindView(R.id.txtdestvalue)
    TextView txtdestvalue;
    @BindView(R.id.count_down)
    CountdownView countDown;
    @BindView(R.id.txt_tym_value)
    TextView txtTymValue;
    @BindView(R.id.image_price)
    ImageView imagePrice;
    @BindView(R.id.txtprice)
    TextView txtprice;
    @BindView(R.id.txtpricevalue)
    TextView txtpricevalue;
    @BindView(R.id.pricelayout)
    RelativeLayout pricelayout;
    @BindView(R.id.txtpickaddress)
    TextView txtpickaddress;
    @BindView(R.id.txtdesheading)
    TextView txtdesheading;
    @BindView(R.id.txtsource)
    TextView txtsource;
    @BindView(R.id.txtsourceHeading)
    TextView txtsourceHeading;
    @BindView(R.id.img_ride)
    ImageView imgRide;
    @BindView(R.id.txtRideType)
    TextView txtRideType;
    @BindView(R.id.img_pool)
    ImageView imgPool;
    @BindView(R.id.txtRequestType)
    TextView txtRequestType;
    @BindView(R.id.btnaccept)
    TextView btnaccept;
    @BindView(R.id.btReject)
    TextView btReject;
    Context context;
    Double currentLattitude, currentLongitude;
    ArrayList<LatLng> polyLineList;
    Dialog progressDialog;
    APIInterface apiInterface;
    CsPrefrences prefrences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_dialog);
        ButterKnife.bind(this);
        context = this;
        this.setFinishOnTouchOutside(false);
        countDown.setOnCountdownEndListener(this);
        isActiveDialog = true;
        getIntentData();
        startTimer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(context);
        isActiveDialog = false;
    }

    private void getIntentData() {
        bundle = getIntent().getExtras();
        prefrences = new CsPrefrences(context);
        if (bundle != null) {
            if (Appcontroller.lattitude != null && Appcontroller.longitude != null) {
                currentLattitude = Appcontroller.lattitude;
                currentLongitude = Appcontroller.longitude;
            }
            if (bundle.containsKey("sourceLattitude")) {
                sourceLattitude = bundle.getDouble("sourceLattitude");
                sourceLongitude = bundle.getDouble("sourceLongitude");
                destLattitude = bundle.getDouble("destLattitude");
                destLongitude = bundle.getDouble("destLongitude");
                customerName = bundle.getString("customerName");
                customer_id = bundle.getString("customer_id");
                customerImage = bundle.getString("customerImage");
                rideId = bundle.getString("rideId");
                distance = bundle.getString("distance");
                destinationName = bundle.getString("destination");
                price = bundle.getString("price");
                pickupLocation = bundle.getString("sourceName");
                paymentType = bundle.getString("payment_mode");
            }
        }
        txtdistance.setText(distance);
        txtpickaddress.setText(pickupLocation);
        txtprice.setText(price);
        txtcustomerName.setText(customerName);
        if (!customerImage.equalsIgnoreCase("")) {
            Picasso.with(context).load(customerImage.replaceAll("\\s+", "")).placeholder(R.mipmap.user).into(imagevieww);
        }
        driverID = prefrences.getSharedPref("driverID");

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event event) {
        switch (event.getKey()) {

            case Constants.LOCATION_GET:
                currentLattitude = event.getLattitude();
                currentLongitude = event.getLongitude();
                break;

            case Constants.DRAW_POLYLINE:
                polyLineList = (ArrayList<LatLng>) GoogleApis.polyLineList;
                Intent intent = new Intent(context, PathMapActivity.class);
                intent.putExtra("sourceLattitude", sourceLattitude);
                intent.putExtra("sourceLongitude", sourceLongitude);
                intent.putExtra("destLattitude", destLattitude);
                intent.putExtra("destLongitude", destLongitude);
                intent.putExtra("customer_id", customer_id);
                intent.putExtra("price", price);
                intent.putExtra("destination", destinationName);
                intent.putExtra("sourceName", pickupLocation);
                intent.putExtra("rideId", rideId);
                intent.putExtra("customerName", customerName);
                intent.putExtra("payment_mode", paymentType);
                intent.putExtra("customerImage", customerImage);
                Log.d("AppLattitudeLongitude", currentLattitude + "application" + " " + currentLongitude + "application");
                startActivity(intent);
                finish();
                break;
        }
    }


    @OnClick({R.id.btnaccept, R.id.btReject})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnaccept:
                countDown.stop();
                hitAcceptApi();

                break;
            case R.id.btReject:
                finish();
                break;
        }
    }

    private void startTimer() {
        countDown.start(10000);
    }

    @Override
    public void onEnd(CountdownView cv) {
        finish();
    }

    @Override
    protected void onStart() {
        EventBus.getDefault().register(context);
        super.onStart();
    }

    private void hitAcceptApi() {
        if (progressDialog != null) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
        progressDialog = Utilss.progressDialogs(context);
        progressDialog.show();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ModelDriverAccept> call = apiInterface.hitDriverAcceptApi(customer_id, driverID, "1", rideId);
        call.enqueue(new Callback<ModelDriverAccept>() {
            @Override
            public void onResponse(Call<ModelDriverAccept> call, Response<ModelDriverAccept> response) {
                if (progressDialog != null) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }
                if (response.body().getStatus() != null) {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        GoogleApis.hitDirectionApi(Utilss.directionApi(context, currentLattitude, currentLongitude, sourceLattitude, sourceLongitude));

                    }
                }
            }

            @Override
            public void onFailure(Call<ModelDriverAccept> call, Throwable t) {
                if (progressDialog != null) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }
}
