package com.android.deviceinfo.activitys.car;

import com.android.deviceinfo.bean.BaseResponseBean;

import java.io.Serializable;
import java.util.List;

/**
 */
public class CarListBean extends BaseResponseBean implements Serializable {

    public List<DataBean> data;

    public static class DataBean implements Serializable{

        /**
         * vehicle_id : 1
         * model : 氮气车
         * name : 哈哈
         * organiz : 航空兵xx旅
         * service : 8588
         * armyId : 编号
         * product : 生产厂
         * productTime : 2019-10-24
         * life : 总寿命
         * stageCourse : 阶段行驶里程
         * repairNumber : 大修次数
         * taskState : 作业中
         * state : 大修
         * mileage : 总里程
         * __v : 0
         */

        public int vehicle_id;
        public String model;
        public String name;
        public String code;
        public String organiz;
        public String service;
        public String armyId;
        public String product;
        public String productTime;
        public String life;
        public String stageCourse;
        public String repairNumber;
        public String taskState;
        public String state;
        public String mileage;
        public int __v;
    }

}
