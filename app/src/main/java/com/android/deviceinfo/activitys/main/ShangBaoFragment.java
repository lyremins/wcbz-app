package com.android.deviceinfo.activitys.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.deviceinfo.R;
import com.android.deviceinfo.activitys.upload_state.CarStateFragment;
import com.android.deviceinfo.activitys.upload_state.PeopleStateFragment;
import com.android.deviceinfo.activitys.upload_state.PlaneStateFragment;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * 状态上报
 */
public class ShangBaoFragment extends Fragment {

    private TabLayout tablayout;
    private ViewPager viewpager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shangbao, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tablayout = view.findViewById(R.id.tablayout);
        viewpager = view.findViewById(R.id.viewpager);

        tablayout.addTab(tablayout.newTab().setText("飞机"));
        tablayout.addTab(tablayout.newTab().setText("车辆"));
        tablayout.addTab(tablayout.newTab().setText("机务人员"));

        viewpager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                if(position == 0) {
                    return new PlaneStateFragment();
                }else if (position == 1){
                    return new CarStateFragment();
                }else {
                    return new PeopleStateFragment();
                }
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                if (position == 0){
                    return "飞机";
                }else if (position == 1){
                    return "车辆";
                }else {
                    return "机务人员";
                }
            }
        });
        tablayout.setupWithViewPager(viewpager);

    }
}
