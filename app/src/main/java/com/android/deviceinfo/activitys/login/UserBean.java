package com.android.deviceinfo.activitys.login;

import com.android.deviceinfo.bean.BaseResponseBean;

/**
 */
public class UserBean extends BaseResponseBean {

    public DataBean data;

    public static class DataBean{
        public String username;
        public int user_id;
        public String orgname;
    }
}
