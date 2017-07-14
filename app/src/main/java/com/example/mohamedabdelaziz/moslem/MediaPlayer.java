package com.example.mohamedabdelaziz.moslem;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.crash.FirebaseCrash;
import org.json.JSONArray;
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
import java.util.ArrayList;

public class MediaPlayer extends AppCompatActivity {

    ArrayList<attachments> attachmentsArrayList ;
    TextView textView;
    RecyclerView recyclerView ;
    RecyclerView.LayoutManager mLayoutManager ;
    ProgressBar progressBar;
    LinearLayout linearLayout ;
    AutoCompleteTextView autoCompleteTextView ;
    ArrayList<String>titles ;
    int public_position= 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_media_player);
            textView = (TextView) findViewById(R.id.description);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle(getIntent().getStringExtra("title"));
            progressBar = (ProgressBar) findViewById(R.id.progressbar);
            linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
            titles = new ArrayList<>();
            new getdata(getIntent().getStringExtra("url")).execute();
            attachmentsArrayList = new ArrayList<>();
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(new recycleview_adapter());
            autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.search_text);
            autoCompleteTextView.setThreshold(1);
            autoCompleteTextView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, titles));
            autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    public_position= getindex("" + parent.getItemAtPosition(position));
                    autoCompleteTextView.setText("");
                    autoCompleteTextView.setVisibility(View.GONE);
                    findViewById(R.id.floatingActionButton).setVisibility(View.VISIBLE);
                    setvediourl(public_position);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.scrollToPosition(50);
                        }
                    }, 200);
                    if (getCurrentFocus() != null) {
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
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
        findViewById(R.id.floatingActionButton).setVisibility(View.INVISIBLE);
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
                    if (getCurrentFocus()!=null){
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                }
            }
        }, 6000);
    }

    public void play(View view) {
        services.resume();
        findViewById(R.id.play).setVisibility(View.INVISIBLE);
        findViewById(R.id.pause).setVisibility(View.VISIBLE);
    }

    public void pause(View view) {
        services.pause();
        findViewById(R.id.play).setVisibility(View.VISIBLE);
        findViewById(R.id.pause).setVisibility(View.INVISIBLE);
    }

    public void prev(View view) {

        if(public_position>0) {
            public_position--;
        setvediourl(public_position);
        }
    }
    public void next(View view) {
        if(public_position<attachmentsArrayList.size()) {
            public_position++;
        setvediourl(public_position);
        }
    }
    class getdata extends AsyncTask<Void,Void,Void>
    {
        String urlS ;

        public getdata(String urlS) {
            this.urlS = urlS;
        }
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
                    url = new URL(urlS);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.connect();
                    httpURLConnection.setConnectTimeout(3000);

                    inputStream = httpURLConnection.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    if (bufferedReader == null)
                        return null;
                    while ((line = bufferedReader.readLine()) != null)
                        stringBuffer.append(line + "\n");
                    data = stringBuffer.toString();

                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray jsonArray = jsonObject.getJSONArray("attachments");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                        attachmentsArrayList.add(new attachments(jsonObject1.getString("order"), jsonObject1.getString("title"),
                                jsonObject1.getString("duration"), jsonObject1.getString("size"),
                                jsonObject1.getString("url")));
                        titles.add(jsonObject1.getString("title"));
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
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(!titles.isEmpty()) {
                progressBar.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                findViewById(R.id.floatingActionButton).setVisibility(View.VISIBLE);
                setvediourl(public_position);
            }
            else
            {
                progressBar.setVisibility(View.INVISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                findViewById(R.id.floatingActionButton).setVisibility(View.INVISIBLE);
                textView.setText(R.string.problem_with_network);
            }
        }
    }
    private void setvediourl(int position) {
            if(position>attachmentsArrayList.size() || position<0)
                Toast.makeText(getApplicationContext(), R.string.error_try_again,Toast.LENGTH_SHORT).show();
        else {
                textView.setText(attachmentsArrayList.get(position).getOrder() + " : " + attachmentsArrayList.get(position).getTitle() + "\n" + attachmentsArrayList.get(position).getSize());
            services.play(attachmentsArrayList.get(position).getUrl());
                findViewById(R.id.media_progress).setVisibility(View.VISIBLE);
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                findViewById(R.id.media_progress).setVisibility(View.INVISIBLE);
                    }
                }, 3000);
            }
        }

    class recycleview_adapter extends RecyclerView.Adapter<recycle_holder> {

        @Override
        public recycle_holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_recycle_item,null);
            recycle_holder holder= new recycle_holder(view) ;
            return holder;
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onBindViewHolder(recycle_holder holder, final int position) {
            holder.textView.setText(""+attachmentsArrayList.get(position).getTitle());
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    public_position=position ;
                    setvediourl(public_position);
                }
            });

        }

        @Override
        public int getItemCount() {
            return attachmentsArrayList.size();
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
}