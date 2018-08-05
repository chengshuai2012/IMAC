package com.link.cloud.bean;

import io.realm.RealmObject;

/**
 * Created by 49488 on 2018/7/25.
 */

public class RegisterBean extends RealmObject {

    /**
     * deviceId : pmljt8z
     * numberType : 1
     */

    private String deviceId;
    private int numberType;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getNumberType() {
        return numberType;
    }

    public void setNumberType(int numberType) {
        this.numberType = numberType;
    }
}
