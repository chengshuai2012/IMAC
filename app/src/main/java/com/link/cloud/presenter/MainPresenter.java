package com.link.cloud.presenter;

import com.google.gson.JsonObject;
import com.link.cloud.base.BaseEntity;
import com.link.cloud.base.BaseObserver;
import com.link.cloud.bean.DownLoadDataBean;
import com.link.cloud.contract.MainContract;
import com.link.cloud.network.IOMainThread;
import com.link.cloud.network.RetrofitFactory;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

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
                .compose(IOMainThread.<BaseEntity<ArrayList<DownLoadDataBean>>>composeIO2main())
                .subscribe(new BaseObserver<ArrayList<DownLoadDataBean>>() {
                    @Override
                    protected void onSuccees(final BaseEntity<ArrayList<DownLoadDataBean>> t) throws Exception {

                        Logger.e("start"+t.getData().size()+">>>>>>");
                        long l = System.currentTimeMillis();

                        Realm mRealm=Realm.getDefaultInstance();
                        mRealm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                for(int x=0;x<t.getData().size();x++){
                                    DownLoadDataBean downLoadDataBean =t.getData().get(x);
                                    downLoadDataBean.setId(x);
                                    realm.copyToRealm(downLoadDataBean);
                                }

                            }
                        });
                        Logger.e(System.currentTimeMillis()-l+"时耗");

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
                });
    }
}
