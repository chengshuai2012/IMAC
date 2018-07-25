package com.link.cloud.base;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.bugly.crashreport.CrashReport;

import io.realm.Realm;
import io.realm.RealmConfiguration;


/**
 * Created by OFX002 on 2018/7/19.
 */

public class App extends Application{
    public int count =0;
    public static final String COUNT_CHANGE = "change_count";
    public static App instances;
    public static App getInstances() {
        return instances;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instances =this;
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("myrealm.realm").build();
        Realm.setDefaultConfiguration(config);
        CrashReport.initCrashReport(getApplicationContext(), "62ab7bf668", true);
        this.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                count++;
                Log.e("onActivityStarted: ",count+"" );
                Intent countIntent = new Intent(COUNT_CHANGE);
                countIntent.putExtra("count",count);
                sendBroadcast(countIntent);
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                count--;
                Intent countIntent = new Intent(COUNT_CHANGE);
                countIntent.putExtra("count",count);
                sendBroadcast(countIntent);
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }
}
