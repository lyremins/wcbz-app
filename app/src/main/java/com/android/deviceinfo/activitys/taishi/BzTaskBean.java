package com.android.deviceinfo.activitys.taishi;

import com.android.deviceinfo.bean.BaseResponseBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author
 */
public class BzTaskBean extends BaseResponseBean {


    /**
     * status : 1
     * data : [{"ensure_id":1,"filed1":"2019-10-19","filed2":"特定检查","filed6":"","filed7":"","filed8":"99","filed5":["出场号码"],"filed4":[],"filed3":["哈哈","阿斯打算"],"__v":0}]
     */

    public List<DataBean> data;

    public static class DataBean {
        /**
         * ensure_id : 1
         * filed1 : 2019-10-19
         * filed2 : 特定检查
         * filed6 :
         * filed7 :
         * filed8 : 99
         * filed5 : ["出场号码"]
         * filed4 : []
         * filed3 : ["哈哈","阿斯打算"]
         * __v : 0
         */

        public int ensure_id;
        public String filed1;
        public String filed2;
        public String filed6;
        public String filed7;
        public String filed8;
        public int __v;
        public List<String> filed5;
        public List<String> filed4;
        public List<String> filed3;
    }
}
