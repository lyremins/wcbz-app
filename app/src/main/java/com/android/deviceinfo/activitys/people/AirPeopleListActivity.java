package com.android.deviceinfo.activitys.people;

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
import com.android.deviceinfo.activitys.fault.AddFaultActivity;
import com.android.deviceinfo.activitys.fault.FaultListBean;
import com.android.deviceinfo.activitys.plane.PlaneDetailActivity;
import com.android.deviceinfo.base.BaseActivity;
import com.android.deviceinfo.base.ICallBack;
import com.android.deviceinfo.bean.BaseResponseBean;
import com.android.deviceinfo.utils.NetUtils;
import com.android.deviceinfo.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class AirPeopleListActivity extends BaseActivity {

    private TextView tvNum;
    private ListView listview;
    private Button btnAdd;
    private ImageView imgBack;
    private RelativeLayout loading;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_people_manager);
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
                startActivityForResult(new Intent(mContext, AddAirPeopleActivity.class), 11);
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
        tvNum.setText(orgname + "\n机务人员数量：0");
        NetUtils.executeGetRequest(this, "getPersonnel", null,
                new ICallBack<AirPeopleListBean>() {
                    @Override
                    public void onSucceed(AirPeopleListBean data) {
                        loading.setVisibility(View.GONE);
                        if (data.isSucceed()) {
                            if (data.data == null || data.data.isEmpty()) {
//                                tvEmpty.setVisibility(View.VISIBLE);
                                tvNum.setText(orgname + "\n机务人员数量：0");
                                setData(new ArrayList<AirPeopleListBean.DataBean>());
                            } else {
                                tvNum.setText(orgname + "\n机务人员数量：" + data.data.size());
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

    private void setData(final List<AirPeopleListBean.DataBean> list) {
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
                viewHolder.tvType = convertView.findViewById(R.id.tv_type);
                viewHolder.tvCreateTime = convertView.findViewById(R.id.tv_create_time);
                viewHolder.tvModel = convertView.findViewById(R.id.tv_model);
                viewHolder.tvState = convertView.findViewById(R.id.tv_state);
                viewHolder.tvChangjia = convertView.findViewById(R.id.tv_changjia);
                viewHolder.content = convertView.findViewById(R.id.content);
                viewHolder.imgage = convertView.findViewById(R.id.imgage);
                viewHolder.imgage.setVisibility(View.GONE);
                final AirPeopleListBean.DataBean data = list.get(i);
                viewHolder.tvType.setText("姓名：" + data.user_name);
                viewHolder.tvCreateTime.setText("操作时间：" + data.create_time + "\n" +
                        "性别：" + data.sex + "\n联系方式：" + data.phone + "\n" +
                        "籍贯：" + data.nativeX + "\n所属单位：" + data.company + "\n职务：" + data.post + "\n" + "是否在岗：" + data.duty + "\n" +
                        "专业：" + data.major + "\n毕业院校：" + data.school+ "\n绑定飞机：" + data.bindAir);
                viewHolder.tvType.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (viewHolder.content.getVisibility() == View.VISIBLE) {
                            viewHolder.content.setVisibility(View.GONE);
                        } else {
                            if (i == list.size() - 1) {
                                listview.smoothScrollToPosition(i);
                            }
                            viewHolder.content.setVisibility(View.VISIBLE);
                        }
                    }
                });

                viewHolder.content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, PeopleDetailActivity.class);
                        intent.putExtra("data", list.get(i));
                        startActivityForResult(intent, 11);

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
                                        delete(data.person_id);
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
        NetUtils.executeDeleteRequest(this,"deletePersonnel/" + id, null,
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

    private class ViewHolder {

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
        if (requestCode == 11 && resultCode == RESULT_OK) {
            initData();
        }
    }
}
