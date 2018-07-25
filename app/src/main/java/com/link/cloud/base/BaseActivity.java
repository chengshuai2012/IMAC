package com.link.cloud.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.link.cloud.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.link.cloud.utils.Constant.ACTION_UPDATEUI;

/**
 * Created by 49488 on 2018/7/20.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private TimeReceiver timeReceiver;
    private Unbinder bind;

    protected  void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutID());
        bind = ButterKnife.bind(this);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,WindowManager.LayoutParams. FLAG_FULLSCREEN);
        timeReceiver = new TimeReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_UPDATEUI);
        registerReceiver(timeReceiver, intentFilter);

    }

    protected void startAcivity(Class activity){
        Intent intent = new Intent();
        intent.setClass(this, activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
//        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }
    protected abstract  void setTime(String date,String time,String timeWithSecond);

    protected abstract int getLayoutID() ;

    public class TimeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
          setTime(intent.getStringExtra("timeData"),intent.getStringExtra("timeStr"),intent.getStringExtra("timethisStr"));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(timeReceiver);
        bind.unbind();
    }
}
