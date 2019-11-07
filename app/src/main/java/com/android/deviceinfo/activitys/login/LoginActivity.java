package com.android.deviceinfo.activitys.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.deviceinfo.MyApp;
import com.android.deviceinfo.R;
import com.android.deviceinfo.activitys.chat.ContactListActivity;
import com.android.deviceinfo.activitys.main.MainActivity;
import com.android.deviceinfo.base.BaseActivity;
import com.android.deviceinfo.base.ICallBack;
import com.android.deviceinfo.bean.BaseResponseBean;
import com.android.deviceinfo.constants.NetContants;
import com.android.deviceinfo.utils.NetUtils;
import com.android.deviceinfo.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录界面
 */
public class LoginActivity extends BaseActivity {

    private ImageView imgClose;
    private EditText etName;
    private EditText etPwd;
    private TextView tvLoginButton;
    private TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        setHudLable("正在登录");
    }

    private void initView() {
        imgClose = findViewById(R.id.img_close);
        etName = findViewById(R.id.et_name);
        etPwd = findViewById(R.id.et_login_code);
        tvLoginButton = findViewById(R.id.tv_login_button);
        tvRegister = findViewById(R.id.tv_register);

        // 登录按钮
        tvLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 登录
     */
    private void login() {
        final String name = etName.getText().toString().trim();
        final String pwd = etPwd.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.toastCenter(mContext, "请输入用户名");
            return;
        }
        if (TextUtils.isEmpty(name)) {
            ToastUtil.toastCenter(mContext, "请输入用户名");
            return;
        }
        showHud();
        Map<String, String> map = new HashMap<>();
        map.put("username", name);
        map.put("password", pwd);
        NetUtils.executePostRequest(this,"v2/login", map,
                new ICallBack<UserBean>() {
                    @Override
                    public void onSucceed(UserBean data) {
                        disMissHud();
                        ToastUtil.toastCenter(mContext,"登录成功");
                        MyApp.editor.putString("name",data.data.username);
                        MyApp.editor.putString("password",pwd);
                        MyApp.editor.putInt("user_id",data.data.user_id);
                        MyApp.editor.putString("org",data.data.orgname);
                        MyApp.editor.apply();
                        startActivity(new Intent(mContext, MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onFailed(String msg) {
                        disMissHud();
                        ToastUtil.toastCenter(mContext,msg);
                    }
                });
    }
}
