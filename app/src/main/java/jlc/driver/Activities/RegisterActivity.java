package jlc.driver.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import jlc.driver.Adapter.CityAdapter;
import jlc.driver.Adapter.PeopleAdapter;
import jlc.driver.Adapter.StateAdapter;
import jlc.driver.Model.City;
import jlc.driver.Model.Country;
import jlc.driver.Model.Login;
import jlc.driver.Model.RegisterStageOne;
import jlc.driver.Model.State;
import jlc.driver.R;
import jlc.driver.Retrofit.APIClient;
import jlc.driver.Retrofit.APIInterface;
import jlc.driver.Retrofit.CommonParams;
import jlc.driver.Utils.ConnectionDetector;
import jlc.driver.Utils.CustomFunction;
import jlc.driver.Utils.Utilss;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.http.Part;

public class RegisterActivity extends AppCompatActivity {

    Context context;
    Dialog dialog;
    TextView tvUploadPhoto,SgDob;
    ArrayList<Country> mList;
    ArrayList<State> mstate;
    ArrayList<City> mcity;
    PeopleAdapter adapter;
    StateAdapter stateadapter;
    CityAdapter cityAdapter;
    AutoCompleteTextView country, state, city;
    String country_id, state_id, city_id, strCode;
    public static String stInsurancePath;
    private File insuranceImageCompressed;
    private File insuranceImage;
    private int insuranceImageId = 1;
    Bitmap original, bp;
    private static int RESULT_INSURANCE_IMAGE = 1;
    public static String path, pass, strCountry, strState, strCity;
    public static String stRadioValue;
    CircleImageView ivphoto;
    Button btnRegister;
    APIInterface apiInterface;
    public EditText SgFirstName, SgLastName, SgEmail, SgMobile, SgAddress, SgPassword, SgConPassword;
    private RadioGroup SgRadioGroup;
    ImageView imgDob;
    private RadioButton SgRadioButton;
    public static String stFirstName, stLastName, stEmailId, stDob, stMobileNumber, stAddress, stPassword;
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    LinearLayout background;
    private static final int STORAGE_PERMISSION_CODE = 123;

