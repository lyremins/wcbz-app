package com.android.deviceinfo.activitys.chat;

import com.android.deviceinfo.bean.BaseResponseBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 */
public class ChatBean extends BaseResponseBean {


    /**
     * status : 1
     * data : [{"chat_id":1,"content":"哈哈","createtime":"2019-10-31 00:27","contentType":"0","chatType":"0","fromuid":1,"touid":2,"image_path":"0.png","__v":0},{"chat_id":2,"content":"吃吧","createtime":"2019-10-31 00:27","contentType":"0","chatType":"0","fromuid":1,"touid":2,"image_path":"0.png","__v":0},{"chat_id":3,"content":"啊啊","createtime":"2019-10-31 00:27","contentType":"0","chatType":"0","fromuid":2,"touid":1,"image_path":"0.png","__v":0},{"chat_id":4,"content":"凤飞飞","createtime":"2019-10-31 00:28","contentType":"0","chatType":"0","fromuid":2,"touid":1,"image_path":"0.png","__v":0}]
     */
    public List<DataBean> data;

    public static class DataBean {
        /**
         * chat_id : 1
         * content : 哈哈
         * createtime : 2019-10-31 00:27
         * contentType : 0
         * chatType : 0
         * fromuid : 1
         * touid : 2
         * image_path : 0.png
         * __v : 0
         */

        public int chat_id;
        public String content;
        public String createtime;
        public String contentType;
        public String chatType;
        public int fromuid;
        public int touid;
        public String image_path;
    }
}
