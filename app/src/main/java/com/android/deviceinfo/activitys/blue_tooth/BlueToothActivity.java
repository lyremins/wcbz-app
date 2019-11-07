package com.android.deviceinfo.activitys.blue_tooth;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.deviceinfo.MyApp;
import com.android.deviceinfo.R;
import com.android.deviceinfo.base.BaseActivity;
import com.android.deviceinfo.base.ICallBack;
import com.android.deviceinfo.bean.BaseResponseBean;
import com.android.deviceinfo.utils.AppUtils;
import com.android.deviceinfo.utils.LogUtils;
import com.android.deviceinfo.utils.NetUtils;
import com.android.deviceinfo.utils.ThreadUtils;
import com.android.deviceinfo.utils.ToastUtil;
import com.bdsdk.dto.CardLocationDto;
import com.google.gson.Gson;
import com.pancoit.bdboxsdk.bdsdk.AgentListener;
import com.pancoit.bdboxsdk.bdsdk.BeidouHandler;
import com.pancoit.bdboxsdk.constant.BdBoxParams;
import com.pancoit.bdboxsdk.entity.CardMessage;
import com.pancoit.bdboxsdk.entity.ReceiptType;
import com.pancoit.bdboxsdk.entity.UserMessage;
import com.pancoit.bdboxsdk.util.ContentConvertTOBoxMessage;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BlueToothActivity extends BaseActivity implements AgentListener {

    public static final String TAG = "BlueToothActivity";

    private ListView listview;
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    List<BluetoothDevice> bluetoothDevices = new ArrayList<>();

    List<String> uuids = new ArrayList<>();

    BlueToothAdapter adapter;
    private RelativeLayout rlTitle;
    private ImageView imgBack;
    private TextView btnRefresh,btnSend;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_tooth);
        initView();
    }

    private void initView() {
        textView = findViewById(R.id.textview);
        listview = findViewById(R.id.listview);
        btnSend = findViewById(R.id.btn_send);
        adapter = new BlueToothAdapter();
        listview.setAdapter(adapter);
        if (mBluetoothAdapter == null) {
            // 说明此设备不支持蓝牙操作
            ToastUtil.toastCenter(mContext, "当前设备不支持蓝牙");
            return;
        }
        String[] PERMISSIONS = {Manifest.permission.BLUETOOTH, Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_COARSE_LOCATION,};

        AndPermission.with(this).runtime().permission(PERMISSIONS)
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        LogUtils.i("权限判断", "拒绝");
                        // 判断用户是不是不再显示权限弹窗了，若不再显示的话进入权限设置页
                        ToastUtil.toastCenter(mContext, "没有蓝牙权限");
                    }
                }).onGranted(new Action<List<String>>() {
            @Override
            public void onAction(List<String> data) {
                LogUtils.i("权限判断", "允许");
                initBlue();
            }
        }).start();

        rlTitle = findViewById(R.id.rl_title);
        imgBack = findViewById(R.id.img_back);
        btnRefresh = findViewById(R.id.btn_refresh);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBluetoothAdapter.startDiscovery();
            }
        });

        BeidouHandler.agentListeners.add(this);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String json = getIntent().getStringExtra("json");
                final String touid = "943676".equals(MyApp.bdBoxId) ? "943675" : "943676";
                CardMessage cardMessage = ContentConvertTOBoxMessage.
                        getInstance().castUserMessageTo0x12(touid,json);
                //发送北斗短报文
                BeidouHandler.getInstance().sendBeidouMessage(cardMessage);
                ToastUtil.toastCenter(mContext,"发送消息");
            }
        });
    }

    private void initBlue() {
        // 没有开始蓝牙
        if (!mBluetoothAdapter.isEnabled()) {
            ToastUtil.toastCenter(mContext, "请开启蓝牙");
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 11);
        } else {
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    // 把名字和地址取出来添加到适配器中
                    if (uuids.indexOf(device.getAddress()) == -1) {
                        bluetoothDevices.add(device);
                        uuids.add(device.getAddress());
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }
        // 注册这个 BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        mBluetoothAdapter.startDiscovery();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                // 关闭发现设备
                mBluetoothAdapter.cancelDiscovery();
                ThreadUtils.exec(new Runnable() {
                    @Override
                    public void run() {
                        connect(bluetoothDevices.get(i));
                    }
                });
            }
        });
        rlTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }

    private void showDialog(){
        final View view = LayoutInflater.from(this).inflate(R.layout.edit, null);//这里必须是final的
        final EditText edit = (EditText)view.findViewById(R.id.editText);//获得输入框对象

        new AlertDialog.Builder(this)
                .setTitle("填写发送内容")//提示框标题
                .setView(view)
                .setPositiveButton("确定",//提示框的两个按钮
                        new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                final String touid = "943676".equals(MyApp.bdBoxId) ? "943675" : "943676";
                                CardMessage cardMessage = ContentConvertTOBoxMessage.
                                        getInstance().castUserMessageTo0x12(touid, edit.getText().toString().trim());
                                //发送北斗短报文
                                BeidouHandler.getInstance().sendBeidouMessage(cardMessage);
                                ToastUtil.toastCenter(mContext,"发送消息");
                            }
                        }).setNegativeButton("取消", null).create().show();
    }

    private void connect(BluetoothDevice device) {
        BeidouHandler.getInstance().startConnectBluetooth(device);
//        boolean connect = BeidouSDKHandler.bdbleHandler.initialize();
//        BeidouSDKHandler.bdbleHandler.connectDevice(device.getAddress());

//        BluetoothGatt bluetoothGatt = device.connectGatt(this, false, new BluetoothGattCallback() {
//            @Override
//            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
//                super.onConnectionStateChange(gatt, status, newState);
//                if (newState == BluetoothProfile.STATE_CONNECTED){
//                    ToastUtil.toastCenter(mContext,"连接成功");
//                }
//            }
//        });
//        bluetoothGatt.connect();
    }

    // 创建一个接受 ACTION_FOUND 的 BroadcastReceiver
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 当 Discovery 发现了一个设备
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // 从 Intent 中获取发现的 BluetoothDevice
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // 将名字和地址放入要显示的适配器中
                if (uuids.indexOf(device.getAddress()) == -1) {
                    bluetoothDevices.add(device);
                    uuids.add(device.getAddress());
                    adapter.notifyDataSetChanged();
                }

            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
        mBluetoothAdapter.cancelDiscovery();
        BeidouHandler.agentListeners.remove(this);
    }

    @Override
    public void onBeidouDisconnectSuccess() {
        ToastUtil.toastCenter(mContext, "onBeidouDisconnectSuccess" + "" + "---");
        LogUtils.e(TAG + "onBeidouDisconnectSuccess", "---");
    }

    @Override
    public void onBeidouConnectSuccess() {
        // 连接成功
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.toastCenter(mContext, "链接成功" + BdBoxParams.bdBoxId);

            }
        });

