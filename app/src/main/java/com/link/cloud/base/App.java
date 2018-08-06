package com.link.cloud.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.link.cloud.activity.SplashActivity;
import com.link.cloud.bean.RegisterBean;
import com.link.cloud.contract.AppContract;
import com.link.cloud.presenter.AppPresenter;
import com.link.cloud.utils.MyUtils;
import com.link.cloud.utils.VenueUtils;
import com.orhanobut.logger.Logger;

import io.realm.Realm;
import io.realm.RealmConfiguration;


/**
 * Created by OFX002 on 2018/7/19.
 */

public class App extends Application implements AppContract.View{
    private static final String TAG ="app" ;
    public int count =0;
    public static final String COUNT_CHANGE = "change_count";
    public static App instances;
    String deviceTargetValue;
    AppPresenter presenter;
    public static VenueUtils venueUtils;
    public  static VenueUtils getVenueUtils(){
        synchronized (VenueUtils.class) {
            if (venueUtils==null){
                return new VenueUtils();
            }
            return venueUtils;
        }
    }
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
       // CrashReport.initCrashReport(getApplicationContext(), "62ab7bf668", true);
        presenter= new AppPresenter(this);
        Thread.setDefaultUncaughtExceptionHandler(restartHandler);
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
        initCloudChannel(this);
    }

    private Thread.UncaughtExceptionHandler restartHandler = new Thread.UncaughtExceptionHandler() {
        public void uncaughtException(Thread thread, Throwable ex) {
            Throwable cause = ex.getCause();
            StringBuilder builder = new StringBuilder();
            builder.append(ex.getCause().toString()+"\r\n");
            for(int x=0;x<cause.getStackTrace().length;x++){
                builder.append("FileName:"+cause.getStackTrace()[x].getFileName()+">>>>Method:"+cause.getStackTrace()[x].getMethodName()+">>>>FileLine:"+cause.getStackTrace()[x].getLineNumber()+"\r\n");
            }

            Logger.e(builder.toString());
            restartApp();
        }
    };
    public void restartApp() {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());  //结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
    }

    private void initCloudChannel(final Context applicationContext) {
        PushServiceFactory.init(applicationContext);
        final CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {

                deviceTargetValue = MyUtils.getMD5(MyUtils.getMac());
                presenter.registerDevice(deviceTargetValue,1);
                Logger.e("init cloudchannel success" +"   deviceTargetValue:" + deviceTargetValue);

            }
            @Override
            public void onFailed(String errorCode, String errorMessage) {
                Logger.e("init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });

    }

    @Override
    public void Fail(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void Error(Boolean isNet, Throwable e) {
        Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void registerDeviceSuccess(BaseEntity<RegisterBean> t) {
        //Toast.makeText(this,"注册成功",Toast.LENGTH_SHORT).show();
    }

}
