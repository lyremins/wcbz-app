package com.android.deviceinfo.activitys.air_plan;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.deviceinfo.R;
import com.android.deviceinfo.base.BaseActivity;
import com.android.deviceinfo.base.ICallBack;
import com.android.deviceinfo.bean.BaseResponseBean;
import com.android.deviceinfo.utils.AppUtils;
import com.android.deviceinfo.utils.NetUtils;
import com.android.deviceinfo.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 添加飞行计划
 */
public class AddAirPlanActivity extends BaseActivity {

    private ImageView imgBack;
    private TextView btnAdd;
    private EditText etName;
    private EditText etAirSubject;
    private EditText etSceneSubject;
    private EditText etQiluoNum;
    private EditText etFlyTime;
    private EditText etApproachTime;
    private TextView tvDate;
    private EditText etTotalNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_air_plan);
        initView();
    }

    private void initView() {
        imgBack = findViewById(R.id.img_back);
        btnAdd = findViewById(R.id.btn_add);
        etName = findViewById(R.id.et_name);
        etAirSubject = findViewById(R.id.et_air_subject);
        etSceneSubject = findViewById(R.id.et_scene_subject);
        etQiluoNum = findViewById(R.id.et_qiluo_num);
        etFlyTime = findViewById(R.id.et_fly_time);
        etApproachTime = findViewById(R.id.et_approach_time);
        tvDate = findViewById(R.id.tv_date);
        etTotalNum = findViewById(R.id.et_total_num);

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
    }
    private void save() {
        /**
         plan_id: Number, // ⻜机
         ID name: String, 
         dateTime: String,
          airName: String,
          vehicleName: String,
          subjectName: String,
          dateTime: String, // 选择日期
         airSubject: String, // ⻜行科目
         sceneSubject: String, // 气象科目
         upDownNumber: String, // 起落次数
         flightTime: String, // ⻜行时间
         approachTime: String, // 进场时间
         totalNumber: String, // 总人数
         */
        Map<String, String> map = new HashMap<>();
        map.put("airSubject", etAirSubject.getText().toString().trim());
        map.put("sceneSubject", etSceneSubject.getText().toString().trim());
        map.put("upDownNumber", etQiluoNum.getText().toString().trim());
        map.put("flightTime", etFlyTime.getText().toString().trim());
        map.put("approachTime", etApproachTime.getText().toString().trim());
        map.put("totalNumber", etTotalNum.getText().toString().trim());
        map.put("dateTime", tvDate.getText().toString().trim());
        map.put("create_time", AppUtils.getDateWithFormater("yyyy-MM-dd"));
        NetUtils.executePostRequest(this,"addPlan", map,
                new ICallBack<BaseResponseBean>() {
                    @Override
                    public void onSucceed(BaseResponseBean data) {
                        ToastUtil.toastCenter(mContext, "添加成功");
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailed(final String msg) {
                        ToastUtil.toastCenter(mContext, msg);
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
