package com.android.deviceinfo.utils;


import android.util.Log;

import com.android.deviceinfo.BuildConfig;

import androidx.annotation.NonNull;


/**
 *
 */

public class LogUtils {

    private LogUtils() {
        throw new RuntimeException("不要实例化我！");
    }

    /**
     * 输出info
     * @param TAG 标签
     * @param msg 消息
     */
    public static void i(@NonNull String TAG, @NonNull String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, msg + "");
        }
    }

    /**
     * 输出警报
     * @param TAG 标签
     * @param msg 消息
     */
    public static void w(@NonNull String TAG, @NonNull String msg) {
        if (BuildConfig.DEBUG) {
            Log.w(TAG, msg+ "");
        }
    }


    /**
     * 输出错误
     * @param TAG 标签
     * @param msg 消息
     */
    public static void e(@NonNull String TAG, @NonNull String msg) {
        if (BuildConfig.DEBUG) {
            // FIXME: 2018/9/25 以后如果需要统计自定义错误，可以在这里加
            Log.e(TAG, msg+ "");
        }
    }
}
