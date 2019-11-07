package com.android.deviceinfo.activitys.plane;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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
import com.android.deviceinfo.bean.UploadImageBean;
import com.android.deviceinfo.constants.NetContants;
import com.android.deviceinfo.utils.AppUtils;
import com.android.deviceinfo.utils.NetUtils;
import com.android.deviceinfo.utils.ThreadUtils;
import com.android.deviceinfo.utils.ToastUtil;
import com.android.deviceinfo.utils.matisse_utils.MatissePhotoHelper;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * 添加飞机界面
 */
public class AddPlaneActivity extends BaseActivity {

    private RelativeLayout rlTitle;
    private ImageView imgBack;
    private Button btnAdd;
    private TextView tvXinghao;
    private EditText etCcnum;
    private EditText etArmyNum;
    private EditText etFactory;
    private TextView tvCreateTime;
    private EditText etDanwei;
    private EditText etFeiXingQiLuo;
    private EditText etFlyHour;
    private EditText etJieduanFly;
    private EditText etJiXing;
    private EditText etJieDuanHour;
    private EditText etBigXiuNum;
    private EditText etOneHour;
    private EditText etTwoHour;
    private TextView tvState;
    private TextView tvTaskState;
    private EditText etFlyTime;
    private ImageView imgAdd;
    private EditText etXiuliFac;
    private File file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plane);
        initView();
    }

    private void initView() {
        imgBack = findViewById(R.id.img_back);
        btnAdd = findViewById(R.id.btn_add);
        tvXinghao = findViewById(R.id.tv_xinghao);
        etCcnum = findViewById(R.id.et_ccnum);
        etArmyNum = findViewById(R.id.et_army_num);
        etFactory = findViewById(R.id.et_factory);
        tvCreateTime = findViewById(R.id.tv_create_time);
        etDanwei = findViewById(R.id.et_danwei);
        etFeiXingQiLuo = findViewById(R.id.et_fei_xing_qi_luo);
        etFlyHour = findViewById(R.id.et_fly_hour);
        etJieduanFly = findViewById(R.id.et_jieduan_fly);
        etJiXing = findViewById(R.id.et_ji_xing);
        etJieDuanHour = findViewById(R.id.et_jie_duan_hour);
        etBigXiuNum = findViewById(R.id.et_big_xiu_num);
        etOneHour = findViewById(R.id.et_one_hour);
        etTwoHour = findViewById(R.id.et_two_hour);
        tvState = findViewById(R.id.tv_state);
        tvTaskState = findViewById(R.id.tv_task_state);
        etFlyTime = findViewById(R.id.et_fly_time);
        etXiuliFac = findViewById(R.id.et_xiuli_fac);

        // 返回键
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //保存按钮
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePlane();
            }
        });

        // 生产时间
        tvCreateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShengChanTimeDialog();
            }
        });

        // 飞机状态
        tvState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlaneState();
            }
        });

        // 任务状态
        tvTaskState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTaskState();
            }
        });


        imgAdd = findViewById(R.id.img_add);

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MatissePhotoHelper.selectPhoto(AddPlaneActivity.this, 9);
            }
        });

        tvXinghao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlaneModel();
            }
        });

    }

    private void savePlane() {
        if (file != null) {
            ThreadUtils.exec(new Runnable() {
                @Override
                public void run() {
                    NetUtils.upload(file, AddPlaneActivity.this,
                            new ICallBack<UploadImageBean>() {
                                @Override
                                public void onSucceed(UploadImageBean data) {
                                    commitData(data.image_path);
                                }

                                @Override
                                public void onFailed(String msg) {
                                    if (TextUtils.equals(msg,NetUtils.NET_ERROR)){
                                        commitData("");
                                    }else {
                                        ToastUtil.toastCenter(AddPlaneActivity.this, msg);
                                    }
                                }
                            });
                }
            });
        } else {
            commitData("");
        }

    }

    private void commitData(String path) {
        final Map<String, String> map = new HashMap<>();
        map.put("model", tvXinghao.getText().toString().trim());
        map.put("code", etCcnum.getText().toString().trim());
        map.put("army_id", etArmyNum.getText().toString().trim());
        map.put("factory", etFactory.getText().toString().trim());
        map.put("airUpOrDown", etFeiXingQiLuo.getText().toString().trim());
        map.put("unit", etDanwei.getText().toString().trim());
        map.put("airTime", etFlyTime.getText().toString().trim());
        map.put("date", tvCreateTime.getText().toString().trim());
        map.put("stageUpOrDown", etJieduanFly.getText().toString().trim());
        map.put("engine_1", etOneHour.getText().toString().trim());
        map.put("engine_2", etTwoHour.getText().toString().trim());
        map.put("state", tvState.getText().toString().trim());
        map.put("task", tvTaskState.getText().toString().trim());
        map.put("create_time", AppUtils.getDateWithFormater("yyyy-MM-dd"));
        map.put("type", etJiXing.getText().toString().trim());
        map.put("stageUpOrDownTime", etJieDuanHour.getText().toString().trim());
        map.put("repairFactory", etXiuliFac.getText().toString().trim());
        map.put("repairNumber", etBigXiuNum.getText().toString().trim());
        map.put("airHour", etFlyHour.getText().toString().trim());

        if (!TextUtils.isEmpty(path)) {
            map.put("image_path", path);
        }
        showHud();
        NetUtils.executePostRequest(this, "addAirplane", map,
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
                        if (TextUtils.equals(msg, NetUtils.NET_ERROR)){
                            // 离线存储
                            ThreadUtils.exec(new Runnable() {
                                @Override
                                public void run() {
                                    MyApp.editor.putString("addAirplane",new Gson().toJson(map));
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
                        tvCreateTime.setText(data);
                    }
                },
                mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    /**
     * 飞机态势
     */
    public void showPlaneState() {
        final String[] array = MyApp.strToArray(MyApp.bean.stateModel);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("选择飞机状态");
        alertBuilder.setItems(array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                tvState.setText(array[i]);
            }
        }).create().show();
    }

    private void showPlaneModel(){
        final String[] array = MyApp.strToArray(MyApp.bean.airTypeModel);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("选择飞机类型");
        alertBuilder.setItems(array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                tvXinghao.setText(array[i]);
            }
        }).create().show();
    }

    /**
     * 任务态势
     */
    public void showTaskState() {
        final String[] array = MyApp.strToArray(MyApp.bean.taskModel);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("选择任务态势");
        alertBuilder.setItems(array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                tvTaskState.setText(array[i]);
            }
        }).create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MatissePhotoHelper.REQUEST_CODE_CHOOSE) {
            if (resultCode == RESULT_OK) {
                // 获取返回的图片列表
                final List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                file = new File(path.get(0));
                Glide.with(mContext).load(file).into(imgAdd);
            }
        }
    }
}
