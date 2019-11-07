package com.android.deviceinfo.activitys.car;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.deviceinfo.R;
import com.android.deviceinfo.activitys.ammo.AddAmmoActivity;
import com.android.deviceinfo.activitys.ammo.AmmoBean;
import com.android.deviceinfo.activitys.plane.PlaneDetailActivity;
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

import androidx.annotation.Nullable;

public class CarListActivity extends BaseActivity {

    private TextView tvNum;
    private ListView listview;
    private Button btnAdd;
    private ImageView imgBack;
    private RelativeLayout loading;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);
        initView();
        initData();
    }

    private void initView() {
        loading = findViewById(R.id.loading);
        tvEmpty = findViewById(R.id.tv_empty);
        tvNum = findViewById(R.id.tv_num);
        listview = findViewById(R.id.listview);
        btnAdd = findViewById(R.id.btn_add);
        imgBack = findViewById(R.id.img_back);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(mContext, AddCarActivity.class),11);
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initData() {
        tvNum.setText(orgname + "车辆数量：0");
        NetUtils.executeGetRequest(this,"getVehicle", null,
                new ICallBack<CarListBean>() {
                    @Override
                    public void onSucceed(CarListBean data) {
                        loading.setVisibility(View.GONE);
                        if (data.isSucceed()) {
                            if (data.data == null || data.data.isEmpty()) {
//                                tvEmpty.setVisibility(View.VISIBLE);
                                tvNum.setText(orgname + "车辆数量：0");
                                setData(new ArrayList<CarListBean.DataBean>());
                            } else {
                                tvNum.setText(orgname + "车辆数量：" + data.data.size());
                                tvEmpty.setVisibility(View.GONE);
                                setData(data.data);
                            }
                        }
                    }

                    @Override
                    public void onFailed(final String msg) {
                        if (isFinishing()) {
                            return;
                        }
                        loading.setVisibility(View.GONE);
                        ToastUtil.toastCenter(mContext, msg);
                    }
                });
    }

    private void setData(final List<CarListBean.DataBean> list) {
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
                final ViewHolder viewHolder;
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(viewGroup.getContext()).
                        inflate(R.layout.item_plane, viewGroup, false);
                convertView.setTag(viewHolder);

                viewHolder.layout = convertView.findViewById(R.id.layout);
                viewHolder.tvType = convertView.findViewById(R.id.tv_type);
                viewHolder.tvCreateTime = convertView.findViewById(R.id.tv_create_time);
                viewHolder.tvModel = convertView.findViewById(R.id.tv_model);
                viewHolder.tvState = convertView.findViewById(R.id.tv_state);
                viewHolder.tvChangjia = convertView.findViewById(R.id.tv_changjia);
                viewHolder.content = convertView.findViewById(R.id.content);
                viewHolder.imgage = convertView.findViewById(R.id.imgage);
                viewHolder.imgage.setVisibility(View.GONE);
                final CarListBean.DataBean data = list.get(i);
                /**
                 * organiz: String, // 单位
                 *     vehicle_id: Number, // 车辆ID
                 *     model: String,       // 车辆类型
                 *     name: String, // 车辆名称
                 *     service: String, // 服务机型
                 *     code: String,    // 出厂号码
                 *     armyId: String, // 部队编号
                 *     product: String, // 制造厂
                 *     productTime: String, // 出厂时间
                 *     mileage: String, // 总里程
                 *     life: String, // 总寿命
                 *     stageCourse: String, // 行驶里程
                 *     repairNumber: String, // 大修次数
                 *     state: String, // 车辆状态
                 *     taskState: String, // 车辆任务状态
                 */
                viewHolder.tvType.setText("车辆名称：" + data.name);
                viewHolder.tvCreateTime.setText("出厂时间：" + data.productTime + "\n" +
                        "车辆类型：" + data.model + "\n车辆任务状态：" + data.taskState + "\n" +
                        "车辆状态：" + data.state + "\n服务机型：" + data.service + "\n" +
                        "部队编号：" + data.armyId + "\n制造厂：" + data.product);

                viewHolder.content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, CarDetailActivity.class);
                        intent.putExtra("data",list.get(i));
                        startActivityForResult(intent,11);
                    }
                });

                viewHolder.tvType.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (viewHolder.content.getVisibility() == View.VISIBLE){
                            viewHolder.content.setVisibility(View.GONE);
                        }else {
                            if (i == list.size() - 1){
                                listview.smoothScrollToPosition(i);
                            }
                            viewHolder.content.setVisibility(View.VISIBLE);
                        }
                    }
                });

                viewHolder.content.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        new AlertDialog.Builder(mContext)
                                .setTitle("提示")
                                .setMessage("确认删除此项吗？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        delete(data.vehicle_id);
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .show();
                        return false;
                    }
                });
                return convertView;
            }
        });
    }

    private void delete(int id){
        NetUtils.executeDeleteRequest(this,"deleteVehicle/" + id, null,
                new ICallBack<BaseResponseBean>() {
                    @Override
                    public void onSucceed(BaseResponseBean data) {
                        ToastUtil.toastCenter(mContext, "删除成功");
                        initData();
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

    private class ViewHolder{

        private LinearLayout layout;
        private TextView tvChangjia;
        private TextView tvState;
        private TextView tvModel;
        private TextView tvCreateTime;
        private TextView tvType;
        private LinearLayout content;
        private ImageView imgage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11 && resultCode == RESULT_OK){
            initData();
        }
    }
}
