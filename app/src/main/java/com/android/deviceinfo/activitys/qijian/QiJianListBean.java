package com.android.deviceinfo.activitys.qijian;

import com.android.deviceinfo.bean.BaseResponseBean;

import java.io.Serializable;
import java.util.List;


public class QiJianListBean extends BaseResponseBean implements Serializable {

    public List<Databean> data;

    public static class Databean implements Serializable{
        /**
         * device_id : 11
         * filed1 : test1
         * filed2 : 001
         * filed3 : test1
         * filed4 : 11
         * filed5 : 5
         * filed6 : 4
         * filed7 : 20
         * filed8 : 飞机哈哈
         * filed9 : 2019-10-09
         * filed10 : 5
         * filed11 : 5
         * filed12 : test1
         * __v : 0
         */

        public int device_id;
        public String filed1;
        public String filed2;
        public String filed3;
        public String filed4;
        public String filed5;
        public String filed6;
        public String filed7;
        public String filed8;
        public String filed9;
        public String filed10;
        public String filed11;
        public String filed12;
        public String sy,smType;

    }

}
