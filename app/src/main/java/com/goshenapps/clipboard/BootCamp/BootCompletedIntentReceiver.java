package com.goshenapps.clipboard.BootCamp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;



public class BootCompletedIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
            Intent pushIntent = new Intent(context, Clipboard.class);
            context.startService(pushIntent);

    }
}