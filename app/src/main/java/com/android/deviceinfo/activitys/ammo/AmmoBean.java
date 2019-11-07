package com.android.deviceinfo.activitys.ammo;

import com.android.deviceinfo.bean.BaseResponseBean;

import java.io.Serializable;
import java.util.List;

/**
 */
public class AmmoBean extends BaseResponseBean implements Serializable {

    public List<DataBean> data;

    public static class DataBean implements Serializable{
        /***
         * filed1: String, // 型号
         *  filed2: String, // 出厂号码
         * filed3: String, // 制造厂
         * filed4: String, // 日历寿命
         * filed5: String, // 出厂日期
         * filed6: String, // 总挂⻜小时
         * filed7: String // 已挂⻜小时
         */
        public String filed1;
        public String filed2;
        public String filed3;
        public String filed4;
        public String filed5;
        public String filed6;
        public String filed7;
        public int ammo_id;
    }

}
