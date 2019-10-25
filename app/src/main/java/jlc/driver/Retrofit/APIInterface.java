package jlc.driver.Retrofit;


import jlc.driver.Model.DriverStatus;
import jlc.driver.Model.Login;
import jlc.driver.Model.ModelCancelRide;
import jlc.driver.Model.ModelCashCollected;
import jlc.driver.Model.ModelCompleteTrip;
import jlc.driver.Model.ModelDriverAccept;
import jlc.driver.Model.ModelDriverArrived;
import jlc.driver.Model.ModelEditProfile;
import jlc.driver.Model.ModelGetAmount;
import jlc.driver.Model.ModelGetCustomerProfile;
import jlc.driver.Model.ModelTripStart;
import jlc.driver.Model.ModelUpdateLocation;
import jlc.driver.Model.RegisterStageOne;
import jlc.driver.Model.RegisterStagesTwo;
import jlc.driver.Model.ResetPassword;
import jlc.driver.Model.Test;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIInterface {
    @Multipart
    @POST("api/editProfileimage")
    Call<ResetPassword> editProfile(@Part("driver_id") RequestBody driver_id, @Part MultipartBody.Part customer_image);


    @FormUrlEncoded
    @POST("api/profileEdit")
    Call<ModelEditProfile> editDriverProfile(@Field("driver_id") String driver_id,@Field("name") String name,@Field("dob") String dob,@Field("contact") String contact,@Field("email") String email);


    @FormUrlEncoded
    @POST("Api/getDriverinfo")
    Call<ModelGetCustomerProfile> getDriverProfile(@Field("driver_id") String driver_id);

    @FormUrlEncoded
    @POST("Api/resetPassword")
    Call<ResetPassword> resetPass(@Field("email") String email, @Field("password") String password, @Field("token") String token);



    @FormUrlEncoded
    @POST("api/driverLogin")
    Call<Login> login(@Field("email") String email, @Field("password") String password, @Field("token") String token);


    @FormUrlEncoded
    @POST("Api/updateDriverlatlong")
    Call<ModelUpdateLocation> hitUpdateLocation(@Field("driver_id") String driver_id  , @Field("source_lat") String source_lat, @Field("source_lang") String source_lang);


    @FormUrlEncoded
    @POST("Api/driverCancelride")
    Call<ModelCancelRide> hitCancelRideApi( @Field("driver_id") String driver_id  ,@Field("customer_id") String customer_id,@Field("ride_id") String ride_id);

    @FormUrlEncoded
    @POST("Customerapi/cashPayment")
    Call<ModelGetAmount> hitGetAmountApi(@Field("ride_id") String ride_id, @Field("customer_id") String customer_id);

    @FormUrlEncoded
    @POST("Api/cashCollected")
    Call<ModelCashCollected> hitCashCollectApi(@Field("ride_id") String ride_id);



    @FormUrlEncoded
    @POST("api/driverAccept")
    Call<ModelDriverAccept> hitDriverAcceptApi(@Field("customer_id") String customer_id, @Field("driver_id") String driver_id, @Field("ride_status") String ride_status, @Field("ride_id") String ride_id);


    @FormUrlEncoded
    @POST("api/tripStart")
    Call<ModelTripStart> hitStartTripApi(@Field("customer_id") String customer_id, @Field("driver_id") String driver_id, @Field("ride_id") String ride_id);

    @FormUrlEncoded
    @POST("api/tripComplete")
    Call<ModelCompleteTrip> hitCompleteTrip(@Field("customer_id") String customer_id, @Field("driver_id") String driver_id, @Field("ride_id") String ride_id);


    @FormUrlEncoded
    @POST("api/driverArrived")
    Call<ModelDriverArrived> hitDriverArrivedApi(@Field("driver_id") String driver_id, @Field("customer_id") String customer_id, @Field("ride_id") String ride_id);

    @FormUrlEncoded
    @POST("api/driverStatus")
    Call<DriverStatus> driverStatusApi(@Field("driver_id") String driver_id, @Field("status") String status, @Field("source_lat") String source_lat, @Field("source_lang") String source_lang);

    @Multipart
    @POST("api/driverSignup")
    Call<RegisterStageOne> register(@Part("stage") RequestBody stage, @Part MultipartBody.Part driver_image, @Part("dob") RequestBody dob, @Part("fname") RequestBody fname,
                                    @Part("lname") RequestBody lname, @Part("password") RequestBody password, @Part("email") RequestBody email,
                                    @Part("contact") RequestBody contact, @Part("country") RequestBody country, @Part("state") RequestBody state,
                                    @Part("city") RequestBody city, @Part("gender") RequestBody gender, @Part("address") RequestBody address);

    @Multipart
    @POST("api/driverStages")
    Call<RegisterStagesTwo> registerTwo(@Part("driver_id") RequestBody driver_id, @Part("stage_id") RequestBody stage_id, @Part MultipartBody.Part driver_license_front, @Part MultipartBody.Part driver_license_back,
                                        @Part MultipartBody.Part work_eligibility_front, @Part MultipartBody.Part work_eligibility_back, @Part MultipartBody.Part police_record_front, @Part MultipartBody.Part police_record_back);


    @Multipart
    @POST("api/driverStages")
    Call<RegisterStagesTwo> registerThree(@Part("driver_id") RequestBody driver_id, @Part("stage_id") RequestBody stage_id, @Part MultipartBody.Part vehicle_insurance_front, @Part MultipartBody.Part vehicle_insurance_back,
                                          @Part MultipartBody.Part vehicle_registration_front, @Part MultipartBody.Part vehicle_registration_back, @Part MultipartBody.Part safety_certificate_front, @Part MultipartBody.Part safety_certificate_back);


    @Multipart
    @POST("api/driverStages")
    Call<RegisterStagesTwo> registerFour(@Part("driver_id") RequestBody driver_id, @Part("stage_id") RequestBody stage_id, @Part MultipartBody.Part vehicle_picture, @Part("vehicle_plate_number") RequestBody vehicle_plate_number, @Part("vehicle_model") RequestBody vehicle_model,
                                         @Part("vehicle_year") RequestBody vehicle_year, @Part("vehicle_color") RequestBody vehicle_color, @Part("vehicle_brand") RequestBody vehicle_brand, @Part("vehicle_seats") RequestBody vehicle_seats);


}
