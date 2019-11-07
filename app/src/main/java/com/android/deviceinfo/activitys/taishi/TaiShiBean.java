package com.android.deviceinfo.activitys.taishi;

import com.android.deviceinfo.bean.BaseResponseBean;
import com.google.gson.annotations.SerializedName;

/**

 */
public class TaiShiBean extends BaseResponseBean {

    /**
     * status : 1
     * data : {"totalAirplane":0,"enterAirplane":0,"totalUpDown":0,"doneUpdown":0,"enterPerson":0,"donePerson":0,"totalCar":0,"totalTask":0,"enterCar":0,"doneTask":0}
     */
    public DataBean data;

    public static class DataBean {
        /**
         * totalAirplane : 0
         * enterAirplane : 0
         * totalUpDown : 0
         * doneUpdown : 0
         * enterPerson : 0
         * donePerson : 0
         * totalCar : 0
         * totalTask : 0
         * enterCar : 0
         * doneTask : 0
         */

        public int totalAirplane;
        public int enterAirplane;
        public int totalUpDown;
        public int doneUpdown;
        public int enterPerson;
        public int donePerson;
        public int totalCar;
        public int totalTask;
        public int enterCar;
        public int doneTask;
        public int totalFlyHour;
        public int doneFlyHour;

    }
}
