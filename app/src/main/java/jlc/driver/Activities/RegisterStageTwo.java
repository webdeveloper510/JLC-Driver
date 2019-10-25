package jlc.driver.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Objects;

import id.zelory.compressor.Compressor;
import jlc.driver.Firebase.FirebaseIDService;
import jlc.driver.Model.RegisterStageOne;
import jlc.driver.Model.RegisterStagesTwo;
import jlc.driver.R;
import jlc.driver.Retrofit.APIClient;
import jlc.driver.Retrofit.APIInterface;
import jlc.driver.Utils.CsPrefrences;
import jlc.driver.Utils.Utilss;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class RegisterStageTwo extends AppCompatActivity {

    ImageView ivDriverFront, ivDriverBack, ivPoliceFront, ivPoliceBack, ivWorkFront, ivWorkBack;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private static int RESULT_INSURANCE_IMAGE = 1;
    private static int RESULT_DRIVER_BACK = 2;
    private static int RESULT_POLICE_FRONT = 3;
    private static int RESULT_POLICE_BACK = 4;
    private static int RESULT_WORK_FRONT = 5;
    private static int RESULT_WORK_BACK = 6;
    Context context;
    APIInterface apiInterface;
    Dialog dialog;
    Button submitDriverDocument;
    public static String stInsurancePath, stdriverback, stpolicefront, stpoliceback, stworkfont, stworkback;
    private File insuranceImageCompressed, driverbackcompress, policefrontcompress, policebackcompress, workfrontcompress, workbackcompress;
    private File insuranceImage, driverBack, policefront, policeback, workfront, workback;
    Bitmap original, bp;
    CsPrefrences prefrences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_stage_two);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        }
        getSupportActionBar().setTitle("Driver Documents");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        prefrences = new CsPrefrences(context);
        ivDriverFront = findViewById(R.id.driverFront);
        ivDriverBack = findViewById(R.id.driverBack);
        ivPoliceFront = findViewById(R.id.policeFront);
        ivPoliceBack = findViewById(R.id.policeBack);
        ivWorkFront = findViewById(R.id.workEligibilityfront);
        ivWorkBack = findViewById(R.id.workElicgibilityback);
        submitDriverDocument = findViewById(R.id.submitDriverDocument);

        ivDriverFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(RegisterStageTwo.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestStoragePermission();
                } else {

                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_INSURANCE_IMAGE);


                }


            }
        });

        ivDriverBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(RegisterStageTwo.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestStoragePermission();
                } else {

                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_DRIVER_BACK);


                }


            }
        });
        ivPoliceFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(RegisterStageTwo.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestStoragePermission();
                } else {

                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_POLICE_FRONT);


                }


            }
        });


        ivPoliceBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(RegisterStageTwo.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestStoragePermission();
                } else {

                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_POLICE_BACK);


                }


            }
        });


        ivWorkFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(RegisterStageTwo.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestStoragePermission();
                } else {

                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_WORK_FRONT);


                }


            }
        });
        ivWorkBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(RegisterStageTwo.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestStoragePermission();
                } else {

                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_WORK_BACK);


                }


            }
        });


        submitDriverDocument.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                webServiceFunction1();


            }
        });


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
                    Toast.makeText(RegisterStageTwo.this, "Request Denied", Toast.LENGTH_LONG).show();
                    Log.e("Permission", "Denied");
                }
                return;
            }
        }
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
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
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

                    ivDriverFront.setImageBitmap(Bitmap.createBitmap(original, 0, 0, original.getWidth(), original.getHeight(), matrix, true));

                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (insuranceImage != null) {
                    // Compress image in main thread using custom Compressor

                    Log.d("name", String.valueOf(insuranceImage));
                    insuranceImageCompressed = new Compressor.Builder(RegisterStageTwo.this).setMaxWidth(800).setMaxHeight(600).setQuality(100).setCompressFormat(Bitmap.CompressFormat.PNG).build().compressToFile(insuranceImage);
                    Log.d("sizeeeeeee", getReadableFileSize(insuranceImageCompressed.length()));


                }
                //compressImage(800, 600, insuranceImageId);


            } catch (Exception e) {
                e.printStackTrace();

            }
        } else if (requestCode == RESULT_DRIVER_BACK && resultCode == RESULT_OK && null != data) {

            try {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                stdriverback = cursor.getString(columnIndex);
                original = BitmapFactory.decodeFile(stdriverback);
                cursor.close();

                bp = decodeUri(selectedImage, 400);

                driverBack = new File(stdriverback);


                try {

                    ExifInterface exif = new ExifInterface(driverBack.getPath());
                    int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    int rotationInDegrees = exifToDegrees(rotation);
                    Log.d("rotation=", String.valueOf(rotationInDegrees));
                    Matrix matrix = new Matrix();
                    if (rotation != 0f) {
                        matrix.preRotate(rotationInDegrees);
                    }

                    ivDriverBack.setImageBitmap(Bitmap.createBitmap(original, 0, 0, original.getWidth(), original.getHeight(), matrix, true));

                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (driverBack != null) {
                    // Compress image in main thread using custom Compressor

                    Log.d("name", String.valueOf(driverBack));
                    driverbackcompress = new Compressor.Builder(RegisterStageTwo.this)
                            .setMaxWidth(800)
                            .setMaxHeight(600)
                            .setQuality(100)
                            .setCompressFormat(Bitmap.CompressFormat.PNG)
                            .build()
                            .compressToFile(driverBack);

                    Log.d("sizeeeedeee", getReadableFileSize(driverbackcompress.length()));


                }
                //compressImage(800, 600, insuranceImageId);


            } catch (Exception e) {
                e.printStackTrace();

            }
        } else if (requestCode == RESULT_POLICE_FRONT && resultCode == RESULT_OK && null != data) {

            try {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                stpolicefront = cursor.getString(columnIndex);
                original = BitmapFactory.decodeFile(stpolicefront);
                cursor.close();

                bp = decodeUri(selectedImage, 400);

                policefront = new File(stpolicefront);

                try {

                    ExifInterface exif = new ExifInterface(policefront.getPath());
                    int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    int rotationInDegrees = exifToDegrees(rotation);
                    Log.d("rotation=", String.valueOf(rotationInDegrees));
                    Matrix matrix = new Matrix();
                    if (rotation != 0f) {
                        matrix.preRotate(rotationInDegrees);
                    }

                    ivPoliceFront.setImageBitmap(Bitmap.createBitmap(original, 0, 0, original.getWidth(), original.getHeight(), matrix, true));

                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (policefront != null) {
                    // Compress image in main thread using custom Compressor

                    Log.d("name", String.valueOf(policefront));
                    policefrontcompress = new Compressor.Builder(RegisterStageTwo.this)
                            .setMaxWidth(800)
                            .setMaxHeight(600)
                            .setQuality(100)
                            .setCompressFormat(Bitmap.CompressFormat.PNG)
                            .build()
                            .compressToFile(policefront);

                    Log.d("sizeeeeeee", getReadableFileSize(policefrontcompress.length()));


                }
                //compressImage(800, 600, insuranceImageId);


            } catch (Exception e) {
                e.printStackTrace();

            }
        } else if (requestCode == RESULT_POLICE_BACK && resultCode == RESULT_OK && null != data) {

            try {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                stpoliceback = cursor.getString(columnIndex);
                original = BitmapFactory.decodeFile(stpoliceback);
                cursor.close();

                bp = decodeUri(selectedImage, 400);

                policeback = new File(stpoliceback);


                try {

                    ExifInterface exif = new ExifInterface(policeback.getPath());
                    int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    int rotationInDegrees = exifToDegrees(rotation);
                    Log.d("rotation=", String.valueOf(rotationInDegrees));
                    Matrix matrix = new Matrix();
                    if (rotation != 0f) {
                        matrix.preRotate(rotationInDegrees);
                    }

                    ivPoliceBack.setImageBitmap(Bitmap.createBitmap(original, 0, 0, original.getWidth(), original.getHeight(), matrix, true));

                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (policeback != null) {
                    // Compress image in main thread using custom Compressor

                    Log.d("name", String.valueOf(policeback));
                    policebackcompress = new Compressor.Builder(RegisterStageTwo.this)
                            .setMaxWidth(800)
                            .setMaxHeight(600)
                            .setQuality(100)
                            .setCompressFormat(Bitmap.CompressFormat.PNG)
                            .build()
                            .compressToFile(policeback);

                    Log.d("sizeeeeeee", getReadableFileSize(policebackcompress.length()));


                }
                //compressImage(800, 600, insuranceImageId);


            } catch (Exception e) {
                e.printStackTrace();

            }
        } else if (requestCode == RESULT_WORK_FRONT && resultCode == RESULT_OK && null != data) {

            try {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                stworkfont = cursor.getString(columnIndex);
                original = BitmapFactory.decodeFile(stworkfont);
                cursor.close();

                bp = decodeUri(selectedImage, 400);

                workfront = new File(stworkfont);


                try {
                    ExifInterface exif = new ExifInterface(workfront.getPath());
                    int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    int rotationInDegrees = exifToDegrees(rotation);
                    Log.d("rotation=", String.valueOf(rotationInDegrees));
                    Matrix matrix = new Matrix();
                    if (rotation != 0f) {
                        matrix.preRotate(rotationInDegrees);
                    }

                    ivWorkFront.setImageBitmap(Bitmap.createBitmap(original, 0, 0, original.getWidth(), original.getHeight(), matrix, true));

                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (workfront != null) {
                    // Compress image in main thread using custom Compressor

                    Log.d("name", String.valueOf(workfront));
                    workfrontcompress = new Compressor.Builder(RegisterStageTwo.this)
                            .setMaxWidth(800)
                            .setMaxHeight(600)
                            .setQuality(100)
                            .setCompressFormat(Bitmap.CompressFormat.PNG)
                            .build()
                            .compressToFile(workfront);

                    Log.d("sizeeeeeee", getReadableFileSize(workfrontcompress.length()));


                }
                //compressImage(800, 600, insuranceImageId);


            } catch (Exception e) {
                e.printStackTrace();

            }
        } else if (requestCode == RESULT_WORK_BACK && resultCode == RESULT_OK && null != data) {

            try {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                stworkback = cursor.getString(columnIndex);
                original = BitmapFactory.decodeFile(stworkback);
                cursor.close();

                bp = decodeUri(selectedImage, 400);

                workback = new File(stworkback);


                try {
                    ExifInterface exif = new ExifInterface(workback.getPath());
                    int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    int rotationInDegrees = exifToDegrees(rotation);
                    Log.d("rotation=", String.valueOf(rotationInDegrees));
                    Matrix matrix = new Matrix();
                    if (rotation != 0f) {
                        matrix.preRotate(rotationInDegrees);
                    }

                    ivWorkBack.setImageBitmap(Bitmap.createBitmap(original, 0, 0, original.getWidth(), original.getHeight(), matrix, true));

                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (workback != null) {
                    // Compress image in main thread using custom Compressor

                    Log.d("name", String.valueOf(workback));
                    workbackcompress = new Compressor.Builder(RegisterStageTwo.this)
                            .setMaxWidth(800)
                            .setMaxHeight(600)
                            .setQuality(100)
                            .setCompressFormat(Bitmap.CompressFormat.PNG)
                            .build()
                            .compressToFile(workback);

                    Log.d("sizeeeeeee", getReadableFileSize(workbackcompress.length()));


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


    public void webServiceFunction1() {

        try {
            String driverID = prefrences.getSharedPref("driverID");
            Log.d("driverId", driverID);
            Log.d("driverId", insuranceImageCompressed.getPath()+" driver front");
            Log.d("driverId", driverbackcompress.getPath()+" driver back");
            Log.d("driverId", policefrontcompress.getPath()+" police front");
            Log.d("driverId", policebackcompress.getPath()+" police back");
            Log.d("driverId", workfrontcompress.getPath()+" work front");
            Log.d("driverId", workbackcompress.getPath()+" work back");
            RequestBody requestImage = RequestBody.create(MediaType.parse("*/*"), insuranceImageCompressed);
            RequestBody requestImage2 = RequestBody.create(MediaType.parse("*/*"), driverbackcompress);
            RequestBody requestImage3 = RequestBody.create(MediaType.parse("*/*"), policefrontcompress);
            RequestBody requestImage4 = RequestBody.create(MediaType.parse("*/*"), policebackcompress);
            RequestBody requestImage5 = RequestBody.create(MediaType.parse("*/*"), workfrontcompress);
            RequestBody requestImage6 = RequestBody.create(MediaType.parse("*/*"), workbackcompress);

            RequestBody stageid = RequestBody.create(MediaType.parse("text/plain"), "2");
            RequestBody driverid = RequestBody.create(MediaType.parse("text/plain"), driverID);
            MultipartBody.Part driverFront1 = MultipartBody.Part.createFormData("driver_license_front", insuranceImageCompressed.getName(), requestImage);
            MultipartBody.Part driverBack1 = MultipartBody.Part.createFormData("driver_license_back", driverbackcompress.getName(), requestImage2);
            MultipartBody.Part policeFront1 = MultipartBody.Part.createFormData("police_record_front", policefrontcompress.getName(), requestImage3);

            MultipartBody.Part policeFront2 = MultipartBody.Part.createFormData("police_record_back", policebackcompress.getName(), requestImage4);

            MultipartBody.Part workFront1 = MultipartBody.Part.createFormData("work_eligibility_front", workfrontcompress.getName(), requestImage5);

            MultipartBody.Part workBack1 = MultipartBody.Part.createFormData("work_eligibility_back", workbackcompress.getName(), requestImage6);
            hitRegisterTwo(driverid, stageid, driverFront1, driverBack1, workFront1, workBack1, policeFront1, policeFront2);

        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(RegisterStageTwo.this, "Something went wrong.please try again later", Toast.LENGTH_LONG);
        }

    }


    private void hitRegisterTwo(RequestBody driverid, RequestBody stageid, MultipartBody.Part driverFront1, MultipartBody.Part driverBack1, MultipartBody.Part workFront1,
                                MultipartBody.Part workBack1, MultipartBody.Part policeFront1, MultipartBody.Part policeFront2) {

        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        dialog = Utilss.progressDialog(context);
        dialog.show();

        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<RegisterStagesTwo> call = apiInterface.registerTwo(driverid, stageid, driverFront1, driverBack1, workFront1, workBack1, policeFront1, policeFront2);

        call.enqueue(new Callback<RegisterStagesTwo>() {
            @Override
            public void onResponse(Call<RegisterStagesTwo> call, retrofit2.Response<RegisterStagesTwo> response) {

                dialog.hide();

                if (response.isSuccessful()) {
                    if (response.body().getCode().equals(100)) {

                        Toast.makeText(RegisterStageTwo.this, "Upload Documents Successfully", Toast.LENGTH_SHORT).show();
                        // Utilss.makeSnackBar(RegisterActivity.this, background, "Register Successfully");
                        Intent i = new Intent(RegisterStageTwo.this, RegisterStageThree.class);
                        startActivity(i);


                    } else {

//                        String text = response.body().getText();

                        Toast.makeText(RegisterStageTwo.this, "Email already Exists", Toast.LENGTH_SHORT).show();
//
                        // Utilss.makeSnackBar(RegisterActivity.this, background, "Email already Exists");


                    }
                }


            }

            @Override
            public void onFailure(Call<RegisterStagesTwo> call, Throwable t) {


                dialog.hide();

                Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                // Toast.makeText(context,"SERVER ERROR",Toast.LENGTH_LONG).show();
                // Log.d("listData", t.getMessage() + "Failure");

            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