//        ToastUtil.toastCenter(mContext,"链接成功");
        MyApp.bdBoxId = BdBoxParams.bdBoxId;
        final String touid = "943676".equals(MyApp.bdBoxId) ? "943675" : "943676";
//        CardMessage cardMessage= ContentConvertTOBoxMessage.getInstance().castUserMessageTo0x12
//                (touid,"999");
//////发送北斗短报文
//        BeidouHandler.getInstance().sendBeidouMessage(cardMessage);
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                ToastUtil.toastCenter(mContext,MyApp.bdBoxId + "发送给"+ touid);
//            }
//        });
    }

    @Override
    public void sendStatusReceipt(final ReceiptType receiptType) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.toastCenter(mContext, receiptType.getValue() + "---" + receiptType.getStr());
            }
        });
        LogUtils.e("收到回执", receiptType.toString());
    }

    @Override
    public void onGetOfflineStateChange(int i, int i1) {
        ToastUtil.toastCenter(mContext, "onGetOfflineStateChange" + "" + i + "---" + i1);
        LogUtils.e(TAG + "onGetOfflineStateChange", "" + i + "---" + i1);
    }

    @Override
    public void onBeidouCardMessageReceivedOffline(UserMessage userMessage) {
        ToastUtil.toastCenter(mContext, "onBeidouCardMessageReceivedOffline" + userMessage.toString());
        LogUtils.e(TAG + "onBeidouCardMessageReceivedOffline", userMessage.toString());
    }

    @Override
    public void onReceiptMessages(String s, long l) {
        ToastUtil.toastCenter(mContext, "onReceiptMessages" + s + l);
        LogUtils.e(TAG + "onReceiptMessages", s + l);
    }

    @Override
    public void onReceiveUserMessage(final UserMessage userMessage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.toastCenter(mContext, userMessage.getContent() + "asdasdas");
            }
        });
        LogUtils.e(TAG + "onReceiveUserMessage", userMessage.getContent() + "asdasd" + userMessage.getSendNumber());
    }

    @Override
    public void onReceiveOtherMessage(String s, String s1, String s2) {
        ToastUtil.toastCenter(mContext, "onReceiveOtherMessage" + s + "---" + s1 + "---" + s2);
        LogUtils.e(TAG + "onReceiveOtherMessage", s);
        String str = AppUtils.hexStringToString(s1);
        textView.setText("收到消息：" + str);

        final Map<String,String> map = new Gson().fromJson(str,Map.class);
        if (str != null && map != null){
            String url = "";
            if (str.contains("person_id")){
                url = "updatePersonnel";
                ToastUtil.toastCenter(mContext, "接收到北斗上报人员状态消息，开始上传到服务器");
            }else if (str.contains("vehicle_id")){
                url = "updateVehicle";
                ToastUtil.toastCenter(mContext, "接收到北斗上报车辆状态消息，开始上传到服务器");
            }else {
                url = "updateAirplane";
                ToastUtil.toastCenter(mContext, "接收到北斗上报飞机状态消息，开始上传到服务器");
            }
            NetUtils.executePostRequest(mContext, url, map,
                    new ICallBack<BaseResponseBean>() {
                        @Override
                        public void onSucceed(BaseResponseBean data) {
                            ToastUtil.toastCenter(mContext, "上报成功");
                        }

                        @Override
                        public void onFailed(final String msg) {
                            if (TextUtils.equals(msg, NetUtils.NET_ERROR)){
                                // 离线存储
                                ThreadUtils.exec(new Runnable() {
                                    @Override
                                    public void run() {
                                        MyApp.editor.putString("updateAirplane",new Gson().toJson(map));
                                        MyApp.editor.commit();
                                    }
                                });
                                ToastUtil.toastCenter(mContext,"网络不可用,已离线存储");
                            }else {
                                ToastUtil.toastCenter(mContext, msg);
                            }
                        }
                    });
        }
//        map.put("airplane_id", id+"");
//        map.put("state", tvPlaneState.getText().toString().trim());
//        map.put("task", tvPlaneTask.getText().toString().trim());
//        if (!etQiluoNum.getText().toString().trim().isEmpty()) {
//            map.put("airUpOrDown", etQiluoNum.getText().toString().trim());
//        }
//        if (!etFlyHour.getText().toString().trim().isEmpty()) {
//            map.put("airHour", etFlyHour.getText().toString().trim());
//        }
//        if (!etQiluoTime.getText().toString().trim().isEmpty()) {
//            map.put("airTime", etQiluoTime.getText().toString().trim());
//        }
//        if (!TextUtils.isEmpty(tvPlaneEnter.getText().toString())) {
//            map.put("enter", tvPlaneEnter.getText().toString().trim());
//        }

    }

    @Override
    public void onReceiveBeidouUserId(final String s) {
        ToastUtil.toastCenter(mContext, "onReceiveBeidouUserId" + s);
        LogUtils.e(TAG + "onReceiveBeidouUserId", s);
    }

    @Override
    public void onBeidouCardLocationReceived(CardLocationDto cardLocationDto) {
        ToastUtil.toastCenter(mContext, "onBeidouCardLocationReceived" + cardLocationDto.getFromCardNumber());
        LogUtils.e(TAG + "onBeidouCardLocationReceived", cardLocationDto.getFromCardNumber());
    }

    @Override
    public void onBeidouLocationReceived(String s, String s1) {
        ToastUtil.toastCenter(mContext, "onBeidouLocationReceived" + s + "---" + s1);
        LogUtils.e(TAG + "onBeidouLocationReceived", s);
    }

    @Override
    public void onReceiveBeidouSOSCS(String s, String s1) {
        ToastUtil.toastCenter(mContext, "onReceiveBeidouSOSCS" + s);
        LogUtils.e(TAG + "onReceiveBeidouSOSCS", s);
    }

    @Override
    public void onReceiveBeidouPassword(String s) {
        ToastUtil.toastCenter(mContext, "onReceiveBeidouPassword" + s);
        LogUtils.e(TAG + "onReceiveBeidouPassword", s);
    }

    @Override
    public void onBleDataReceive(String s, String s1) {
        ToastUtil.toastCenter(mContext, "onBleDataReceive" + s);
        LogUtils.e(TAG + "onBleDataReceive", s);
    }

    @Override
    public void onBleBDPWXReceived(String s, String[] strings) {
        ToastUtil.toastCenter(mContext, "onBleBDPWXReceived" + s);
        LogUtils.e(TAG + "onBleBDPWXReceived", s);
    }

    @Override
    public void onBleBDBSIReceived(String s, String[] strings) {
        ToastUtil.toastCenter(mContext, "onBleBDBSIReceived" + s);
        LogUtils.e(TAG + "onBleBDBSIReceived", s);
    }

    @Override
    public void onBleBDDWRReceived(String s, String[] strings) {
        ToastUtil.toastCenter(mContext, "onBleBDDWRReceived" + s);
        LogUtils.e(TAG + "onBleBDDWRReceived", s);
    }

    @Override
    public void onBleBDFKIReceived(String s, String[] strings) {
        ToastUtil.toastCenter(mContext, "onBleBDFKIReceived" + s);
        LogUtils.e(TAG + "onBleBDFKIReceived", s);
    }

    @Override
    public void onBleBDTXAReceived(final String s, final String[] strings) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.toastCenter(BlueToothActivity.this, s + (strings != null ? strings.length : "数组为空"));
            }
        });
        LogUtils.e("onBleBDTXAReceived", s + (strings != null ? strings.length : "数组为空"));
    }

    @Override
    public void onBleBDPOSReceived(String s, String[] strings) {
        ToastUtil.toastCenter(mContext, "onBleBDPOSReceived" + s);
        LogUtils.e(TAG + "onBleBDPOSReceived", s);
    }

    @Override
    public void onBleCCTXAReceived(String s, String[] strings) {
        ToastUtil.toastCenter(mContext, "onBleCCTXAReceived" + s);
        LogUtils.e(TAG + "onBleCCTXAReceived", s);
    }

    @Override
    public void onBleBDMDXReceived(String s, String[] strings) {
        ToastUtil.toastCenter(mContext, "onBleBDMDXReceived" + s);
        LogUtils.e(TAG + "onBleBDMDXReceived", s);
    }

    @Override
    public void onBleBDMSHReceived(String s, String[] strings) {
        ToastUtil.toastCenter(mContext, "onBleBDMSHReceived" + s);
        LogUtils.e(TAG + "onBleBDMSHReceived", s);
    }

    @Override
    public void onBleBDPRXReceived(String s, String[] strings) {
        ToastUtil.toastCenter(mContext, "onBleBDPRXReceived" + s);
        LogUtils.e(TAG + "onBleBDPRXReceived", s);
    }

    @Override
    public void onBleBDVRXReceived(String s, String[] strings) {
        ToastUtil.toastCenter(mContext, "onBleBDVRXReceived" + s);
        LogUtils.e(TAG + "onBleBDVRXReceived", s);
    }

    @Override
    public void onBleBDZTXReceived(String s, String[] strings) {
        ToastUtil.toastCenter(mContext, "onBleBDZTXReceived" + s);
        LogUtils.e(TAG + "onBleBDZTXReceived", s);
    }

    @Override
    public void onBleBDHMXReceived(String s, String[] strings) {
        ToastUtil.toastCenter(mContext, "onBleBDHMXReceived" + s);
        LogUtils.e(TAG + "onBleBDHMXReceived", s);
    }

    @Override
    public void onBleBDOKXReceived(String s, String[] strings) {
        ToastUtil.toastCenter(mContext, "onBleBDOKXReceived" + s);
        LogUtils.e(TAG + "onBleBDOKXReceived", s);
    }

    @Override
    public void onBleBDQZXReceived(String s, String[] strings) {
        ToastUtil.toastCenter(mContext, "onBleBDQZXReceived" + s);
        LogUtils.e(TAG + "onBleBDQZXReceived", s);
    }

    @Override
    public void onBleBDZZXReceived(String s, String[] strings) {
        ToastUtil.toastCenter(mContext, "onBleBDZZXReceived" + s);
        LogUtils.e(TAG + "onBleBDZZXReceived", s);
    }

    @Override
    public void onBleBDQDXReceived(String s, String[] strings) {
        ToastUtil.toastCenter(mContext, "onBleBDQDXReceived" + s);
        LogUtils.e(TAG + "onBleBDQDXReceived", s);
    }

    @Override
    public void onBleBDMSXReceived(String s, String[] strings) {
        ToastUtil.toastCenter(mContext, "onBleBDMSXReceived" + s);
        LogUtils.e(TAG + "onBleBDMSXReceived", s);
    }

    @Override
    public void onBleBDFRXReceived(String s, String[] strings) {
        ToastUtil.toastCenter(mContext, "onBleBDFRXReceived" + s);
        LogUtils.e(TAG + "onBleBDFRXReceived", s);
    }

    @Override
    public void onBleBDZDXReceived(String s, String[] strings) {
//        ToastUtil.toastCenter(mContext, "onBleBDZDXReceived" + s);
//        LogUtils.e(TAG + "onBleBDZDXReceived", s);
    }

    @Override
    public void onBleBDDLXReceived(String s, String[] strings) {
        ToastUtil.toastCenter(mContext, "onBleBDDLXReceived" + s);
        LogUtils.e(TAG + "onBleBDDLXReceived", s);
    }

    @Override
    public void onBleBDDLCReceived(String s, String[] strings) {
        ToastUtil.toastCenter(mContext, "onBleBDDLCReceived" + s);
        LogUtils.e(TAG + "onBleBDDLCReceived", s);
    }

    @Override
    public void onBleBDIDXReceived(String s, String[] strings) {
        ToastUtil.toastCenter(mContext, "onBleBDIDXReceived" + s);
        LogUtils.e(TAG + "onBleBDIDXReceived", s);
    }

    @Override
    public void onBleBDRSXReceived(String s) {
        ToastUtil.toastCenter(mContext, "onBleBDRSXReceived" + s);
        LogUtils.e(TAG + "onBleBDRSXReceived", s);
    }

    @Override
    public void onBleBDRNXReceived(String s, String[] strings) {
        ToastUtil.toastCenter(mContext, "onBleBDRNXReceived" + s);
        LogUtils.e(TAG + "onBleBDRNXReceived", s);
    }

    @Override
    public void onBleGGAReceived(String s, String[] strings) {
        ToastUtil.toastCenter(mContext, "onBleGGAReceived" + s);
        LogUtils.e(TAG + "onBleGGAReceived", s);
    }

    @Override
    public void onBleGLLReceived(String s, String[] strings) {
        ToastUtil.toastCenter(mContext, "onBleGLLReceived" + s);
        LogUtils.e(TAG + "onBleGLLReceived", s);
    }

    @Override
    public void onBleGSAReceived(String s, String[] strings) {
        ToastUtil.toastCenter(mContext, "onBleGSAReceived" + s);
        LogUtils.e(TAG + "onBleGSAReceived", s);
    }

    @Override
    public void onBleGSVReceived(String s, String[] strings) {
        ToastUtil.toastCenter(mContext, "onBleGSVReceived" + s);
        LogUtils.e(TAG + "onBleGSVReceived", s);
    }

    @Override
    public void onBleRMCReceived(String s, String[] strings) {
//        ToastUtil.toastCenter(mContext, "onBleRMCReceived" + s);
        LogUtils.e(TAG + "onBleRMCReceived", s);
    }

    @Override
    public void onBleZDAReceived(String s, String[] strings) {
        ToastUtil.toastCenter(mContext, "onBleZDAReceived" + s);
        LogUtils.e(TAG + "onBleZDAReceived", s);
    }

    @Override
    public void onBleBDICIReceived(String s, String[] strings) {
        ToastUtil.toastCenter(mContext, "onBleBDICIReceived" + s);
        LogUtils.e(TAG + "onBleBDICIReceived", s);
    }

    @Override
    public void onBleStatusLog(String s) {
        ToastUtil.toastCenter(mContext, "onBleStatusLog" + s);
        LogUtils.e(TAG + "onBleStatusLog", s);
    }

    @Override
    public void onDataReceived(String s) {
//        ToastUtil.toastCenter(mContext, "onDataReceived" + s);
//        LogUtils.e(TAG + "onDataReceived", s);
    }

    class BlueToothAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return bluetoothDevices.size();
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
        public View getView(int i, View view, ViewGroup viewGroup) {
            BluetoothDevice device = bluetoothDevices.get(i);
            TextView textView = new TextView(mContext);
            textView.setPadding(24, 24, 24, 24);
            textView.setTextSize(16f);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setText((device.getName() == null || device.getName().isEmpty()
                    ? "未知名称" : device.getName()) + "\n" + device.getAddress());
            return textView;
        }
    }

}
