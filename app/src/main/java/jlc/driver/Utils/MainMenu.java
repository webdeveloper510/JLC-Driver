package jlc.driver.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.MenuItem;

import jlc.driver.Activities.EditProfileActivity;
import jlc.driver.Activities.LoginActivity;
import jlc.driver.Activities.MainActivity;
import jlc.driver.R;


public class MainMenu {
    Context cxt;
CsPrefrences csPrefrences;
    public MainMenu(Context context, MenuItem item){
        this.cxt = context;
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(cxt, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            cxt.startActivity(intent);
            ((Activity) cxt).overridePendingTransition(0,0);

        }
//        if (id == R.id.nav_affiliate) {
//
//            Intent intent = new Intent(cxt, AffiliateUrlActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            cxt.startActivity(intent);
//            ((Activity) cxt).overridePendingTransition(0,0);
//
//        }
//
//        if (id == R.id.nav_share) {
//
//            Intent intent = new Intent(cxt, EditProfileActivity.class);
//            cxt.startActivity(intent);
//            ((Activity) cxt).overridePendingTransition(0,0);
//
//        }
//
//        if (id == R.id.nav_referrals) {
//            Intent intent = new Intent(cxt, ReferralsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            cxt.startActivity(intent);
//            ((Activity) cxt).overridePendingTransition(0,0);
//        }
//
//        if (id == R.id.nav_program) {
//            Intent intent = new Intent(cxt, ProgramActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            cxt.startActivity(intent);
//            ((Activity) cxt).overridePendingTransition(0,0);
//        }
//
//
//        if (id == R.id.nav_ewallet) {
//            Intent intent = new Intent(cxt, EwalletActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            cxt.startActivity(intent);
//            ((Activity) cxt).overridePendingTransition(0,0);
//        }
//
//
//        if (id == R.id.nav_make_payment) {
//            Intent intent = new Intent(cxt, MakePayment.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            cxt.startActivity(intent);
//            ((Activity) cxt).overridePendingTransition(0,0);
//        }
//
//        if (id == R.id.nav_manually_funds) {
//            Intent intent = new Intent(cxt, ManuallyfundActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            cxt.startActivity(intent);
//            ((Activity) cxt).overridePendingTransition(0,0);
//        }
//
//        if (id == R.id.nav_editProfile) {
//            Intent intent = new Intent(cxt, SettingsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            cxt.startActivity(intent);
//            ((Activity) cxt).overridePendingTransition(0,0);
//        }


        if (id == R.id.nav_edit_profile) {
            Intent intent = new Intent(cxt, EditProfileActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            cxt.startActivity(intent);
            ((Activity) cxt).overridePendingTransition(0,0);
        }

        if(id == R.id.nav_logout){


            SessionManager session = new SessionManager(cxt);
            session.setLogin(false);
            SharedPreferences preferences = cxt.getSharedPreferences("sharedPrefName",0);
            SharedPreferences.Editor editor = preferences.edit();
            csPrefrences=new CsPrefrences(cxt);
            csPrefrences.clearPreferences();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(cxt, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            cxt.startActivity(intent);
            ((Activity) cxt).overridePendingTransition(0, 0);
          /*  Intent myIntent = new Intent(cxt, LoginActivity.class);
            cxt.startActivity(myIntent);*/



        }

    }
}
