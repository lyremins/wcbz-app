package com.android.deviceinfo.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.android.deviceinfo.MyApp;
import com.android.deviceinfo.activitys.login.UserBean;
import com.android.deviceinfo.base.ICallBack;
import com.android.deviceinfo.bean.BaseResponseBean;
import com.android.deviceinfo.bean.UploadImageBean;
import com.android.deviceinfo.constants.NetContants;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Dispatcher;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * 网络请求类
 */
public class NetUtils {

    private static final String TAG = "NetUtils";

    private static OkHttpClient sHttpClient;

    public static final String NET_ERROR = "当前网络不可用或连接不到服务器";

    private static Handler mainHandler = new Handler(MyApp.getContext().getMainLooper());

    static {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                int uid = MyApp.sharedPreferences.getInt("user_id",0);
                if (uid == 0){
                    return chain.proceed(chain.request());
                }else {
                    // 拦截旧的请求添加authId公共参数
                    Request oldRequest = chain.request();
                    HttpUrl httpUrl = oldRequest.url()
                            .newBuilder()
                            .addQueryParameter("uid",uid + "")
                            .build();
                    // 新的请求
                    Request newRequest = oldRequest.newBuilder()
                            .method(oldRequest.method(), oldRequest.body())
                            .url(httpUrl)
                            .build();
                    return chain.proceed(newRequest);
                }
            }
        };
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(100);
        dispatcher.setMaxRequestsPerHost(30);
        sHttpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(headerInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 执行get请求
     *
     * @param url      接口地址
     * @param callback 回调
     * @param <T>      返回结果类型
     */
    public static <T extends BaseResponseBean> void executeGetRequest(Context context, String url,
                                                                      @Nullable Map<String, String> params,
                                                                      @NonNull final ICallBack<T> callback) {
        enqueueCall(context, createGetCall(url, params), callback);
    }

    /**
     * 执行delete请求
     *
     * @param url      接口地址
     * @param callback 回调
     * @param <T>      返回结果类型
     */
    public static <T extends BaseResponseBean> void executeDeleteRequest(Context context, String url,
                                                                      @Nullable Map<String, String> params,
                                                                      @NonNull final ICallBack<T> callback) {
        enqueueCall(context, createDeleteCall(url, params), callback);
    }

    /**
     * 执行post请求
     *
     * @param url      接口地址
     * @param callback 回调
     * @param <T>      返回结果类型
     */
    public static <T extends BaseResponseBean> void executePostRequest(Context context, String url,
                                                                       @Nullable Map<String, String> params,
                                                                       @NonNull final ICallBack<T> callback) {
        enqueueCall(context, createPostCall(url, params), callback);
    }

    /**
     * 返回get请求call对象
     *
     * @param url
     * @param params
     * @return
     */
    public static Call createGetCall(String url,
                                     @Nullable Map<String, String> params) {
        String baseUrl = "";
        if(url.contains("v2") || url.contains("v1")){
            baseUrl = NetContants.URL;
        }else {
            baseUrl = NetContants.BASE_URL;
        }
        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl + url).newBuilder();
        if (params != null) {
            for (String key : params.keySet()) {
                if (params.get(key) != null) {
                    urlBuilder.setQueryParameter(key, params.get(key));
                }
            }
        }
//        urlBuilder.setQueryParameter("authId", UserManager.getInstance().getAuthId());
        Request request = new Request.Builder()
                .get()
                .url(urlBuilder.build())
                .build();
        return sHttpClient.newCall(request);
    }

    public static Call createDeleteCall(String url,
                                     @Nullable Map<String, String> params) {
        FormBody.Builder formBody = new FormBody.Builder();
        if (params != null) {
            for (String key : params.keySet()) {
                if (params.get(key) != null) {
                    formBody.add(key, params.get(key));
                }
            }
        }
        String baseUrl = "";
        if(url.contains("v2") || url.contains("v1")){
            baseUrl = NetContants.URL;
        }else {
            baseUrl = NetContants.BASE_URL;
        }
//        formBody.add("authId", UserManager.getInstance().getAuthId());
        Request request = new Request.Builder()
                .delete(formBody.build())
                .url(baseUrl + url)
                .build();
        return sHttpClient.newCall(request);
    }


    public static Call createPostCall(String url,
                                      @Nullable Map<String, String> params) {
        FormBody.Builder formBody = new FormBody.Builder();
        if (params != null) {
            for (String key : params.keySet()) {
                if (params.get(key) != null) {
                    formBody.add(key, params.get(key));
                }
            }
        }
        String baseUrl = "";
        if(url.contains("v2") || url.contains("v1")){
            baseUrl = NetContants.URL;
        }else {
            baseUrl = NetContants.BASE_URL;
        }
//        formBody.add("authId", UserManager.getInstance().getAuthId());
        Request request = new Request.Builder()
                .post(formBody.build())
                .url(baseUrl + url)
                .build();
        return sHttpClient.newCall(request);
    }

    /**
     * 异步执行一个请求
     *
     * @param call     请求
     * @param callback 回调
     * @param <T>      返回结果类型
     */
    private static <T extends BaseResponseBean> void enqueueCall(final Context context, @NonNull final Call call,
                                                                 @NonNull final ICallBack<T> callback) {
        LogUtils.i(TAG, "使用okhttp调度请求：" + call);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailed(NET_ERROR);

                    }
                });
                LogUtils.w(TAG, "使用okhttp调度请求失败：" + call);
                LogUtils.w(TAG, "错误信息：" + e.getMessage());
            }

            @RequiresApi(api = 28)
            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) throws IOException {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            if (AppUtils.isContextFinishing(context)) {
                                return;
                            }
                            //请求成功
                            ResponseBody responseBody = response.body();
                            if (responseBody == null) {
                                LogUtils.w(TAG, "okhttp 返回结果responseBody为空");
                                mainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onFailed(NET_ERROR);

                                    }
                                });
                                return;
                            }
                            String raw = null;
                            try {
                                raw = responseBody.string();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (raw != null) {
                                T result = null;
                                try {
                                    Type[] dataType = callback.getClass().getGenericInterfaces();
                                    for (Type t : dataType) {
                                        Type[] genericType2 = ((ParameterizedType) t).getActualTypeArguments();
                                        for (Type t2 : genericType2) {
                                            result = new Gson().fromJson(raw, t2);
                                        }
                                    }
                                    if (result == null) {
                                        callback.onFailed("请求数据异常" + raw);
                                        return;
                                    }
                                    if (result.isSucceed()) {
                                        callback.onSucceed(result);
                                    } else {
                                        callback.onFailed(result.message);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    callback.onFailed("请求数据异常" + e.getMessage());
                                }
                            } else {
                                LogUtils.w(TAG, "无法获取到请求结果");
                                callback.onFailed(NET_ERROR);
                            }
                        } else {
                            if (AppUtils.isContextFinishing(context)) {
                                return;
                            }
                            LogUtils.w(TAG, "无法获取到请求结果");
                            callback.onFailed(NET_ERROR);
                        }

                    }
                });

            }
        });

    }

    /**
     * 上传文件
     *
     * @param filesPath
     * @param callback
     */
    public static void uploadFile(List<String> filesPath,
                                  final ICallBack callback) {
        if (filesPath == null || filesPath.size() == 0) {
            callback.onFailed("文件不存在！");
            return;
        }

        File[] arrayFile = new File[filesPath.size()];
        for (int i = 0; i < filesPath.size(); i++) {
            try {
                if (new File(filesPath.get(i)).exists()) {
                    arrayFile[i] = new File(filesPath.get(i));
                }
            } catch (Exception e) {
                callback.onFailed("数据错误:" + e.getMessage());
                e.printStackTrace();
            }
        }
        // 创建RequestBody
        MultipartBody.Builder builder = new MultipartBody.Builder();
        // 传入文件集合
        for (File file : arrayFile) {
            if (file != null) {
                builder.addFormDataPart("files", file.getName(),
                        RequestBody.create(MediaType.parse("file"), file));

            }
        }
        // 创建Request
        Request request = new Request.Builder()
                .post(builder.build())
                .url(NetContants.UPLOAD_IMG)
                .build();

        Call call = sHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailed(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    //请求成功
                    ResponseBody responseBody = response.body();
                    if (responseBody == null) {
                        callback.onFailed(NET_ERROR);
                        return;
                    }
                    String raw = responseBody.string();
                    if (raw != null) {
//                        UploadImageBean result = null;
                        try {
                            Type[] dataType = callback.getClass().getGenericInterfaces();
                            for (Type t : dataType) {
                                Type[] genericType2 = ((ParameterizedType) t).getActualTypeArguments();
                                for (Type t2 : genericType2) {
//                                    result = new Gson().fromJson(raw, t2);
                                }
                            }
//                            if (result == null){
//                                callback.onFailed("请求数据异常" + raw);
//                                return;
//                            }
//                            if (TextUtils.equals(result.status,"0")){
//                                callback.onSucceed(result);
//                            }else {
//                                callback.onFailed(result.errmsg);
//                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.onFailed("请求数据异常" + e.getMessage());
                        }
                    } else {
                        callback.onFailed(NET_ERROR);
                    }
                } else {
                    callback.onFailed(NET_ERROR);
                }
            }
        });
    }

    public static void upload(File file, Activity activity, final ICallBack<UploadImageBean> callBack) {

        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpg");
        final OkHttpClient client = new OkHttpClient();

        RequestBody fileBody = RequestBody.create(MediaType.parse("file"), file);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "head_image.png", fileBody)
                .build();

        Request request = new Request.Builder()
                .url(NetContants.UPLOAD_IMG)
                .post(requestBody)
                .build();
        try {
            final Response response = client.newCall(request).execute();
            final String jsonString = response.body().string();
            if (activity == null || activity.isFinishing()){
                return;
            }
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, " upload jsonString =" + jsonString);
                    if (!response.isSuccessful()) {
                        callBack.onFailed(jsonString);
                    } else {
                        UploadImageBean bean = new Gson().fromJson(jsonString,UploadImageBean.class);
                        if (bean != null){
                            if (bean.isSucceed()){
                                callBack.onSucceed(bean);
                            }else {
                                callBack.onFailed(bean.message);
                            }
                        }
                    }
                }
            });

        } catch (IOException e) {
            Log.d(TAG, "upload IOException ", e);
        }
    }

}
