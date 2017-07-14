package com.example.mohamedabdelaziz.moslem;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    ArrayList<json_data> arrayList;
    String json;
    Toolbar toolbar;
    ArrayList<String> titles;
    AutoCompleteTextView autoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.main_recycle);
        Display display = getWindowManager().getDefaultDisplay();
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), (int) display.getWidth() / 200);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.main);
        titles = new ArrayList<>();
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.search_text) ;
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_dropdown_item_1line,titles));
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              int x = getindex(""+parent.getItemAtPosition(position));
                Intent intent =new Intent(getApplicationContext(),MediaPlayer.class);
                autoCompleteTextView.setText("");
                autoCompleteTextView.setVisibility(View.GONE);
                findViewById(R.id.floatingActionButton).setVisibility(View.VISIBLE);
                intent.putExtra("url",arrayList.get(x).getApi_url()) ;
                intent.putExtra("title",arrayList.get(x).prepared.getTitle()) ;
                startActivity(intent);

            }
        });
        arrayList=new ArrayList<>() ;
        if(getIntent()!=null) {
            json = getIntent().getStringExtra("json");
            new parse_data().execute();
            toolbar.setTitle(getIntent().getStringExtra("title"));
        }
        else
            Toast.makeText(getApplicationContext(), R.string.no_data_try_again,Toast.LENGTH_LONG).show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference message = database.getReference("message");
        final DatabaseReference message2 = database.getReference("message2");
        message.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class) ;

                if(!value.equals("false")) {
                    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.message), getApplicationContext().MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    if(!sharedPref.getString("message","null").equalsIgnoreCase(value)){
                    editor.putString("message", value);
                    editor.commit();
                    NotificationManager manager;
                    NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(MainActivity.this)
                            .setContentText(value)
                            .setContentTitle("Hello")
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setSmallIcon(R.drawable.message);
                    manager = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
                    manager.notify(1, builder.build());
                }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        message2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class) ;

                if(!value.equals("false")) {
                    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.message), getApplicationContext().MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    if(!sharedPref.getString("message2","null").equalsIgnoreCase(value)){
                        editor.putString("message2", value);
                        editor.commit();
                        NotificationManager manager;
                        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(MainActivity.this)
                                .setContentText(value)
                                .setContentTitle("هام")
                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                .setSmallIcon(R.drawable.message);
                        manager = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
                        manager.notify(1, builder.build());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
            }

    private int getindex(String t) {
        for (int i = 0; i < titles.size(); i++) {
            if(t.equalsIgnoreCase(titles.get(i))) {
                return i;
            }
        }
        return -1 ;
    }
    public void search(View view) {
        autoCompleteTextView.setVisibility(View.VISIBLE);
        findViewById(R.id.floatingActionButton).setVisibility(View.GONE);
        auto_hide_search();
    }

    private void auto_hide_search() {
        Handler handler =new Handler() ;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(autoCompleteTextView.getVisibility()==View.VISIBLE && autoCompleteTextView.getText().toString().equalsIgnoreCase("")) {
                    autoCompleteTextView.setVisibility(View.INVISIBLE);
                    findViewById(R.id.floatingActionButton).setVisibility(View.VISIBLE);
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0) ;
                }
            }
        }, 6000);
    }

    @Override
    public void onBackPressed() {
        if(autoCompleteTextView.getVisibility()==View.VISIBLE) {
            autoCompleteTextView.setVisibility(View.INVISIBLE);
            findViewById(R.id.floatingActionButton).setVisibility(View.VISIBLE);
        }else
            super.onBackPressed();
    }

    public void gotosettings(MenuItem item) {
        startActivity(new Intent(getApplicationContext(),Setting.class));
    }

    class recycleview_adapter extends RecyclerView.Adapter<recycle_holder>
    {
        @Override
        public recycle_holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mainactivity_recycle_item,null);
            recycle_holder holder= new recycle_holder(view) ;
            return holder;
        }
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onBindViewHolder(recycle_holder holder, final int position) {
                holder.textView.setText(""+arrayList.get(position).getTitle());
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent =new Intent(getApplicationContext(),MediaPlayer.class);
                        intent.putExtra("url",arrayList.get(position).getApi_url()) ;
                        intent.putExtra("title",arrayList.get(position).prepared.getTitle()) ;
                        startActivity(intent);
                    }
                });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }
    class recycle_holder extends RecyclerView.ViewHolder
    {

        View view;
        TextView textView ;
        public recycle_holder(View itemView) {
            super(itemView);
            view=itemView ;
            textView= (TextView) itemView.findViewById(R.id.main_recycle_text_item);
        }
    }
    class parse_data extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... params) {
            try{
                JSONObject jsonObject = new JSONObject(json);
                JSONArray jsonArray = jsonObject.getJSONArray("recitations") ;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = (JSONObject) jsonArray.get(i);
                    JSONArray jsonArray1 = object.getJSONArray("prepared_by");
                    JSONObject jsonObject1 = (JSONObject) jsonArray1.get(0);
                    arrayList.add(new json_data(object.getString("title"),object.getString("api_url"),new prepared_by(jsonObject1.getString("title")))) ;
                    titles.add(object.getString("title")) ;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                FirebaseCrash.report(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            recyclerView.setAdapter(new recycleview_adapter());
        }
    }
}
