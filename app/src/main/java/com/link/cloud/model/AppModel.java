package com.link.cloud.model;

import android.util.Log;

import com.google.gson.JsonObject;
import com.link.cloud.base.BaseCallBack;
import com.link.cloud.base.BaseEntity;
import com.link.cloud.base.BaseObserver;
import com.link.cloud.bean.BindUserInfo;
import com.link.cloud.bean.RegisterBean;
import com.link.cloud.contract.AppContract;
import com.link.cloud.network.IOMainThread;
import com.link.cloud.network.RetrofitFactory;
import com.orhanobut.logger.Logger;

/**
 * Created by 49488 on 2018/7/25.
 */

public class AppModel implements AppContract.Model {
    public RegisterCallBack mRegisterCallBack;
    public interface RegisterCallBack extends BaseCallBack {
        void success( BaseEntity<RegisterBean> t);
    }
    public void setRegisterCallBack(RegisterCallBack mRegisterCallBack){
        this.mRegisterCallBack=mRegisterCallBack;
    }
    @Override
    public void registerDevice(String deviceTargetValue,int deviceType) {
        JsonObject params = new JsonObject();
        try {
            params.addProperty("deviceTargetValue", deviceTargetValue);
            params.addProperty("deviceType", deviceType);
        } catch (Exception e) {
            Logger.e("HttpClientHelper"+e.getMessage());
        }
        RetrofitFactory.getInstence().API().registerDevice(params).compose(IOMainThread.<BaseEntity<RegisterBean>>composeIO2main()).subscribe(new BaseObserver<RegisterBean>() {
            @Override
            protected void onSuccees(BaseEntity<RegisterBean> t) throws Exception {
                mRegisterCallBack.success(t);
            }

            @Override
            protected void onCodeError(String msg) throws Exception {
                Logger.e(msg);
                mRegisterCallBack.errorCode(msg);
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                Logger.e(e.getMessage());
                mRegisterCallBack.fail(e,isNetWorkError);
            }
        });
    }

}
