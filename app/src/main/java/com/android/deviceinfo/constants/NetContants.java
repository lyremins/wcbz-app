package com.android.deviceinfo.constants;

import com.android.deviceinfo.BuildConfig;

/**
 * 接口常量
 */
public interface NetContants {

//    String URL = "http://172.20.10.4:8000/";
//    String H5_URL = "http://172.20.10.4:8000/";

//    String URL = "http://192.168.43.35:8000/";
//    String H5_URL = "http://192.168.43.35:8000/";

    String URL = BuildConfig.URL;
    String H5_URL = BuildConfig.H5_URL;
    String BASE_URL = URL + "wcbz/";

    String IMAGE_URL = URL + "img/";

    String UPLOAD_IMG = URL + "v1/addimg/airplane";

}
