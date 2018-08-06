package com.link.cloud.bean;

import io.realm.RealmObject;

/**
 * Created by 49488 on 2018/7/19.
 */

    public class DownLoadDataBean extends RealmObject {
        private String uid;
        private String shopId;
        private String userName;
        private String appid;
        private String feature;
        private int fingerId;
        private String deviceId;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getFeature() {
            return feature;
        }

        public void setFeature(String feature) {
            this.feature = feature;
        }

        public int getFingerId() {
            return fingerId;
        }

        public void setFingerId(int fingerId) {
            this.fingerId = fingerId;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }
    }

