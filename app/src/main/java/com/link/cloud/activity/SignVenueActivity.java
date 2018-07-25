package com.link.cloud.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.link.cloud.R;
import com.link.cloud.adapter.SignPagerViewAdapter;
import com.link.cloud.base.BaseActivity;
import com.link.cloud.utils.MyUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 49488 on 2018/7/19.
 */

public class SignVenueActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    @BindView(R.id.entrance_date)
    TextView entranceDate;
    @BindView(R.id.entrance_name)
    TextView entranceName;
    @BindView(R.id.entrance_time)
    TextView entranceTime;
    @BindView(R.id.entrance_top)
    RelativeLayout entranceTop;
    @BindView(R.id.entrance_address)
    TextView entranceAddress;
    @BindView(R.id.entrance_weather)
    TextView entranceWeather;
    @BindView(R.id.entrance_local)
    RelativeLayout entranceLocal;
    @BindView(R.id.sign_first_image)
    ImageView signFirstImage;
    @BindView(R.id.sign_second_image)
    ImageView signSecondImage;
    @BindView(R.id.sign_third_image)
    ImageView signThirdImage;
    @BindView(R.id.sign_viewpager)
    ViewPager signViewpager;
    @BindView(R.id.sign_back)
    TextView signBack;
    @BindView(R.id.sign_next)
    TextView signNext;
    @BindView(R.id.sign_first_button)
    LinearLayout signFirstButton;

    @Override
    protected void setTime(String date, String time, String timeWithSecond) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SignPagerViewAdapter adapter = new SignPagerViewAdapter(this);
        signViewpager.setAdapter(adapter);
        signViewpager.setOnPageChangeListener(this);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity__sign_venue;

    }
    @OnClick({R.id.sign_next})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.sign_next:
                int currentItem = signViewpager.getCurrentItem();
                signViewpager.setCurrentItem(currentItem+1);
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


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if(position==0){
            signFirstImage.setBackgroundResource(R.drawable.pink_to_purple);
            signSecondImage.setBackgroundColor(getResources().getColor(R.color.transparent));
            signThirdImage.setBackgroundColor(getResources().getColor(R.color.transparent));
        }
        if(position==1){
            signSecondImage.setBackgroundResource(R.drawable.pink_to_purple);
            signFirstImage.setBackgroundColor(getResources().getColor(R.color.transparent));
            signThirdImage.setBackgroundColor(getResources().getColor(R.color.transparent));
        }
        if(position==2){
            signThirdImage.setBackgroundResource(R.drawable.pink_to_purple);
            signSecondImage.setBackgroundColor(getResources().getColor(R.color.transparent));
            signFirstImage.setBackgroundColor(getResources().getColor(R.color.transparent));
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
