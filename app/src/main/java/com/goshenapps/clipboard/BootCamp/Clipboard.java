package com.goshenapps.clipboard.BootCamp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.i18n.phonenumbers.PhoneNumberMatch;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.goshenapps.clipboard.AppConstant;
import com.goshenapps.clipboard.MainActivity;
import com.goshenapps.clipboard.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.security.AccessController.getContext;


public class Clipboard extends Service {
    private ClipboardManager mCM;
    IBinder mBinder;
    int mStartMode;
    FirebaseAuth auth;
     DatabaseReference mDatabase;
    DatabaseReference ref;
    String newClip;


    boolean isCall = false;
    boolean isEmail = false;
    boolean isWeb = false;

    String callValue;
    String emailValue;
    String webValue;


    Random rand = new Random();
    int diceRoll;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        diceRoll = rand.nextInt(1000) + 1;

        mCM = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        mCM.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {

            @Override
            public void onPrimaryClipChanged() {
                ClipData clipData = mCM.getPrimaryClip();
                ClipData.Item item = clipData.getItemAt(0);
                newClip = item.getText().toString();
                auth = FirebaseAuth.getInstance();
                mDatabase = FirebaseDatabase.getInstance().getReference().child(auth.getCurrentUser().getUid());
                String lock = newClip.toString();

                if (lock.length() <= 30) {

                    lock.replaceAll("([^a-zA-Z]|\\s)+", "");
                }else{
                    lock = lock.substring(0, 30).replaceAll("([^a-zA-Z]|\\s)+", "");
                }



                ref = mDatabase.child(lock);
                ref.child("timestamp").setValue(String.valueOf(new Date().getTime()));
                ref.child("clip").setValue(newClip.toString());
                ref.child("key").setValue(lock);
                ref.child("sorts").setValue(-1 * new Date().getTime());
                buildNotification();





            }
        });
        return mStartMode;
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }







    public void buildNotification() {

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(this,0, intent, 0);



        Intent shareReceive = new Intent();
        shareReceive.setAction(AppConstant.SHARE_ACTION);
        shareReceive.putExtra("fishbowl",newClip.toString());
        PendingIntent pendingIntentShare = PendingIntent.getBroadcast(this, diceRoll+123, shareReceive, PendingIntent.FLAG_UPDATE_CURRENT);


        Intent qrReceive = new Intent();
        qrReceive.setAction(AppConstant.QR_ACTION);
        qrReceive.putExtra("fishbowl",newClip.toString());
        PendingIntent pendingIntentQR = PendingIntent.getBroadcast(this, diceRoll+124, qrReceive, PendingIntent.FLAG_UPDATE_CURRENT);


        Intent findReceive = new Intent();
        findReceive.setAction(AppConstant.FIND_ACTION);
        findReceive.putExtra("fishbowl",newClip.toString());
        PendingIntent pendingIntentFIND = PendingIntent.getBroadcast(this, diceRoll+125, findReceive, PendingIntent.FLAG_UPDATE_CURRENT);



        Intent talkReceive = new Intent();
        talkReceive.setAction(AppConstant.TALK_ACTION);
        talkReceive.putExtra("fishbowl",newClip.toString());
        PendingIntent pendingIntentTALK = PendingIntent.getBroadcast(this, diceRoll+126, talkReceive, PendingIntent.FLAG_UPDATE_CURRENT);


        Intent shotReceive = new Intent();
        shotReceive.setAction(AppConstant.SHOT_ACTION);
        shotReceive.putExtra("fishbowl",newClip.toString());
        PendingIntent pendingIntentSHOT = PendingIntent.getBroadcast(this, diceRoll+127, shotReceive, PendingIntent.FLAG_UPDATE_CURRENT);


        Intent copyReceive = new Intent();
        copyReceive.setAction(AppConstant.COPY_ACTION);
        copyReceive.putExtra("fishbowl",newClip.toString());
        PendingIntent pendingIntentCOPY = PendingIntent.getBroadcast(this, diceRoll+157, copyReceive, PendingIntent.FLAG_UPDATE_CURRENT);


        RemoteViews notificationView = new RemoteViews(this.getPackageName(), R.layout.notificate);
        notificationView.setCharSequence(R.id.notifitext, "setText", ""+newClip.toString());
        notificationView.setOnClickPendingIntent(R.id.share_menu,pendingIntentShare);
        notificationView.setOnClickPendingIntent(R.id.qr_menu,pendingIntentQR);
        notificationView.setOnClickPendingIntent(R.id.find_menu,pendingIntentFIND);
        notificationView.setOnClickPendingIntent(R.id.talk_menu,pendingIntentTALK);
        notificationView.setOnClickPendingIntent(R.id.shot_menu,pendingIntentSHOT);
        notificationView.setOnClickPendingIntent(R.id.copy_action,pendingIntentCOPY);


        Opera(newClip.toString());

        if (isCall)
        {
            notificationView.setViewVisibility(R.id.call_action,1);
            Intent callReceive = new Intent();
            callReceive.setAction(AppConstant.CALL_ACTION);
            callReceive.putExtra("fishbowl",newClip.toString());
            PendingIntent pendingIntentCALL = PendingIntent.getBroadcast(this, diceRoll+128, callReceive, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationView.setOnClickPendingIntent(R.id.call_action,pendingIntentCALL);

        }


        if (isWeb)
        {
            notificationView.setViewVisibility(R.id.link_action,1);
            Intent webReceive = new Intent();
            webReceive.setAction(AppConstant.WEB_ACTION);
            webReceive.putExtra("fishbowl",newClip.toString());
            PendingIntent pendingIntentWEB = PendingIntent.getBroadcast(this, diceRoll+129, webReceive, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationView.setOnClickPendingIntent(R.id.link_action,pendingIntentWEB);


        }


        if (isEmail)
        {
            notificationView.setViewVisibility(R.id.mail_action,1);
            Intent emailReceive = new Intent();
            emailReceive.setAction(AppConstant.EMAIL_ACTION);
            emailReceive.putExtra("fishbowl",newClip.toString());
            PendingIntent pendingIntentEMAIL = PendingIntent.getBroadcast(this, diceRoll+130, emailReceive, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationView.setOnClickPendingIntent(R.id.call_action,pendingIntentEMAIL);
        }


        RemoteViews headsView = new RemoteViews(this.getPackageName(), R.layout.heads_notifi);
        headsView.setCharSequence(R.id.notifitext, "setText", ""+newClip.toString());


        Notification.Builder builder = new Notification.Builder(getApplicationContext())
        .setSmallIcon(R.drawable.clipboarder)
             .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                     R.mipmap.ic_launcher))
                .setContentTitle(newClip.toString() )
                .setContentText(getApplicationContext().getString(R.string.ticker))
                .setTicker(getApplicationContext().getString(R.string.ticker))
                .setAutoCancel(true)
                .setContentIntent(pIntent)
                .setPriority(Notification.PRIORITY_MIN)
                .setDefaults(Notification.DEFAULT_SOUND);

        final Notification notification = builder.build();
        notification.bigContentView = notificationView;



        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(diceRoll, notification);

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

}