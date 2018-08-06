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
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.link.cloud.R;
import com.link.cloud.adapter.SignPagerViewAdapter;
import com.link.cloud.base.App;
import com.link.cloud.base.BaseActivity;
import com.link.cloud.bean.MdDevice;
import com.link.cloud.service.MdUsbService;
import com.link.cloud.utils.VenueUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import md.com.sdk.MicroFingerVein;

/**
 * Created by 49488 on 2018/7/19.
 */

public class SignVenueActivity extends BaseActivity implements ViewPager.OnPageChangeListener,VenueUtils.VenueCallBack {
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
    public static MdDevice mdDevice;
    UsbDeviceConnection usbDevConn;
    VenueUtils venueUtils;
    public  static MicroFingerVein microFingerVein;
    private final int MSG_REFRESH_LIST=0;
    private List<MdDevice> mdDevicesList=new ArrayList<MdDevice>();
    private SignPagerViewAdapter signPagerViewAdapter;

    @Override
    protected void setTime(String date, String time, String timeWithSecond) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signPagerViewAdapter = new SignPagerViewAdapter(this);
        signViewpager.setAdapter(signPagerViewAdapter);
        signViewpager.setOnPageChangeListener(this);
        Intent intent=new Intent(this,MdUsbService.class);
        bindService(intent,mdSrvConn, Service.BIND_AUTO_CREATE);
        venueUtils = App.getVenueUtils();
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
                        SignVenueActivity.this.usbDevConn =usbDevConn;
                    }
                });
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            microFingerVein=null;
        }
    };
    @Override
    protected int getLayoutID() {
        return R.layout.activity__sign_venue;

    }
    @OnClick({R.id.sign_next,R.id.sign_back})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.sign_next:
                int currentItem = signViewpager.getCurrentItem();
                signViewpager.setCurrentItem(currentItem+1);
                break;
            case R.id.sign_back:
               finish();
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
    private Handler listManageH=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case MSG_REFRESH_LIST:{
                    mdDevicesList.clear();
                    mdDevicesList=getDevList();
                    if(mdDevicesList.size()>0){
                        mdDevice=mdDevicesList.get(0);
                        venueUtils.initVenue(microFingerVein,SignVenueActivity.this,SignVenueActivity.this,true,false,false);
                    }else {
                        listManageH.sendEmptyMessageDelayed(MSG_REFRESH_LIST,1500L);
                    }
                    break;
                }
            }
            return false;
        }
    });
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
        if(signViewpager.getCurrentItem()==1) {
            runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                            signPagerViewAdapter.setIdentyMsg(msg);
                        }});
        }
    }

    @Override
    public void identifyMsg(String msg, String uid) {

    }
}
