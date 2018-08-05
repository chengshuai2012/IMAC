package com.link.cloud.activity;


import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.usb.UsbDeviceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.view.ViewPager;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.link.cloud.R;
import com.link.cloud.adapter.BindPagerViewAdapter;
import com.link.cloud.base.App;
import com.link.cloud.base.BaseActivity;
import com.link.cloud.base.BaseEntity;
import com.link.cloud.bean.BindUserInfo;
import com.link.cloud.bean.MdDevice;
import com.link.cloud.contract.BindContract;
import com.link.cloud.network.IOMainThread;
import com.link.cloud.presenter.BindPresenter;
import com.link.cloud.service.MdUsbService;
import com.link.cloud.service.VenueService;
import com.link.cloud.utils.VenueUtils;
import com.orhanobut.logger.Logger;


import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import md.com.sdk.MicroFingerVein;


/**
 * Created by 49488 on 2018/7/20.
 */

public class BindActivity extends BaseActivity implements BindContract.View, ViewPager.OnPageChangeListener, BindPagerViewAdapter.BindViewListener, VenueUtils.VenueCallBack {
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
    @BindView(R.id.bind_first_image)
    ImageView bindFirstImage;
    @BindView(R.id.bind_second_image)
    ImageView bindSecondImage;
    @BindView(R.id.bind_third_image)
    ImageView bindThirdImage;
    @BindView(R.id.bind_four_image)
    ImageView bindFourImage;
    @BindView(R.id.bind_viewpager)
    ViewPager bindViewpager;
    @BindView(R.id.bind_first_button)
    LinearLayout bindFirstButton;
    @BindView(R.id.bind_back)
    TextView bindBack;
    @BindView(R.id.bind_next)
    TextView bindNext;
    private BindPagerViewAdapter bindPagerViewAdapter;
    UsbDeviceConnection usbDevConn;
    VenueUtils venueUtils;
    @Override
    protected void setTime(String date, String time, String timeWithSecond) {

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity__bind_venue;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindPresenter bindPresenter = new BindPresenter(this);
        bindPresenter.RequstUserInfo("", 1, "", "");
        bindPagerViewAdapter = new BindPagerViewAdapter(this,this);
        bindViewpager.setAdapter(bindPagerViewAdapter);
        bindViewpager.setOnPageChangeListener(this);
        bindViewpager.setBackgroundColor(getResources().getColor(R.color.transparent));
        bindFirstButton.setVisibility(View.INVISIBLE);
        Intent intent=new Intent(this,MdUsbService.class);
        bindService(intent,mdSrvConn, Service.BIND_AUTO_CREATE);
        venueUtils =App.getVenueUtils();
    }

    @Override
    public void getUserInfoSuccess() {

    }

    @Override
    public void getUserInfoFail() {

    }

    @Override
    public void getUserInfoError() {

    }

    @Override
    public void getUserBindSuccess(BaseEntity<BindUserInfo> t) {

    }

    @Override
    public void getUserBindFail(String msg) {
        showToast(msg);
    }

    @Override
    public void getUserBindError(Boolean isNet, Throwable e) {
        showToast(e.getMessage());
    }

