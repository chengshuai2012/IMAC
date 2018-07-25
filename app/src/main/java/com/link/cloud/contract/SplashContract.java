package com.link.cloud.contract;

/**
 * Created by OFX002 on 2018/7/19.
 */

public interface SplashContract {

    interface View {
        void SyncSuccess();
        void SyncFail(String msg);
        void NetWorkError(Boolean isNet,Throwable e);
        void StartSync();
    }

    interface Presenter {
        void getSyncData(String deviceID);
    }
}
