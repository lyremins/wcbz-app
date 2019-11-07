package com.android.deviceinfo;

/**
 * Created by dong on 2016/6/27.
 * APP监听器
 */
public interface AppListener {

    /**
     * 蓝牙连接状态改变
     */
    void onConnectStatusChange(Class<?> cls, String status);
}
