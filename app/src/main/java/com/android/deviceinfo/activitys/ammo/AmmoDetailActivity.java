package com.android.deviceinfo.activitys.ammo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.deviceinfo.MyApp;
import com.android.deviceinfo.R;
import com.android.deviceinfo.base.BaseActivity;
import com.android.deviceinfo.base.ICallBack;
import com.android.deviceinfo.bean.BaseResponseBean;
import com.android.deviceinfo.utils.AppUtils;
import com.android.deviceinfo.utils.NetUtils;
import com.android.deviceinfo.utils.ThreadUtils;
import com.android.deviceinfo.utils.ToastUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class AmmoDetailActivity extends BaseActivity {

    private RelativeLayout rlTitle;
    private ImageView imgBack;
    private TextView btnAdd;
    private EditText etModel;
    private EditText etFacNum;
    private EditText etFactory;
    private EditText etDayLife;
    private TextView tvTaskState;
    private EditText etTotalHour;
    private EditText etHasHour;

    AmmoBean.DataBean dataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ammo_detail);
        initView();
    }

    private void initView() {
        dataBean = (AmmoBean.DataBean) getIntent().getSerializableExtra("data");
        if (dataBean == null){
            return;
        }
        rlTitle = findViewById(R.id.rl_title);
        imgBack = findViewById(R.id.img_back);
        btnAdd = findViewById(R.id.btn_add);
        etModel = findViewById(R.id.et_model);
        etFacNum = findViewById(R.id.et_fac_num);
        etFactory = findViewById(R.id.et_factory);
        etDayLife = findViewById(R.id.et_day_life);
        tvTaskState = findViewById(R.id.tv_task_state);
        etTotalHour = findViewById(R.id.et_total_hour);
        etHasHour = findViewById(R.id.et_has_hour);
        /***
         * filed1: String, // 型号
         *  filed2: String, // 出厂号码
         * filed3: String, // 制造厂
         * filed4: String, // 日历寿命
         * filed5: String, // 出厂日期
         * filed6: String, // 总挂⻜小时
         * filed7: String // 已挂⻜小时
         */
        etModel.setText(dataBean.filed1);
        etFacNum.setText(dataBean.filed2);
        etFactory.setText(dataBean.filed3);
        etDayLife.setText(dataBean.filed4);
        etTotalHour.setText(dataBean.filed6);
        etHasHour.setText(dataBean.filed7);
        tvTaskState.setText(dataBean.filed5);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAmmo();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void saveAmmo() {
        /***
         * filed1: String, // 型号
         *  filed2: String, // 出厂号码
         * filed3: String, // 制造厂
         * filed4: String, // 日历寿命
         * filed5: String, // 出厂日期
         * filed6: String, // 总挂⻜小时
         * filed7: String // 已挂⻜小时
         */
        final Map<String, String> map = new HashMap<>();
        map.put("filed1", etModel.getText().toString().trim());
        map.put("filed2", etFacNum.getText().toString().trim());
        map.put("filed3", etFactory.getText().toString().trim());
        map.put("filed4", etDayLife.getText().toString().trim());
        map.put("filed5", tvTaskState.getText().toString().trim());
        map.put("filed6", etTotalHour.getText().toString().trim());
        map.put("filed7", etHasHour.getText().toString().trim());
        map.put("ammo_id", dataBean.ammo_id + "");
        showHud();
        NetUtils.executePostRequest(this,"updateAmmo", map,
                new ICallBack<BaseResponseBean>() {
                    @Override
                    public void onSucceed(BaseResponseBean data) {
                        disMissHud();
                        ToastUtil.toastCenter(mContext, "添加成功");
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailed(final String msg) {
                        disMissHud();
                        if (TextUtils.equals(msg, NetUtils.NET_ERROR)) {
                            // 离线存储
                            ThreadUtils.exec(new Runnable() {
                                @Override
                                public void run() {
                                    MyApp.editor.putString("updateAmmo", new Gson().toJson(map));
                                    MyApp.editor.commit();
                                }
                            });
                            ToastUtil.toastCenter(mContext, "网络不可用,已离线存储");
                        } else {
                            ToastUtil.toastCenter(mContext, msg);
                        }
                    }
                });
    }
}
