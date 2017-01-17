package com.goshenapps.clipboard.BootCamp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.IBinder;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goshenapps.clipboard.R;

import java.util.Random;

public class ShotService extends Service {

    String mString;
    Random rand = new Random();
    int diceRoll;
    Notification.Builder mBuilder;
    Bitmap bitmap;
    LinearLayout linearLayout;
    public ShotService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mString = intent.getStringExtra("fishbowl");
        diceRoll = rand.nextInt(1000) + 1;


        linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundColor(Color.parseColor("#263238"));
        linearLayout.setPadding(10, 10, 10, 10); // in pixels (left, top, right, bottom)

        // Add textviews
        TextView textView1 = new TextView(this);
        textView1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        textView1.setText(""+mString);
        textView1.setTextColor(Color.parseColor("#FFFFFF"));
        linearLayout.addView(textView1);

        bitmap = null;
       bitmap = screenShot();
        NoticeMe();


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }



    public Bitmap screenShot() {

        Bitmap bitmap = Bitmap.createBitmap(linearLayout.getWidth(),
                linearLayout.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        linearLayout.draw(canvas);
        return bitmap;
    }




    public void NoticeMe()
    {

        mBuilder = new Notification.Builder(getApplicationContext())
                .setContentTitle(getApplicationContext().getString(R.string.notice_qr_title))
                .setContentText(getApplicationContext().getString(R.string.notice_qr_sub))
                .setSmallIcon(android.R.drawable.ic_menu_camera)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), android.R.drawable.ic_menu_camera))
                .setStyle(new Notification.BigPictureStyle()
                        .bigPicture(bitmap)
                        .setBigContentTitle(getApplicationContext().getString(R.string.notice_qr_title)));

        NotificationManager mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(diceRoll+3,mBuilder.build());
    }
}
