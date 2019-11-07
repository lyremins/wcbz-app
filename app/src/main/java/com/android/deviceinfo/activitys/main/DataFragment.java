package com.android.deviceinfo.activitys.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.deviceinfo.R;
import com.android.deviceinfo.activitys.ammo.AmmoListActivity;
import com.android.deviceinfo.activitys.car.CarListActivity;
import com.android.deviceinfo.activitys.guanlian.CommonWebActivity;
import com.android.deviceinfo.activitys.org.OrgManagerActivity;
import com.android.deviceinfo.activitys.people.AirPeopleListActivity;
import com.android.deviceinfo.activitys.plane.PlaneListActivity;
import com.android.deviceinfo.activitys.qijian.QiJianListActivity;
import com.android.deviceinfo.activitys.state_config.StateConfigActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * 数据管理
 */
public class DataFragment extends Fragment {

    private TextView tvPlane;
    private TextView tvPeople;
    private TextView tvLongevity;
    private TextView tvState;
    private TextView tvCar;
    private TextView tvOrg;
    private TextView tvDanyao;
    private RelativeLayout rlTitle;
    private TextView tvPlaneQijian;
    private TextView tvPlaneCar;
    private TextView tvPlaneAmmo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tvPlane = view.findViewById(R.id.tv_plane);
        tvPeople = view.findViewById(R.id.tv_people);
        tvLongevity = view.findViewById(R.id.tv_longevity);
        tvState = view.findViewById(R.id.tv_state);
        tvCar = view.findViewById(R.id.tv_car);
        tvOrg = view.findViewById(R.id.tv_org);

        // 飞机管理
        tvPlane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PlaneListActivity.class));
            }
        });

        // 状态上报
        tvState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), StateConfigActivity.class));
            }
        });

        // 机务人员
        tvPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AirPeopleListActivity.class));
            }
        });

        // 有寿器件
        tvLongevity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), QiJianListActivity.class));
            }
        });

        // 车辆管理
        tvCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), CarListActivity.class));
            }
        });

        // 组织架构
        tvOrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), OrgManagerActivity.class));
            }
        });

        tvDanyao = view.findViewById(R.id.tv_danyao);

        //弹药
        tvDanyao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AmmoListActivity.class));
            }
        });

        tvPlaneQijian = view.findViewById(R.id.tv_plane_qijian);
        tvPlaneCar = view.findViewById(R.id.tv_plane_car);
        tvPlaneAmmo = view.findViewById(R.id.tv_plane_ammo);


        tvPlaneCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonWebActivity.start(getContext(),"飞机-保障车辆关联","showairplaneCar");
            }
        });

        tvPlaneQijian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonWebActivity.start(getContext(),"飞机-有寿器件关联","showAirplaneDevice");
            }
        });

        tvPlaneAmmo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonWebActivity.start(getContext(),"飞机-弹药关联","showairplaneAmmo");
            }
        });


    }
}
