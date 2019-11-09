package com.android.deviceinfo.activitys.people;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.deviceinfo.MyApp;
import com.android.deviceinfo.R;
import com.android.deviceinfo.activitys.org.OrgBean;
import com.android.deviceinfo.activitys.plane.PlaneListBean;
import com.android.deviceinfo.base.BaseActivity;
import com.android.deviceinfo.base.ICallBack;
import com.android.deviceinfo.bean.BaseResponseBean;
import com.android.deviceinfo.utils.NetUtils;
import com.android.deviceinfo.utils.ThreadUtils;
import com.android.deviceinfo.utils.ToastUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PeopleDetailActivity extends BaseActivity {

    private RelativeLayout rlTitle;
    private ImageView imgBack;
    private TextView btnAdd;
    private EditText etName;
    private TextView etSex;
    private EditText etPhone;
    private EditText etJiguan;
    private TextView etDanwei;
    private EditText etRow;
    private EditText etZhiwu;
    private TextView etMajor;
    private TextView tvPlane;
    private TextView tvDate;
    private EditText etGrade;
    private EditText etSchool;
    private EditText etBigTask;

    private List<PlaneListBean.DataBean> planeList = new ArrayList<>();

    private String airplaneId;

    private AirPeopleListBean.DataBean dataBean;
    private TextView tvZaigang;

    private List<OrgBean.DataBean.OrganizArrayBean> orgList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_detail);
        initView();
    }

    private void initView() {
        dataBean = (AirPeopleListBean.DataBean) getIntent().getSerializableExtra("data");
        if (dataBean == null) {
            return;
        }
        rlTitle = findViewById(R.id.rl_title);
        imgBack = findViewById(R.id.img_back);
        btnAdd = findViewById(R.id.btn_add);
        etName = findViewById(R.id.et_name);
        etSex = findViewById(R.id.et_sex);
        etPhone = findViewById(R.id.et_phone);
        etJiguan = findViewById(R.id.et_jiguan);
        etDanwei = findViewById(R.id.et_danwei);
        etRow = findViewById(R.id.et_row);
        etZhiwu = findViewById(R.id.et_zhiwu);
        etMajor = findViewById(R.id.et_major);
        tvPlane = findViewById(R.id.tv_plane);
        tvDate = findViewById(R.id.tv_date);
        etGrade = findViewById(R.id.et_grade);
        etSchool = findViewById(R.id.et_school);
        etBigTask = findViewById(R.id.et_big_task);
        tvZaigang = findViewById(R.id.tv_zaigang);

        etName.setText(dataBean.user_name);
        etSex.setText(dataBean.sex);
        etPhone.setText(dataBean.phone);
        etJiguan.setText(dataBean.nativeX);
        etDanwei.setText(dataBean.company);
        etRow.setText(dataBean.row);
        etZhiwu.setText(dataBean.post);
        etMajor.setText(dataBean.major);
        etGrade.setText(dataBean.grade);
        etSchool.setText(dataBean.school);
        etBigTask.setText(dataBean.greatTask);
        tvDate.setText(dataBean.enlist);
        tvZaigang.setText(dataBean.duty);
        tvPlane.setText(dataBean.bindAir);

        // 返回键
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 保存按钮
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        etJiguan = findViewById(R.id.et_jiguan);
        etDanwei = findViewById(R.id.et_danwei);
        tvPlane = findViewById(R.id.tv_plane);

        tvPlane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlane();
            }
        });

        etMajor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMajor();
            }
        });

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShengChanTimeDialog();
            }
        });

        etDanwei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOrg();
            }
        });


        MyApp.getPlane(this, new ICallBack<PlaneListBean>() {
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
                ToastUtil.toastCenter(mContext, msg);

            }
        });

        NetUtils.executeGetRequest(mContext, "getOrganiz", null,
                new ICallBack<OrgBean>() {
                    @Override
                    public void onSucceed(OrgBean data) {
                        if (data == null) {
                            return;
                        }
                        if (data.isSucceed()) {
                            if (data.data == null || data.data.isEmpty()) {
                            } else {
                                orgList.clear();
                                orgList.addAll(data.data.get(0).organizArray);
                            }
                        }
                    }

                    @Override
                    public void onFailed(final String msg) {
                        ToastUtil.toastCenter(mContext, msg);

                    }
                });


        tvZaigang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showZaiGang();
            }
        });
    }

    private void showMajor() {
        final String[] array = MyApp.strToArray(MyApp.bean.pMajorModel);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("选择专业");
        alertBuilder.setItems(array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                etMajor.setText(array[i]);
            }
        }).create().show();
    }

    private void showZaiGang() {
        final String[] array = {"是", "否"};
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("选择是否在岗");
        alertBuilder.setItems(array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                tvZaigang.setText(array[i]);
            }
        }).create().show();
    }

    private void save() {
        /**
         user_name: String, // 姓名
         sex: String, // 性别
         person_id: Number,
         create_time: String, 
         phone: String, 
         type: String,
          detachment: String,
          remark: String,
          organiz: String,
          native: String,
          company: String, 
         row: String, // 排
          post: String, // 职务
          major: String, // 专业
          grade: String, // 等级
          bindAir: String, // 绑定⻜机
         enlist: String, // 入伍时间
          school: String, // 毕业院校
         greatTask: String, //执行重大任务
         */
        final Map<String, String> map = new HashMap<>();
        map.put("phone", etPhone.getText().toString().trim());
        map.put("native", etJiguan.getText().toString().trim());
        map.put("company", etDanwei.getText().toString().trim());
        map.put("user_name", etName.getText().toString().trim());
        map.put("sex", etSex.getText().toString().trim());
        map.put("row", etRow.getText().toString().trim());
        map.put("post", etZhiwu.getText().toString().trim());
        map.put("grade", etGrade.getText().toString().trim());
        map.put("enlist", tvDate.getText().toString().trim());
        map.put("school", etSchool.getText().toString().trim());
        map.put("greatTask", etBigTask.getText().toString().trim());
        map.put("duty", tvZaigang.getText().toString().trim());
        map.put("major", etMajor.getText().toString().trim());
        map.put("bindAir", tvPlane.getText().toString());
        map.put("person_id", dataBean.person_id + "");
        showHud();
        NetUtils.executePostRequest(this, "updatePersonnel", map,
                new ICallBack<BaseResponseBean>() {
                    @Override
                    public void onSucceed(BaseResponseBean data) {
                        disMissHud();
                        ToastUtil.toastCenter(mContext, "保存成功");
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailed(final String msg) {
                        disMissHud();
                        if (TextUtils.equals(msg, NetUtils.NET_ERROR)) {
                            // 离线存储
                            ThreadUtils.exec(new Runnable() {
                                @Override
                                public void run() {
                                    MyApp.editor.putString("updatePersonnel", new Gson().toJson(map));
                                    MyApp.editor.commit();
                                }
                            });
                            ToastUtil.toastCenter(mContext, "网络不可用,已离线存储");
                        } else {
                            ToastUtil.toastCenter(mContext, msg);
                        }
                    }
                });
    }

    /**
     * 选择组织
     */
    private void showOrg() {
        final String[] array = new String[orgList.size()];
        for (int i = 0; i < orgList.size(); i++) {
            array[i] = orgList.get(i).label;
        }
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
        alertBuilder.setTitle("选择单位");
        alertBuilder.setItems(array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (orgList.get(i).children == null || orgList.get(i).children.isEmpty()) {
                    etDanwei.setText(array[i]);
                }else {
                    showTwoOrg(orgList.get(i).children,orgList.get(i).label);
                }
            }
        }).create().show();
    }

    private void showTwoOrg(final List<OrgBean.DataBean.OrganizArrayBean.ChildrenBeanX> list, String title){
        final String[] array = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i).label;
        }
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
        alertBuilder.setTitle(title);
        alertBuilder.setItems(array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (list.get(i).children == null || list.get(i).children.isEmpty()) {
                    etDanwei.setText(array[i]);
                }else {
                    showThreeOrg(list.get(i).children,list.get(i).label);
                }
            }
        }).create().show();

    }
    private void showThreeOrg(final List<OrgBean.DataBean.OrganizArrayBean.ChildrenBeanX.ChildrenBean> list,String title){
        final String[] array = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i).label;
        }
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
        alertBuilder.setTitle(title);
        alertBuilder.setItems(array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                etDanwei.setText(array[i]);
            }
        }).create().show();

    }

    private void showPlane() {
        final String[] array = new String[planeList.size()];
        for (int i = 0; i < planeList.size(); i++) {
            if (planeList.get(i).code != null) {
                array[i] = planeList.get(i).code;
            } else {
                array[i] = "";
            }
        }
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
        alertBuilder.setTitle("绑定飞机");
        alertBuilder.setItems(array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                tvPlane.setText(array[i]);
                airplaneId = planeList.get(i).code;
            }
        }).create().show();
    }

    /**
     * 入伍时间
     */
    private void showShengChanTimeDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mYear = year;
                        mMonth = month;
                        mDay = dayOfMonth;
                        String data = year + "-" + (month + 1) + "-" + dayOfMonth;
                        tvDate.setText(data);
                    }
                },
                mYear, mMonth, mDay);
        datePickerDialog.show();
    }

}
