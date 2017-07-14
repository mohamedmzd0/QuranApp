package com.example.mohamedabdelaziz.moslem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class dialod3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialod3);

    }

    public void send(View view) {
        String text = ((TextView)findViewById(R.id.feeed)).getText().toString() ;
        Random r = new Random() ;
        String user = "user"+r.nextInt(10000) ;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference feed = database.getReference(user);
        feed.setValue(text);
        finish();

    }

    public void cancel(View view) {
        finish();
    }
}
