package jlc.driver.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelGetCustomerProfile {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("fname")
    @Expose
    private String fname;
    @SerializedName("lname")
    @Expose
    private String lname;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("contact")
    @Expose
    private String contact;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("driver_image")
    @Expose
    private String driverImage;
    @SerializedName("driver_license_front")
    @Expose
    private String driverLicenseFront;
    @SerializedName("driver_license_back")
    @Expose
    private String driverLicenseBack;
    @SerializedName("veh_insurance_front")
    @Expose
    private String vehInsuranceFront;
    @SerializedName("veh_insurance_back")
    @Expose
    private String vehInsuranceBack;
    @SerializedName("veh_registration_front")
    @Expose
    private String vehRegistrationFront;
    @SerializedName("veh_registration_back")
    @Expose
    private String vehRegistrationBack;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("work_eligibility_front")
    @Expose
    private String workEligibilityFront;
    @SerializedName("work_eligibility_back")
    @Expose
    private String workEligibilityBack;
    @SerializedName("police_record_front")
    @Expose
    private String policeRecordFront;
    @SerializedName("police_record_back")
    @Expose
    private String policeRecordBack;
    @SerializedName("vehicle_picture")
    @Expose
    private String vehiclePicture;
    @SerializedName("vehicle_model")
    @Expose
    private String vehicleModel;
    @SerializedName("vehicle_brand")
    @Expose
    private String vehicleBrand;
    @SerializedName("vehicle_year")
    @Expose
    private String vehicleYear;
    @SerializedName("vehicle_color")
    @Expose
    private String vehicleColor;
    @SerializedName("vehicle_seats")
    @Expose
    private String vehicleSeats;
    @SerializedName("vehicle_door")
    @Expose
    private String vehicleDoor;
    @SerializedName("final_seats")
    @Expose
    private String finalSeats;
    @SerializedName("vehicle_plate_number")
    @Expose
    private String vehiclePlateNumber;
    @SerializedName("safety_certificate_front")
    @Expose
    private String safetyCertificateFront;
    @SerializedName("safety_certificate_back")
    @Expose
    private String safetyCertificateBack;
    @SerializedName("signupdate")
    @Expose
    private String signupdate;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("emailverification")
    @Expose
    private String emailverification;
    @SerializedName("ridecount")
    @Expose
    private String ridecount;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("driver_status")
    @Expose
    private String driverStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDriverImage() {
        return driverImage;
    }

    public void setDriverImage(String driverImage) {
        this.driverImage = driverImage;
    }

    public String getDriverLicenseFront() {
        return driverLicenseFront;
    }

    public void setDriverLicenseFront(String driverLicenseFront) {
        this.driverLicenseFront = driverLicenseFront;
    }

    public String getDriverLicenseBack() {
        return driverLicenseBack;
    }

    public void setDriverLicenseBack(String driverLicenseBack) {
        this.driverLicenseBack = driverLicenseBack;
    }

    public String getVehInsuranceFront() {
        return vehInsuranceFront;
    }

    public void setVehInsuranceFront(String vehInsuranceFront) {
        this.vehInsuranceFront = vehInsuranceFront;
    }

    public String getVehInsuranceBack() {
        return vehInsuranceBack;
    }

    public void setVehInsuranceBack(String vehInsuranceBack) {
        this.vehInsuranceBack = vehInsuranceBack;
    }

    public String getVehRegistrationFront() {
        return vehRegistrationFront;
    }

    public void setVehRegistrationFront(String vehRegistrationFront) {
        this.vehRegistrationFront = vehRegistrationFront;
    }

    public String getVehRegistrationBack() {
        return vehRegistrationBack;
    }

    public void setVehRegistrationBack(String vehRegistrationBack) {
        this.vehRegistrationBack = vehRegistrationBack;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWorkEligibilityFront() {
        return workEligibilityFront;
    }

    public void setWorkEligibilityFront(String workEligibilityFront) {
        this.workEligibilityFront = workEligibilityFront;
    }

    public String getWorkEligibilityBack() {
        return workEligibilityBack;
    }

    public void setWorkEligibilityBack(String workEligibilityBack) {
        this.workEligibilityBack = workEligibilityBack;
    }

    public String getPoliceRecordFront() {
        return policeRecordFront;
    }

    public void setPoliceRecordFront(String policeRecordFront) {
        this.policeRecordFront = policeRecordFront;
    }

    public String getPoliceRecordBack() {
        return policeRecordBack;
    }

    public void setPoliceRecordBack(String policeRecordBack) {
        this.policeRecordBack = policeRecordBack;
    }

    public String getVehiclePicture() {
        return vehiclePicture;
    }

    public void setVehiclePicture(String vehiclePicture) {
        this.vehiclePicture = vehiclePicture;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleBrand() {
        return vehicleBrand;
    }

    public void setVehicleBrand(String vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    public String getVehicleYear() {
        return vehicleYear;
    }

    public void setVehicleYear(String vehicleYear) {
        this.vehicleYear = vehicleYear;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getVehicleSeats() {
        return vehicleSeats;
    }

    public void setVehicleSeats(String vehicleSeats) {
        this.vehicleSeats = vehicleSeats;
    }

    public String getVehicleDoor() {
        return vehicleDoor;
    }

    public void setVehicleDoor(String vehicleDoor) {
        this.vehicleDoor = vehicleDoor;
    }

    public String getFinalSeats() {
        return finalSeats;
    }

    public void setFinalSeats(String finalSeats) {
        this.finalSeats = finalSeats;
    }

    public String getVehiclePlateNumber() {
        return vehiclePlateNumber;
    }

    public void setVehiclePlateNumber(String vehiclePlateNumber) {
        this.vehiclePlateNumber = vehiclePlateNumber;
    }

    public String getSafetyCertificateFront() {
        return safetyCertificateFront;
    }

    public void setSafetyCertificateFront(String safetyCertificateFront) {
        this.safetyCertificateFront = safetyCertificateFront;
    }

    public String getSafetyCertificateBack() {
        return safetyCertificateBack;
    }

    public void setSafetyCertificateBack(String safetyCertificateBack) {
        this.safetyCertificateBack = safetyCertificateBack;
    }

    public String getSignupdate() {
        return signupdate;
    }

    public void setSignupdate(String signupdate) {
        this.signupdate = signupdate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmailverification() {
        return emailverification;
    }

    public void setEmailverification(String emailverification) {
        this.emailverification = emailverification;
    }

    public String getRidecount() {
        return ridecount;
    }

    public void setRidecount(String ridecount) {
        this.ridecount = ridecount;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(String driverStatus) {
        this.driverStatus=driverStatus;
    }
}
