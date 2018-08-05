package com.link.cloud.base;

/**
 * Created by 49488 on 2018/7/25.
 */
 public interface  BaseView {
    void Fail(String msg);
    void Error(Boolean isNet,Throwable e);
}
