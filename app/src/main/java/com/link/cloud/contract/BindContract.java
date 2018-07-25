package com.link.cloud.contract;

import com.link.cloud.base.BaseEntity;
import com.link.cloud.bean.BindUserInfo;

/**
 * Created by 49488 on 2018/7/20.
 */

public interface BindContract {
    interface Model {
        void getUserInfo(String deviceID,int numberType,String numberValue,String fromType);
        void bindUser();
    }

    interface View {
        void getUserInfoSuccess();
        void getUserInfoFail();
        void getUserInfoError();
        void getUserBindSuccess(BaseEntity<BindUserInfo> t);
        void getUserBindFail(String msg);
        void getUserBindError(Boolean isNet,Throwable e);

    }

    interface Presenter {
        void RequstUserInfo(String deviceID,int numberType,String numberValue,String fromType);
        void RequstBindUser();
    }
}