    private DatePickerDialog mDatePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        context = this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        }
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setDateTimeField();
        imgDob = findViewById(R.id.imgDob);
        tvUploadPhoto = findViewById(R.id.tvUploadPhoto);
        SgFirstName = findViewById(R.id.edfirstname);
        SgLastName = findViewById(R.id.edlastname);
        SgEmail = findViewById(R.id.edemail);
        SgDob = findViewById(R.id.eddob);
        SgMobile = findViewById(R.id.edPhone);
        SgAddress = findViewById(R.id.edAddress);
        SgPassword = findViewById(R.id.edPassword);
        SgConPassword = findViewById(R.id.edConfimPassword);
        background = findViewById(R.id.background);
        country = findViewById(R.id.edcountry);
        state = findViewById(R.id.edstate);
        city = findViewById(R.id.edcity);
        ivphoto = findViewById(R.id.ivphoto);
        btnRegister = findViewById(R.id.btn_Register);
        btnRegister = findViewById(R.id.btnRegister);
        SgRadioGroup = findViewById(R.id.SgradioGroup);
        imgDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePickerDialog.show();

            }
        });

        if (new ConnectionDetector(RegisterActivity.this).isConnectingToInternet()) {

            getCountry();

        } else {

            Toast.makeText(RegisterActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }


        country.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub
                Country freindPOJO = adapter.getItem(position);
                country_id = ((Country) freindPOJO).getId();
                strCountry = ((Country) freindPOJO).getName();
                strCode = ((Country) freindPOJO).getPhonecode();
                Toast.makeText(RegisterActivity.this, strCountry, Toast.LENGTH_SHORT).show();

                getState(country_id);
            }
        });


        state.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub
                State freindPOJO1 = stateadapter.getItem(position);
                state_id = ((State) freindPOJO1).getId();
                strState = ((State) freindPOJO1).getName();
                Toast.makeText(RegisterActivity.this, strState, Toast.LENGTH_SHORT).show();

                getCity(state_id);
            }
        });


        city.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub
                City freindPOJO1 = cityAdapter.getItem(position);
                city_id = ((City) freindPOJO1).getId();
                strCity = ((City) freindPOJO1).getName();
                Toast.makeText(RegisterActivity.this, strCity, Toast.LENGTH_SHORT).show();

                // getCity(state_id);
            }
        });


        tvUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestStoragePermission();
                } else {

                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_INSURANCE_IMAGE);


                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int SelectingId = SgRadioGroup.getCheckedRadioButtonId();
                SgRadioButton = findViewById(SelectingId);
                Log.d("gender", "gender" + SelectingId);

                stFirstName = SgFirstName.getText().toString();
                Log.d("FirstName", stFirstName);
                stLastName = SgLastName.getText().toString();
                Log.d("LastName", stLastName);
                stRadioValue = SgRadioButton.getText().toString();
                Log.d("RadioValue", stRadioValue);
                stEmailId = SgEmail.getText().toString();
                Log.d("EmailID", stEmailId);
                stDob = SgDob.getText().toString();
                Log.d("Dob", stDob);
                stMobileNumber = SgMobile.getText().toString();
                Log.d("MobileNumber", stMobileNumber);
                stAddress = SgAddress.getText().toString();
                Log.d("Address", stAddress);
                pass = SgPassword.getText().toString();
                String conpass = SgConPassword.getText().toString();

                if (TextUtils.isEmpty(stInsurancePath)) {
                    Toast.makeText(RegisterActivity.this, "Upload Profile Pic", Toast.LENGTH_SHORT).show();

                } else if (SgFirstName.getText().toString().trim().length() == 0) {
                    SgFirstName.setError("Enter First Name");
                    CustomFunction.setFocus(SgFirstName);

                } else if (SgFirstName.getText().toString().matches(".*\\d+.*")) {
                    SgFirstName.setError("Name must contain alphabets");
                    CustomFunction.setFocus(SgFirstName);


                } else if (SgLastName.getText().toString().matches(".*\\d+.*")) {
                    SgLastName.setError("Name must contain alphabets");
                    CustomFunction.setFocus(SgFirstName);


                } else if (!Patterns.EMAIL_ADDRESS.matcher(SgEmail.getText().toString().trim()).matches()) {
                    SgEmail.setError("Invalid Email");
                    CustomFunction.setFocus(SgEmail);

                } else if (SgDob.getText().toString().trim().length() == 0) {
                   /* SgDob.setError("Enter Date Of Birth");*/
                    CustomFunction.setFocus(SgDob);
                    Toast.makeText(RegisterActivity.this, "Enter Date of Birth", Toast.LENGTH_SHORT).show();

                } else if (SgMobile.getText().toString().trim().length() == 0) {
                    SgMobile.setError("Enter Number");
                    CustomFunction.setFocus(SgMobile);

                } else if (SgMobile.getText().toString().length() < 10) {
                    SgMobile.setError("Invalid Number");
                    CustomFunction.setFocus(SgMobile);

                } else if (!TextUtils.isDigitsOnly(SgMobile.getText().toString().trim())) {
                    SgMobile.setError("Invalid Number");
                    CustomFunction.setFocus(SgMobile);

                } else if (SgAddress.getText().toString().trim().length() == 0) {
                    SgAddress.setError("Enter Address");
                    CustomFunction.setFocus(SgAddress);

                } else if (pass.length() == 0) {
                    SgPassword.setError("Enter Password");
                    CustomFunction.setFocus(SgPassword);

                } else if (pass.length() < 6) {
                    SgPassword.setError("Password must be of 6 characters");
                    CustomFunction.setFocus(SgPassword);

                } else if (conpass.length() == 0) {
                    SgConPassword.setError("Please Confirm Password");
                    CustomFunction.setFocus(SgConPassword);

                } else if (!pass.equals(conpass)) {
                    SgConPassword.setError("Password does't Match");

                } else {
                    if (PasswordValidator(pass)) {

                    } else {
                        Toast.makeText(RegisterActivity.this, "Weak Password", Toast.LENGTH_SHORT).show();
                        SgPassword.setError("Password must Contain A-Z,a-z,0-9");

                    }
                    webServiceFunction();
                }
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setDateTimeField() {

        Calendar newCalendar = Calendar.getInstance();
        newCalendar.add(Calendar.MONTH, 6);
        int mYear = newCalendar.get(Calendar.YEAR);
        int mMonth = newCalendar.get(Calendar.MONTH);
        int mDay = newCalendar.get(Calendar.DAY_OF_MONTH);


        mDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat sd = new SimpleDateFormat("dd-MM-yyyy");
                final Date startDate = newDate.getTime();
                String fdate = sd.format(startDate);

                SgDob.setText(fdate);

            }
        },  mYear, mMonth, mDay);
        mDatePickerDialog.getDatePicker().setMaxDate(newCalendar.getTimeInMillis());

    }


    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//If the user has denied the permission previously your code will come to this block
