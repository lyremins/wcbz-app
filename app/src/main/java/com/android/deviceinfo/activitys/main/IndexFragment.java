package com.android.deviceinfo.activitys.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.deviceinfo.MyApp;
import com.android.deviceinfo.R;
import com.android.deviceinfo.activitys.chat.ContactListActivity;
import com.android.deviceinfo.activitys.ensure_manager.EnsureManagerActivity;
import com.android.deviceinfo.activitys.guanlian.CommonWebActivity;
import com.android.deviceinfo.activitys.login.LoginActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * 首页
 */
public class IndexFragment extends Fragment {

    private TextView tvZongheetaishi;
    private TextView tvShangbao;
    private TextView tvChat;
    private TextView tvData;
    private TextView tvBzPlan;
    private TextView tvTongji;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tvZongheetaishi = view.findViewById(R.id.tv_zongheetaishi);
        tvShangbao = view.findViewById(R.id.tv_shangbao);
        tvChat = view.findViewById(R.id.tv_chat);
        tvData = view.findViewById(R.id.tv_data);
        tvTongji = view.findViewById(R.id.tv_tongji);

        final MainActivity activity = (MainActivity) getActivity();

        tvShangbao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.setVpItem(3);
            }
        });

        tvZongheetaishi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.setVpItem(1);
            }
        });

        tvData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.setVpItem(2);
            }
        });

        tvChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int uid = MyApp.sharedPreferences.getInt("user_id", 0);
                if (uid == 0) {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                } else {
                    startActivity(new Intent(getContext(), ContactListActivity.class));
                }
            }
        });
        tvBzPlan = view.findViewById(R.id.tv_bz_plan);
        tvBzPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), EnsureManagerActivity.class));
            }
        });

        tvTongji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonWebActivity.start(getContext(),"统计分析","analys");
            }
        });

    }
}
