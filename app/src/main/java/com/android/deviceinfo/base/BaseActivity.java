package com.android.deviceinfo.base;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.os.Bundle;

import com.android.deviceinfo.MyApp;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.Calendar;

public class BaseActivity extends FragmentActivity {

    protected Context mContext;

    private KProgressHUD mHud;

    protected Calendar ca = Calendar.getInstance();

    protected int mYear;
    protected int mMonth;
    protected int mDay;

    protected String orgname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mHud = KProgressHUD.create(this).setLabel("加载中");
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
        orgname = MyApp.sharedPreferences.getString("org","");
        if (orgname == null){
            orgname = "";
        }
    }

    public void showHud(){
        mHud.show();
    }

    public void disMissHud(){
        mHud.dismiss();
    }

    public void setHudLable(String lable){
        mHud.setLabel(lable);
    }
}
