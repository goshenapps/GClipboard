package com.goshenapps.clipboard.Intro;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import com.goshenapps.clipboard.R;


public class MuseA extends Fragment  {
    TextView worded,title;
    VideoView screen;
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.muse, null);

        screen = (VideoView) v.findViewById(R.id.screen);
        worded = (TextView)v.findViewById(R.id.worded);
        title = (TextView)v.findViewById(R.id.title);

        title.setText(R.string.intro1Title);
        worded.setText(R.string.intro1Text);


        Uri uri = Uri.parse("android.resource://"+ getActivity().getPackageName() + "/"+ R.raw.intro1);



        screen.setVideoURI(uri);
        screen.requestFocus();
        screen.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer pee) {
                pee.setLooping(true);
            }
        });
        screen.start();





        return v;

	}




    }