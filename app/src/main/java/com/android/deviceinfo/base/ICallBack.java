package com.android.deviceinfo.base;

/**
 * @author zhiyuan
 * @date 2018/9/18
 */

public interface ICallBack<T> {

    /**
     * 成功
     * @param data
     */
    void onSucceed(T data);

    /**
     * 失败
     * @param msg
     */
    void onFailed(String msg);
}
