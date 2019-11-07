package com.android.deviceinfo.activitys.ensure_manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.deviceinfo.R;
import com.android.deviceinfo.base.BaseActivity;

/**
 * 保障管理界面
 */
public class EnsureManagerActivity extends BaseActivity {

    private ImageView imgBack;
    private TextView tvPlanePlan;
    private TextView tvBzPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ensure_manager);
        initView();
    }

    private void initView() {
        imgBack = findViewById(R.id.img_back);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvPlanePlan = findViewById(R.id.tv_plane_plan);
        tvBzPlan = findViewById(R.id.tv_bz_plan);

        tvPlanePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EnsureManagerActivity.this,AirPlanWebActivity.class));
            }
        });

        tvBzPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EnsureManagerActivity.this,BzTaskWebActivity.class));
            }
        });
    }
}
