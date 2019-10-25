package jlc.driver.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jlc.driver.Model.Login;
import jlc.driver.Model.Test;
import jlc.driver.R;
import jlc.driver.Retrofit.APIClient;
import jlc.driver.Retrofit.APIInterface;
import jlc.driver.Utils.ConnectionDetector;
import jlc.driver.Utils.CsPrefrences;
import jlc.driver.Utils.SessionManager;
import jlc.driver.Utils.Utilss;
import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {

    AlertDialog alertDialogs;
    Button btnCancel, btnOk, btnLogin, btnRegister;
    TextView tvforgotPassword;
    Context context;
    Dialog dialog;
    APIInterface apiInterface;
    SessionManager session;
    String stremail, strPass;
    EditText edEmail, edPassord;
    CheckBox chRemember;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    LinearLayout background;
    CsPrefrences prefrences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_login);
        prefrences = new CsPrefrences(context);
        tvforgotPassword = findViewById(R.id.forgetPassword);
        btnLogin = findViewById(R.id.bt_login);
        btnRegister = findViewById(R.id.btn_Register);
        chRemember = (CheckBox) findViewById(R.id.saveLoginCheckBox);
        edEmail = (EditText) findViewById(R.id.et_Email);
        edPassord = (EditText) findViewById(R.id.et_Password);


        session = new SessionManager(getApplicationContext());
        background = findViewById(R.id.background);


        //  hitText();
        if (session.isLoggedIn()) {
            // User is already logged in.Take him to main activity

            try {

                Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(loginIntent);
                LoginActivity.this.finish();


            } catch (Exception ex) {

                ex.printStackTrace();
            }
        } else {


        }

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);

        if (saveLogin == true) {

            edEmail.setText(loginPreferences.getString("username", ""));
            edPassord.setText(loginPreferences.getString("password", ""));
            chRemember.setChecked(true);
        }

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);

            }
        });


        tvforgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ForgotDialog(context);
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stremail = edEmail.getText().toString();
                strPass = edPassord.getText().toString();


                if (TextUtils.isEmpty(stremail)) {

                    edEmail.requestFocus();
                    edEmail.setError("Enter Email");
                    return;
                }

                if (!isValidEmail(stremail)) {

                    edEmail.requestFocus();
                    edEmail.setError("Enter Valid Email");
                    return;
                }
                if (TextUtils.isEmpty(strPass)) {

                    edPassord.requestFocus();
                    edPassord.setError("Enter Password");
                    return;
                }

                if (chRemember.isChecked()) {

                    loginPrefsEditor.putBoolean("saveLogin", true);
                    loginPrefsEditor.putString("username", stremail);
                    loginPrefsEditor.putString("password", strPass);
                    loginPrefsEditor.commit();
                } else {

                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                }

                if (new ConnectionDetector(LoginActivity.this).isConnectingToInternet()) {

                    hitLogin(stremail, strPass);

                } else {

                    Toast.makeText(LoginActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }


    private void ForgotDialog(final Context context) {


        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.forget_dialog, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setView(dialogView);
        alertDialogs = builder.create();
        alertDialogs.show();
        btnCancel = (Button) alertDialogs.findViewById(R.id.bt_cancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialogs.dismiss();
            }
        });


    }

    private void hitLogin(String name, String pwd) {

        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        dialog = Utilss.progressDialog(context);
        dialog.show();

        apiInterface = APIClient.getClient().create(APIInterface.class);
        String token = prefrences.getSharedPref("tokenId");
        Call<Login> call = apiInterface.login(name, pwd, token);

        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, retrofit2.Response<Login> response) {

                dialog.hide();

                if (response.isSuccessful()) {
                    if (response.body().getCode().equals(201)) {
                        String Id = response.body().getResult().getId();
                        int stage = response.body().getResult().getStage();
                        prefrences.setSharedPref("driverID", Id);
                        prefrences.setSharedPref("driverEmail", response.body().getResult().getEmail());
                        prefrences.setSharedPref("driverName", response.body().getResult().getFname());
                        prefrences.setSharedPref("driverMobile",response.body().getResult().getContact());
                        prefrences.setSharedPref("driverImage",response.body().getResult().getDriverImage());
                        //String Id= response.body().getId();
                        Log.d("Id", Id);
                        Log.d("stage", String.valueOf(stage));

                        SharedPreferences sharedpreferences = getSharedPreferences("sharedPrefName", 0);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("Key_Login_Id", Id);
                        editor.commit();

                        if (stage == 0) {

                            session.setLogin(true);
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                        } else if (stage == 2) {

                            Intent i = new Intent(LoginActivity.this, RegisterStageTwo.class);
                            startActivity(i);

                        } else if (stage == 3) {

                            Intent i = new Intent(LoginActivity.this, RegisterStageThree.class);
                            startActivity(i);

                        } else if (stage == 4) {

                            Intent i = new Intent(LoginActivity.this, RegisterStageFour.class);
                            startActivity(i);
                        } else {


                        }

                    } else {

                        String text = response.body().getText();

                        Utilss.makeSnackBar(LoginActivity.this, background, text);

                    }
                }


            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {

                dialog.hide();
                Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                // Toast.makeText(context,"SERVER ERROR",Toast.LENGTH_LONG).show();
                // Log.d("listData", t.getMessage() + "Failure");

            }
        });

    }

    private boolean isValidEmail(String str_email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(str_email);
        return matcher.matches();
    }

}
