package com.android.deviceinfo.bean;

public class BaseResponseBean {

    private String status;

    public String message;

    public boolean isSucceed(){
        return "1".equals(status);
    }
}
