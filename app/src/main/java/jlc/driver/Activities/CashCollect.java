package jlc.driver.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jlc.driver.Model.ModelCancelRide;
import jlc.driver.Model.ModelCashCollected;
import jlc.driver.Model.ModelGetAmount;
import jlc.driver.R;
import jlc.driver.Retrofit.APIClient;
import jlc.driver.Retrofit.APIInterface;
import jlc.driver.Utils.Utilss;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CashCollect extends AppCompatActivity {
    Dialog progressDialog;
    Context context;
    APIInterface apiInterface;
    String amount, customerId, rideId;
    Bundle bundle;
    @BindView(R.id.backCash)
    ImageView backCash;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.txtbasefare)
    TextView txtbasefare;
    @BindView(R.id.collectcash)
    TextView collectcash;
    @BindView(R.id.driver_cash_collect)
    LinearLayout driverCashCollect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_collect);
        ButterKnife.bind(this);
        context = this;
        getIntentData();
        hitGetAmountApi();

    }

    //*** Hit api when driver Cancel Ride
    private void hitGetAmountApi() {
        if (progressDialog != null) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
        progressDialog = Utilss.progressDialogs(context);
        progressDialog.show();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ModelGetAmount> call = apiInterface.hitGetAmountApi("", "");
        call.enqueue(new Callback<ModelGetAmount>() {
            @Override
            public void onResponse(Call<ModelGetAmount> call, Response<ModelGetAmount> response) {
                if (progressDialog != null) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    txtbasefare.setText("$ " + response.body().getData().getAmount());
                }
            }

            @Override
            public void onFailure(Call<ModelGetAmount> call, Throwable t) {
                if (progressDialog != null) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            }

        });
    }

    //***End
    private void getIntentData() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("rideId")) {
                customerId = bundle.getString("customerID");
                rideId = bundle.getString("rideId");
            }
        }
    }

    //*** Hit api when driver Cancel Ride
    private void hitCollectAmount() {
        if (progressDialog != null) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
        progressDialog = Utilss.progressDialogs(context);
        progressDialog.show();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ModelCashCollected> call = apiInterface.hitCashCollectApi(rideId);
        call.enqueue(new Callback<ModelCashCollected>() {
            @Override
            public void onResponse(Call<ModelCashCollected> call, Response<ModelCashCollected> response) {
                if (progressDialog != null) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    Toast.makeText(context, "Payment Done", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("comesFrom", "CashCollect");
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ModelCashCollected> call, Throwable t) {
                if (progressDialog != null) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            }

        });
    }

    @OnClick(R.id.collectcash)
    public void onViewClicked() {
        hitCollectAmount();
    }
}
