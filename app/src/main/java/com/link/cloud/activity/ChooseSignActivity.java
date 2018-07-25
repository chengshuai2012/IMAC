package com.link.cloud.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.link.cloud.R;
import com.link.cloud.base.BaseActivity;
import com.link.cloud.utils.MyUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 49488 on 2018/7/22.
 */

public class ChooseSignActivity extends BaseActivity {
    @BindView(R.id.entrance_date)
    TextView entranceDate;
    @BindView(R.id.entrance_name)
    TextView entranceName;
    @BindView(R.id.entrance_time)
    TextView entranceTime;
    @BindView(R.id.entrance_address)
    TextView entranceAddress;
    @BindView(R.id.entrance_weather)
    TextView entranceWeather;
    @BindView(R.id.entrance_local)
    RelativeLayout entranceLocal;
    @BindView(R.id.venue_sign)
    ImageView venueSign;
    @BindView(R.id.face_sign)
    ImageView faceSign;
    @BindView(R.id.scan_sign)
    ImageView scanSign;

    @Override
    protected void setTime(String date, String time, String timeWithSecond) {

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_choose_signway;
    }
    @OnClick({R.id.scan_sign,R.id.face_sign,R.id.venue_sign})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.scan_sign:
                if (MyUtils.isFastClick()) {
                    startAcivity(ChooseSignActivity.class);
                }
                break;
            case R.id.face_sign:
                if (MyUtils.isFastClick()) {
                    startAcivity(GoToClassActivity.class);
                }
                break;
            case R.id.venue_sign:
                if (MyUtils.isFastClick()) {
                    startAcivity(SignVenueActivity.class);
                }
                break;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
