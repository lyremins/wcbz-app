package com.android.deviceinfo.activitys.air_plan;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.deviceinfo.R;
import com.android.deviceinfo.activitys.fault.AddFaultActivity;
import com.android.deviceinfo.activitys.fault.FaultListBean;
import com.android.deviceinfo.base.BaseActivity;
import com.android.deviceinfo.base.ICallBack;
import com.android.deviceinfo.utils.NetUtils;
import com.android.deviceinfo.utils.ToastUtil;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class AirPlanListActivity extends BaseActivity {

    private TextView tvNum;
    private ListView listview;
    private Button btnAdd;
    private ImageView imgBack;
    private RelativeLayout loading;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_plan_list);
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
                startActivityForResult(new Intent(mContext, AddAirPlanActivity.class),11);
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
        NetUtils.executeGetRequest(this,"getPlan", null,
                new ICallBack<AirPlanListBean>() {
                    @Override
                    public void onSucceed(AirPlanListBean data) {
                        loading.setVisibility(View.GONE);
                        if (data.isSucceed()) {
                            if (data.data == null || data.data.isEmpty()) {
//                                tvEmpty.setVisibility(View.VISIBLE);
                            } else {
                                tvNum.setText("飞行计划数量：" + data.data.size());
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

    private void setData(final List<AirPlanListBean.DataBean> list) {
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
            public View getView(int i, View convertView, ViewGroup viewGroup) {
                ViewHolder viewHolder;
                if (convertView == null) {
                    viewHolder = new ViewHolder();
                    convertView = LayoutInflater.from(viewGroup.getContext()).
                            inflate(R.layout.item_plane, viewGroup, false);
                    convertView.setTag(viewHolder);
                }else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                viewHolder.tvType = convertView.findViewById(R.id.tv_type);
                viewHolder.tvCreateTime = convertView.findViewById(R.id.tv_create_time);
                viewHolder.tvModel = convertView.findViewById(R.id.tv_model);
                viewHolder.tvState = convertView.findViewById(R.id.tv_state);
                viewHolder.tvChangjia = convertView.findViewById(R.id.tv_changjia);
                AirPlanListBean.DataBean data = list.get(i);
                /**
                 plan_id: Number, // ⻜机
                 ID name: String, 
                 dateTime: String,
                  airName: String,
                  vehicleName: String,
                  subjectName: String,
                  dateTime: String, // 选择日期
                 airSubject: String, // ⻜行科目
                 sceneSubject: String, // 气象科目
                 upDownNumber: String, // 起落次数
                 flightTime: String, // ⻜行时间
                 approachTime: String, // 进场时间
                 totalNumber: String, // 总人数
                 */
                viewHolder.tvType.setText("飞行科目：" + data.airSubject);
                viewHolder.tvCreateTime.setText("气象科目：" + data.sceneSubject);
                viewHolder.tvModel.setText("起落次数：" + data.upDownNumber + "    飞行时间：" + data.flightTime);
                viewHolder.tvState.setText("进场时间：" + data.approachTime + "    总人数：" + data.totalNumber);
//                viewHolder.tvChangjia.setText("生产厂家：" + data.filed3 + "   出厂号码：" + data.filed2);
                return convertView;
            }
        });
    }

    private class ViewHolder{

        private TextView tvChangjia;
        private TextView tvState;
        private TextView tvModel;
        private TextView tvCreateTime;
        private TextView tvType;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11 && resultCode == RESULT_OK){
            initData();
        }
    }
}
