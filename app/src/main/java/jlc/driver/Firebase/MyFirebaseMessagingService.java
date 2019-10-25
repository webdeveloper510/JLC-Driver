package jlc.driver.Firebase;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import jlc.driver.Activities.NotificationDialog;
import jlc.driver.Model.Login;
import jlc.driver.R;
import jlc.driver.Utils.Constants;
import jlc.driver.Utils.Event;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMsgService";
    int notifyID = 1;


    String CHANNEL_ID = "my_channel_01";// The id of the channel.
    CharSequence name = "plago_notification";// The user-visible name of the channel.
    int importance = NotificationManager.IMPORTANCE_HIGH;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("NotiifcationCheckFirst", remoteMessage.getData() + "");
        Map<String, String> notiifcationData = remoteMessage.getData();
        if (notiifcationData.containsKey("moredata")) {
            String completeData = notiifcationData.get("moredata");
            String message = null;
            String sourceName = "";
            String destination = "";
            String customerId = null;
            String senderId = null;
            String customerImage = null;
            Double source_lat, source_long, dest_lat, dest_long;
            String userName = null;
            String rideId = null;
            String amount = "";
            String customerName = "";
            String paymentMode = "";
            String distance = "";
            String key = "";
            try {
                JSONObject jsonComplete = new JSONObject(completeData);
                if (jsonComplete.has("key")) {
                    key = jsonComplete.getString("key");
                    if (key.equalsIgnoreCase("customer_cancel")) {
                        rideId = jsonComplete.getString("ride_id");
                        message = "Customer Cancel Ride";
                        EventBus.getDefault().post(new Event(Constants.CANCEL_RIDE, message, rideId));

                    }
                } else {
                    customerImage = jsonComplete.getString("image");
                    paymentMode = jsonComplete.getString("payment_mode");
                    distance = jsonComplete.getString("distance");
                    source_lat = jsonComplete.getDouble("source_lat");
                    source_long = jsonComplete.getDouble("source_long");
                    dest_lat = jsonComplete.getDouble("dest_lat");
                    dest_long = jsonComplete.getDouble("dest_long");
                    customerId = jsonComplete.getString("customer_id");
                    rideId = jsonComplete.getString("ride_id");
                    amount = jsonComplete.getString("amount");
                    sourceName = jsonComplete.getString("source");
                    destination = jsonComplete.getString("destination");
                    customerName = jsonComplete.getString("customer_name");

                    Intent intent = new Intent(MyFirebaseMessagingService.this, NotificationDialog.class);
                    intent.putExtra("sourceLattitude", source_lat);
                    intent.putExtra("sourceLongitude", source_long);
                    intent.putExtra("destLattitude", dest_lat);
                    intent.putExtra("destLongitude", dest_long);
                    intent.putExtra("customer_id", customerId);
                    intent.putExtra("price", amount);
                    intent.putExtra("payment_mode", paymentMode);
                    intent.putExtra("sourceName", sourceName);
                    intent.putExtra("destination", destination);
                    intent.putExtra("rideId", rideId);
                    intent.putExtra("customerName", customerName);
                    intent.putExtra("customerImage", customerImage);
                    intent.putExtra("distance", distance);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (isAppOnForeground(MyFirebaseMessagingService.this)) {
                if (Build.VERSION.SDK_INT <= 25) {
                    Intent intent = new Intent(this, NotificationDialog.class);
                    intent.putExtra("hostId", senderId);
                    intent.putExtra("hostName", userName);
                    PendingIntent pendingIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                    builder.setSmallIcon(R.mipmap.ic_icon_green_dot)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_icon_blue_dot))
                            .setContentTitle(message)
                            .setContentIntent(pendingIntent)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setPriority(NotificationManager.IMPORTANCE_HIGH);
                    builder.setAutoCancel(true);
                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(001, builder.build());
                } else {
                    sendNotification(message, senderId, userName);
                }


            } else {
                if (Build.VERSION.SDK_INT <= 25) {
                    Intent intent = new Intent(this, NotificationDialog.class);
                    intent.putExtra("hostId", senderId);
                    intent.putExtra("hostName", userName);
                    PendingIntent pendingIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                    builder.setSmallIcon(R.mipmap.ic_icon_green_dot)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_icon_blue_dot))
                            .setContentTitle(message)
                            .setContentIntent(pendingIntent)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setPriority(NotificationManager.IMPORTANCE_HIGH);
                    builder.setAutoCancel(true);

                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(001, builder.build());
                } else {
                    sendNotification(message, senderId, userName);
                }
            }
        } else {
            Log.d("NotiifcationCheckFirst", "wrong format");

        }

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendNotification(String messageBody, String senderId, String userName) {
        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
        Intent intent = new Intent(this, NotificationDialog.class);
        intent.putExtra("hostId", senderId);
        intent.putExtra("hostName", userName);
        PendingIntent pendingIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("New Message")
                .setContentText(messageBody)
                .setSmallIcon(R.mipmap.ic_icon_green_dot)
                .setChannelId(CHANNEL_ID)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);
        notificationBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.createNotificationChannel(mChannel);
        mNotificationManager.notify(notifyID, notificationBuilder.build());

    }

    private boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }
}
