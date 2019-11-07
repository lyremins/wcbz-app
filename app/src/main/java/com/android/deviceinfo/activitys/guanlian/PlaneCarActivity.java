package com.android.deviceinfo.activitys.guanlian;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.deviceinfo.MyApp;
import com.android.deviceinfo.R;
import com.android.deviceinfo.activitys.car.CarListBean;
import com.android.deviceinfo.activitys.plane.PlaneListBean;
import com.android.deviceinfo.base.BaseActivity;
import com.android.deviceinfo.base.ICallBack;
import com.android.deviceinfo.bean.BaseResponseBean;
import com.android.deviceinfo.utils.NetUtils;
import com.android.deviceinfo.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaneCarActivity extends BaseActivity {

    AlertDialog alertDialog3;

    private List<CarListBean.DataBean> carList = new ArrayList<>();

    private List<PlaneListBean.DataBean> planeList = new ArrayList<>();

    private List<String> sCarList = new ArrayList<>();

    private int position;
    private ImageView imgBack;
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plane_car);
        initData();
        initView();
    }

    private void initData() {
        NetUtils.executeGetRequest(this, "getVehicle", null,
                new ICallBack<CarListBean>() {
                    @Override
                    public void onSucceed(CarListBean data) {
                        if (data == null) {
                            return;
                        }
                        if (data.isSucceed()) {
                            if (data.data == null || data.data.isEmpty()) {
                            } else {
                                carList.addAll(data.data);
                            }
                        }
                    }

                    @Override
                    public void onFailed(final String msg) {
                        ToastUtil.toastCenter(mContext, msg);
                    }
                });
        MyApp.getPlane(this,
                new ICallBack<PlaneListBean>() {
                    @Override
                    public void onSucceed(PlaneListBean data) {
                        if (data == null) {
                            return;
                        }
                        if (data.isSucceed()) {
                            if (data.data == null || data.data.isEmpty()) {

                            } else {
                                setData(data.data);
                            }
                        }
                    }

                    @Override
                    public void onFailed(final String msg) {
                        if (isFinishing()) {
                            return;
                        }
                        ToastUtil.toastCenter(mContext, msg);

                    }
                });

    }

    public void showMutilAlertDialog() {
        final String[] array = new String[carList.size()];
        for (int i = 0; i < carList.size(); i++) {
            array[i] = carList.get(i).name;
        }
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("选择车辆");
        /**
         *第一个参数:弹出框的消息集合，一般为字符串集合
         * 第二个参数：默认被选中的，布尔类数组
         * 第三个参数：勾选事件监听
         */
        alertBuilder.setMultiChoiceItems(array, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                if (isChecked) {
                    sCarList.add(carList.get(i).vehicle_id + "");
                } else {
                    sCarList.remove(carList.get(i).vehicle_id + "");
                }
            }
        });
        alertBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog3.dismiss();
                if (sCarList.isEmpty()){
                    return;
                }
                for (String s : sCarList){
                    bind(s);
                }
            }
        });

        alertBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog3.dismiss();
            }
        });
        alertDialog3 = alertBuilder.create();
        alertDialog3.show();
    }

    private void bind(String s) {
        final Map<String, String> map = new HashMap<>();
        map.put("air_code", planeList.get(position).airplane_id + "");
        map.put("car_code", s);
        NetUtils.executePostRequest(mContext, "addAirplaneCar", map,
                new ICallBack<BaseResponseBean>() {
                    @Override
                    public void onSucceed(BaseResponseBean data) {
                        disMissHud();
                        ToastUtil.toastCenter(mContext, "上传成功");
                    }

                    @Override
                    public void onFailed(final String msg) {
                        disMissHud();
                        ToastUtil.toastCenter(mContext, msg);
                    }
                });
    }


    private void setData(final List<PlaneListBean.DataBean> list) {
        planeList.clear();
        planeList.addAll(list);
        listview.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(final int i, View convertView, ViewGroup viewGroup) {
                convertView = LayoutInflater.from(viewGroup.getContext()).
                        inflate(R.layout.item_user, viewGroup, false);
//                }else {
//                    viewHolder = (ViewHolder) convertView.getTag();
//                }
                TextView name = convertView.findViewById(R.id.tv_name);
                PlaneListBean.DataBean data = list.get(i);
                name.setText(data.type);
                return convertView;
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                position = i;
                showMutilAlertDialog();
            }
        });
    }

    private void initView() {
        imgBack = findViewById(R.id.img_back);
        listview = findViewById(R.id.listview);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
