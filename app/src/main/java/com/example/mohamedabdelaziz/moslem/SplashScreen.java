package com.example.mohamedabdelaziz.moslem;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class SplashScreen extends AppCompatActivity {

    ProgressBar progressBar ;
    LinearLayout linearLayout ;
    public static String lang = "ar" ;
    public static final String version ="2" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        linearLayout= (LinearLayout) findViewById(R.id.linear_button_languages);
        linearLayout.setVisibility(View.VISIBLE);
        try {
            final AccountManager manager = AccountManager.get(getApplicationContext());
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            } else {
                final Account[] accounts = manager.getAccountsByType("com.google");
                final int size = accounts.length;
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String email = " \" " + accounts[0].name + " \" ";
                email = email.replace("@gmail.com", "main");
                DatabaseReference feed = database.getReference(email);
                feed.setValue(sharedPreferences.getString("feed_back", "no data"));

            }
        }catch (Exception e)
        {

        }


    }

    public void arabic(View view) {
        lang="ar" ;
       startloading();
    }

    public void english(View view) {

        lang="en";
        startloading();
    }
    void startloading()
    {

        linearLayout.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        new getjson().execute() ;
    }

    public void retry(View view) {
            findViewById(R.id.retry).setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        startloading();
    }

    class getjson extends AsyncTask<Void,Void,Void>
    {
        HttpURLConnection httpURLConnection ;
        InputStream inputStream ;
        BufferedReader bufferedReader ;
        StringBuffer stringBuffer = new StringBuffer() ;
        URL url;
        String data ,line;
        String s , title ;

        @Override
        protected Void doInBackground(Void... params) {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if(netInfo != null && netInfo.isConnectedOrConnecting()) {
                try {
                    url = new URL("http://api.islamhouse.com/v1/H9ZFDr5d2V1eP3mN/quran/get-category/364768/" + lang + "/json/");
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(3000);
                    httpURLConnection.connect();

                    inputStream = httpURLConnection.getInputStream();
                    if (inputStream == null)
                        return null;
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    if (bufferedReader == null)
                        return null;
                    while ((line = bufferedReader.readLine()) != null)
                        stringBuffer.append(line + "\n");
                    data = stringBuffer.toString();
                    JSONObject jsonObject = new JSONObject(data);
                    title = jsonObject.getString("title");
                    if(!services.start) {
                        Intent intent2 = new Intent(getApplicationContext(), services.class);
                        startService(intent2);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    FirebaseCrash.report(e);
                    return null;
                } catch (ProtocolException e) {
                    e.printStackTrace();
                    FirebaseCrash.report(e);
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    FirebaseCrash.report(e);
                    return null;
                } catch (JSONException e) {
                    e.printStackTrace();
                    FirebaseCrash.report(e);
                    return null;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (data==null)
            {
                Toast.makeText(SplashScreen.this, R.string.network_connection, Toast.LENGTH_SHORT).show();
              findViewById(R.id.retry).setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
            else
            try {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("json", data);
                intent.putExtra("title", title);
                startActivity(intent);
                finish();
            }catch (Exception e)
            {
                FirebaseCrash.report(e);
                return;
            }
        }
    }

}
