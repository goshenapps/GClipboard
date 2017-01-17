package com.goshenapps.clipboard;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.app.VoiceInteractor;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.NavigationView;
import android.support.transition.TransitionManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.i18n.phonenumbers.PhoneNumberMatch;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.goshenapps.clipboard.ActionPack.ShotTaker;
import com.goshenapps.clipboard.BootCamp.Clipboard;
import com.goshenapps.clipboard.BottomSheets.MyBottomSheetDialogFragment;
import com.goshenapps.clipboard.Intro.Hello;
import com.goshenapps.clipboard.LoginError.Errors;
import com.goshenapps.clipboard.MFM.Fire;
import com.goshenapps.clipboard.MFM.FireHolder;
import com.goshenapps.clipboard.QRCodes.QRDialogFragment;
import com.goshenapps.clipboard.Siri.SiriDialogFragment;
import com.goshenapps.clipboard.Welcome.Kaboo;
import com.squareup.picasso.Picasso;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.goshenapps.clipboard.R.id.clipz;
import static com.goshenapps.clipboard.R.id.email;
import static com.goshenapps.clipboard.R.id.positron;

public class MainActivity extends AppCompatActivity  implements
        NavigationView.OnNavigationItemSelectedListener{

    DividerItemDecoration mDividerItemDecoration;
    FirebaseAuth auth;
    TextView username,emails;
    CircleImageView circleImageView;

    Boolean isShowing = false;
    private int expandedPosition = -1;


    RecyclerView recyclerView;
    FirebaseRecyclerAdapter adapter;
    RecyclerView.AdapterDataObserver mObserver;

    private DatabaseReference mDatabase;
    ProgressDialog progressDialog;


    private static final long DRAWER_CLOSE_DELAY_MS = 250;
    private static final String NAV_ITEM_ID = "navItemId";

    private final Handler mDrawerActionHandler = new Handler();
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private int mNavItemId;


    NavigationView navigationView;



    boolean isCall = false;
    boolean isEmail = false;
    boolean isWeb = false;

    String callValue;
    String emailValue;
    String webValue;

    RelativeLayout spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        View hView =  navigationView.getHeaderView(0);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        spinner = (RelativeLayout) findViewById(R.id.spinner);
        spinner.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        circleImageView = (CircleImageView) hView.findViewById(R.id.circularcular);
        username = (TextView) hView.findViewById(R.id.username);
        emails = (TextView) hView.findViewById(R.id.emailz);








        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Clipboard");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // load saved navigation state if present
        if (null == savedInstanceState) {

        } else {
            mNavItemId = savedInstanceState.getInt(NAV_ITEM_ID);
        }

        // listen for navigation events

        // select the correct nav menu item

        // set up the hamburger icon to open and close the drawer
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open,
                R.string.close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        navigate(mNavItemId);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()) {



                    case R.id.mail:

                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto","goshenapps@gmail.com", null));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello Developer");
                        startActivity(Intent.createChooser(emailIntent, "Send email..."));

                        return true;


                    case R.id.share:

                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT,
                                "Best Clipboard app: https://play.google.com/store/apps/details?id="+getPackageName());
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);

                        return true;


                    case R.id.rate:
                        Toast.makeText(MainActivity.this, "Please rate this app", Toast.LENGTH_LONG).show();
                        Intent intet = new Intent(Intent.ACTION_VIEW);
                        intet.setData(Uri.parse("market://details?id=" + getPackageName()));
                        startActivity(intet);

                        return true;


                    case  R.id.logouts:
                        AuthUI.getInstance()
                                .signOut(MainActivity.this)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    public void onComplete(@NonNull Task<Void> task) {
                                        // user is now signed out

                                        SharedPreferences edits = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                                        SharedPreferences.Editor headies = edits.edit();
                                        headies.putString("LOGIN",null);
                                        headies.putString("PASS",null);
                                        headies.commit();
                                        stopService(new Intent(MainActivity.this,Clipboard.class));

                                        startActivity(new Intent(MainActivity.this,Kaboo.class));


                                        finish();
                                    }
                                });
                        return true;


                    default:

                        return true;
                }
            }
        });



    }






    @Override
    protected void onStart() {
        super.onStart();
        auth = FirebaseAuth.getInstance();



        if (auth.getCurrentUser() != null) {



            Activated();


        } else {


            startActivity(new Intent(getApplicationContext(), Kaboo.class));

        }


    }


    @Override
    protected void onResume() {
        super.onResume();




    }




    public static String printKeyHash(Activity context) {


        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

//            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=========", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }



    private void navigate(final int itemId) {
        // perform the actual navigation logic, updating the main content fragment etc
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem menuItem) {
        menuItem.setChecked(true);
        mNavItemId = menuItem.getItemId();

        // allow some time after closing the drawer before performing real navigation
        // so the user can see what is happening
        mDrawerLayout.closeDrawer(GravityCompat.START);
        mDrawerActionHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navigate(menuItem.getItemId());
            }
        }, DRAWER_CLOSE_DELAY_MS);
        return true;
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NAV_ITEM_ID, mNavItemId);
    }





    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if("com.goshenapps.clipboard.BootCamp.Clipboard".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }




    public void Activated()
    {
        if (isServiceRunning())
        {
            DataFetch();


        }else {
            startService(new Intent(MainActivity.this, Clipboard.class));
            DataFetch();



        }



    }







    public void DataFetch()
    {

        String banku = auth.getCurrentUser().getDisplayName().toString();
           username.setText(""+banku);
        emails.setText(auth.getCurrentUser().getEmail().toString()  );
            Picasso.with(getApplicationContext()).load(auth.getCurrentUser().getPhotoUrl()).into(circleImageView);
            mDatabase = FirebaseDatabase.getInstance().getReference();
            DatabaseReference raps = mDatabase.child(auth.getCurrentUser().getUid());
            adapter = new FirebaseRecyclerAdapter<Fire, FireHolder>(Fire.class, R.layout.acrobat, FireHolder.class, raps.orderByChild("sorts")) {
                @Override
                protected void populateViewHolder(FireHolder viewHolder, final Fire fire, final int position) {







                        viewHolder.setClips(fire.getClip());
                        viewHolder.setKeys(fire.getKey());
                        viewHolder.setTickers(fire.getTimestamp());
                        viewHolder.setPositrons("" + position);






                    View bottomSheet = (LinearLayout) viewHolder.itemView.findViewById(R.id.bottom_sheet);
                    final boolean isExpanded = position== expandedPosition;
                    bottomSheet.setVisibility(isExpanded?View.VISIBLE:View.GONE);
                    viewHolder.itemView.setActivated(isExpanded);

                    // Copy button onclicklistener!!
                    viewHolder.itemView.findViewById(R.id.copy).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Copys(fire.getClip());
                        }
                    });

                    // Qr button onclicklistener!!
                    viewHolder.itemView.findViewById(R.id.qr).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Qrs(fire.getClip());
                        }
                    });


                    // Search button onclicklistener!!
                    viewHolder.itemView.findViewById(R.id.searched).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Searchs(fire.getClip());
                        }
                    });


                   // Delete button onclicklistener!!
                    viewHolder.itemView.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Remover(position);
                        }
                    });



                    // Share button onclicklistener!!
                    viewHolder.itemView.findViewById(R.id.shared).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Shares(fire.getClip());
                        }
                    });



                    // Screenshot button onclicklistener!!
                    viewHolder.itemView.findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Cameras(fire.getClip());
                        }
                    });


                    // Speaker button onclicklistener!!
                    viewHolder.itemView.findViewById(R.id.speaker).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Speakers(fire.getClip());
                        }
                    });



                    // Copy button onclicklistener!!
                    viewHolder.itemView.findViewById(R.id.copy).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Copys(fire.getClip());
                        }
                    });


                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            expandedPosition = isExpanded ? -1:position;
                            TransitionManager.beginDelayedTransition(recyclerView);
                            notifyDataSetChanged();
                            isEmail = false;
                            isCall = false;
                            isWeb = false;


                            // Imageview call,sms,web,email are inline clipboard action icons
                            ImageView call = (ImageView) view.findViewById(R.id.call);
                            ImageView web = (ImageView) view.findViewById(R.id.web);
                            ImageView email = (ImageView) view.findViewById(R.id.email);
                            ImageView sms = (ImageView) view.findViewById(R.id.sms);


                            // all clipboard action icons are set to disabled by default
                            call.setColorFilter(Color.GRAY);
                            sms.setColorFilter(Color.GRAY);
                            web.setColorFilter(Color.GRAY);
                            email.setColorFilter(Color.GRAY);

                            // onclicklistener to check if the call action icon was clicked
                            call.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (isCall)
                                    {
                                        Intent intent = new Intent(Intent.ACTION_DIAL);
                                        intent.setData(Uri.parse("tel:"+callValue));
                                        startActivity(intent);
                                    }
                                }
                            });


                            // onclicklistener to check if the sms action icon was clicked
                            sms.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (isCall)
                                    {
                                        Intent intent = new Intent(Intent.ACTION_DIAL);
                                        intent.setData(Uri.parse("tel:"+callValue));
                                        startActivity(intent);
                                    }
                                }
                            });


                            // onclicklistener to check if the email action icon was clicked
                            email.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (isEmail)
                                    {
                                        String[] bull = {emailValue};
                                        composeEmail(bull,fire.getClip());
                                    }
                                }
                            });



                            // onclicklistener to check if the email action icon was clicked
                            web.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (isWeb)
                                    {
                                        Intent intent= new Intent(Intent.ACTION_VIEW,Uri.parse(webValue));
                                        startActivity(intent);
                                    }
                                }
                            });

                            // Method to run operations on String

                            Opera(fire.getClip());


                            if (isCall)
                            {
                                call.setColorFilter(null);
                                sms.setColorFilter(null);
                            }




                            if (isWeb)
                            {
                                web.setColorFilter(null);
                            }


                            if (isEmail)
                            {
                                email.setColorFilter(null);
                            }





                        }
                    });

                }
            };
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();



        raps.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                spinner.setVisibility(View.GONE);

                if (adapter.getItemCount() == 0){
                    String intro = getString(R.string.clip_intro);
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("intro", ""+intro);
                    clipboard.setPrimaryClip(clip);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }



    public void Gestura(String string,String banana)
    {

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("posit",""+banana);
        editor.commit();

        BottomSheetDialogFragment myBottomSheet = MyBottomSheetDialogFragment.newInstance(string);
        myBottomSheet.show(getSupportFragmentManager(), myBottomSheet.getTag());




    }



    public void Remover(int position)
    {
        adapter.getRef(position).removeValue();


    }



    public void Copys(String strings)
    {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("labels", strings);
        clipboard.setPrimaryClip(clip);
    }


    public void Qrs(String strings)
    {
        QRDialogFragment qrDialogFragment =  QRDialogFragment.newInstance(strings);
        qrDialogFragment.show(getSupportFragmentManager(),qrDialogFragment.getTag());

    }

    public void Searchs(String strings)
    {
        Uri uri = Uri.parse("http://www.google.com/#q="+strings);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void Shares(String strings)
    {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, ""+strings);
        startActivity(shareIntent);
    }

    public void Cameras(String strings)
    {

        Intent intent = new Intent(MainActivity.this, ShotTaker.class);
        intent.putExtra("fishbowl",""+strings);
        startActivity(intent);
    }

    public void Speakers(String strings)
    {
        SiriDialogFragment sirDialog = SiriDialogFragment.newInstance(strings);
        sirDialog.show(getSupportFragmentManager(), sirDialog.getTag());
    }














    public void Opera(String aString)
    {

        if(aString != null) {

            Iterator<PhoneNumberMatch> existsPhone = PhoneNumberUtil.getInstance().findNumbers(aString, "IN").iterator();

            if (existsPhone.hasNext()) {
                long ayo = existsPhone.next().number().getNationalNumber();
                callValue = String.valueOf(ayo);
                isCall = true;

            }else {


            }



        }




        if (aString.contains("http://") || aString.contains("https://") || aString.contains("www."))
        {


            webValue = pullLinks(aString).get(0);
            isWeb = true;

        }



        if (aString.contains("@")){

            Pattern p = Pattern.compile("\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b",
                    Pattern.CASE_INSENSITIVE);
            Matcher matcher = p.matcher(aString);
            while(matcher.find()) {
                emailValue = matcher.group();
                isEmail = true;
            }
        }


    }






    public ArrayList<String> pullLinks(String text)
    {
        ArrayList<String> links = new ArrayList<String>();

        //String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";
        String regex = "\\(?\\b(https?://|www[.]|ftp://)[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);

        while(m.find())
        {
            String urlStr = m.group();

            if (urlStr.startsWith("(") && urlStr.endsWith(")"))
            {
                urlStr = urlStr.substring(1, urlStr.length() - 1);
            }

            links.add(urlStr);
        }

        return links;
    }







    public void composeEmail(String[] addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }




}
