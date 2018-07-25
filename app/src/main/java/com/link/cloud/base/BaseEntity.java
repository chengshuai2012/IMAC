package com.link.cloud.base;

import java.util.ArrayList;

public class BaseEntity<T> {
    private static int SUCCESS_CODE=0;
    private int status;
    private String msg;
    private T data;

    public boolean isSuccess(){
        return getCode()==SUCCESS_CODE;
    }
    public int getCode() {
        return status;
    }

    public void setCode(int code) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }





}
