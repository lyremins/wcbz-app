package com.android.deviceinfo.activitys.state_config;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.deviceinfo.MyApp;
import com.android.deviceinfo.R;
import com.android.deviceinfo.base.BaseActivity;
import com.android.deviceinfo.base.ICallBack;
import com.android.deviceinfo.base.SimpleRecyclerAdapter;
import com.android.deviceinfo.base.SimpleViewHolder;
import com.android.deviceinfo.bean.BaseResponseBean;
import com.android.deviceinfo.utils.AppUtils;
import com.android.deviceinfo.utils.NetUtils;
import com.android.deviceinfo.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class EditStatusActivity extends BaseActivity {

    private List<String> list = new ArrayList<>();
    private ImageView imgBack;
    private RecyclerView rvStateConfig;
    private TextView btnAdd;

    private String name;

    private String value;

    EditAdapter adapter;

    public static void start(Context context,String name,String value){
        Intent intent = new Intent(context,EditStatusActivity.class);
        intent.putExtra("name",name);
        intent.putExtra("value",value);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_status);
        initView();
    }

    private void initView() {

        imgBack = findViewById(R.id.img_back);
        rvStateConfig = findViewById(R.id.rv_state_config);

        value = getIntent().getStringExtra("value");
        name = getIntent().getStringExtra("name");
        adapter = new EditAdapter();
        rvStateConfig.setAdapter(adapter);
        rvStateConfig.setLayoutManager(new LinearLayoutManager(this));
        List<String> strlist = MyApp.strToList(value);
        if (strlist != null){
            list.addAll(strlist);
            adapter.setListData(list);
            adapter.notifyDataSetChanged();
        }
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnAdd = findViewById(R.id.btn_add);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
    }

    public class EditAdapter extends SimpleRecyclerAdapter<String> {

        @NonNull
        @Override
        public SimpleViewHolder<String> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new EditViewHolder(LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.item_edit, parent, false));
        }


        class EditViewHolder extends SimpleViewHolder<String> {

            private EditText etItem;
            private TextView tvDelete;

            public EditViewHolder(View itemView) {
                super(itemView);
                etItem = itemView.findViewById(R.id.et_item);
                tvDelete = itemView.findViewById(R.id.tv_delete);
            }

            @Override
            protected void refreshView(String data) {
                super.refreshView(data);
                etItem.setText(data);
                tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (getAdapterPosition() == -1){
                            return;
                        }
                        new AlertDialog.Builder(mContext).setTitle("提示").setMessage("确认删除此项吗？").
                                setNegativeButton("取消",null).
                                setPositiveButton("确认删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                list.remove(getAdapterPosition());
                                EditStatusActivity.this.adapter.notifyDataSetChanged();
                            }
                        }).show();
                    }
                });

                etItem.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        String s = etItem.getText().toString();
                        list.set(getAdapterPosition(),s);
                    }
                });

            }
        }
    }

    private void save() {
        String value = MyApp.listToStr(list);
        showHud();
        Map<String,String> map = new HashMap<>();
        map.put(name, value);
        NetUtils.executePostRequest(this, "updateConfig", map,
                new ICallBack<BaseResponseBean>() {
                    @Override
                    public void onSucceed(BaseResponseBean data) {
                        disMissHud();
                        ToastUtil.toastCenter(mContext, "编辑成功");
                        MyApp.getConfig();
                        finish();
                    }

                    @Override
                    public void onFailed(final String msg) {
                        disMissHud();
                        ToastUtil.toastCenter(mContext, msg);
                    }
                });
    }

}
