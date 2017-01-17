package com.goshenapps.clipboard.BootCamp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Toast;

import com.google.i18n.phonenumbers.PhoneNumberMatch;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.goshenapps.clipboard.ActionPack.QrShare;
import com.goshenapps.clipboard.ActionPack.ShotTaker;
import com.goshenapps.clipboard.AppConstant;
import com.goshenapps.clipboard.MainActivity;
import com.goshenapps.clipboard.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NotificationReceiver extends BroadcastReceiver {
    String mString;
    private TextToSpeech tts;
    Random rand = new Random();
    int diceRoll;
    Notification.Builder mBuilder;
    Bitmap bitmapz;
    public static int WHITE = 0xFFFFFFFF;
    public static int BLACK = 0xFF000000;
    public final static int WIDTH = 100;
    File fileQR;
    Context kona;
    int UNIQUE_INT_PER_CALL =0;
    Iterator<PhoneNumberMatch> existsPhone;
    String webValue;
    String emailValue;
    String callValue;
    @Override
    public void onReceive(final Context context, Intent intent) {

        kona = context;
        mString = intent.getStringExtra("fishbowl");

        diceRoll = rand.nextInt(1000) + 1;


        // check if string contains  phone number
        if(mString != null) {

            Iterator<PhoneNumberMatch> existsPhone = PhoneNumberUtil.getInstance().findNumbers(mString, "IN").iterator();

            if (existsPhone.hasNext()) {
                long ayo = existsPhone.next().number().getNationalNumber();
                callValue = String.valueOf(ayo);
            }

        }

        // check if string contains link
        if (mString.contains("http://") || mString.contains("https://") || mString.contains("www."))
        {


            webValue = pullLinks(mString).get(0);

        }


        // check if string contains email address
        if (mString.contains("@")){

            Pattern p = Pattern.compile("\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b",
                    Pattern.CASE_INSENSITIVE);
            Matcher matcher = p.matcher(mString);
            while(matcher.find()) {
                emailValue = matcher.group();
            }
        }

        String action = intent.getAction();
        if (AppConstant.SHARE_ACTION.equals(action)) {

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "" + mString);
            context.startActivity(shareIntent);
        }

        if (AppConstant.QR_ACTION.equals(action)) {

            GenerateQr(context);

        }

        if (AppConstant.FIND_ACTION.equals(action)) {
            Uri uri = Uri.parse("http://www.google.com/#q=" + mString);
            Intent findintent = new Intent(Intent.ACTION_VIEW, uri);
            findintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(findintent);
        }

        if (AppConstant.TALK_ACTION.equals(action)) {


            Intent playIntent = new Intent(context, PlayService.class);
            playIntent.putExtra("fishbowl", "" + mString);
            context.startService(playIntent);


        }

        if (AppConstant.SHOT_ACTION.equals(action)) {
            Intent shotIntent = new Intent(context, ShotTaker.class);
            shotIntent.putExtra("fishbowl", "" + mString);
            shotIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(shotIntent);
        }


        if (AppConstant.COPY_ACTION.equals(action)) {
            Copys(mString);

        }

        if (AppConstant.CALL_ACTION.equals(action)) {


            if (existsPhone.hasNext()) {
              
                Intent callintent = new Intent(Intent.ACTION_DIAL);
                callintent.setData(Uri.parse("tel:"+callValue));
                kona.startActivity(callintent);

            }

        }

        if (AppConstant.WEB_ACTION.equals(action)) {
            Intent webintent= new Intent(Intent.ACTION_VIEW,Uri.parse(webValue));
            kona.startActivity(webintent);

        }

        if (AppConstant.EMAIL_ACTION.equals(action)) {

            String[] bull = {emailValue};
            composeEmail(bull,mString);


        }


    }


    // Generate the qr code from the copied text and create a Big Picture Styled notification

    public void GenerateQr(Context kontext) {

        bitmapz = null;
        try {
            bitmapz = encodeAsBitmap(mString);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        Saved(bitmapz);



    }


    // Method for endcoding to text to a QR code using the Zxing Qr library


    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 100, 0, 0, w, h);
        return bitmap;
    }


    public void Saved(Bitmap bmp) {
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/clipboard_images";
        File dir = new File(file_path);
        if (!dir.exists())
            dir.mkdirs();
        fileQR = new File(dir, "qrcode_" + diceRoll + ".png");
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(fileQR);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        bmp.compress(Bitmap.CompressFormat.PNG, 85, fOut);
        try {
            fOut.flush();
            fOut.close();
            notify(kona,bitmapz);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    public void notify(Context konky,Bitmap bitmaper)
    {

        Intent intents = new Intent(konky, QrShare.class);
        intents.putExtra("fileq",fileQR.getAbsolutePath());
        intents.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(konky,diceRoll, intents, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder = new Notification.Builder(konky)
                .setContentTitle(konky.getString(R.string.notice_qr_title))
                .setContentText(konky.getString(R.string.notice_qr_sub))
                .setSmallIcon(R.drawable.ic_stat_barcodes)
                .setContentIntent(pIntent)
                .setLargeIcon(BitmapFactory.decodeResource(konky.getResources(), R.drawable.ic_stat_barcodes))
                .setStyle(new Notification.BigPictureStyle()
                        .bigPicture(bitmaper)
                        .setBigContentTitle(konky.getString(R.string.notice_qr_title)));

        NotificationManager mNotificationManager = (NotificationManager) konky.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(diceRoll + 2, mBuilder.build());
    }



    public void Copys(String strings)
    {
        ClipboardManager clipboard = (ClipboardManager) kona.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("labels", strings);
        clipboard.setPrimaryClip(clip);
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
        if (intent.resolveActivity(kona.getPackageManager()) != null) {
            kona.startActivity(intent);
        }
    }


}