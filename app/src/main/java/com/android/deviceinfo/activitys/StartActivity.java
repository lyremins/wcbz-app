package com.android.deviceinfo.activitys;

import android.content.Intent;
import android.os.Bundle;

import com.android.deviceinfo.MyApp;
import com.android.deviceinfo.activitys.login.LoginActivity;
import com.android.deviceinfo.activitys.login.UserBean;
import com.android.deviceinfo.activitys.main.MainActivity;
import com.android.deviceinfo.base.BaseActivity;
import com.android.deviceinfo.base.ICallBack;
import com.android.deviceinfo.utils.NetUtils;
import com.android.deviceinfo.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

public class StartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int uid = MyApp.sharedPreferences.getInt("user_id",0);
        if (uid == 0) {
            startActivity(new Intent(mContext, LoginActivity.class));
        }else {
            startActivity(new Intent(mContext, MainActivity.class));
            Map<String, String> map = new HashMap<>();
            map.put("username", MyApp.sharedPreferences.getString("name",""));
            map.put("password", MyApp.sharedPreferences.getString("password",""));
            NetUtils.executePostRequest(this,"v2/login", map,
                    new ICallBack<UserBean>() {
                        @Override
                        public void onSucceed(UserBean data) {
                            MyApp.editor.putInt("user_id",data.data.user_id);
                            MyApp.editor.putString("org",data.data.orgname);
                            MyApp.editor.apply();
                        }

                        @Override
                        public void onFailed(String msg) {
                        }
                    });
        }
        finish();
    }
}
