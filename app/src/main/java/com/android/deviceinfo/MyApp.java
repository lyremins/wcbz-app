package com.android.deviceinfo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;

import com.android.deviceinfo.activitys.car.CarListBean;
import com.android.deviceinfo.activitys.plane.PlaneListBean;
import com.android.deviceinfo.base.ICallBack;
import com.android.deviceinfo.bean.ConfigBean;
import com.android.deviceinfo.utils.AppUtils;
import com.android.deviceinfo.utils.NetUtils;
import com.android.deviceinfo.utils.ThreadUtils;
import com.android.deviceinfo.utils.ToastUtil;
import com.bdsdk.BeidouSDK;
import com.bdsdk.ble.BeidouSDKHandler;
import com.google.gson.Gson;
import com.pancoit.bdboxsdk.bdsdk.BeidouHandler;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.List;


public class MyApp extends Application {

    private static MyApp mContext;

    public static ConfigBean.DataBean bean;

    public static SharedPreferences sharedPreferences;

    public static SharedPreferences.Editor editor;

    /**
     * 盒子ID
     */
    public static String bdBoxId = null;


    private static Handler mainHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        mainHandler = new Handler(getMainLooper());
        CrashReport.initCrashReport(getApplicationContext(), "1b24f34082", false);

        String json = sharedPreferences.getString("config","");
        if (!TextUtils.isEmpty(json)){
            bean = new Gson().fromJson(json,ConfigBean.DataBean.class);
        }else {
            bean = AppUtils.getConfig();
        }

        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        //注册北斗SDk
        BeidouHandler.getInstance().install(this);

        //初始化盒子SDK
//        BeidouSDK.getInstance(this);
//        BeidouSDKHandler.getInstance();
        getConfig();
    }

    public static MyApp getContext() {
        return mContext;
    }

    public static void getConfig(){
        NetUtils.executeGetRequest(mContext,"getConfig", null,
                new ICallBack<ConfigBean>() {
                    @Override
                    public void onSucceed(final ConfigBean data) {
                        try {
                            MyApp.bean = data.data.get(0);
                            ThreadUtils.exec(new Runnable() {
                                @Override
                                public void run() {
                                    editor.putString("config",new Gson().toJson(data.data.get(0)));
                                    editor.apply();
                                }
                            });
                        }catch (Exception e){

                        }

                    }

                    @Override
                    public void onFailed(final String msg) {

                    }
                });
    }

    public static List<String> strToList(String str){
        List<String> list = new ArrayList<>();
        if (TextUtils.isEmpty(str)){
            return list;
        }
        String[] array = str.split(",");
        for (int i = 0; i < array.length; i++){
            if (!TextUtils.isEmpty(array[i])){
                list.add(array[i]);
            }
        }
        return list;
    }

    public static String listToStr(List<String> list){
        if (list == null){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++){
            if (!TextUtils.isEmpty(list.get(0))){
                if (sb.toString().isEmpty()){
                    sb.append(list.get(i));
                }else {
                    sb.append(",").append(list.get(i));
                }
            }
        }
        return sb.toString();
    }

    public static String[] strToArray(String str){
        if (TextUtils.isEmpty(str)){
            return new String[0];
        }
        String[] array = str.split(",");
        return array;
    }

    /**
     * 获取飞机列表
     * @param callBack
     */
    public static void getPlane(Context context,final ICallBack<PlaneListBean> callBack){
        ThreadUtils.exec(new Runnable() {
            @Override
            public void run() {
                String json = sharedPreferences.getString("plane","");
                if (!TextUtils.isEmpty(json)){
                    final PlaneListBean data = new Gson().fromJson(json,PlaneListBean.class);
                    if (data != null){
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onSucceed(data);
                            }
                        });

                    }
                }
            }
        });
        NetUtils.executeGetRequest(context,"getAirplane", null,
                new ICallBack<PlaneListBean>() {
                    @Override
                    public void onSucceed(final PlaneListBean data) {
                        if (data == null) {
                            return;
                        }
                        if (data.isSucceed()) {
                            if (data.data == null || data.data.isEmpty()) {
//                                tvEmpty.setVisibility(View.VISIBLE);
                            } else {
                                ThreadUtils.exec(new Runnable() {
                                    @Override
                                    public void run() {
                                        editor.putString("plane",new Gson().toJson(data));
                                        editor.commit();
                                    }
                                });
                            }
                            callBack.onSucceed(data);
                        }
                    }

                    @Override
                    public void onFailed(final String msg) {
                        callBack.onFailed(msg);

                    }
                });
    }

}

