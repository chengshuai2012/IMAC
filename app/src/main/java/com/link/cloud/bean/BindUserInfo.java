package com.link.cloud.bean;

/**
 * Created by 49488 on 2018/7/20.
 */

public class BindUserInfo {

    /**
     * sex : 0
     * phone : 18610933991
     * name : 秦永波
     * img : https://imagecdn.rocketbird.cn/test/image/9b24661e4001798aafbc9b638e3b8547.png@184w_184h_90q_1pr
     * userType : 0
     */

    private int sex;
    private String phone;
    private String name;
    private String img;
    private int userType;

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}
