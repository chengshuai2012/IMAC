package com.link.cloud.presenter;

import com.google.gson.JsonObject;
import com.link.cloud.base.BaseEntity;
import com.link.cloud.base.BaseObserver;
import com.link.cloud.bean.DownLoadData;
import com.link.cloud.contract.MainContract;
import com.link.cloud.network.IOMainThread;
import com.link.cloud.network.RetrofitFactory;
import com.orhanobut.logger.Logger;

/**
 * Created by OFX002 on 2018/7/19.
 */

public class MainPresenter implements MainContract.Presenter {

    MainContract.View view;
    public MainPresenter(MainContract.View view) {
        this.view=view;
    }

    @Override
    public void getSyncData(String deviceID) {
        JsonObject params = new JsonObject();
        try {
            params.addProperty("deviceId", deviceID);
        } catch (Exception e) {
            Logger.e("HttpClientHelper"+e.getMessage());
        }
        RetrofitFactory.getInstence().API()
                .syncUserFeature(params)
                .compose(IOMainThread.<BaseEntity<DownLoadData>>compose())
                .subscribe(new BaseObserver<DownLoadData>() {
                    @Override
                    protected void onSuccees(BaseEntity<DownLoadData> t) throws Exception {
                        view.SyncSuccess(t);
                    }

                    @Override
                    protected void onCodeError(String msg) throws Exception {
                        view.SyncFail(msg);
                    }



                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        view.NetWorkError(isNetWorkError,e);
                    }
                });
    }
}
