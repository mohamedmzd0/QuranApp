package com.example.mohamedabdelaziz.moslem;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.media.MediaPlayer ;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by Mohamed Abd ELaziz on 7/11/2017.
 */

public class services extends Service {

    static MediaPlayer mediaPlayer ;
    static Context context ;
    public static boolean start =false ;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        start=true ;
        context=getApplicationContext() ;
        return super.onStartCommand(intent, flags, startId);
    }
    public static void play(String url)
    {
          new playmedia(url).execute() ;
    }

    public static void pause()
    {
        mediaPlayer.pause();
    }
    public static void resume()
    {
        mediaPlayer.start();
    }
    public static void forward()
    {
        mediaPlayer.seekTo(5);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer=new MediaPlayer() ;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }
    public static class playmedia extends AsyncTask<Void,Void,Void>
    {
        String url ;
        playmedia(String url)
        {
            this.url=url ;
        }
        @Override
        protected Void doInBackground(Void... params) {
            if (mediaPlayer.isPlaying())
                mediaPlayer.stop();
            mediaPlayer= MediaPlayer.create(context, Uri.parse(url)) ;
            mediaPlayer.start();
            return null;
        }
    }
    }