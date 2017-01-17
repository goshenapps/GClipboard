package com.goshenapps.clipboard.LoginError;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.goshenapps.clipboard.MainActivity;
import com.goshenapps.clipboard.R;
import com.jetradar.desertplaceholder.DesertPlaceholder;

public class Errors extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.errors);

        DesertPlaceholder desertPlaceholder = (DesertPlaceholder) findViewById(R.id.placeholder);
        desertPlaceholder.setOnButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                // do stuff
            }
        });
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(getApplicationContext(), MainActivity.class));

    }
}
