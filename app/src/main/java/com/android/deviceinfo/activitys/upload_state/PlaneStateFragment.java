package com.android.deviceinfo.activitys.upload_state;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;

import com.android.deviceinfo.MyApp;
import com.android.deviceinfo.R;
import com.android.deviceinfo.activitys.blue_tooth.BlueToothActivity;
import com.android.deviceinfo.activitys.plane.PlaneListBean;
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
 * 飞机状态上报
 */
public class PlaneStateFragment extends Fragment {
    private TextView tvPlane;
    private TextView tvPlaneState;
    private TextView tvPlaneTask;
    private EditText etQiluoNum;
    private TextView tvQiluoTime;
    private EditText etFlyHour;
    private Button btnUpload;

    private List<PlaneListBean.DataBean> planeList = new ArrayList<>();

    private int id;
    private TextView tvPlaneEnter;

    private KProgressHUD mHud;
    private EditText et1Num;
    private EditText et2Num;


    private CheckBox ckWuqi;
    private EditText etWuqiNum;
    private int maxUpdownCount;

    private String houre,minute;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plane_state, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        mHud = KProgressHUD.create(getActivity());
        tvPlane = view.findViewById(R.id.tv_plane);
        tvPlaneState = view.findViewById(R.id.tv_plane_state);
        tvPlaneTask = view.findViewById(R.id.tv_plane_task);
        etQiluoNum = view.findViewById(R.id.et_qiluo_num);
        tvQiluoTime = view.findViewById(R.id.tv_qiluo_time);
        etFlyHour = view.findViewById(R.id.et_fly_hour);
        btnUpload = view.findViewById(R.id.btn_upload);
        tvPlaneEnter = view.findViewById(R.id.tv_plane_enter);
        et1Num = view.findViewById(R.id.et_1_num);
        et2Num = view.findViewById(R.id.et_2_num);

        ckWuqi = view.findViewById(R.id.ck_wuqi);
        etWuqiNum = view.findViewById(R.id.et_wuqi_num);
        ckWuqi.setChecked(false);

