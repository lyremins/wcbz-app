package com.android.deviceinfo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import com.android.deviceinfo.BuildConfig;
import com.android.deviceinfo.MyApp;
import com.android.deviceinfo.bean.ConfigBean;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.Nullable;


public class AppUtils {

    /**
     * 上下文是否可用
     * @param context 上下文
     * @return 返回false禁止使用
     */
    public static boolean isContextFinishing(@Nullable Context context) {
        if (context == null) {
            return true;
        }
        if (context instanceof Activity) {
            return ((Activity) context).isFinishing();
        }
        return false;
    }

    /**
     * 从assets文件中获得Json串
     *
     * @param fileName
     * @return
     */
    public static String getJsonFromAssets(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(MyApp.getContext()
                    .getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ConfigBean.DataBean getConfig(){
        return new Gson().fromJson(getJsonFromAssets("json.txt"), ConfigBean.DataBean.class);
    }

    /**
     * 根据格式获取时间
     * @param pattern
     * @return
     */
    public static String getDateWithFormater(String pattern) {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
        return format.format(date);
    }

    /**
     * 跳转到系统设置界面
     *
     * @return
     */
    public static Intent getAppDetailSettingIntent() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", BuildConfig.APPLICATION_ID, null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", BuildConfig.APPLICATION_ID);
        }
        return localIntent;
    }

    /**
     * 字符串转化成为16进制字符串
     * @param s
     * @return
     */
    public static String strTo16(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }

    /**
     * 16进制转换成为string类型字符串
     * @param s
     * @return
     */
    public static String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "UTF-8");
            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    /**
     * 判断time1是否在time2之后 是的话返回true
     *
     * @return
     */
    public static boolean getTimeIsAfter(String time1, String time2) {
        if (TextUtils.isEmpty(time1) || TextUtils.isEmpty(time2)) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1, date2;
        try {
            date1 = sdf.parse(time1);
            date2 = sdf.parse(time2);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        if (date1.getTime() - date2.getTime() > 0) {
            return true;
        }
        return false;
    }

}
