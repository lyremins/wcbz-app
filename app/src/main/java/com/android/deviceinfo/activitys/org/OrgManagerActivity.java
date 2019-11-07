package com.android.deviceinfo.activitys.org;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.deviceinfo.MyApp;
import com.android.deviceinfo.R;
import com.android.deviceinfo.base.BaseActivity;
import com.android.deviceinfo.constants.NetContants;
import com.android.deviceinfo.weights.MyWebView;

/**
 * 组织架构
 */
public class OrgManagerActivity extends BaseActivity {

    private ImageView imgBack;
    private MyWebView webview;

    private RelativeLayout loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_manager);
        initView();
    }

    private void initView() {
        imgBack = findViewById(R.id.img_back);
        webview = findViewById(R.id.webview);
        loading = findViewById(R.id.loading);
        String url = NetContants.H5_URL + "#/organiz?device=h5&uid=" + MyApp.sharedPreferences.getInt("user_id",0);
        Log.e("url",url);
        webview.loadUrl(url);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        webview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (loading == null){
                    return;
                }
                loading.setVisibility(View.GONE);
            }
        });

        webview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
