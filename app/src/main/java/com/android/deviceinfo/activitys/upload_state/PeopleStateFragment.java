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
import android.widget.TextView;

import com.android.deviceinfo.MyApp;
import com.android.deviceinfo.R;
import com.android.deviceinfo.activitys.air_plan.AirPlanListBean;
import com.android.deviceinfo.activitys.blue_tooth.BlueToothActivity;
import com.android.deviceinfo.activitys.people.AirPeopleListBean;
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
 * 机务人员状态上报
 */
public class PeopleStateFragment extends Fragment {

    private TextView tvPeople;
    private TextView tvPlaneState;
    private TextView tvTaskContent;
    private Button btnUpload;

    private List<AirPeopleListBean.DataBean> peopleList = new ArrayList<>();
    private List<PlaneListBean.DataBean> planeList = new ArrayList<>();
    private List<AirPlanListBean.DataBean> planList = new ArrayList<>();

    private int id,airplaneId,planId;
    private TextView tvObject;

    private KProgressHUD mHud;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_people_state, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        mHud = KProgressHUD.create(getActivity());
        tvPeople = view.findViewById(R.id.tv_plane);
        tvPlaneState = view.findViewById(R.id.tv_plane_state);
        tvTaskContent = view.findViewById(R.id.tv_plane_task);
        tvObject = view.findViewById(R.id.tv_object);
        btnUpload = view.findViewById(R.id.btn_upload);
    }

    private void initData() {

        NetUtils.executeGetRequest(getActivity(), "getAirplaneToPlan", null,
                new ICallBack<PlaneListBean>() {
                    @Override
                    public void onSucceed(PlaneListBean data) {
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


        NetUtils.executeGetRequest(getActivity(), "getPlanToToday", null,
                new ICallBack<AirPlanListBean>() {
                    @Override
                    public void onSucceed(AirPlanListBean data) {
                        if (data == null) {
                            return;
                        }
                        if (data.isSucceed()) {
                            if (data.data == null || data.data.isEmpty()) {
                            } else {
                                planList.addAll(data.data);
                            }
                        }
                    }

                    @Override
                    public void onFailed(final String msg) {
                        ToastUtil.toastCenter(getContext(), msg);

                    }
                });
        NetUtils.executeGetRequest(getActivity(), "getPersonnel", null,
                new ICallBack<AirPeopleListBean>() {
                    @Override
                    public void onSucceed(AirPeopleListBean data) {
                        if (data == null) {
                            return;
                        }
                        if (data.isSucceed()) {
                            if (data.data == null || data.data.isEmpty()) {
                            } else {
                                peopleList.addAll(data.data);
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
                    ToastUtil.toastCenter(getContext(), "还未选择人员");
                    return;
                }
                save();
            }
        });

        tvPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUser();
            }
        });

        tvPlaneState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlaneState();
            }
        });

        tvTaskContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTaskContent();
            }
        });

        tvObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlane();
            }
        });

        tvPlaneState.setText(MyApp.strToArray(MyApp.bean.pStatusModel)[0]);
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
                    map.put("person_id", id + "");
                    if (planId != 0) {
                        map.put("plan_id", planId + "");
                    }
                    map.put("state", tvPlaneState.getText().toString().trim());
                    if (airplaneId != 0) {
                        map.put("airplane_id", airplaneId + "");
                    }
                    mHud.show();
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
        map.put("person_id", id + "");
        if (planId != 0) {
            map.put("plan_id", planId + "");
        }
        map.put("state", tvPlaneState.getText().toString().trim());
        if (airplaneId != 0) {
            map.put("airplane_id", airplaneId + "");
        }
        map.put("moudle", "uppeople");
        mHud.show();
        NetUtils.executePostRequest(getActivity(), "updatePersonnel", map,
                new ICallBack<BaseResponseBean>() {
                    @Override
                    public void onSucceed(BaseResponseBean data) {
                        mHud.dismiss();
                        ToastUtil.toastCenter(getContext(), "上报成功");
                    }

                    @Override
                    public void onFailed(final String msg) {
                        mHud.dismiss();
                        if (TextUtils.equals(msg, NetUtils.NET_ERROR)){
                            // 离线存储
                            ThreadUtils.exec(new Runnable() {
                                @Override
                                public void run() {
                                    MyApp.editor.putString("updatePersonnel",new Gson().toJson(map));
                                    MyApp.editor.commit();
                                }
                            });
                            ToastUtil.toastCenter(getContext(),"网络不可用,已离线存储");
                        }else {
                            ToastUtil.toastCenter(getContext(), msg);
                        }

                    }
                });
    }

    private void showPlane() {
        final String[] array = new String[planeList.size()];
        for (int i = 0; i < planeList.size(); i++) {
            array[i] = planeList.get(i).code;
        }
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setTitle("选择对象");
        alertBuilder.setItems(array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                tvObject.setText(array[i]);
                airplaneId = planeList.get(i).airplane_id;
            }
        }).create().show();
    }

    private void showUser() {
        final String[] array = new String[peopleList.size()];
        for (int i = 0; i < peopleList.size(); i++) {
            array[i] = peopleList.get(i).user_name + "";
        }
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setTitle("选择人员");
        alertBuilder.setItems(array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                tvPeople.setText(array[i]);
                id = peopleList.get(i).person_id;
            }
        }).create().show();
    }

    /**
     * 工作状态
     */
    private void showPlaneState() {
        final String[] array = MyApp.strToArray(MyApp.bean.pStatusModel);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setTitle("选择人员工作状态");
        alertBuilder.setItems(array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                tvPlaneState.setText(array[i]);
            }
        }).create().show();
    }

    /**
     * 工作内容
     */
    private void showTaskContent() {
        final String[] array = new String[planList.size()];
        for (int i = 0; i < planList.size(); i++) {
            array[i] = planList.get(i).name;
        }
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setTitle("选择工作内容");
        alertBuilder.setItems(array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                planId = planList.get(i).plan_id;
                tvTaskContent.setText(array[i]);
            }
        }).create().show();
    }

}
