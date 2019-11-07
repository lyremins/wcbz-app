package com.android.deviceinfo.activitys.people;

import com.android.deviceinfo.bean.BaseResponseBean;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 */
public class AirPeopleListBean extends BaseResponseBean implements Serializable {
    public List<DataBean> data;

    public static class DataBean implements Serializable{

        /**
         * person_id : 1
         * user_name : 一下
         * sex : 男
         * phone : 5157545
         * type : 至于
         * detachment : 咯
         * remark :
         * create_time : 2019-10-28 15:16
         * organiz :
         * native : 54545
         * company :
         * row :
         * post :
         * major : 军械
         * grade : 裤
         * bindAir :
         * enlist :
         * school : 院校
         * greatTask : 重大任务
         * __v : 0
         */

        public int person_id;
        public String user_name;
        public String sex;
        public String phone;
        public String type;
        public String detachment;
        public String remark;
        public String create_time;
        public String organiz;
        @SerializedName("native")
        public String nativeX;
        public String company;
        public String row;
        public String post;
        public String major;
        public String grade;
        public String bindAir;
        public String enlist;
        public String school;
        public String greatTask;
        public String duty;
        public int __v;
    }
}
