package com.example.mohamedabdelaziz.moslem;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);



//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//
    }

    public void feedback(View view) {

                       startActivity(new Intent(getApplicationContext(),dialod3.class));
                        }


    public void update(View view) {
        startActivity(new Intent(getApplicationContext(),dialogactivity2.class));

    }

    public void aboutapp(View view) {
        startActivity(new Intent(getApplicationContext(),dialogactivity.class));

    }
}
