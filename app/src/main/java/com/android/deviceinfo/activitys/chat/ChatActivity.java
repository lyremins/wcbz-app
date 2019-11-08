package com.android.deviceinfo.activitys.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.deviceinfo.MyApp;
import com.android.deviceinfo.R;
import com.android.deviceinfo.activitys.car.CarListBean;
import com.android.deviceinfo.activitys.plane.AddPlaneActivity;
import com.android.deviceinfo.base.BaseActivity;
import com.android.deviceinfo.base.ICallBack;
import com.android.deviceinfo.bean.BaseResponseBean;
import com.android.deviceinfo.bean.UploadImageBean;
import com.android.deviceinfo.utils.AppUtils;
import com.android.deviceinfo.utils.NetUtils;
import com.android.deviceinfo.utils.ThreadUtils;
import com.android.deviceinfo.utils.ToastUtil;
import com.android.deviceinfo.utils.matisse_utils.MatissePhotoHelper;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * 聊天界面
 */
public class ChatActivity extends BaseActivity {

    private RelativeLayout rlTitle;
    private ImageView imgBack;
    private TextView tvTitle;
    private RecyclerView recyclerview;
    private LinearLayout ltBottoom;
    private EditText etContent;
    private Button btnSend;

    private ImageView image;
    private int touid;

    private ChatAdapter chatAdapter = new ChatAdapter();

    private int chatNum;

    public static void start(Context context, String name, int chatType, int touid) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("touid", touid);
        intent.putExtra("name", name);
        intent.putExtra("type", chatType);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
        tvTitle.setText(getIntent().getStringExtra("name"));
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(chatAdapter);
        touid = getIntent().getIntExtra("touid", 0);
        getData(true);
    }

    private void getData(final boolean flg) {
        Map<String, String> map = new HashMap<>();
        map.put("fromuid", MyApp.sharedPreferences.getInt("user_id", 0) + "");
        map.put("touid", touid + "");
        NetUtils.executeGetRequest(this, "getChat", map,
                new ICallBack<ChatBean>() {
                    @Override
                    public void onSucceed(ChatBean data) {
                        if (data.isSucceed()) {
                            if (data.data == null || data.data.isEmpty()) {
                            } else {
                                setData(data.data, flg);
                            }
                            ThreadUtils.exec(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            getData(false);
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

    private void setData(List<ChatBean.DataBean> data, boolean flg) {
        chatAdapter.setListData(data);
        chatAdapter.notifyDataSetChanged();
//        if (flg) {
//            recyclerview.scrollToPosition(data.size() - 1);
//        }
        if (chatNum < data.size()){
            chatNum = data.size();
            recyclerview.scrollToPosition(data.size() - 1);
        }
    }

    private void initView() {
        imgBack = findViewById(R.id.img_back);
        tvTitle = findViewById(R.id.tv_title);
        recyclerview = findViewById(R.id.recyclerview);
        etContent = findViewById(R.id.et_content);
        btnSend = findViewById(R.id.btn_send);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        image = findViewById(R.id.image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MatissePhotoHelper.selectPhoto(ChatActivity.this, 1);
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(etContent.getText().toString().trim())) {
                    sendTextMessage();
                }
            }
        });
    }

    private void sendTextMessage() {
        /**
         *content //内容
         * createtime //发送时间
         * contentType //类型 0文本 1图片
         * chatType // 0单聊 1群聊
         * img_path  //类型为图片时的图片地址
         * fromuid 发送人uid
         * touid 接收人uid
         */
        Map<String, String> map = new HashMap<>();
        map.put("touid", touid + "");
        map.put("image_path", "0.png");
        map.put("contentType", "0");
        map.put("content", etContent.getText().toString().trim());
        map.put("chatType", "0");
        map.put("fromuid", MyApp.sharedPreferences.getInt("user_id", 0) + "");

        NetUtils.executePostRequest(this, "addChat", map,
                new ICallBack<BaseResponseBean>() {
                    @Override
                    public void onSucceed(BaseResponseBean data) {
                        ToastUtil.toastCenter(mContext, "发送成功");
                        etContent.setText("");
                        getData(true);
                    }

                    @Override
                    public void onFailed(final String msg) {
                        ToastUtil.toastCenter(mContext, msg);

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MatissePhotoHelper.REQUEST_CODE_CHOOSE) {
            if (resultCode == RESULT_OK) {
                // 获取返回的图片列表
                final List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                final File file = new File(path.get(0));
                showHud();
                ThreadUtils.exec(new Runnable() {
                    @Override
                    public void run() {
                        NetUtils.upload(file, ChatActivity.this,
                                new ICallBack<UploadImageBean>() {
                                    @Override
                                    public void onSucceed(UploadImageBean data) {
                                        disMissHud();
                                        sendImageMessage(data.image_path);
                                    }

                                    @Override
                                    public void onFailed(String msg) {
                                        disMissHud();
                                        ToastUtil.toastCenter(ChatActivity.this, msg);
                                    }
                                });
                    }
                });
            }
        }
    }

    private void sendImageMessage(String url) {
        /**
         *content //内容
         * createtime //发送时间
         * contentType //类型 0文本 1图片
         * chatType // 0单聊 1群聊
         * imgurl  //类型为图片时的图片地址
         * fromuid 发送人uid
         * touid 接收人uid
         */
        Map<String, String> map = new HashMap<>();
        map.put("touid", touid + "");
        map.put("image_path", url);
        map.put("fromuid", "1");
        map.put("contentType", "1");
        map.put("chatType", "0");
        map.put("fromuid", MyApp.sharedPreferences.getInt("user_id", 0) + "");
        NetUtils.executePostRequest(this, "addChat", map,
                new ICallBack<BaseResponseBean>() {
                    @Override
                    public void onSucceed(BaseResponseBean data) {
                        ToastUtil.toastCenter(mContext, "发送成功");
                        etContent.setText("");
                        getData(true);
                    }

                    @Override
                    public void onFailed(final String msg) {
                        ToastUtil.toastCenter(mContext, msg);

                    }
                });
    }

}
