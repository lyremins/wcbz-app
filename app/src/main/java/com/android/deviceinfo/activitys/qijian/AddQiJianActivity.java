package com.android.deviceinfo.activitys.qijian;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 添加有售器件
 */
public class AddQiJianActivity extends BaseActivity {

    private ImageView imgBack;
    private TextView btnAdd;
    private EditText etName;
    private EditText etModel;
    private EditText etTotalLife;
    private EditText etFac;
    private TextView tvDate;
    private EditText etRestLife;
    private EditText etJixing;

    Calendar ca = Calendar.getInstance();
    int mYear;
    int mMonth;
    int mDay;

    private EditText etSmType;
    private EditText etYuzhi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_qi_jian);
        initView();
    }

    private void initView() {
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
        imgBack = findViewById(R.id.img_back);
        btnAdd = findViewById(R.id.btn_add);
        etName = findViewById(R.id.et_name);
        etModel = findViewById(R.id.et_model);
        etTotalLife = findViewById(R.id.et_total_life);
        etFac = findViewById(R.id.et_xiuli_num);
        tvDate = findViewById(R.id.tv_date);
        etRestLife = findViewById(R.id.et_rest_life);
        etJixing = findViewById(R.id.et_jixing);

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

        // 生产时间
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShengChanTimeDialog();
            }
        });

        etSmType = findViewById(R.id.et_sm_type);
        etYuzhi = findViewById(R.id.et_yuzhi);
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
        NetUtils.executePostRequest(this, "addDevice", map,
                new ICallBack<BaseResponseBean>() {
                    @Override
                    public void onSucceed(BaseResponseBean data) {
                        ToastUtil.toastCenter(mContext, "添加成功");
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailed(final String msg) {
                        if (TextUtils.equals(msg, NetUtils.NET_ERROR)) {
                            // 离线存储
                            ThreadUtils.exec(new Runnable() {
                                @Override
                                public void run() {
                                    MyApp.editor.putString("addDevice", new Gson().toJson(map));
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

    /**
     * 生产时间
     */
    private void showShengChanTimeDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mYear = year;
                        mMonth = month;
                        mDay = dayOfMonth;
                        String data = year + "-" + (month + 1) + "-" + dayOfMonth;
                        tvDate.setText(data);
                    }
                },
                mYear, mMonth, mDay);
        datePickerDialog.show();
    }
}
