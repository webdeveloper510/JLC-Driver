package jlc.driver.Utils;

public class Event {
    private int key;
    public int colorValue;
    double lattitude,longitude;
    private String value, id, senderId,timeStamp,message,rideId;

    public Event(int key, String value) {
        this.key = key;
        this.value = value;
    }
    public Event(int key, String rideId,String message) {
        this.key = key;
        this.rideId = rideId;
        this.message=message;
    }



    public Event(int key, int colorValue) {
        this.key = key;
        this.colorValue = colorValue;
    }
    public Event(int key, double lattitude, double longitude) {
        this.key = key;
        this.lattitude = lattitude;
        this.longitude=longitude;
    }


    public Event(int key, String message, String timeStamp, String id, String senderId) {
        this.key = key;
        this.id = id;
        this.message = message;
        this.senderId = senderId;
        this.timeStamp = timeStamp;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getColorValue() {

        return colorValue;
    }

    public void setColorValue(int colorValue) {
        this.colorValue = colorValue;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public double getLattitude() {
        return lattitude;
    }

    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }




}