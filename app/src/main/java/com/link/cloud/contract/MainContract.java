package com.link.cloud.contract;

/**
 * Created by OFX002 on 2018/7/19.
 */

public interface MainContract {
    interface Model {
        void getSyncData(String deviceId);
    }

    interface View {
        void SyncSuccess();
        void SyncFail(String msg);
        void NetWorkError(Boolean isNet,Throwable e);
    }

    interface Presenter {
        void getSyncData(String deviceID);
    }
}
