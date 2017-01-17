package com.goshenapps.clipboard.MFM;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.goshenapps.clipboard.BottomSheets.MyBottomSheetDialogFragment;
import com.goshenapps.clipboard.MainActivity;
import com.goshenapps.clipboard.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class FireHolder extends RecyclerView.ViewHolder {

   TextView clipz,keyz,positron;
    RelativeTimeTextView timestamp;
    LinearLayout bottomSheet;



    public FireHolder(final View itemView) {
        super(itemView);

        clipz = (TextView) itemView.findViewById(R.id.clipz);
        keyz = (TextView) itemView.findViewById(R.id.keyz);
        positron = (TextView) itemView.findViewById(R.id.positron);
        timestamp = (RelativeTimeTextView) itemView.findViewById(R.id.timestamp);
        bottomSheet = (LinearLayout) itemView.findViewById(R.id.bottom_sheet);



    }


    public void setClips(String clipper){

        clipz.setText(clipper);
    }


    public void setPositrons(String positroner){

        positron.setText(positroner);
    }

    public void setKeys(String keyer) {

        keyz.setText(keyer);

    }








    public void setTickers(String timer)

    {


            long lins = Long.parseLong(timer);
            timestamp.setReferenceTime(lins);



    }



}
