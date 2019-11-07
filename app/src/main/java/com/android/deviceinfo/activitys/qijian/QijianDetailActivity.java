package com.android.deviceinfo.activitys.qijian;

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
import com.android.deviceinfo.utils.NetUtils;
import com.android.deviceinfo.utils.ThreadUtils;
import com.android.deviceinfo.utils.ToastUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class QijianDetailActivity extends BaseActivity {


    private QiJianListBean.Databean databean;
    private RelativeLayout rlTitle;
    private ImageView imgBack;
    private EditText etName;
    private EditText etModel;
    private EditText etJixing;
    private EditText etTotalLife;
    private EditText etFac;
    private TextView tvDate;
    private EditText etRestLife;
    private EditText etSmType;
    private EditText etYuzhi;
    private TextView btnAdd;
    private EditText etXiuliNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qijian_detail);
        initView();
    }

    private void initView() {
        databean = (QiJianListBean.Databean) getIntent().getSerializableExtra("data");
        if (databean == null) {
            return;
        }

        rlTitle = findViewById(R.id.rl_title);
        imgBack = findViewById(R.id.img_back);
        etName = findViewById(R.id.et_name);
        etModel = findViewById(R.id.et_model);
        etJixing = findViewById(R.id.et_jixing);
        etTotalLife = findViewById(R.id.et_total_life);
        etFac = findViewById(R.id.et_xiuli_num);
        tvDate = findViewById(R.id.tv_date);
        etRestLife = findViewById(R.id.et_rest_life);
        etSmType = findViewById(R.id.et_sm_type);
        etYuzhi = findViewById(R.id.et_yuzhi);
        btnAdd = findViewById(R.id.btn_add);

        etName.setText(databean.filed1);
        etModel.setText(databean.filed2);
        etJixing.setText(databean.filed3);
        etTotalLife.setText(databean.filed5);
        etFac.setText(databean.filed4);
        etRestLife.setText(databean.filed8);
        etYuzhi.setText(databean.filed6);
        tvDate.setText(databean.filed7);

        // 返回键
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        // 保存按钮
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }

    private void save() {
        /***
         filed1: String, // 机器名称
         filed2: String, // 型号
         filed3: String, //装机日期
         filed4: String, // 修理次数
         filed5: String, // 总寿命
         filed6: String, // 总寿命
         filed7: String, // 机型
         sy: String // 剩余寿命
         */
        final Map<String, String> map = new HashMap<>();
        map.put("filed2", etModel.getText().toString().trim());
        map.put("filed1", etName.getText().toString().trim());
        map.put("filed7", tvDate.getText().toString().trim());
        map.put("filed4", etFac.getText().toString().trim());
        map.put("filed5", etTotalLife.getText().toString().trim());
        map.put("filed6", etYuzhi.getText().toString().trim());
        map.put("filed3", etJixing.getText().toString().trim());
        map.put("filed8", etRestLife.getText().toString().trim());
        map.put("device_id", databean.device_id + "");
        showHud();
        NetUtils.executePostRequest(this, "updateDevice", map,
                new ICallBack<BaseResponseBean>() {
                    @Override
                    public void onSucceed(BaseResponseBean data) {
                        disMissHud();
                        ToastUtil.toastCenter(mContext, "保存成功");
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
                                    MyApp.editor.putString("updateDevice", new Gson().toJson(map));
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
