package com.android.deviceinfo.activitys.ammo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.deviceinfo.R;
import com.android.deviceinfo.activitys.plane.AddPlaneActivity;
import com.android.deviceinfo.activitys.plane.PlaneListBean;
import com.android.deviceinfo.activitys.taishi.AmmoStateActivity;
import com.android.deviceinfo.base.BaseActivity;
import com.android.deviceinfo.base.ICallBack;
import com.android.deviceinfo.bean.BaseResponseBean;
import com.android.deviceinfo.utils.NetUtils;
import com.android.deviceinfo.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 弹药列表
 */
public class AmmoListActivity extends BaseActivity {

    private TextView tvNum;
    private ListView listview;
    private Button btnAdd;
    private ImageView imgBack;
    private RelativeLayout loading;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ammo_list);
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
        tvNum.setText(orgname + "弹药数量：0");
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(mContext, AddAmmoActivity.class),11);
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
        NetUtils.executeGetRequest(this,"getAmmo", null,
                new ICallBack<AmmoBean>() {
                    @Override
                    public void onSucceed(AmmoBean data) {
                        loading.setVisibility(View.GONE);
                        if (data.isSucceed()) {
                            if (data.data == null || data.data.isEmpty()) {
//                                tvEmpty.setVisibility(View.VISIBLE);
                                tvNum.setText(orgname + "弹药数量：0");
                                setData(new ArrayList<AmmoBean.DataBean>());
                            } else {
                                tvNum.setText(orgname + "弹药数量：" + data.data.size());
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

    private void setData(final List<AmmoBean.DataBean> list) {
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
                if (convertView == null) {
                    viewHolder = new ViewHolder();
                    convertView = LayoutInflater.from(viewGroup.getContext()).
                            inflate(R.layout.item_plane, viewGroup, false);
                    convertView.setTag(viewHolder);
                }else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                /***
                 * filed1: String, // 型号
                 *  filed2: String, // 出厂号码
                 * filed3: String, // 制造厂
                 * filed4: String, // 日历寿命
                 * filed5: String, // 出厂日期
                 * filed6: String, // 总挂⻜小时
                 * filed7: String // 已挂⻜小时
                 */
                viewHolder.tvType = convertView.findViewById(R.id.tv_type);
                viewHolder.tvCreateTime = convertView.findViewById(R.id.tv_create_time);
                viewHolder.tvModel = convertView.findViewById(R.id.tv_model);
                viewHolder.tvState = convertView.findViewById(R.id.tv_state);
                viewHolder.tvChangjia = convertView.findViewById(R.id.tv_changjia);
                viewHolder.content = convertView.findViewById(R.id.content);
                viewHolder.imgage = convertView.findViewById(R.id.imgage);
                final AmmoBean.DataBean data = list.get(i);
                viewHolder.tvType.setText("型号：" + data.filed1 );
                viewHolder.tvCreateTime.setText("总挂⻜小时：" + data.filed6 + "\n已挂⻜小时：" + data.filed7 + "\n" +
                                "出厂日期：" + data.filed5 + "\n日历寿命：" + data.filed4 + "\n" +
                        "生产厂家：" + data.filed3 + "\n出厂号码：" + data.filed2);
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
                viewHolder.imgage.setVisibility(View.GONE);
                viewHolder.content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, AmmoDetailActivity.class);
                        intent.putExtra("data",list.get(i));
                        startActivityForResult(intent,11);
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
                                        delete(data.ammo_id);
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
        NetUtils.executeDeleteRequest(this,"deleteAmmo/" + id, null,
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
