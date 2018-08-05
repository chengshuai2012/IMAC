package com.link.cloud.presenter;

import com.link.cloud.base.BaseEntity;
import com.link.cloud.bean.BindUserInfo;
import com.link.cloud.bean.DownLoadDataBean;
import com.link.cloud.bean.RegisterBean;
import com.link.cloud.contract.AppContract;
import com.link.cloud.model.AppModel;
import com.link.cloud.model.BindModel;
import com.orhanobut.logger.Logger;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by 49488 on 2018/7/25.
 */

public class AppPresenter implements AppContract.Presenter {
    AppContract.View view ;
    AppModel model;
    public AppPresenter(AppContract.View view){
        this.view=view;
        model= new AppModel();
    }
    @Override
    public void registerDevice(String deviceTargetValue,int deviceType) {
        model.setRegisterCallBack(new AppModel.RegisterCallBack() {
            @Override
            public void success(final BaseEntity<RegisterBean> t) {

                final Realm mRealm=Realm.getDefaultInstance();
                mRealm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<RegisterBean> all = realm.where(RegisterBean.class).findAll();
                        RegisterBean registerBean =t.getData();
                        if(all.size()>0){
                            all.deleteAllFromRealm();
                            realm.copyToRealm(registerBean);
                        }else {
                            realm.copyToRealm(registerBean);
                        }
                    }

                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        Logger.e("success");
                        view.registerDeviceSuccess(t);
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        Logger.e(error.getMessage());
                        view.Fail(error.getMessage());
                    }
                });

            }

            @Override
            public void fail(Throwable e, boolean isNetWorkError) {
                view.Error(isNetWorkError,e);
            }

            @Override
            public void errorCode(String msg) {
                Logger.e(msg);
                view.Fail(msg);
            }
        });
        model.registerDevice(deviceTargetValue,deviceType);
    }
}
