package com.android.deviceinfo.activitys.air_plan;

import com.android.deviceinfo.bean.BaseResponseBean;

import java.util.List;

/**
 */
public class AirPlanListBean extends BaseResponseBean {

    public List<DataBean> data;

    public static class DataBean{
        /**
         *  plan_id: Number, // ⻜机
         *  ID name: String,
         *   airName: String, 
         *  vehicleName: String, 
         *  subjectName: String, 
         *  dateTime: String, // 选择日期
         *  airSubject: String, // ⻜行科目
         *  sceneSubject: String, // 气象科目
         *  upDownNumber: String, // 起落次数
         *  flightTime: String, // ⻜行时间
         *  approachTime: String, // 进场时间
         *  totalNumber: String, // 总人数
         */
        public int plan_id;
        public String name;
        public String dateTime;
        public String airName;
        public String vehicleName;
        public String subjectName;
        public String upDownNumber;
        public String airSubject;
        public String sceneSubject;
        public String flightTime;
        public String approachTime;
        public String totalNumber;
    }

}
