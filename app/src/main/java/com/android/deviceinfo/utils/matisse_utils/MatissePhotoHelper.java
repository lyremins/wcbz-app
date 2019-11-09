package com.android.deviceinfo.utils.matisse_utils;

import android.Manifest;
import android.app.Activity;

import com.android.deviceinfo.utils.AppUtils;
import com.android.deviceinfo.utils.LogUtils;
import com.android.deviceinfo.utils.ToastUtil;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.util.List;

import androidx.fragment.app.Fragment;
import me.nereo.multi_image_selector.MultiImageSelector;

public class MatissePhotoHelper {

    public static final String NO_PERMISSION_HINT = "请先开启拍照及读写权限";

    public static final int REQUEST_CODE_CHOOSE = 23;
    public static String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};

    public static void selectPhoto(final Activity activity, final int maxsize) {
        AndPermission.with(activity).runtime().permission(PERMISSIONS)
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        LogUtils.i("权限判断","拒绝");
                        // 判断用户是不是不再显示权限弹窗了，若不再显示的话进入权限设置页
                        if (AndPermission.hasAlwaysDeniedPermission(
                                activity, PERMISSIONS)) {
                            // 打开权限设置页
                            ToastUtil.toastCenter(activity,NO_PERMISSION_HINT);
                            activity.startActivity(AppUtils.getAppDetailSettingIntent());
                        }
                    }
                }).onGranted(new Action<List<String>>() {
            @Override
            public void onAction(List<String> data) {
                LogUtils.i("权限判断","允许");
                MultiImageSelector.create()
                        .showCamera(true)
                        .count(maxsize)
                        .multi()
                        .origin(null)
                        .start(activity, REQUEST_CODE_CHOOSE);
            }
        }).start();
    }

    public static void selectPhoto(final Fragment activity, final int maxsize) {
        AndPermission.with(activity).runtime().permission(PERMISSIONS)
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        LogUtils.i("权限判断","拒绝");
                        // 判断用户是不是不再显示权限弹窗了，若不再显示的话进入权限设置页
                        if (AndPermission.hasAlwaysDeniedPermission(
                                activity, PERMISSIONS)) {
                            // 打开权限设置页
                            ToastUtil.toastCenter(activity.getContext(),NO_PERMISSION_HINT);
                            activity.startActivity(AppUtils.getAppDetailSettingIntent());
                        }
                    }
                }).onGranted(new Action<List<String>>() {
            @Override
            public void onAction(List<String> data) {
                LogUtils.i("权限判断","允许");
                MultiImageSelector.create()
                        .showCamera(true)
                        .count(maxsize)
                        .multi()
                        .origin(null)
                        .start(activity, REQUEST_CODE_CHOOSE);
            }
        }).start();
    }

}