    @OnClick({R.id.bind_next,R.id.bind_back})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.bind_next:
                int currentItem = bindViewpager.getCurrentItem();
                bindViewpager.setCurrentItem(currentItem + 1);
                break;
            case R.id.bind_back:
                finish();
                break;

        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            bindFirstImage.setBackgroundResource(R.drawable.pink_to_purple);
            bindSecondImage.setBackgroundColor(getResources().getColor(R.color.transparent));
            bindThirdImage.setBackgroundColor(getResources().getColor(R.color.transparent));
            bindFourImage.setBackgroundColor(getResources().getColor(R.color.transparent));
            bindViewpager.setBackgroundColor(getResources().getColor(R.color.transparent));
            bindFirstButton.setVisibility(View.INVISIBLE);
        }
        if (position == 2) {
            bindThirdImage.setBackgroundResource(R.drawable.pink_to_purple);
            bindSecondImage.setBackgroundColor(getResources().getColor(R.color.transparent));
            bindFirstImage.setBackgroundColor(getResources().getColor(R.color.transparent));
            bindViewpager.setBackgroundColor(getResources().getColor(R.color.black));
            bindFourImage.setBackgroundColor(getResources().getColor(R.color.transparent));
            bindFirstButton.setVisibility(View.VISIBLE);
        }
        if (position == 3) {
            bindThirdImage.setBackgroundColor(getResources().getColor(R.color.transparent));
            bindSecondImage.setBackgroundColor(getResources().getColor(R.color.transparent));
            bindFirstImage.setBackgroundColor(getResources().getColor(R.color.transparent));
            bindViewpager.setBackgroundColor(getResources().getColor(R.color.black));
            bindFourImage.setBackgroundResource(R.drawable.pink_to_purple);
            bindFirstButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void next(String phone) {
        int currentItem = bindViewpager.getCurrentItem();
        bindViewpager.setCurrentItem(currentItem + 1);
        bindViewpager.setBackgroundColor(getResources().getColor(R.color.black));
        bindFirstButton.setVisibility(View.VISIBLE);
        bindSecondImage.setBackgroundResource(R.drawable.pink_to_purple);
        bindFirstImage.setBackgroundColor(getResources().getColor(R.color.transparent));
        bindThirdImage.setBackgroundColor(getResources().getColor(R.color.transparent));
        bindFourImage.setBackgroundColor(getResources().getColor(R.color.transparent));
    }
    private ServiceConnection mdSrvConn=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MdUsbService.MyBinder myBinder=(MdUsbService.MyBinder)service;
            if(myBinder!=null){
                microFingerVein=myBinder.getMicroFingerVeinInstance();
                listManageH.removeMessages(MSG_REFRESH_LIST);
                listManageH.sendEmptyMessage(MSG_REFRESH_LIST);
                Logger.e("microFingerVein initialized OK,get microFingerVein from MdUsbService success.");
                myBinder.setOnUsbMsgCallback(new MdUsbService.UsbMsgCallback() {
                    @Override
                    public void onUsbConnSuccess(String usbManufacturerName, String usbDeviceName) {

                    }

                    @Override
                    public void onUsbDisconnect() {

                    }

                    @Override
                    public void onUsbDeviceConnection(UsbDeviceConnection usbDevConn) {
                        BindActivity.this.usbDevConn =usbDevConn;
                    }
                });
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            microFingerVein=null;
        }
    };
    public  static MicroFingerVein microFingerVein;
    private final int MSG_REFRESH_LIST=0;
    private List<MdDevice> mdDevicesList=new ArrayList<MdDevice>();
    private Handler listManageH=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case MSG_REFRESH_LIST:{
                    mdDevicesList.clear();
                    mdDevicesList=getDevList();
                    if(mdDevicesList.size()>0){
                        mdDevice=mdDevicesList.get(0);
                        venueUtils.initVenue(microFingerVein,BindActivity.this,BindActivity.this,false,true,false);
                    }else {
                        listManageH.sendEmptyMessageDelayed(MSG_REFRESH_LIST,1500L);
                    }
                    break;
                }
            }
            return false;
        }
    });
    public static MdDevice mdDevice;
    private List<MdDevice> getDevList(){
        List<MdDevice> mdDevList=new ArrayList<MdDevice>();
        if(microFingerVein!=null) {
            int deviceCount=MicroFingerVein.fvdev_get_count();
            for (int i = 0; i < deviceCount; i++) {
                MdDevice mdDevice = new MdDevice();
                mdDevice.setDeviceIndex(i);
                mdDevice.setDeviceNo(microFingerVein.getNo(i));
                mdDevList.add(mdDevice);
            }
        }else{
            Logger.e("microFingerVein not initialized by MdUsbService yet,wait a moment...");
        }
        return mdDevList;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listManageH.removeCallbacksAndMessages(null);
        if(usbDevConn!=null){
            usbDevConn.close();
        }
        unbindService(mdSrvConn);
        venueUtils.StopIdenty();
    }


    @Override
    public void modelMsg(final String msg) {
        if(bindViewpager.getCurrentItem()==1) {
            runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                            bindPagerViewAdapter.setModelMsg(msg);
                        }
                    }
            );
        }
    }

    @Override
    public void identifyMsg(String msg, String uid) {

    }
}
