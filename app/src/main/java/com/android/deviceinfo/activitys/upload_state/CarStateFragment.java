package com.android.deviceinfo.activitys.upload_state;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.android.deviceinfo.MyApp;
import com.android.deviceinfo.R;
import com.android.deviceinfo.activitys.blue_tooth.BlueToothActivity;
import com.android.deviceinfo.activitys.car.CarListBean;
import com.android.deviceinfo.base.ICallBack;
import com.android.deviceinfo.bean.BaseResponseBean;
import com.android.deviceinfo.utils.NetUtils;
import com.android.deviceinfo.utils.ThreadUtils;
import com.android.deviceinfo.utils.ToastUtil;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * 车辆状态上报
 */
public class CarStateFragment extends Fragment {

    private TextView tvPlane;
    private TextView tvPlaneState;
    private TextView tvPlaneTask;
    private Button btnUpload;

    private List<CarListBean.DataBean> planeList = new ArrayList<>();

    private int id;
    private TextView tvPlaneEnter;

    private KProgressHUD mHud;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_state, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        mHud = KProgressHUD.create(getActivity());
        tvPlane = view.findViewById(R.id.tv_plane);
        tvPlaneState = view.findViewById(R.id.tv_plane_state);
        tvPlaneTask = view.findViewById(R.id.tv_plane_task);
        tvPlaneEnter = view.findViewById(R.id.tv_plane_enter);
        btnUpload = view.findViewById(R.id.btn_upload);

    }

    private void initData() {
        NetUtils.executeGetRequest(getActivity(), "getCarToEnsure", null,
                new ICallBack<CarListBean>() {
                    @Override
                    public void onSucceed(CarListBean data) {
                        if (data == null) {
                            return;
                        }
                        if (data.isSucceed()) {
                            if (data.data == null || data.data.isEmpty()) {
                            } else {
                                planeList.clear();
                                planeList.addAll(data.data);
                            }
                        }
                    }

                    @Override
                    public void onFailed(final String msg) {
                        ToastUtil.toastCenter(getContext(), msg);

                    }
                });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id == 0) {
                    ToastUtil.toastCenter(getContext(), "还未选择车辆");
                    return;
                }
                save();
            }
        });

        tvPlane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlane();
            }
        });

        tvPlaneState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlaneState();
            }
        });

        tvPlaneTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTaskState();
            }
        });

        tvPlaneEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEnter();
            }
        });

        tvPlaneState.setText(MyApp.strToArray(MyApp.bean.carStateModel)[0]);
        tvPlaneTask.setText(MyApp.strToArray(MyApp.bean.carTaskModel)[0]);
    }

    private void save() {
        final String[] array = {"北斗上传", "网络上传"};
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setTitle("选择上传方式");
        alertBuilder.setItems(array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (i == 0) {
                    // 北斗
                    final Map<String, String> map = new HashMap<>();
                    map.put("vehicle_id", id + "");
                    map.put("state", tvPlaneState.getText().toString().trim());
                    map.put("taskState", tvPlaneTask.getText().toString().trim());
                    if (!TextUtils.isEmpty(tvPlaneEnter.getText().toString())) {
                        map.put("enter", tvPlaneEnter.getText().toString().trim());
                    }
                    Intent intent = new Intent(getActivity(), BlueToothActivity.class);
                    intent.putExtra("json", new Gson().toJson(map));
                    startActivity(intent);
                } else {
                    commitData();
                }
            }
        }).create().show();

    }

    private void commitData() {
        final Map<String, String> map = new HashMap<>();
        map.put("vehicle_id", id + "");
        map.put("state", tvPlaneState.getText().toString().trim());
        map.put("taskState", tvPlaneTask.getText().toString().trim());
        map.put("moudle", "upcar");

        if (!TextUtils.isEmpty(tvPlaneEnter.getText().toString())) {
            map.put("enter", tvPlaneEnter.getText().toString().trim());
        }
        mHud.show();
        NetUtils.executePostRequest(getActivity(), "updateVehicle", map,
                new ICallBack<BaseResponseBean>() {
                    @Override
                    public void onSucceed(BaseResponseBean data) {
                        mHud.dismiss();
                        ToastUtil.toastCenter(getContext(), "上报成功");
                    }

                    @Override
                    public void onFailed(final String msg) {
                        mHud.dismiss();
                        if (TextUtils.equals(msg, NetUtils.NET_ERROR)) {
                            // 离线存储
                            ThreadUtils.exec(new Runnable() {
                                @Override
                                public void run() {
                                    MyApp.editor.putString("updateVehicle", new Gson().toJson(map));
                                    MyApp.editor.commit();
                                }
                            });
                            ToastUtil.toastCenter(getContext(), "网络不可用,已离线存储");
                        } else {
                            ToastUtil.toastCenter(getContext(), msg);
                        }

                    }
                });
    }

    private void showPlane() {
        final String[] array = new String[planeList.size()];
        for (int i = 0; i < planeList.size(); i++) {
            array[i] = planeList.get(i).name;
        }
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setTitle("选择车辆");
        alertBuilder.setItems(array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                tvPlane.setText(array[i]);
                id = planeList.get(i).vehicle_id;
            }
        }).create().show();
    }

    private void showEnter() {
        final String[] array = {"进场", "未进场"};
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setTitle("选择车辆进场状态");
        alertBuilder.setItems(array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                tvPlaneEnter.setText(array[i]);
            }
        }).create().show();
    }

    /**
     * 车辆态势
     */
    private void showPlaneState() {
        final String[] array = MyApp.strToArray(MyApp.bean.carStateModel);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setTitle("选择车辆状态");
        alertBuilder.setItems(array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                tvPlaneState.setText(array[i]);
            }
        }).create().show();
    }

    /**
     * 任务态势
     */
    private void showTaskState() {
        final String[] array = MyApp.strToArray(MyApp.bean.carTaskModel);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setTitle("选择任务态势");
        alertBuilder.setItems(array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                tvPlaneTask.setText(array[i]);
            }
        }).create().show();
    }
}
