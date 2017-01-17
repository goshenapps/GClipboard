package com.goshenapps.clipboard.Intro;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.goshenapps.clipboard.MainActivity;
import com.goshenapps.clipboard.R;


public class Hello extends AppIntro {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.App);
        super.onCreate(savedInstanceState);

        askForPermissions(new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED,Manifest.permission.INTERNET,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2); // OR

        addSlide(new MuseA());
        addSlide(new MuseB());
        addSlide(new MuseC());



        showSkipButton(true);
        setSwipeLock(true);







    }


    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the visualiser changes.
    }


    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        SharedPreferences edits = PreferenceManager.getDefaultSharedPreferences(Hello.this);
        SharedPreferences.Editor headies = edits.edit();
        headies.putString("LOGIN","ciroma");
        headies.putString("PASS","segun");
        headies.apply();
        Intent intent = new Intent(Hello.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        SharedPreferences edits = PreferenceManager.getDefaultSharedPreferences(Hello.this);
        SharedPreferences.Editor headies = edits.edit();
        headies.putString("LOGIN","ciroma");
        headies.putString("PASS","segun");
        headies.apply();
        Intent intent = new Intent(Hello.this, MainActivity.class);
        startActivity(intent);
    }
}

