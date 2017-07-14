package com.example.mohamedabdelaziz.moslem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class dialogactivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogactivity);
        ((TextView)findViewById(R.id.textView)).setText(R.string.about_app_text);
    }

    public void disappear(View view) {
        finish();
    }
}
