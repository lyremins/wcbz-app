package com.android.deviceinfo.activitys.fault;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.deviceinfo.R;
import com.android.deviceinfo.base.BaseActivity;
import com.android.deviceinfo.base.ICallBack;
import com.android.deviceinfo.utils.NetUtils;
import com.android.deviceinfo.utils.ToastUtil;

import java.util.List;

import androidx.annotation.Nullable;

public class FaultListActivity extends BaseActivity {

    private TextView tvNum;
    private ListView listview;
    private Button btnAdd;
    private ImageView imgBack;
    private RelativeLayout loading;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fault_list);
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
                startActivityForResult(new Intent(mContext, AddFaultActivity.class),11);
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
        NetUtils.executeGetRequest(this,"getFault", null,
                new ICallBack<FaultListBean>() {
                    @Override
                    public void onSucceed(FaultListBean data) {
                        loading.setVisibility(View.GONE);
                        if (data.isSucceed()) {
                            if (data.data == null || data.data.isEmpty()) {
//                                tvEmpty.setVisibility(View.VISIBLE);
                            } else {
                                tvNum.setText("故障数量：" + data.data.size());
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

    private void setData(final List<FaultListBean.DataBean> list) {
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
                FaultListBean.DataBean data = list.get(i);
//                viewHolder.tvType.setText("型号：" + data.filed1);
//                viewHolder.tvCreateTime.setText("操作时间：" + data.create_time);
//                viewHolder.tvModel.setText("总挂⻜小时：" + data.filed6 + "    已挂⻜小时：" + data.filed7);
//                viewHolder.tvState.setText("出厂日期：" + data.filed5 + "    日历寿命：" + data.filed4);
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
