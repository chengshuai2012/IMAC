package com.link.cloud.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.link.cloud.base.BaseActivity;
import com.link.cloud.base.BaseEntity;
import com.link.cloud.bean.BindUserInfo;
import com.link.cloud.contract.BindContract;
import com.link.cloud.presenter.BindPresenter;

/**
 * Created by 49488 on 2018/7/20.
 */

public class BindActivity extends BaseActivity implements BindContract.View{
    @Override
    protected void setTime(String date, String time, String timeWithSecond) {

    }

    @Override
    protected int getLayoutID() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindPresenter bindPresenter =new BindPresenter(this);
        bindPresenter.RequstUserInfo("",1,"","");
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
}