//Here you can explain why you need this permission
//Explain here why you need this permission
        }
//And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case STORAGE_PERMISSION_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Permission", "Granted");
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_INSURANCE_IMAGE);


                } else {
                    Toast.makeText(RegisterActivity.this, "Request Denied", Toast.LENGTH_LONG).show();
                    Log.e("Permission", "Denied");
                }
                return;
            }
        }
    }


    private void getCountry() {

        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        dialog = Utilss.progressDialog(context);
        dialog.show();

        StringRequest loadVideos = new StringRequest(Request.Method.GET, "http://jlcpooling.com/jason/CI/api/getCountry", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();


                try {
                    JSONObject obj = new JSONObject(response);

                    JSONArray heroArray = obj.getJSONArray("data");
                    mList = new ArrayList<>();

                    for (int i = 0; i < heroArray.length(); i++) {
                        JSONObject heroObject = heroArray.getJSONObject(i);

                        Country hero = new Country(heroObject.getString("id"), heroObject.getString("name"), heroObject.getString("phonecode"));

                        mList.add(hero);

                    }

                    adapter = new PeopleAdapter(RegisterActivity.this, R.layout.activity_main, R.id.lbl_name, mList);
                    country.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //getData();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.d("Error", "Error");
                Log.d("Error", String.valueOf(error));

            }
        });
        Volley.newRequestQueue(RegisterActivity.this).add(loadVideos);
    }

    private void getState(final String country_id) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        dialog = Utilss.progressDialog(context);
        dialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, "http://jlcpooling.com/jason/CI/api/getState", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                dialog.dismiss();
                try {
                    JSONObject jObj = new JSONObject(response);
                    String Status = jObj.getString("code");

                    if (Status.equals("100")) {
                        try {
                            JSONObject obj = new JSONObject(response);

                            JSONArray heroArray = obj.getJSONArray("data");
                            mstate = new ArrayList<>();

                            for (int i = 0; i < heroArray.length(); i++) {
                                JSONObject heroObject = heroArray.getJSONObject(i);

                                State hero = new State(heroObject.getString("id"), heroObject.getString("name"), heroObject.getString("country_id"));

                                mstate.add(hero);

                            }

                            stateadapter = new StateAdapter(RegisterActivity.this, R.layout.activity_main, R.id.lbl_name, mstate);
                            state.setAdapter(stateadapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    } else {

                        // Error in login. Get the error message

                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), CommonParams.INTERNETERROR, Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("country_id", country_id);
                return params;
            }

            private Map<String, String> checkParams(Map<String, String> map) {
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;
            }

        };

        // Adding request to request queue
        jlc.driver.Retrofit.Appcontroller.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void getCity(final String state_id) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        dialog = Utilss.progressDialog(context);
        dialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, "http://jlcpooling.com/jason/CI/api/getCity", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                dialog.dismiss();
                try {
                    JSONObject jObj = new JSONObject(response);
                    String Status = jObj.getString("code");

                    if (Status.equals("100")) {


                        try {
                            JSONObject obj = new JSONObject(response);

                            JSONArray heroArray = obj.getJSONArray("data");
                            mcity = new ArrayList<>();

                            for (int i = 0; i < heroArray.length(); i++) {
                                JSONObject heroObject = heroArray.getJSONObject(i);

                                City hero = new City(heroObject.getString("id"), heroObject.getString("name"), heroObject.getString("state_id"));

                                mcity.add(hero);

                            }

                            cityAdapter = new CityAdapter(RegisterActivity.this, R.layout.activity_main, R.id.lbl_name, mcity);
                            city.setAdapter(cityAdapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    } else {

                        // Error in login. Get the error message

                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), CommonParams.INTERNETERROR, Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("state_id", state_id);
                return params;
            }

            private Map<String, String> checkParams(Map<String, String> map) {
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;
            }

        };

        // Adding request to request queue
        jlc.driver.Retrofit.Appcontroller.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("image", "------");

        Bitmap bmp;
        if (requestCode == RESULT_INSURANCE_IMAGE && resultCode == RESULT_OK && null != data) {

            try {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                stInsurancePath = cursor.getString(columnIndex);
                original = BitmapFactory.decodeFile(stInsurancePath);
                cursor.close();

                bp = decodeUri(selectedImage, 400);

                insuranceImage = new File(stInsurancePath);


                try {
                    ExifInterface exif = new ExifInterface(insuranceImage.getPath());
                    int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    int rotationInDegrees = exifToDegrees(rotation);
                    Log.d("rotation=", String.valueOf(rotationInDegrees));
                    Matrix matrix = new Matrix();
                    if (rotation != 0f) {
                        matrix.preRotate(rotationInDegrees);
                    }

                    ivphoto.setImageBitmap(Bitmap.createBitmap(original, 0, 0, original.getWidth(), original.getHeight(), matrix, true));

                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (insuranceImage != null) {
                    // Compress image in main thread using custom Compressor

                    Log.d("name", String.valueOf(insuranceImage));
                    insuranceImageCompressed = new Compressor.Builder(RegisterActivity.this)
                            .setMaxWidth(800)
                            .setMaxHeight(600)
                            .setQuality(100)
                            .setCompressFormat(Bitmap.CompressFormat.PNG)
                            .build()
                            .compressToFile(insuranceImage);

                    Log.d("sizeeeeeee", getReadableFileSize(insuranceImageCompressed.length()));


                }
                //compressImage(800, 600, insuranceImageId);


            } catch (Exception e) {
                e.printStackTrace();

            }
        }


    }


    protected Bitmap decodeUri(Uri selectedImage, int REQUIRED_SIZE) {

        try {

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

            // The new size we want to scale to
            // final int REQUIRED_SIZE =  size;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;

            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void webServiceFunction1() {

        try {
            //    Log.d("imageccccfde", tvphone.getText().toString());
//            RequestBody carmodel = RequestBody.create(MediaType.parse("text/plain"), strUserId);
//            RequestBody password = RequestBody.create(MediaType.parse("text/plain"), edAddress.getText().toString());
//            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), tvphone.getText().toString());
//            //Image Convert
//            RequestBody flag = RequestBody.create(MediaType.parse("text/plain"), "edit_profile");
//
//            hitEditProfileUpdate(carmodel, password, name, flag);

        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(RegisterActivity.this, "Something went wrong.please try again later", Toast.LENGTH_LONG);
        }

    }


    public void webServiceFunction() {

        try {

            Log.d("image", String.valueOf(insuranceImageCompressed));

            //Log.d("imageccccfde", tvphone.getText().toString());


            RequestBody requestImage = RequestBody.create(MediaType.parse("*/*"), insuranceImageCompressed);
            //final RequestBody requestBody4 = RequestBody.create(MediaType.parse("*/*"), insuranceImageCompressed);
            // RequestBody carmodel = RequestBody.create(MediaType.parse("text/plain"), strUserId);
            MultipartBody.Part InsurencePath1 = MultipartBody.Part.createFormData("driver_image", insuranceImageCompressed.getName(), requestImage);
            RequestBody stage = RequestBody.create(MediaType.parse("text/plain"), "1");
            RequestBody dob = RequestBody.create(MediaType.parse("text/plain"), stDob);
            RequestBody fname = RequestBody.create(MediaType.parse("text/plain"), stFirstName);
            RequestBody lname = RequestBody.create(MediaType.parse("text/plain"), stLastName);
            RequestBody password = RequestBody.create(MediaType.parse("text/plain"), pass);
            RequestBody email = RequestBody.create(MediaType.parse("text/plain"), stEmailId);
            RequestBody contact = RequestBody.create(MediaType.parse("text/plain"), stMobileNumber);
            RequestBody countrys = RequestBody.create(MediaType.parse("text/plain"), country_id);
            RequestBody states = RequestBody.create(MediaType.parse("text/plain"), state_id);
            RequestBody citys = RequestBody.create(MediaType.parse("text/plain"), city_id);
            RequestBody gender = RequestBody.create(MediaType.parse("text/plain"), stRadioValue);
            RequestBody address = RequestBody.create(MediaType.parse("text/plain"), stAddress);


            hitRegister(stage, InsurencePath1, dob, fname, lname, password, email, contact, countrys, states, citys, gender, address);


        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(RegisterActivity.this, "Something went wrong.please try again later", Toast.LENGTH_LONG);
        }

    }


    public String getReadableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    private int exifToDegrees(int exifOrientation) {
        Log.d("orientation", String.valueOf(exifOrientation));
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private boolean PasswordValidator(String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }


    private void hitRegister(RequestBody stage, MultipartBody.Part InsurencePath1, RequestBody dob, RequestBody fname, RequestBody lname, RequestBody password,
                             RequestBody email, RequestBody contact, RequestBody countrys, RequestBody states, RequestBody citys, RequestBody gender, RequestBody address) {

        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        dialog = Utilss.progressDialog(context);
        dialog.show();

        apiInterface = APIClient.getClient().create(APIInterface.class);


        Call<RegisterStageOne> call = apiInterface.register(stage, InsurencePath1, dob, fname, lname, password, email, contact, countrys, states, citys, gender, address);

        call.enqueue(new Callback<RegisterStageOne>() {
            @Override
            public void onResponse(Call<RegisterStageOne> call, retrofit2.Response<RegisterStageOne> response) {

                dialog.hide();

                if (response.isSuccessful()) {
                    if (response.body().getCode().equals(100)) {
                        Toast.makeText(RegisterActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                        // Utilss.makeSnackBar(RegisterActivity.this, background, "Register Successfully");
                        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(i);


                    } else {

//                        String text = response.body().getText();

                        Toast.makeText(RegisterActivity.this, "Email already Exists", Toast.LENGTH_SHORT).show();
//
                        // Utilss.makeSnackBar(RegisterActivity.this, background, "Email already Exists");


                    }
                }


            }

            @Override
            public void onFailure(Call<RegisterStageOne> call, Throwable t) {


                dialog.hide();

                Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                // Toast.makeText(context,"SERVER ERROR",Toast.LENGTH_LONG).show();
                // Log.d("listData", t.getMessage() + "Failure");

            }
        });

    }


}
