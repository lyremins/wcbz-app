package com.android.deviceinfo.activitys.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.deviceinfo.MyApp;
import com.android.deviceinfo.R;
import com.android.deviceinfo.activitys.fault.FaultListBean;
import com.android.deviceinfo.activitys.login.UserBean;
import com.android.deviceinfo.base.BaseActivity;
import com.android.deviceinfo.base.ICallBack;
import com.android.deviceinfo.utils.NetUtils;
import com.android.deviceinfo.utils.ToastUtil;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class ContactListActivity extends BaseActivity {

    private ListView listview;
    private ImageView imgBack;
    private RelativeLayout loading;
    private TextView tvEmpty,tvGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        initView();
        initData();
    }

    private void initView() {
        imgBack = findViewById(R.id.img_back);
        listview = findViewById(R.id.listview);
        loading = findViewById(R.id.loading);
        tvEmpty = findViewById(R.id.tv_empty);
        tvGroup = findViewById(R.id.tv_group);

        tvGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatActivity.start(mContext,"群聊",1
                        ,MyApp.sharedPreferences.getInt("user_id",0));
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
        NetUtils.executeGetRequest(this,"v1/users/list", null,
                new ICallBack<ContractBean>() {
                    @Override
                    public void onSucceed(ContractBean data) {
                        loading.setVisibility(View.GONE);
                        if (data.isSucceed()) {
                            if (data.data == null || data.data.isEmpty()) {
                                tvEmpty.setVisibility(View.VISIBLE);
                            } else {
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

    private void setData(final List<UserBean.DataBean> list) {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (list.get(i).user_id == MyApp.sharedPreferences.getInt("user_id",0)){
                    ToastUtil.toastCenter(mContext,"不能和自己聊天");
                    return;
                }
                ChatActivity.start(mContext,list.get(i).username,0,list.get(i).user_id);
            }
        });
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
                    convertView = LayoutInflater.from(viewGroup.getContext()).
                            inflate(R.layout.item_user, viewGroup, false);
                TextView tvType = convertView.findViewById(R.id.tv_name);
                ImageView imageView = convertView.findViewById(R.id.img_head);
                imageView.setVisibility(View.VISIBLE);
                tvType.setText(list.get(i).username);
                return convertView;
            }
        });
    }


}
