package com.android.deviceinfo.activitys.main;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;

import com.android.deviceinfo.MyApp;
import com.android.deviceinfo.R;
import com.android.deviceinfo.activitys.login.UserBean;
import com.android.deviceinfo.activitys.taishi.TaiShiFragment;
import com.android.deviceinfo.base.BaseActivity;
import com.android.deviceinfo.base.ICallBack;
import com.android.deviceinfo.bean.BaseResponseBean;
import com.android.deviceinfo.utils.NetUtils;
import com.android.deviceinfo.utils.ThreadUtils;
import com.android.deviceinfo.utils.ToastUtil;
import com.blankj.utilcode.util.NetworkUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * 主界面
 *
 * @author smm
 */
public class MainActivity extends BaseActivity {

    private BottomNavigationView bottomNavigationView;
    private ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        initView();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void initView() {
        boolean upload = getIntent().getBooleanExtra("upload",false);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        viewpager = findViewById(R.id.viewpager);

        viewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return new IndexFragment();
                } else if (position == 1) {
                    return new TaiShiFragment();
                } else if (position == 2) {
                    return new DataFragment();
                } else {
                    return new ShangBaoFragment();
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        });

        final int[] itemId = {R.id.item_bottom_1, R.id.item_bottom_2, R.id.item_bottom_3, R.id.item_bottom_4};

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.setSelectedItemId(itemId[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.item_bottom_1:
                                viewpager.setCurrentItem(0, false);
                                break;
                            case R.id.item_bottom_2:
                                viewpager.setCurrentItem(1, false);
                                break;
                            case R.id.item_bottom_3:
                                viewpager.setCurrentItem(2, false);
                                break;
                            case R.id.item_bottom_4:
                                viewpager.setCurrentItem(3, false);
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });
        if (upload){
            viewpager.setCurrentItem(3,false);
        }
    }

    public void setVpItem(int num) {
        viewpager.setCurrentItem(num, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ThreadUtils.exec(new Runnable() {
            @Override
            public void run() {
                upLoadData();
            }
        });
    }

    private void upLoadData() {
        if (!NetworkUtils.isAvailableByPing()) {
            return;
        }
        String planeJson = MyApp.sharedPreferences.getString("addAirplane", "");
        if (!TextUtils.isEmpty(planeJson)) {
            Map map = new Gson().fromJson(planeJson, Map.class);
            NetUtils.executePostRequest(this, "addAirplane", map,
                    new ICallBack<BaseResponseBean>() {
                        @Override
                        public void onSucceed(BaseResponseBean data) {
                            ToastUtil.toastCenter(mContext, "离线缓存上传飞机数据完成");
                            Log.i("addAirplane", "离线缓存上传完成");
                            MyApp.editor.putString("addAirplane", "");
                            MyApp.editor.commit();
                        }

                        @Override
                        public void onFailed(final String msg) {

                        }
                    });
        }
        String perJson = MyApp.sharedPreferences.getString("addPersonnel", "");
        if (!TextUtils.isEmpty(perJson)) {
            Map map = new Gson().fromJson(perJson, Map.class);
            NetUtils.executePostRequest(this, "addPersonnel", map,
                    new ICallBack<BaseResponseBean>() {
                        @Override
                        public void onSucceed(BaseResponseBean data) {
                            ToastUtil.toastCenter(mContext, "离线缓存上传机务人员数据完成");
                            Log.i("addPersonnel", "离线缓存上传完成");
                            MyApp.editor.putString("addPersonnel", "");
                            MyApp.editor.commit();
                        }

                        @Override
                        public void onFailed(final String msg) {

                        }
                    });
        }

        String veJson = MyApp.sharedPreferences.getString("addVehicle", "");
        if (!TextUtils.isEmpty(veJson)) {
            Map map = new Gson().fromJson(veJson, Map.class);
            NetUtils.executePostRequest(this, "addVehicle", map,
                    new ICallBack<BaseResponseBean>() {
                        @Override
                        public void onSucceed(BaseResponseBean data) {
                            ToastUtil.toastCenter(mContext, "离线缓存上传车辆数据完成");
                            Log.i("addVehicle", "离线缓存上传完成");
                            MyApp.editor.putString("addVehicle", "");
                            MyApp.editor.commit();
                        }

                        @Override
                        public void onFailed(final String msg) {

                        }
                    });
        }

        String ammoJson = MyApp.sharedPreferences.getString("addAmmo", "");
        if (!TextUtils.isEmpty(ammoJson)) {
            Map map = new Gson().fromJson(ammoJson, Map.class);
            NetUtils.executePostRequest(this, "addAmmo", map,
                    new ICallBack<BaseResponseBean>() {
                        @Override
                        public void onSucceed(BaseResponseBean data) {
                            ToastUtil.toastCenter(mContext, "离线缓存上传弹药数据完成");
                            Log.i("addAmmo", "离线缓存上传完成");
                            MyApp.editor.putString("addAmmo", "");
                            MyApp.editor.commit();
                        }

                        @Override
                        public void onFailed(final String msg) {

                        }
                    });
        }

        String deviceJson = MyApp.sharedPreferences.getString("addDevice", "");
        if (!TextUtils.isEmpty(deviceJson)) {
            Map map = new Gson().fromJson(deviceJson, Map.class);
            NetUtils.executePostRequest(this, "addDevice", map,
                    new ICallBack<BaseResponseBean>() {
                        @Override
                        public void onSucceed(BaseResponseBean data) {
                            ToastUtil.toastCenter(mContext, "离线缓存上传器件数据完成");
                            Log.i("addDevice", "离线缓存上传完成");
                            MyApp.editor.putString("addDevice", "");
                            MyApp.editor.commit();
                        }

                        @Override
                        public void onFailed(final String msg) {

                        }
                    });
        }


        String uplaneJson = MyApp.sharedPreferences.getString("updateAirplane", "");
        if (!TextUtils.isEmpty(uplaneJson)) {
            Map map = new Gson().fromJson(uplaneJson, Map.class);
            NetUtils.executePostRequest(this, "updateAirplane", map,
                    new ICallBack<BaseResponseBean>() {
                        @Override
                        public void onSucceed(BaseResponseBean data) {
                            ToastUtil.toastCenter(mContext, "离线缓存上传飞机状态数据完成");
                            Log.i("updateAirplane", "离线缓存上传完成");
                            MyApp.editor.putString("updateAirplane", "");
                            MyApp.editor.commit();
                        }

                        @Override
                        public void onFailed(final String msg) {

                        }
                    });
        }
        String uPerJson = MyApp.sharedPreferences.getString("updatePersonnel", "");
        if (!TextUtils.isEmpty(uPerJson)) {
            Map map = new Gson().fromJson(uPerJson, Map.class);
            NetUtils.executePostRequest(this, "updatePersonnel", map,
                    new ICallBack<BaseResponseBean>() {
                        @Override
                        public void onSucceed(BaseResponseBean data) {
                            ToastUtil.toastCenter(mContext, "离线缓存上传人员状态数据完成");
                            Log.i("updatePersonnel", "离线缓存上传完成");
                            MyApp.editor.putString("updatePersonnel", "");
                            MyApp.editor.commit();
                        }

                        @Override
                        public void onFailed(final String msg) {

                        }
                    });
        }

        String uVJson = MyApp.sharedPreferences.getString("updateVehicle", "");
        if (!TextUtils.isEmpty(uVJson)) {
            Map map = new Gson().fromJson(uVJson, Map.class);
            NetUtils.executePostRequest(this, "updateVehicle", map,
                    new ICallBack<BaseResponseBean>() {
                        @Override
                        public void onSucceed(BaseResponseBean data) {
                            ToastUtil.toastCenter(mContext, "离线缓存上传车辆数据完成");
                            Log.i("updateVehicle", "离线缓存上传完成");
                            MyApp.editor.putString("updateVehicle", "");
                            MyApp.editor.commit();
                        }

                        @Override
                        public void onFailed(final String msg) {

                        }
                    });
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Map<String, String> map = new HashMap<>();
        map.put("username", MyApp.sharedPreferences.getString("name",""));
        map.put("password", MyApp.sharedPreferences.getString("password",""));
        NetUtils.executePostRequest(this,"v2/login", map,
                new ICallBack<UserBean>() {
                    @Override
                    public void onSucceed(UserBean data) {
                        MyApp.editor.putInt("user_id",data.data.user_id);
                        MyApp.editor.putString("org",data.data.orgname);
                        MyApp.editor.apply();
                    }

                    @Override
                    public void onFailed(String msg) {
                    }
                });
    }
}
