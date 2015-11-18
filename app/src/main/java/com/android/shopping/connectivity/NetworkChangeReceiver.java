package com.android.shopping.connectivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.android.shopping.HomeFragment;

/**
 * Created by rejina on 11/14/2015.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        String status = NetworkUtil.getConnectivityStatusString(context);
        if(status == "Wifi enabled" || status == "Mobile data enabled"){
                
        }
    }
}
