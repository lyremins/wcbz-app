package com.android.deviceinfo.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;


public class ToastUtil {

	/**之前现实的内容**/
	private static String oldMsg;
	/** 第一次时间 */
	private static long oneTime = 0 ;
	/** 第二次时间 */
	private static long twoTime = 0 ;

	private static Toast toast = null;

	@SuppressLint("ServiceCast")
	public static void toastCenter(final Context context, final String text) {
		if (AppUtils.isContextFinishing(context)){
			return;
		}
		if (context instanceof Activity){
			((Activity) context).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (text == null) {
						return;
					}
					Toast.makeText(context,text,Toast.LENGTH_LONG).show();
//					if (toast == null) {
//						toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
//						WindowManager manager = (WindowManager) context
//								.getSystemService(Context.WINDOW_SERVICE);
//						int height = manager.getDefaultDisplay().getHeight();
//						toast.setGravity(Gravity.TOP, 0, (int) (0.4 * height));
//						toast.show();
//						oneTime = System.currentTimeMillis();
//					} else {
//						twoTime = System.currentTimeMillis();
//						if (text.equals(oldMsg)) {
//							if (twoTime - oneTime > Toast.LENGTH_SHORT) {
//								WindowManager manager = (WindowManager) context
//										.getSystemService(Context.WINDOW_SERVICE);
//								int height = manager.getDefaultDisplay().getHeight();
//								toast.setGravity(Gravity.TOP, 0, (int) (0.4 * height));
//								toast.show();
//							}
//						}else{
//							oldMsg = text ;
//							toast.setText(text) ;
//							WindowManager manager = (WindowManager) context
//									.getSystemService(Context.WINDOW_SERVICE);
//							int height = manager.getDefaultDisplay().getHeight();
//							toast.setGravity(Gravity.TOP, 0, (int) (0.4 * height));
//							toast.show() ;
//						}
//					}
//					oneTime = twoTime;
				}
			});
		}else {
			if (text == null) {
				return;
			}
			if (AppUtils.isContextFinishing(context)){
				return;
			}
			Toast.makeText(context,text,Toast.LENGTH_LONG).show();
//			if (toast == null) {
//				toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
//				WindowManager manager = (WindowManager) context
//						.getSystemService(Context.WINDOW_SERVICE);
//				int height = manager.getDefaultDisplay().getHeight();
//				toast.setGravity(Gravity.TOP, 0, (int) (0.4 * height));
//				toast.show();
//				oneTime = System.currentTimeMillis();
//			} else {
//				twoTime = System.currentTimeMillis();
//				if (text.equals(oldMsg)) {
//					if (twoTime - oneTime > Toast.LENGTH_SHORT) {
//						WindowManager manager = (WindowManager) context
//								.getSystemService(Context.WINDOW_SERVICE);
//						int height = manager.getDefaultDisplay().getHeight();
//						toast.setGravity(Gravity.TOP, 0, (int) (0.4 * height));
//						toast.show();
//					}
//				}else{
//					oldMsg = text ;
//					toast.setText(text) ;
//					WindowManager manager = (WindowManager) context
//							.getSystemService(Context.WINDOW_SERVICE);
//					int height = manager.getDefaultDisplay().getHeight();
//					toast.setGravity(Gravity.TOP, 0, (int) (0.4 * height));
//					toast.show() ;
//				}
//			}
//			oneTime = twoTime;
		}

	}
}