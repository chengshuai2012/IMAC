package com.link.cloud.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.link.cloud.R;
import com.link.cloud.base.BaseActivity;
import com.link.cloud.contract.SplashContract;
import com.link.cloud.presenter.SplashPresenter;
import com.link.cloud.utils.MyUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 49488 on 2018/7/21.
 */

public class EntranceActivity extends BaseActivity implements SplashContract.View {
    TextView versionName;
    @BindView(R.id.entrance_date)
    TextView entranceDate;
    @BindView(R.id.entrance_time)
    TextView entranceTime;
    @BindView(R.id.entrance_address)
    TextView entranceAddress;
    @BindView(R.id.entrance_weather)
    TextView entranceWeather;
    @BindView(R.id.face_add)
    TextView faceAdd;
    @BindView(R.id.venue_add)
    TextView venueAdd;
    @BindView(R.id.sign)
    ImageView sign;
    @BindView(R.id.finish_class)
    ImageView finishClass;
    @BindView(R.id.go_to_class)
    ImageView goToClass;
    @BindView(R.id.pay_count)
    ImageView payCount;
    private SplashPresenter presenter;
    ObjectAnimator rotationAnimator, translateAnimatorIn, translateAnimatorOut;
    OvershootInterpolator interpolator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initview();
    }

    private void initview() {
//        versionName.setText(MyUtils.getVerName(this));
//        interpolator = new OvershootInterpolator();
//        rotationAnimator = ObjectAnimator.ofFloat(fabExit, "rotation", 0, 360 * 2);
//        rotationAnimator.setDuration(1000);
//        rotationAnimator.setInterpolator(interpolator);
//        layoutTitle.setText("欢迎使用");
    }

    @Override
    public void SyncSuccess() {
        showToast("同步成功");
    }

    @Override
    public void SyncFail(String msg) {
        showToast(msg);
    }

    @Override
    public void NetWorkError(Boolean isNet, Throwable e) {
        if (isNet) {
            showToast("网络连接异常,请检查设备网络");
        } else {
            showToast(e.getMessage());
        }
    }

    @Override
    public void StartSync() {

    }


    @Override
    protected void setTime(String date, String time, String timeWithSecond) {
//        layoutTime.setText(timeWithSecond);
//        textTimeMain.setText(time);
//        dataTime.setText(date);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_entrance;
    }

    @OnClick({R.id.sign,R.id.go_to_class,R.id.finish_class,R.id.pay_count,R.id.face_add,R.id.venue_add})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.sign:
                if (MyUtils.isFastClick()) {
                    startAcivity(ChooseSignActivity.class);
                }
                break;
            case R.id.go_to_class:
                if (MyUtils.isFastClick()) {
                    startAcivity(GoToClassActivity.class);
                }
                break;
                case R.id.venue_add:
                if (MyUtils.isFastClick()) {
                    startAcivity(AddVenueActivity.class);
                }
                break;
        }
    }
//    @OnLongClick(R.id.layout_time)
//    public boolean onLongClick(View view){
//        SharedPreferences sharedPreferences=getSharedPreferences("user_info",0);
//        String deviceId=sharedPreferences.getString("deviceId","");
//        presenter.getSyncData(deviceId);
//        return true;
//    }


}
