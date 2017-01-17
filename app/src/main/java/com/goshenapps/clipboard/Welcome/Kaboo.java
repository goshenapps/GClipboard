package com.goshenapps.clipboard.Welcome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.goshenapps.clipboard.BootCamp.Clipboard;
import com.goshenapps.clipboard.Intro.Hello;
import com.goshenapps.clipboard.LoginError.Errors;
import com.goshenapps.clipboard.MainActivity;
import com.goshenapps.clipboard.R;

import java.util.Arrays;


public class Kaboo extends AppCompatActivity {
    String lux;
    FirebaseAuth auth;
    private static final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.App);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kaboo);



    }


    public void Actions()

    {

        SharedPreferences spoof = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String username = spoof.getString("LOGIN", null);
        String password = spoof.getString("PASS", null);

        if (username != null && password != null && lux == null) {
           Cusp();
        } else {

            Intent intent = new Intent(this, Hello.class);
            startActivity(intent);

        }


    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==  RC_SIGN_IN){


            if (resultCode == RESULT_OK)
            {



                startService(new Intent(Kaboo.this, Clipboard.class));
                startActivity(new Intent(getApplicationContext(), MainActivity.class));



            }else
            {
                startActivity(new Intent(getApplicationContext(), Errors.class));


            }


        }

    }



    public void Cusp()
    {

        auth = FirebaseAuth.getInstance();



        if (auth.getCurrentUser() != null) {

            startActivity(new Intent(getApplicationContext(),MainActivity.class));

        } else {

            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setIsSmartLockEnabled(false).setProviders(Arrays.asList(
                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
                    )).setLogo(R.drawable.product).build(),
                    RC_SIGN_IN);

        }


    }


    @Override
    protected void onStart() {
        super.onStart();

        Intent intents = getIntent();
        lux = intents.getStringExtra("straw");

        Actions();
    }
}
