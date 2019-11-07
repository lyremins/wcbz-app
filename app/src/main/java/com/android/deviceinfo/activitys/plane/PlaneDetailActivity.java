package com.android.deviceinfo.activitys.plane;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.android.deviceinfo.constants.NetContants;
import com.android.deviceinfo.utils.AppUtils;
import com.android.deviceinfo.utils.NetUtils;
import com.android.deviceinfo.utils.ThreadUtils;
import com.android.deviceinfo.utils.ToastUtil;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * 飞机详情
 */
public class PlaneDetailActivity extends BaseActivity {

    private ImageView imgBack;

    private ImageView img;
    private RelativeLayout rlTitle;
    private TextView etXinghao;
    private TextView etCcnum;
    private TextView etArmyNum;
    private TextView etFactory;
    private TextView tvCreateTime;
    private TextView etDanwei;
    private EditText etFlyTime;
    private EditText etFeiXingQiLuo;
    private EditText etFlyHour;
    private EditText etJieduanFly;
    private TextView etJiXing;
    private EditText etJieDuanHour;
    private EditText etBigXiuNum;
    private EditText etOneHour;
    private EditText etTwoHour;
    private TextView tvState;
    private TextView tvTaskState;
    private TextView btnAdd;
    private EditText etXiuliFac;

    PlaneListBean.DataBean dataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plane_detail);
        initView();
        initData();
    }

    private void initData() {
        dataBean = (PlaneListBean.DataBean) getIntent()
                .getSerializableExtra("data");
        if (dataBean == null) {
            return;
        }
        etXinghao.setText(dataBean.model);
        etCcnum.setText(dataBean.code);
        etArmyNum.setText(dataBean.army_id);
        etFactory.setText(dataBean.factory);
        etFeiXingQiLuo.setText(dataBean.airUpOrDown);
        etDanwei.setText(dataBean.unit);
        etFlyTime.setText(dataBean.airTime);
        tvCreateTime.setText(dataBean.date);
        etJieduanFly.setText(dataBean.stageUpOrDown);
        etOneHour.setText(dataBean.engine_1);
        etTwoHour.setText(dataBean.engine_2);
        tvState.setText(dataBean.state);
        tvTaskState.setText(dataBean.task);
        etJiXing.setText(dataBean.type);
        etJieDuanHour.setText(dataBean.stageUpOrDownTime);
        etBigXiuNum.setText(dataBean.repairNumber);
        etXiuliFac.setText(dataBean.repairFactory);
        etFlyHour.setText(dataBean.airHour);
        if (TextUtils.isEmpty(dataBean.image_path)) {
            img.setVisibility(View.GONE);
        }
        Glide.with(mContext).load(NetContants.IMAGE_URL + dataBean.image_path).into(img);
    }

    private void initView() {
        imgBack = findViewById(R.id.img_back);

        img = findViewById(R.id.img);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rlTitle = findViewById(R.id.rl_title);
        etXinghao = findViewById(R.id.et_xinghao);
        etCcnum = findViewById(R.id.et_ccnum);
        etArmyNum = findViewById(R.id.et_army_num);
        etFactory = findViewById(R.id.et_factory);
        tvCreateTime = findViewById(R.id.tv_create_time);
        etDanwei = findViewById(R.id.et_danwei);
        etFlyTime = findViewById(R.id.et_fly_time);
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
        btnAdd = findViewById(R.id.btn_add);
        //保存按钮
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitData();
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

        etXiuliFac = findViewById(R.id.et_xiuli_fac);
    }

    private void commitData() {
        final Map<String, String> map = new HashMap<>();
        map.put("airplane_id", dataBean.airplane_id + "");
        map.put("model", etXinghao.getText().toString().trim());
        map.put("code", etCcnum.getText().toString().trim());
        map.put("army_id", etArmyNum.getText().toString().trim());
        map.put("factory", etFactory.getText().toString().trim());
        map.put("airUpOrDown", etFeiXingQiLuo.getText().toString().trim());
        map.put("unit", etDanwei.getText().toString().trim());
        map.put("airTime", etFlyTime.getText().toString().trim());
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

        showHud();
        NetUtils.executePostRequest(this, "updateAirplane", map,
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
                                    MyApp.editor.putString("updateAirplane", new Gson().toJson(map));
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
}
