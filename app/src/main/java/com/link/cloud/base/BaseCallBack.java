package com.link.cloud.base;

/**
 * Created by 49488 on 2018/7/20.
 */

public interface BaseCallBack <T>{
    void fail(Throwable e, boolean isNetWorkError);
    void errorCode(String msg);
}
