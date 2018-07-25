package com.link.cloud.presenter;

import com.google.gson.JsonObject;
import com.link.cloud.base.BaseEntity;
import com.link.cloud.base.BaseObserver;
import com.link.cloud.bean.DownLoadDataBean;
import com.link.cloud.contract.SplashContract;
import com.link.cloud.network.IOMainThread;
import com.link.cloud.network.RetrofitFactory;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * Created by OFX002 on 2018/7/19.
 */

public class SplashPresenter implements SplashContract.Presenter {

    SplashContract.View view;
    public SplashPresenter(SplashContract.View view) {
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
                .compose(IOMainThread.<BaseEntity<ArrayList<DownLoadDataBean>>>composeIO2main())
                .subscribe(new BaseObserver<ArrayList<DownLoadDataBean>>() {
                    @Override
                    protected void onSuccees(final BaseEntity<ArrayList<DownLoadDataBean>> t) throws Exception {
                        Logger.e("start"+t.getData().size()+">>>>>>");
                        long l = System.currentTimeMillis();
                        Realm mRealm=Realm.getDefaultInstance();
                        Number id = mRealm.where(DownLoadDataBean.class).max("id");
                        Logger.e(id+"" );
                        mRealm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                for (int x = 0; x < t.getData().size(); x++) {
                                    DownLoadDataBean downLoadDataBean = t.getData().get(x);
                                    downLoadDataBean.setId(x);
                                    realm.copyToRealm(downLoadDataBean);
                                }
                            }

                        }, new Realm.Transaction.OnSuccess() {
                            @Override
                            public void onSuccess() {
                                Logger.e("success");
                                view.SyncSuccess();
                            }
                        }, new Realm.Transaction.OnError() {
                            @Override
                            public void onError(Throwable error) {
                                Logger.e("error");
                                view.SyncFail(error.getMessage());
                            }
                        });

                    }

                    @Override
                    protected void onCodeError(String msg) throws Exception {
                        Logger.e(msg);
                        view.SyncFail(msg);
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        Logger.e(e.getMessage());
                        view.NetWorkError(isNetWorkError,e);
                    }

                    @Override
                    protected void onRequestStart() {
                        super.onRequestStart();
                        view.StartSync();
                    }
                });
    }
}
