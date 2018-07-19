package com.link.cloud.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.link.cloud.R;
import com.link.cloud.base.BaseEntity;
import com.link.cloud.bean.DownLoadData;
import com.link.cloud.contract.MainContract;
import com.link.cloud.presenter.MainPresenter;

public class MainActivity extends AppCompatActivity implements MainContract.View{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainPresenter presenter = new MainPresenter(this);
        presenter.getSyncData("1000D05SH72");
    }

    @Override
    public void SyncSuccess(BaseEntity<DownLoadData> downLoadData) {

    }

    @Override
    public void SyncFail(String msg) {

    }

    @Override
    public void NetWorkError(Boolean isNet, Throwable e) {

    }
}