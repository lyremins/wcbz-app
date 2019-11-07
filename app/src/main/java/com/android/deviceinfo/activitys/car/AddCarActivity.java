package com.android.deviceinfo.activitys.car;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
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

import java.util.HashMap;
import java.util.Map;

/**
 * 添加车辆
 */
public class AddCarActivity extends BaseActivity {

    private ImageView imgBack;
    private TextView btnAdd;
    private TextView tvModel;
    private EditText etName;
    private EditText etFacNum;
    private EditText etFactory;
    private TextView tvDate;
    private TextView tvState;
    private EditText etOrganiz;
    private TextView tvService;
    private EditText etArmyNum;
    private EditText etLife;
    private EditText etJieDuanMile;
    private EditText etBigNum;
    private TextView tvTaskState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        initView();
    }

    private void initView() {
        imgBack = findViewById(R.id.img_back);
        btnAdd = findViewById(R.id.btn_add);
        tvModel = findViewById(R.id.tv_model);
        etName = findViewById(R.id.et_name);
        etFacNum = findViewById(R.id.et_fac_num);
        etFactory = findViewById(R.id.et_factory);
        tvDate = findViewById(R.id.tv_date);
        tvState = findViewById(R.id.et_state);
        etOrganiz = findViewById(R.id.et_organiz);
        tvService = findViewById(R.id.tv_service);
        etArmyNum = findViewById(R.id.et_army_num);
        etLife = findViewById(R.id.et_life);
        etJieDuanMile = findViewById(R.id.et_jie_duan_mile);
        etBigNum = findViewById(R.id.et_big_num);
        tvTaskState = findViewById(R.id.tv_task_state);

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

        tvState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCarState();
            }
        });

        tvTaskState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTaskState();
            }
        });

        tvService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showServiceModel();
            }
        });

        tvModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCarModel();
            }
        });
    }


    private void save() {
        /**
         * vehicle_id: Number, // ⻋辆 ID
         * model: String, // ⻋辆型号
         * code: String, // 出厂号码
         * name: String,
         *  state: String, // 状态
         *  organiz: String, // 单位 
         * service: String, // 服务机型
         *  armyId: String, // 部队编号
         *  product: String, // 生产厂
         * productTime: String, // 生产时间 
         * life: String, // 总寿命 
         * stageCourse: String, // 阶段行驶里程
         * repairNumber: String, // 大修次数
         * taskState: String, // ⻋辆任务状态
         */
        final Map<String, String> map = new HashMap<>();
        map.put("model", tvModel.getText().toString().trim());
        map.put("code", etFacNum.getText().toString().trim());
//        map.put("name", etName.getText().toString().trim());
        map.put("state", tvState.getText().toString().trim());
        map.put("organiz", etOrganiz.getText().toString().trim());
        map.put("service", tvService.getText().toString().trim());
        map.put("armyId", etArmyNum.getText().toString().trim());
        map.put("product", etFactory.getText().toString().trim());
        map.put("productTime", tvDate.getText().toString().trim());
        map.put("life", etLife.getText().toString().trim());
        map.put("stageCourse", etJieDuanMile.getText().toString().trim());
        map.put("repairNumber", etBigNum.getText().toString().trim());
        map.put("taskState", tvTaskState.getText().toString().trim());
        map.put("create_time", AppUtils.getDateWithFormater("yyyy-MM-dd"));
        NetUtils.executePostRequest(this,"addVehicle", map,
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
                                    MyApp.editor.putString("addVehicle",new Gson().toJson(map));
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

    private void showCarModel(){
        final String[] array = MyApp.strToArray(MyApp.bean.carTypeModel);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("选择车辆类型");
        alertBuilder.setItems(array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                tvModel.setText(array[i]);
            }
        }).create().show();
    }

    private void showServiceModel(){
        final String[] array = MyApp.strToArray(MyApp.bean.airTypeModel);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("选择服务机型");
        alertBuilder.setItems(array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                tvService.setText(array[i]);
            }
        }).create().show();
    }


    /**
     * 任务态势
     */
    public void showTaskState() {
        final String[] array = MyApp.strToArray(MyApp.bean.carTaskModel);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("选择车辆状态");
        alertBuilder.setItems(array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                tvTaskState.setText(array[i]);
            }
        }).create().show();
    }

    /**
     * 状态态势
     */
    public void showCarState() {
        final String[] array = MyApp.strToArray(MyApp.bean.carStateModel);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("选择车辆任务状态");
        alertBuilder.setItems(array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                tvState.setText(array[i]);
            }
        }).create().show();
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
