package com.goshenapps.clipboard.QRCodes;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.goshenapps.clipboard.R;
import com.goshenapps.clipboard.Siri.SiriWaveView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;


public class QRDialogFragment extends BottomSheetDialogFragment  {

    String mString;
    RelativeLayout spinner;
    ImageView qrview,qrshare;
    File fileQR;

    Random rand = new Random();
    int diceRoll;
    public static int WHITE = 0xFFFFFFFF;
    public static int BLACK = 0xFF000000;
    public final static int WIDTH=150;
    public static QRDialogFragment newInstance(String string) {
        QRDialogFragment f = new QRDialogFragment();
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
        View contentView = View.inflate(getContext(), R.layout.qr_coder, null);
        dialog.setContentView(contentView);
        diceRoll = rand.nextInt(1000) + 1;
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if( behavior != null && behavior instanceof BottomSheetBehavior ) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
            ((BottomSheetBehavior) behavior).setPeekHeight(800);
            contentView.requestLayout();
        }



        spinner = (RelativeLayout)contentView.findViewById(R.id.spinner);
        qrview = (ImageView) contentView.findViewById(R.id.qrview);
        qrshare = (ImageView) contentView.findViewById(R.id.qr_share);



        qrshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Shared();
            }
        });


        try {
            Bitmap bitmap = encodeAsBitmap(mString);
            qrview.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }









    @Override
    public void onResume() {
        super.onResume();
    }


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
        bitmap.setPixels(pixels, 0, 150, 0, 0, w, h);
        Saved(bitmap);
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
        scanMedia();
        try {
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }




    public void Shared()
    {

        File files = new File(fileQR.getAbsolutePath());
        Uri uri = Uri.parse("file://"+files.getAbsolutePath());
        Intent share = new Intent(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        share.setType("image/*");
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(share, "Share screenshot"));

    }


    private void scanMedia() {
        File file = new File(fileQR.getAbsolutePath());
        Uri uri = Uri.fromFile(file);
        Intent scanFileIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        getContext().sendBroadcast(scanFileIntent);
        snackTime(getString(R.string.snackTextQR));

    }


    public void snackTime(String string)
    {
        Snackbar snackbar;

        snackbar = Snackbar.make(getView(), ""+string, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(Color.parseColor("#2E7D32"));
        snackbar.show();


    }



}