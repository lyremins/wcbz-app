package com.android.deviceinfo.activitys.taishi;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.deviceinfo.R;
import com.android.deviceinfo.activitys.air_plan.AirPlanListBean;
import com.android.deviceinfo.activitys.ensure_manager.AirPlanWebActivity;
import com.android.deviceinfo.activitys.ensure_manager.BzTaskWebActivity;
import com.android.deviceinfo.activitys.guanlian.CommonWebActivity;
import com.android.deviceinfo.activitys.qijian.QiJianListBean;
import com.android.deviceinfo.base.ICallBack;
import com.android.deviceinfo.utils.AppUtils;
import com.android.deviceinfo.utils.NetUtils;
import com.android.deviceinfo.utils.ToastUtil;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 *
 */
public class TaiShiFragment extends Fragment {

    private TextView tvFlyPlan;
    private Button btnMoreFlyPlan;
    private TextView tvBzPlan;
    private Button btnMoreBzPlan;
    private RelativeLayout rlTitle;
    private TextView tvPlane;
    private TextView tvCar;
    private TextView tvUser;
    private TextView tvQijian;
    private TextView tvDanyao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_taishi, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tvFlyPlan = view.findViewById(R.id.tv_fly_plan);
        btnMoreFlyPlan = view.findViewById(R.id.btn_more_fly_plan);
        tvBzPlan = view.findViewById(R.id.tv_bz_plan);
        btnMoreBzPlan = view.findViewById(R.id.btn_more_bz_plan);
        rlTitle = view.findViewById(R.id.rl_title);
        tvPlane = view.findViewById(R.id.tv_plane);
        tvCar = view.findViewById(R.id.tv_car);
        tvUser = view.findViewById(R.id.tv_user);
        tvQijian = view.findViewById(R.id.tv_qijian);
        tvDanyao = view.findViewById(R.id.tv_danyao);

        tvFlyPlan.setText(AppUtils.getDateWithFormater("yyyy-MM-dd") + " \n" +
                "总飞机数：0 已进场飞机数：0\n" +
                "总起落数：0 已完成起落数：0\n" +
                "总飞行时间：0 已飞行时间：0" + "\n" +
                "总进场人数：0 已进场人数：0");

        tvBzPlan.setText("今日保障计划概况\n" +
                "总保障车辆数：0 总保障任务数：0\n" +
                "进场车辆数：0 已保障任务数：0");


        btnMoreFlyPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AirPlanWebActivity.class));
            }
        });

        btnMoreBzPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), BzTaskWebActivity.class));
            }
        });

        tvPlane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), PlaneStateActivity.class));
            }
        });

        tvCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), CarStateActivity.class));
            }
        });

        tvDanyao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AmmoStateActivity.class));
            }
        });

        tvQijian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonWebActivity.start(getContext(),"有寿器件态势","showAirplaneDevice");
            }
        });

        tvUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), PeopleStateActivity.class));
            }
        });
        requestData();
    }

    private void requestData(){
        /**
         * "totalAirplane": 0, // 总飞机数
         *         "enterAirplane": 0, // 已进场飞机数
         *         "totalUpDown": 0, // 总起落数
         *         "doneUpdown": 0, // 已完成起落数
         *         "enterPerson": 0, // 总进场人数
         *         "donePerson": 0, // 已进场人数
         *         "totalCar": 0, // 总保障车辆数
         *         "totalTask": 0, // 总保障任务数
         *         "enterCar": 0, // 进场车辆数
         *         "doneTask": 0  // 已保障任务数
         */
        NetUtils.executeGetRequest(getContext(),"getSituation", null,
                new ICallBack<TaiShiBean>() {
                    @Override
                    public void onSucceed(TaiShiBean data) {
                        if (data == null) {
                            return;
                        }
                        TaiShiBean.DataBean bean = data.data;
                        tvFlyPlan.setText(AppUtils.getDateWithFormater("yyyy-MM-dd") + "\n" +
                                "总飞机数：" + bean.totalAirplane + "      已进场飞机数：" + bean.enterAirplane + "\n" +
                                "总起落数：" + bean.totalUpDown + "      已完成起落数：" + bean.doneUpdown + "\n" +
                                "总飞行时间："+ bean.totalFlyHour + "      已飞行时间：" + bean.doneFlyHour + "\n" +
                                "总进场人数："+ bean.enterPerson + "      已进场人数：" + bean.donePerson);

                        tvBzPlan.setText("今日保障计划概况:\n" +
                                "总保障车辆数：" + bean.totalCar + "      总保障任务数：" + bean.totalTask + "\n" +
                                "进场车辆数：" + bean.enterCar + "      已保障任务数：" + bean.doneTask);
                    }

                    @Override
                    public void onFailed(final String msg) {
                        ToastUtil.toastCenter(getContext(), msg);

                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        requestData();
    }
}
