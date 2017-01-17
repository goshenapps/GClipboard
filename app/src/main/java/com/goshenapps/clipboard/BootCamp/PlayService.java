package com.goshenapps.clipboard.BootCamp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goshenapps.clipboard.R;

import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

public class PlayService extends Service implements TextToSpeech.OnInitListener{

    private TextToSpeech tts;
    String mString;
    Random rand = new Random();
    int diceRoll;
    Notification.Builder mBuilder;



    @Override
    public void onCreate() {

        diceRoll = rand.nextInt(1000) + 1;
        tts = new TextToSpeech(getApplicationContext(),  this);


        tts.setOnUtteranceProgressListener(new UtteranceProgressListener()
        {
            @Override
            public void onDone(String utteranceId)
            {

                NotificationManager noticeme = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                noticeme.cancel(diceRoll);
                stopSelf();
            }
            @Override
            public void onError(String utteranceId)
            {


            }

            @Override
            public void onStart(String utteranceId)
            {


                PushNotify();

            }
        });




    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mString = intent.getStringExtra("fishbowl");
        speakText(mString);




        return super.onStartCommand(intent, flags, startId);



    }

    @Override
    public IBinder onBind(Intent intent) {



        throw new UnsupportedOperationException("Not yet implemented");




    }

    // Generates an ID maps it in a Hashmap and starts the TTS engine

    private void speakText(String jerry) {

        Toast.makeText(getApplicationContext(),""+jerry,Toast.LENGTH_LONG).show();
        String toSpeak = jerry;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"messageID");
        tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, map);
    }




    // Generates a Notification to notify the user that the text to speech engine has started.

    public void PushNotify()
    {

        mBuilder = new Notification.Builder(getApplicationContext())
                .setSmallIcon(android.R.drawable.ic_lock_silent_mode_off)
                .setContentTitle(getApplicationContext().getString(R.string.notice_playback_title))
                .setAutoCancel(true)
                .setContentText(getApplicationContext().getString(R.string.notice_playback_sub));
        NotificationManager mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(diceRoll, mBuilder.build());



    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.getDefault());

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {


                speakText(mString);

            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }
}
