package com.example.mohamedabdelaziz.moslem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class dialogactivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogactivity2);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference update = database.getReference("update");
        final DatabaseReference url = database.getReference("url");
        update.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String value = dataSnapshot.getValue(String.class);
                if (!value.equalsIgnoreCase(SplashScreen.version)) {
                    ((TextView) findViewById(R.id.textview)).setText(R.string.update_availble);
                    url.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final String url = dataSnapshot.getValue(String.class);
                            findViewById(R.id.progress).setVisibility(View.INVISIBLE);
                            findViewById(R.id.imageButton).setVisibility(View.VISIBLE);
                            ((ImageButton) findViewById(R.id.imageButton)).setBackgroundResource(R.drawable.update);
                            findViewById(R.id.imageButton).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(url));
                                    startActivity(i);
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    ((TextView) findViewById(R.id.textview)).setText(R.string.last_update);
                    findViewById(R.id.progress).setVisibility(View.INVISIBLE);
                    findViewById(R.id.imageButton).setVisibility(View.VISIBLE);
                    ((ImageButton) findViewById(R.id.imageButton)).setBackgroundResource(R.drawable.no_update);
                    findViewById(R.id.imageButton).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                ((TextView)findViewById(R.id.textview)).setText(R.string.canceled);
            }
        });


    }
}
