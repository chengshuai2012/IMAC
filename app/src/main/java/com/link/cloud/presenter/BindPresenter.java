package com.link.cloud.presenter;

import com.link.cloud.base.BaseCallBack;
import com.link.cloud.base.BaseEntity;
import com.link.cloud.bean.BindUserInfo;
import com.link.cloud.contract.BindContract;
import com.link.cloud.model.BindModel;

/**
 * Created by 49488 on 2018/7/20.
 */

public class BindPresenter implements BindContract.Presenter {
    BindContract.View view ;
    BindModel model;
    public BindPresenter(BindContract.View view){
        this.view=view;
        model= new BindModel();
    }
    @Override
    public void RequstUserInfo(String deviceID,int numberType,String numberValue,String fromType) {
        model.setUserInfoCallBack(new BindModel.GetUserInfoCallBack() {
            @Override
            public void success(BaseEntity<BindUserInfo> t) {
                view.getUserBindSuccess(t);
            }

            @Override
            public void fail(Throwable e, boolean isNetWorkError) {
                view.getUserBindError(isNetWorkError,e);
            }

            @Override
            public void errorCode(String msg) {
                view.getUserBindFail(msg);
            }
        });
        model.getUserInfo(deviceID,numberType,numberValue,fromType);
    }

    @Override
    public void RequstBindUser() {

    }
}
