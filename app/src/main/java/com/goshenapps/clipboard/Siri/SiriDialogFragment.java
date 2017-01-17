package com.goshenapps.clipboard.Siri;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.i18n.phonenumbers.PhoneNumberMatch;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.goshenapps.clipboard.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SiriDialogFragment extends BottomSheetDialogFragment implements
        TextToSpeech.OnInitListener {

    String mString;
    SiriWaveView siriWaveView;
    private TextToSpeech tts;
    RelativeLayout spinner;
    RelativeLayout waves;

    public static SiriDialogFragment newInstance(String string) {
        SiriDialogFragment f = new SiriDialogFragment();
        Bundle args = new Bundle();
        args.putString("string", string);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mString = getArguments().getString("string");
    }




    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.siri_view, null);
        dialog.setContentView(contentView);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if( behavior != null && behavior instanceof BottomSheetBehavior ) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
            ((BottomSheetBehavior) behavior).setPeekHeight(500);
            contentView.requestLayout();
        }


        siriWaveView = (SiriWaveView) contentView.findViewById(R.id.siriWaveView);
        siriWaveView.stopAnimation();
        spinner = (RelativeLayout)contentView.findViewById(R.id.spinner);
        waves = (RelativeLayout)contentView.findViewById(R.id.waves);



        tts = new TextToSpeech(getActivity(), this);
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener()
        {
            @Override
            public void onDone(String utteranceId)
            {


                dismiss();

            }

            @Override
            public void onError(String utteranceId)
            {
            }

            @Override
            public void onStart(String utteranceId)
            {


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        spinner.setVisibility(View.GONE);
                        waves.setVisibility(View.VISIBLE);
                        siriWaveView.startAnimation();
                    }
                });

            }
        });



    }







   @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.getDefault());

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {


                speakText();

            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }




    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    private void speakText() {

        String toSpeak = mString;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"messageID");
        tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, map);
        siriWaveView.startAnimation();
    }


    @Override
    public void onResume() {
        super.onResume();
    }




 
}