        ckWuqi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    etWuqiNum.setVisibility(View.VISIBLE);
                }else {
                    etWuqiNum.setVisibility(View.GONE);
                }
            }
        });



        tvQiluoTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getContext(),new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int min) {
                        if (hourOfDay < 10){
                            houre = "0" + hourOfDay;
                        }else {
                            houre = hourOfDay + "";
                        }
                        if (min < 10){
                            tvQiluoTime.setText(houre+":"+"0"+min);
                        }else {
                            tvQiluoTime.setText(houre+":"+min);
                        }
                    }
                }, 0, 0, true).show();
            }
        });
    }

    private void initData() {
        NetUtils.executeGetRequest(getActivity(),"getAirplaneToPlan", null,
                new ICallBack<PlaneListBean>() {
                    @Override
                    public void onSucceed(final PlaneListBean data) {
                        if (data.data == null || data.data.isEmpty()) {
                        } else {
                            planeList.clear();
                            planeList.addAll(data.data);
                        }
                    }

                    @Override
                    public void onFailed(final String msg) {

                    }
                });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (id == 0) {
//                    ToastUtil.toastCenter(getContext(), "还未选择飞机");
//                    return;
//                }
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

//        tvPlaneState.setText(MyApp.strToArray(MyApp.bean.stateModel)[0]);
//        tvPlaneTask.setText(MyApp.strToArray(MyApp.bean.taskModel)[0]);
    }

    private void save() {
        if (!TextUtils.isEmpty(etQiluoNum.getText().toString().trim())) {
            int num = Integer.valueOf(etQiluoNum.getText().toString().trim());
            if (num > maxUpdownCount) {
                ToastUtil.toastCenter(getActivity(), "该飞机起落次数最多可填写" + maxUpdownCount + "次");
                return;
            }
        }
        if (ckWuqi.isChecked()){
            if (TextUtils.isEmpty(etWuqiNum.getText().toString())){
                ToastUtil.toastCenter(getContext(),"请填写发射武器数量");
                return;
            }else {
            }
        }
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
                    map.put("airplane_id", id + "");
                    map.put("state", tvPlaneState.getText().toString().trim());
                    map.put("task", tvPlaneTask.getText().toString().trim());
                    if (!etQiluoNum.getText().toString().trim().isEmpty()) {
                        map.put("airUpOrDown", etQiluoNum.getText().toString().trim());
                    }
                    if (!etFlyHour.getText().toString().trim().isEmpty()) {
                        map.put("airHour", etFlyHour.getText().toString().trim());
                    }
                    if (!tvQiluoTime.getText().toString().trim().isEmpty()) {
                        map.put("airTime", tvQiluoTime.getText().toString().trim());
                    }
                    if (!TextUtils.isEmpty(tvPlaneEnter.getText().toString())) {
                        map.put("enter", tvPlaneEnter.getText().toString().trim());
                    }
                    if (!TextUtils.isEmpty(et1Num.getText().toString())) {
                        map.put("engine_1", et1Num.getText().toString().trim());
                    }
                    if (!TextUtils.isEmpty(et2Num.getText().toString())) {
                        map.put("engine_2", et2Num.getText().toString().trim());
                    }
                    Intent intent = new Intent(getActivity(), BlueToothActivity.class);
                    intent.putExtra("json",new Gson().toJson(map));
                    startActivity(intent);
                } else {
                    commitData();
                }
            }
        }).create().show();

    }

    private void commitData() {
        final Map<String, String> map = new HashMap<>();
        map.put("airplane_id", id + "");
        map.put("state", tvPlaneState.getText().toString().trim());
        map.put("task", tvPlaneTask.getText().toString().trim());
        if (ckWuqi.isChecked()) {
            map.put("wqNumber", etWuqiNum.getText().toString().trim());
        }
        if (!etQiluoNum.getText().toString().trim().isEmpty()) {
            map.put("airUpOrDown", etQiluoNum.getText().toString().trim());
        }
        if (!etFlyHour.getText().toString().trim().isEmpty()) {
            map.put("airHour", etFlyHour.getText().toString().trim());
        }
        if (!tvQiluoTime.getText().toString().trim().isEmpty()) {
            map.put("airTime", tvQiluoTime.getText().toString().trim());
        }
        if (!TextUtils.isEmpty(tvPlaneEnter.getText().toString())) {
            map.put("enter", tvPlaneEnter.getText().toString().trim());
        }
        if (!TextUtils.isEmpty(et1Num.getText().toString())) {
            map.put("engine_1", et1Num.getText().toString().trim());
        }
        if (!TextUtils.isEmpty(et2Num.getText().toString())) {
            map.put("engine_2", et2Num.getText().toString().trim());
        }
        map.put("moudle", "upplane");
        mHud.show();
        NetUtils.executePostRequest(getActivity(), "updateAirplane", map,
                new ICallBack<BaseResponseBean>() {
                    @Override
                    public void onSucceed(BaseResponseBean data) {
                        mHud.dismiss();
                        ToastUtil.toastCenter(getContext(), "上报成功");
                        etFlyHour.setText("");
                        etQiluoNum.setText("");
                        tvQiluoTime.setText("");
                    }

                    @Override
                    public void onFailed(final String msg) {
                        mHud.dismiss();
                        if (TextUtils.equals(msg, NetUtils.NET_ERROR)) {
                            // 离线存储
                            ThreadUtils.exec(new Runnable() {
                                @Override
                                public void run() {
                                    MyApp.editor.putString("updateAirplane", new Gson().toJson(map));
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
            if (planeList.get(i).code != null) {
                array[i] = planeList.get(i).code;
            }else {
                array[i] = "";
            }
        }
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setTitle("选择飞机");
        alertBuilder.setItems(array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                tvPlane.setText(array[i]);
                maxUpdownCount = planeList.get(i).upDownNumber;
                id = planeList.get(i).airplane_id;
            }
        }).create().show();
    }

    private void showEnter() {
        final String[] array = {"进场", "未进场"};
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setTitle("选择飞机进场状态");
        alertBuilder.setItems(array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                tvPlaneEnter.setText(array[i]);
            }
        }).create().show();
    }

    /**
     * 飞机态势
     */
    private void showPlaneState() {
        final String[] array = MyApp.strToArray(MyApp.bean.stateModel);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setTitle("选择飞机状态");
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
        final String[] array = MyApp.strToArray(MyApp.bean.taskModel);
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
