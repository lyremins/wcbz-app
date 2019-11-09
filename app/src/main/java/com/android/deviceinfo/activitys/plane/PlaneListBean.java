package com.android.deviceinfo.activitys.plane;

import com.android.deviceinfo.bean.BaseResponseBean;

import java.io.Serializable;
import java.util.List;

/**
 */
public class PlaneListBean extends BaseResponseBean implements Serializable {


    /**
     * status : 1
     * data : [{"airplane_id":1,"model":"飞机型号","code":"出场号码","army_id":"部队编号","factory":"出场厂家","date":"","unit":"所属单位","airTime":"飞行时间","airUpOrDown":"哦哦","stageUpOrDown":"了解","engine_1":"2","engine_2":"5","create_time":"2019-10-23 22:55","type":"机型","stageUpOrDownTime":"机型","repairNumber":"机型","image_path":"","__v":0}]
     */
    public List<DataBean> data;

    public static class DataBean implements Serializable{
        /**
         * airplane_id : 1
         * model : 飞机型号
         * code : 出场号码
         * army_id : 部队编号
         * factory : 出场厂家
         * date :
         * unit : 所属单位
         * airTime : 飞行时间
         * airUpOrDown : 哦哦
         * stageUpOrDown : 了解
         * engine_1 : 2
         * engine_2 : 5
         * create_time : 2019-10-23 22:55
         * type : 机型
         * stageUpOrDownTime : 机型
         * repairNumber : 机型
         * image_path :
         * __v : 0
         */

        public int airplane_id;
        public String model;
        public String code;
        public String army_id;
        public String factory;
        public String date;
        public String unit;
        public String airTime;
        public String airUpOrDown;
        public String stageUpOrDown;
        public String engine_1;
        public String engine_2;
        public String create_time;
        public String type;
        public String stageUpOrDownTime;
        public String repairNumber;
        public String repairFactory;
        public String airHour;
        public String image_path;
        public String state;
        public String task;
        public boolean isSpead = false;
        public int upDownNumber;
        public int approachTime;
    }
}
