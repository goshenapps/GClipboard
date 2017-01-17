package com.goshenapps.clipboard.ActionPack;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.goshenapps.clipboard.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;


public class ShotTaker extends AppCompatActivity {

    SeekBar seekBar;
    TextView textView;
    String mString;
    File file;
    CardView cardView;
    LinearLayout canvases;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shot_taker);

        Initialize();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                textView.setTextSize(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }


    public  void Initialize()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.toolbar_screenshot);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }


        Intent intent = getIntent();
        mString = intent.getStringExtra("fishbowl");
        cardView = (CardView) findViewById(R.id.cardoso);
        canvases = (LinearLayout) findViewById(R.id.canvas);
        seekBar = (SeekBar) findViewById(R.id.seekBar1);
        seekBar.setMax(30);
        textView = (TextView) findViewById(R.id.textviewer);
        textView.setText(""+mString);
    }





    private void ShareImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/clipboard_images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        file = new File(myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            scanMedia(file.getAbsolutePath());

            Uri uri = Uri.parse("file://"+file.getAbsolutePath());
            Intent share = new Intent(Intent.ACTION_SEND);
            share.putExtra(Intent.EXTRA_STREAM, uri);
            share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            share.setType("image/*");
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(share, "Share screenshot"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/clipboard_images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        file = new File(myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            scanMedia(file.getAbsolutePath());

            snackTime(getString(R.string.snackText));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    public Bitmap screenShot() {

        Bitmap bitmap = Bitmap.createBitmap(canvases.getWidth(),
                canvases.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvases.draw(canvas);
        return bitmap;
    }

    public void Snap(View view)
    {
        Bitmap bitmaps = null;
        bitmaps = screenShot();
        SaveImage(bitmaps);

    }



    public void ColoMan(final String strings)
    {

        ColorPickerDialogBuilder
                .with(ShotTaker.this)
                .setTitle("Choose color")
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {

                        if (strings.matches("fonts")){
                            textView.setTextColor(selectedColor);
                        }else {

                            cardView.setCardBackgroundColor(selectedColor);
                        }

                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }



    public void textPaint(View view)
    {
        ColoMan("fonts");
    }


    public void cardPaint(View view)
    {
        ColoMan("");
    }


    public void snackTime(String string)
    {
        View parentLayout = findViewById(R.id.shot_taker);
        Snackbar snackbar;

        snackbar = Snackbar.make(parentLayout, ""+string, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(Color.parseColor("#2E7D32"));
        snackbar.show();


    }


    public void Shares(View view)
    {
        Bitmap bitmaps = null;
        bitmaps = screenShot();
        ShareImage(bitmaps);
    }


    private void scanMedia(String path) {
        File file = new File(path);
        Uri uri = Uri.fromFile(file);
        Intent scanFileIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        sendBroadcast(scanFileIntent);
        snackTime(getString(R.string.snackText));

    }

}
