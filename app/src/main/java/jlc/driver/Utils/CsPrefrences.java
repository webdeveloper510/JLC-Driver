package jlc.driver.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class CsPrefrences {
    public static final String MyPREFERENCES = "MyPrefrencesJLC" ;
    SharedPreferences sharedpreferences;
    Context context;

    public CsPrefrences(Context context,String fbUSerID)
    {
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("fbUserId", fbUSerID);
        editor.commit();
    }

    public CsPrefrences(Context context, String id,String emailUser,String name){
        //sharedpreferences = (SharedPreferences) context.getSharedPreferences(MyPREFERENCES, context.MODE_PRIVATE);
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("id", id);
        editor.putString("email", emailUser);
        editor.putString("name", name);
        editor.commit();

    }

    public CsPrefrences(Context context){
        this.context = context;
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public Boolean checkLogin(){
        if(sharedpreferences.contains("email")){
            return true;
        }

        return false;
    }

    public Boolean checkFacebookLogin(){
        if(sharedpreferences.contains("facebook_id")){
            return true;
        }

        return false;
    }

    public Boolean clearPreferences(){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();

        return true;
    }

    public String getSharedPref(String key){
        String value = sharedpreferences.getString(key, "null");
        return value;
    }

    public Boolean setSharedPref(String key, String value){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.commit();
        return true;
    }

    public Boolean clearSharedPref(String key){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.remove(key);
        editor.commit();
        return true;
    }
}
