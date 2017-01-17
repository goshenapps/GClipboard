package com.goshenapps.clipboard.BottomSheets;

import android.app.SearchManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.i18n.phonenumbers.PhoneNumberMatch;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.goshenapps.clipboard.MFM.Fire;
import com.goshenapps.clipboard.MFM.FireHolder;
import com.goshenapps.clipboard.MainActivity;
import com.goshenapps.clipboard.QRCodes.QRDialogFragment;
import com.goshenapps.clipboard.R;
import com.goshenapps.clipboard.Siri.SiriDialogFragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.twitter.sdk.android.core.TwitterCore.TAG;

public class MyBottomSheetDialogFragment extends BottomSheetDialogFragment {

    String mString;
    ImageView call,email,sms,web;
    ImageView shared,qr,searched,speaker,camera,copy,delete;

    boolean isCall = false;
    boolean isEmail = false;
    boolean isWeb = false;

    String callValue;
    String emailValue;
    String webValue;

    FirebaseRecyclerAdapter adapter;

   public static MyBottomSheetDialogFragment newInstance(String string) {
        MyBottomSheetDialogFragment f = new MyBottomSheetDialogFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet, container, false);

        call = (ImageView) v.findViewById(R.id.call);
        sms = (ImageView) v.findViewById(R.id.sms);
        web = (ImageView) v.findViewById(R.id.web);
        email = (ImageView) v.findViewById(R.id.email);



        shared = (ImageView) v.findViewById(R.id.shared);
        qr = (ImageView) v.findViewById(R.id.qr);

        searched = (ImageView) v.findViewById(R.id.searched);
        speaker = (ImageView) v.findViewById(R.id.speaker);
        camera = (ImageView) v.findViewById(R.id.camera);
        delete = (ImageView) v.findViewById(R.id.delete);
        copy = (ImageView) v.findViewById(R.id.copy);






        call.setColorFilter(Color.GRAY);
        sms.setColorFilter(Color.GRAY);
        web.setColorFilter(Color.GRAY);
        email.setColorFilter(Color.GRAY);


        Opera(mString);


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isCall)
                {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+callValue));
                    startActivity(intent);
                }else {

                }
            }
        });



        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (isCall)
                {
                    Uri uri = Uri.parse("smsto:"+callValue);
                    Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                    it.putExtra("sms_body", mString);
                    startActivity(it);
                }else {

                }
            }
        });



        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isEmail)
                {

                    String[] bull = {emailValue};
                    composeEmail(bull,mString);

                }else {

                }

            }
        });


        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isWeb){
                    Intent intent= new Intent(Intent.ACTION_VIEW,Uri.parse(webValue));
                    startActivity(intent);
                }else {

                }
            }
        });



        shared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, ""+mString);
                startActivity(shareIntent);
            }
        });



        searched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://www.google.com/#q="+mString);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                QRDialogFragment qrDialogFragment =  QRDialogFragment.newInstance(mString);
                qrDialogFragment.show(getActivity().getSupportFragmentManager(),qrDialogFragment.getTag());
                dismiss();

            }
        });



        speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SiriDialogFragment sirDialog = SiriDialogFragment.newInstance(mString);
                sirDialog.show(getActivity().getSupportFragmentManager(), sirDialog.getTag());

                dismiss();
            }
        });


        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("labels", mString);
                clipboard.setPrimaryClip(clip);
                dismiss();

            }
        });



        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BurnFire(mString);

            }
        });

        return v;
    }




    public void Opera(String aString)
    {

        if(aString != null) {

            Iterator<PhoneNumberMatch> existsPhone = PhoneNumberUtil.getInstance().findNumbers(aString, "IN").iterator();

            if (existsPhone.hasNext()) {
                long ayo = existsPhone.next().number().getNationalNumber();
                callValue = String.valueOf(ayo);
                call.setColorFilter(null);
                sms.setColorFilter(null);
                isCall = true;

            }else {


            }



        }




        if (aString.contains("http://") || aString.contains("https://") || aString.contains("www."))
        {


            webValue = pullLinks(aString).get(0);
            web.setColorFilter(null);
            isWeb = true;

        }



        if (aString.contains("@")){

            Pattern p = Pattern.compile("\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b",
                    Pattern.CASE_INSENSITIVE);
            Matcher matcher = p.matcher(aString);
            while(matcher.find()) {
                emailValue = matcher.group();
                email.setColorFilter(null);
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
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }





    public void BurnFire(String lock)
    {

        if (lock.length() <= 30) {

            lock.replaceAll("([^a-zA-Z]|\\s)+", "");
        }else{
            lock = lock.substring(0, 30).replaceAll("([^a-zA-Z]|\\s)+", "");
        }

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String possy = sharedPref.getString("posit","");


        ((MainActivity) getContext()).Remover(Integer.parseInt(possy));
        dismiss();

    }


}