package com.link.cloud.contract;

import com.link.cloud.base.BaseEntity;
import com.link.cloud.base.BaseView;
import com.link.cloud.bean.BindUserInfo;
import com.link.cloud.bean.RegisterBean;

/**
 * Created by 49488 on 2018/7/25.
 */

public interface AppContract {
    interface Model {
        void registerDevice(String deviceTargetValue,int deviceType);
    }

    interface View extends BaseView{
        void registerDeviceSuccess(BaseEntity<RegisterBean> t);
    }

    interface Presenter {
        void registerDevice(String deviceTargetValue,int deviceType);
    }
}
