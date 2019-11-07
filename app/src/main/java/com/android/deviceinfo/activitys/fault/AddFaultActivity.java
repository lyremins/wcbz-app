package com.android.deviceinfo.activitys.fault;

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
 * 添加故障
 */
public class AddFaultActivity extends BaseActivity {

    private ImageView imgBack;
    private TextView btnAdd;
    private EditText etFinder;
    private EditText etName;
    private EditText etModel;
    private EditText etReason;
    private EditText etFac;
    private EditText etDesc;
    private TextView tvDate;
    private TextView tvType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fault);
        initView();
    }

    private void initView() {
        imgBack = findViewById(R.id.img_back);
        btnAdd = findViewById(R.id.btn_add);
        etFinder = findViewById(R.id.et_finder);
        etName = findViewById(R.id.et_name);
        etModel = findViewById(R.id.et_model);
        etReason = findViewById(R.id.et_reason);
        etFac = findViewById(R.id.et_fac);
        etDesc = findViewById(R.id.et_desc);
        tvDate = findViewById(R.id.tv_date);
        tvType = findViewById(R.id.tv_type);

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
         user_name: String, // 发现人
          model: String, // 故障件型号
          airModel: String,
          cj: String,
          date: String, // 装机日期 
         reason: String, // 故障原因 
         pheno: String, 
         person: String,
          method: String, 
         image_path: { type: String, default: "" }, // 故障照片
         type: String, // 故障类型 
         deviceName: String, // 故障件名称,
          desc: String, // 故障描述 
         factory: String, // 厂家
         */
        Map<String, String> map = new HashMap<>();
        map.put("model", etModel.getText().toString().trim());
        map.put("user_name", etFinder.getText().toString().trim());
        map.put("deviceName", etName.getText().toString().trim());
        map.put("reason", etReason.getText().toString().trim());
        map.put("type", tvType.getText().toString().trim());
        map.put("desc", etDesc.getText().toString().trim());
        map.put("factory", etModel.getText().toString().trim());
        map.put("date", tvDate.getText().toString().trim());
        map.put("create_time", AppUtils.getDateWithFormater("yyyy-MM-dd"));
        NetUtils.executePostRequest(this,"addFault", map,
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
