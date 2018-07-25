package com.link.cloud.model;

import com.google.gson.JsonObject;
import com.link.cloud.base.BaseCallBack;
import com.link.cloud.base.BaseEntity;
import com.link.cloud.base.BaseObserver;
import com.link.cloud.bean.BindUserInfo;
import com.link.cloud.contract.BindContract;
import com.link.cloud.network.IOMainThread;
import com.link.cloud.network.RetrofitFactory;
import com.orhanobut.logger.Logger;

/**
 * Created by 49488 on 2018/7/20.
 */

public class BindModel implements BindContract.Model {
    public GetUserInfoCallBack mgetUserInfoCallBack;
    public interface GetUserInfoCallBack extends BaseCallBack{
        void success( BaseEntity<BindUserInfo> t);
    }
    public void setUserInfoCallBack(GetUserInfoCallBack mgetUserInfoCallBack){
        this.mgetUserInfoCallBack=mgetUserInfoCallBack;
    }
    @Override
    public void getUserInfo(String deviceID,int numberType,String numberValue,String fromType) {
        JsonObject params = new JsonObject();
        try {
            params.addProperty("deviceId", deviceID);
            params.addProperty("numberType", numberType);
            params.addProperty("numberValue", numberValue);
            params.addProperty("fromType", fromType);
        } catch (Exception e) {
            Logger.e("HttpClientHelper"+e.getMessage());
        }
        RetrofitFactory.getInstence().API().getMemInfo(params).compose(IOMainThread.<BaseEntity<BindUserInfo>>composeIO2main()).subscribe(new BaseObserver<BindUserInfo>() {
            @Override
            protected void onSuccees(BaseEntity<BindUserInfo> t) throws Exception {
                mgetUserInfoCallBack.success(t);
            }

            @Override
            protected void onCodeError(String msg) throws Exception {
                mgetUserInfoCallBack.errorCode(msg);
            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                mgetUserInfoCallBack.fail(e,isNetWorkError);
            }
        });
    }

    @Override
    public void bindUser() {

    }
}
