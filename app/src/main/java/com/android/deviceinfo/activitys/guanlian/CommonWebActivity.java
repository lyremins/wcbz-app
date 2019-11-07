package com.android.deviceinfo.activitys.guanlian;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.deviceinfo.MyApp;
import com.android.deviceinfo.R;
import com.android.deviceinfo.base.BaseActivity;
import com.android.deviceinfo.constants.NetContants;
import com.android.deviceinfo.weights.MyWebView;

public class CommonWebActivity extends BaseActivity {

    private ImageView imgBack;
    private MyWebView webview;

    private RelativeLayout loading;
    private RelativeLayout rlTitle;
    private TextView tvTitle;
    private ProgressBar pb;

    public static void start(Context context,String title, String url){
        Intent intent = new Intent(context,CommonWebActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("url",url);
        context.startActivity(intent);
    }

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
        String url = NetContants.H5_URL + "#/" + getIntent().getStringExtra("url") +"?device=h5&uid=" + MyApp.sharedPreferences.getInt("user_id",0);
        Log.e("url", url);
        webview.loadUrl(url);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (loading == null) {
                    return;
                }
                loading.setVisibility(View.GONE);
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                loading.setVisibility(View.VISIBLE);
            }
        });


        rlTitle = findViewById(R.id.rl_title);
        tvTitle = findViewById(R.id.tv_title);
        pb = findViewById(R.id.pb);
        tvTitle.setText(getIntent().getStringExtra("title"));
    }

    @Override
    public void onBackPressed() {
        if (webview.canGoBack()){
            webview.goBack();
        }else {
            finish();
        }
    }
}
