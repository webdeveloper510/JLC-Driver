package jlc.driver.Firebase;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import jlc.driver.Utils.CsPrefrences;

public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";
  public static String Token;
    CsPrefrences prefrences;


    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
       // super.onTokenRefresh();

        Token = FirebaseInstanceId.getInstance().getToken();
        prefrences=  new CsPrefrences(FirebaseIDService.this);
        prefrences.setSharedPref("tokenId", Token);
        Log.d("Refreshedtoken:" , Token);

    /*    // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);*/
    }

}
