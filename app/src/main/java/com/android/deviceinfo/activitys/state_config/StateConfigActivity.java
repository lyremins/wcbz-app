package com.android.deviceinfo.activitys.state_config;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
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
import com.android.deviceinfo.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 状态配置
 */
public class StateConfigActivity extends BaseActivity {

    private RelativeLayout rlTitle;
    private ImageView imgBack;
    private TextView tvStateModel;
    private EditText etAddPlane;
    private Button btnAddPlane;
    private TextView tvFaultModel;
    private EditText etAddPlaneGuzhang;
    private Button btnAddPlaneGuzhang;
    private TextView tvTaskModel;
    private EditText etAddPlaneTask;
    private Button btnAddPlaneTask;
    private TextView tvSubjectModel;
    private EditText etAddPlaneProject;
    private Button btnAddPlaneProject;
    private TextView tvCarTaskModel;
    private EditText etAddCarTask;
    private Button btnAddCarTask;
    private TextView tvCarStateModel;
    private EditText etAddCarState;
    private Button btnAddCarState;
    private TextView tvCarTypeModel;
    private EditText etAddCarType;
    private Button btnAddCarType;
    private TextView tvCarFaultModel;
    private EditText etAddCarGuzhang;
    private Button btnAddCarGuzhang;
    private TextView tvPTypeModel;
    private EditText etAddUserType;
    private Button btnAddUserType;
    private TextView tvPMajorModel;
    private EditText etAddUserPro;
    private Button btnAddUserPro;
    private TextView tvEnsureModel;
    private EditText etAddBaozhangTask;
    private Button btnAddBaozhangTask;
    private TextView tvPStatusModel;
    private EditText etAddUserWork;
    private Button btnAddUserWork;
    private TextView tvCarWorkModel;
    private EditText etAddCarWork;
    private Button btnAddCarWork;
    private TextView tvPPostModel;
    private EditText etAddUserJob;
    private Button btnAddUserJob;
    private TextView tvSceneModel;
    private EditText etAddSceneModel;
    private Button btnAddSceneModel;
    private TextView tvAirTypeModel;
    private EditText etAirTypeModel;
    private Button btnAirTypeModel;
    private TextView tvFaultMethodModel;
    private EditText etAddFaultMethodModel;
    private Button btnAddFaultMethodModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_config);
        initView();
        getData();
    }

    private void initView() {

        rlTitle = findViewById(R.id.rl_title);
        imgBack = findViewById(R.id.img_back);
        tvStateModel = findViewById(R.id.tv_stateModel);
        etAddPlane = findViewById(R.id.et_add_plane);
        btnAddPlane = findViewById(R.id.btn_add_plane);
        tvFaultModel = findViewById(R.id.tv_faultModel);
        etAddPlaneGuzhang = findViewById(R.id.et_add_plane_guzhang);
        btnAddPlaneGuzhang = findViewById(R.id.btn_add_plane_guzhang);
        tvTaskModel = findViewById(R.id.tv_taskModel);
        etAddPlaneTask = findViewById(R.id.et_add_plane_task);
        btnAddPlaneTask = findViewById(R.id.btn_add_plane_task);
        tvSubjectModel = findViewById(R.id.tv_subjectModel);
        etAddPlaneProject = findViewById(R.id.et_add_plane_project);
        btnAddPlaneProject = findViewById(R.id.btn_add_plane_project);
        tvCarTaskModel = findViewById(R.id.tv_carTaskModel);
        etAddCarTask = findViewById(R.id.et_add_car_task);
        btnAddCarTask = findViewById(R.id.btn_add_car_task);
        tvCarStateModel = findViewById(R.id.tv_carStateModel);
        etAddCarState = findViewById(R.id.et_add_car_state);
        btnAddCarState = findViewById(R.id.btn_add_car_state);
        tvCarTypeModel = findViewById(R.id.tv_carTypeModel);
        etAddCarType = findViewById(R.id.et_add_car_type);
        btnAddCarType = findViewById(R.id.btn_add_car_type);
        tvCarFaultModel = findViewById(R.id.tv_carFaultModel);
        etAddCarGuzhang = findViewById(R.id.et_add_car_guzhang);
        btnAddCarGuzhang = findViewById(R.id.btn_add_car_guzhang);
        tvPTypeModel = findViewById(R.id.tv_pTypeModel);
        etAddUserType = findViewById(R.id.et_add_user_type);
        btnAddUserType = findViewById(R.id.btn_add_user_type);
        tvPMajorModel = findViewById(R.id.tv_pMajorModel);
        etAddUserPro = findViewById(R.id.et_add_user_pro);
        btnAddUserPro = findViewById(R.id.btn_add_user_pro);
        tvEnsureModel = findViewById(R.id.tv_ensureModel);
        etAddBaozhangTask = findViewById(R.id.et_add_baozhang_task);
        btnAddBaozhangTask = findViewById(R.id.btn_add_baozhang_task);
        tvPStatusModel = findViewById(R.id.tv_pStatusModel);
        etAddUserWork = findViewById(R.id.et_add_user_work);
        btnAddUserWork = findViewById(R.id.btn_add_user_work);
        tvCarWorkModel = findViewById(R.id.tv_carWorkModel);
        etAddCarWork = findViewById(R.id.et_add_car_work);
        btnAddCarWork = findViewById(R.id.btn_add_car_work);
        tvPPostModel = findViewById(R.id.tv_pPostModel);
        etAddUserJob = findViewById(R.id.et_add_user_job);
        btnAddUserJob = findViewById(R.id.btn_add_user_job);
        tvSceneModel = findViewById(R.id.tv_sceneModel);
        etAddSceneModel = findViewById(R.id.et_add_sceneModel);
        btnAddSceneModel = findViewById(R.id.btn_add_sceneModel);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvAirTypeModel = findViewById(R.id.tv_airTypeModel);
        etAirTypeModel = findViewById(R.id.et_airTypeModel);
        btnAirTypeModel = findViewById(R.id.btn_airTypeModel);
        tvFaultMethodModel = findViewById(R.id.tv_faultMethodModel);
        etAddFaultMethodModel = findViewById(R.id.et_add_faultMethodModel);
        btnAddFaultMethodModel = findViewById(R.id.btn_add_faultMethodModel);
    }

    private void getData() {
        /**
         * stateModel: String, // 飞机状态
         * taskModel: String,  // 飞机任务状态
         * faultModel: String, // 飞机故障
         * subjectModel: String, // 飞行科目
         * sceneModel: String, // 气象科目
         * carStateModel: String, // 车辆状态
         * carTaskModel: String, // 车辆任务
         * carTypeModel: String, // 车辆类型
         * carFaultModel: String, // 车辆故障
         * pTypeModel: String, // 人员类别
         * pMajorModel: String, // 人员专业
         * pPostModel: String, // 人员职务
         * ensureModel: String, // 保障任务
         * pStatusModel: String, // 人员工作状态
         * carWorkModel: String  // 车辆工作状态 
         */
        tvStateModel.setText(MyApp.bean.stateModel);
        tvTaskModel.setText(MyApp.bean.taskModel);
        tvFaultModel.setText(MyApp.bean.faultModel);
        tvSubjectModel.setText(MyApp.bean.subjectModel);
        tvSceneModel.setText(MyApp.bean.sceneModel);
        tvCarStateModel.setText(MyApp.bean.carStateModel);
        tvCarTaskModel.setText(MyApp.bean.carTaskModel);
        tvCarTypeModel.setText(MyApp.bean.carTypeModel);
        tvCarFaultModel.setText(MyApp.bean.carFaultModel);
        tvPTypeModel.setText(MyApp.bean.pTypeModel);
        tvPMajorModel.setText(MyApp.bean.pMajorModel);
        tvPPostModel.setText(MyApp.bean.pPostModel);
        tvEnsureModel.setText(MyApp.bean.ensureModel);
        tvCarWorkModel.setText(MyApp.bean.carWorkModel);
        tvPStatusModel.setText(MyApp.bean.pStatusModel);
        tvAirTypeModel.setText(MyApp.bean.airTypeModel);
        tvFaultMethodModel.setText(MyApp.bean.faultMethodModel);

        tvStateModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditStatusActivity.start(mContext, "stateModel", MyApp.bean.stateModel);
            }
        });

        tvAirTypeModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditStatusActivity.start(mContext, "airTypeModel", MyApp.bean.airTypeModel);
            }
        });

        tvFaultMethodModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditStatusActivity.start(mContext, "faultMethodModel", MyApp.bean.faultMethodModel);
            }
        });

        tvTaskModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditStatusActivity.start(mContext, "taskModel", MyApp.bean.taskModel);
            }
        });
        tvFaultModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditStatusActivity.start(mContext, "faultModel", MyApp.bean.faultModel);
            }
        });
        tvSubjectModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditStatusActivity.start(mContext, "subjectModel", MyApp.bean.subjectModel);
            }
        });
        tvSceneModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditStatusActivity.start(mContext, "sceneModel", MyApp.bean.sceneModel);
            }
        });
        tvCarStateModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditStatusActivity.start(mContext, "carStateModel", MyApp.bean.carStateModel);
            }
        });
        tvCarTaskModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditStatusActivity.start(mContext, "carTaskModel", MyApp.bean.carTaskModel);
            }
        });
        tvCarTypeModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditStatusActivity.start(mContext, "carTypeModel", MyApp.bean.carTypeModel);
            }
        });
        tvCarFaultModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditStatusActivity.start(mContext, "carFaultModel", MyApp.bean.carFaultModel);
            }
        });
        tvPTypeModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditStatusActivity.start(mContext, "pTypeModel", MyApp.bean.pTypeModel);
            }
        });
        tvPMajorModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditStatusActivity.start(mContext, "pMajorModel", MyApp.bean.pMajorModel);
            }
        });
        tvPPostModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditStatusActivity.start(mContext, "pPostModel", MyApp.bean.pPostModel);
            }
        });
        tvEnsureModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditStatusActivity.start(mContext, "ensureModel", MyApp.bean.ensureModel);
            }
        });
        tvCarWorkModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditStatusActivity.start(mContext, "carWorkModel", MyApp.bean.carWorkModel);
            }
        });
        tvPStatusModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditStatusActivity.start(mContext, "pStatusModel", MyApp.bean.pStatusModel);
            }
        });


        btnAddPlane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = etAddPlane.getText().toString().trim();
                if (str.isEmpty()) {
                    ToastUtil.toastCenter(mContext, "输入不能为空");
                    return;
                }
                MyApp.bean.stateModel += ("," + str);
                save("stateModel", MyApp.bean.stateModel, etAddPlane);
                getData();
            }
        });

        btnAddPlaneTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = etAddPlaneTask.getText().toString().trim();
                if (str.isEmpty()) {
                    ToastUtil.toastCenter(mContext, "输入不能为空");
                    return;
                }
                MyApp.bean.taskModel += ("," + str);
                save("taskModel", MyApp.bean.taskModel, etAddPlaneTask);
                getData();
            }
        });

        btnAddPlaneGuzhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = etAddPlaneGuzhang.getText().toString().trim();
                if (str.isEmpty()) {
                    ToastUtil.toastCenter(mContext, "输入不能为空");
                    return;
                }
                MyApp.bean.faultModel += ("," + str);
                save("faultModel", MyApp.bean.faultModel, etAddPlaneGuzhang);
                getData();
            }
        });

        btnAddPlaneProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = etAddPlaneProject.getText().toString().trim();
                if (str.isEmpty()) {
                    ToastUtil.toastCenter(mContext, "输入不能为空");
                    return;
                }
                MyApp.bean.subjectModel += ("," + str);
                save("subjectModel", MyApp.bean.subjectModel, etAddPlaneProject);
                getData();
            }
        });

        btnAddSceneModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = etAddSceneModel.getText().toString().trim();
                if (str.isEmpty()) {
                    ToastUtil.toastCenter(mContext, "输入不能为空");
                    return;
                }
                MyApp.bean.sceneModel += ("," + str);
                save("sceneModel", MyApp.bean.sceneModel, etAddSceneModel);
                getData();
            }
        });

        btnAddCarState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = etAddCarState.getText().toString().trim();
                if (str.isEmpty()) {
                    ToastUtil.toastCenter(mContext, "输入不能为空");
                    return;
                }
                MyApp.bean.carStateModel += ("," + str);
                save("carStateModel", MyApp.bean.carStateModel, etAddCarState);
                getData();
            }
        });

        btnAddCarTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = etAddCarTask.getText().toString().trim();
                if (str.isEmpty()) {
                    ToastUtil.toastCenter(mContext, "输入不能为空");
                    return;
                }
                MyApp.bean.carTaskModel += ("," + str);
                save("carTaskModel", MyApp.bean.carTaskModel, etAddCarTask);
                getData();
            }
        });

        btnAddCarType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = etAddCarType.getText().toString().trim();
                if (str.isEmpty()) {
                    ToastUtil.toastCenter(mContext, "输入不能为空");
                    return;
                }
                MyApp.bean.carTypeModel += ("," + str);
                save("carTypeModel", MyApp.bean.carTypeModel, etAddCarType);
                getData();
            }
        });

        btnAddCarGuzhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = etAddCarGuzhang.getText().toString().trim();
                if (str.isEmpty()) {
                    ToastUtil.toastCenter(mContext, "输入不能为空");
                    return;
                }
                MyApp.bean.carFaultModel += ("," + str);
                save("carFaultModel", MyApp.bean.carFaultModel, etAddCarGuzhang);
                getData();
            }
        });

        btnAddUserType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = etAddUserType.getText().toString().trim();
                if (str.isEmpty()) {
                    ToastUtil.toastCenter(mContext, "输入不能为空");
                    return;
                }
                MyApp.bean.pTypeModel += ("," + str);
                save("pTypeModel", MyApp.bean.pTypeModel, etAddUserType);
                getData();
            }
        });

        btnAddUserPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = etAddUserPro.getText().toString().trim();
                if (str.isEmpty()) {
                    ToastUtil.toastCenter(mContext, "输入不能为空");
                    return;
                }
                MyApp.bean.pMajorModel += ("," + str);
                save("pMajorModel", MyApp.bean.pMajorModel, etAddUserPro);
                getData();
            }
        });

        btnAddUserJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = etAddUserJob.getText().toString().trim();
                if (str.isEmpty()) {
                    ToastUtil.toastCenter(mContext, "输入不能为空");
                    return;
                }
                MyApp.bean.pPostModel += ("," + str);
                save("pPostModel", MyApp.bean.pPostModel, etAddUserJob);
                getData();
            }
        });

        btnAddBaozhangTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = etAddBaozhangTask.getText().toString().trim();
                if (str.isEmpty()) {
                    ToastUtil.toastCenter(mContext, "输入不能为空");
                    return;
                }
                MyApp.bean.ensureModel += ("," + str);
                save("ensureModel", MyApp.bean.ensureModel, etAddBaozhangTask);
                getData();
            }
        });

        btnAddUserWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = etAddUserWork.getText().toString().trim();
                if (str.isEmpty()) {
                    ToastUtil.toastCenter(mContext, "输入不能为空");
                    return;
                }
                MyApp.bean.pStatusModel += ("," + str);
                save("pStatusModel", MyApp.bean.pStatusModel, etAddUserWork);
                getData();
            }
        });

        btnAddCarWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = etAddCarWork.getText().toString().trim();
                if (str.isEmpty()) {
                    ToastUtil.toastCenter(mContext, "输入不能为空");
                    return;
                }
                MyApp.bean.carWorkModel += ("," + str);
                save("carWorkModel", MyApp.bean.carWorkModel, etAddCarWork);
                getData();
            }
        });

        btnAirTypeModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = etAirTypeModel.getText().toString().trim();
                if (str.isEmpty()) {
                    ToastUtil.toastCenter(mContext, "输入不能为空");
                    return;
                }
                MyApp.bean.airTypeModel += ("," + str);
                save("carWorkModel", MyApp.bean.airTypeModel, etAirTypeModel);
                getData();
            }
        });

        btnAddFaultMethodModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = etAddFaultMethodModel.getText().toString().trim();
                if (str.isEmpty()) {
                    ToastUtil.toastCenter(mContext, "输入不能为空");
                    return;
                }
                MyApp.bean.faultMethodModel += ("," + str);
                save("carWorkModel", MyApp.bean.faultMethodModel, etAddFaultMethodModel);
                getData();
            }
        });
    }

    private void save(String param, String value, final EditText editText) {
        showHud();
        Map<String, String> map = new HashMap<>();
        map.put(param, value);
        NetUtils.executePostRequest(this, "updateConfig", map,
                new ICallBack<BaseResponseBean>() {
                    @Override
                    public void onSucceed(BaseResponseBean data) {
                        disMissHud();
                        ToastUtil.toastCenter(mContext, "添加成功");
                        editText.setText("");
                        MyApp.getConfig();
                    }

                    @Override
                    public void onFailed(final String msg) {
                        disMissHud();
                        ToastUtil.toastCenter(mContext, msg);
                    }
                });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }
                getData();
            }
        }, 1500);
    }
}
