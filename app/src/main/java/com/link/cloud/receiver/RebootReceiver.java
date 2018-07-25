package com.link.cloud.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.link.cloud.activity.SplashActivity;
import com.link.cloud.service.TimeService;

/**
 * Created by OFX002 on 2018/7/19.
 */

public class RebootReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent sayHelloIntent = new Intent(context, SplashActivity.class);
            sayHelloIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            sayHelloIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            context.startActivity(sayHelloIntent);
            try{
                context.startService(new Intent(context,TimeService.class));
            }catch (Exception e){

            }
        }
    }
}
