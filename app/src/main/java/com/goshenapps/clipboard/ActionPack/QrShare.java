package com.goshenapps.clipboard.ActionPack;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.goshenapps.clipboard.R;
import com.squareup.picasso.Picasso;

import java.io.File;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class QrShare extends AppCompatActivity {

    ImageView qr_views;
    String fileString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_share);

       Intent intent = getIntent();
           fileString = intent.getStringExtra("fileq");
        qr_views = (ImageView) findViewById(R.id.qr_view);


        Uri uri = Uri.fromFile(new File(fileString));
        Picasso.with(getApplicationContext()).load(uri).into(qr_views);


    }


    public void Saved(View view)
    {

        scanMedia(fileString);
    }


    public void Shared(View view)
    {

        File files = new File(fileString);
        Uri uri = Uri.parse("file://"+files.getAbsolutePath());
        Intent share = new Intent(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        share.setType("image/*");
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(share, "Share screenshot"));

    }


    private void scanMedia(String path) {
        File file = new File(path);
        Uri uri = Uri.fromFile(file);
        Intent scanFileIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        sendBroadcast(scanFileIntent);
        snackTime(getString(R.string.snackTextQR));

    }


    public void snackTime(String string)
    {
        View parentLayout = findViewById(R.id.qr_share);
        Snackbar snackbar;

        snackbar = Snackbar.make(parentLayout, ""+string, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(Color.parseColor("#2E7D32"));
        snackbar.show();


    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

}
