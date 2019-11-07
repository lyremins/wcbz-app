package com.android.deviceinfo.activitys.ammo;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 添加弹药
 */
public class AddAmmoActivity extends BaseActivity {

    private ImageView imgBack;
    private TextView btnAdd;
    private EditText etModel;
    private EditText etFacNum;
    private EditText etFactory;
    private EditText etDayLife;
    private TextView tvTaskState;
    private EditText etTotalHour;
    private EditText etHasHour;

    Calendar ca = Calendar.getInstance();

    int mYear;
    int mMonth;
    int mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ammo);
        initView();
    }

    private void initView() {
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
        imgBack = findViewById(R.id.img_back);
        btnAdd = findViewById(R.id.btn_add);
        etModel = findViewById(R.id.et_model);
        etFacNum = findViewById(R.id.et_fac_num);
        etFactory = findViewById(R.id.et_factory);
        etDayLife = findViewById(R.id.et_day_life);
        tvTaskState = findViewById(R.id.tv_task_state);
        etTotalHour = findViewById(R.id.et_total_hour);
        etHasHour = findViewById(R.id.et_has_hour);

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
                saveAmmo();
            }
        });

        // 生产时间
        tvTaskState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShengChanTimeDialog();
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
        map.put("create_time", AppUtils.getDateWithFormater("yyyy-MM-dd"));
        NetUtils.executePostRequest(this,"addAmmo", map,
                new ICallBack<BaseResponseBean>() {
                    @Override
                    public void onSucceed(BaseResponseBean data) {
                        ToastUtil.toastCenter(mContext, "添加成功");
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailed(final String msg) {
                        if (TextUtils.equals(msg, NetUtils.NET_ERROR)){
                            // 离线存储
                            ThreadUtils.exec(new Runnable() {
                                @Override
                                public void run() {
                                    MyApp.editor.putString("addAmmo",new Gson().toJson(map));
                                    MyApp.editor.commit();
                                }
                            });
                            ToastUtil.toastCenter(mContext,"网络不可用,已离线存储");
                        }else {
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
                        tvTaskState.setText(data);
                    }
                },
                mYear, mMonth, mDay);
        datePickerDialog.show();
    }
}
