package com.apartment.swamedia.sewaapartemenbandung.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmInboxReceiver extends BroadcastReceiver {
 
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent background = new Intent(context, NotifInboxServices.class);
        context.startService(background);
    }
 
}
