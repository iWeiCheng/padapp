package com.xjysx.xiaochetong.moudle;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.xjysx.xiaochetong.R;
import com.xjysx.xiaochetong.base.BaseActivity;
import com.xjysx.xiaochetong.util.DeviceUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class SplashWebViewActivity extends BaseActivity {
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.tv_entry)
    TextView entry;

    private String url;

    @Override
    protected void loadLayout() {
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //去掉标题栏
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_webview_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    @Override
    public void initPresenter() {
        url = "file:///android_asset/1.html";
        loadLocalHtml(url);
        Logger.d(DeviceUtil.getScreenHeight(this)+"--width---"+DeviceUtil.getScreenWidth(this));
    }


    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    public void loadLocalHtml(String url) {
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);//开启JavaScript支持
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //重写此方法，用于捕捉页面上的跳转链接
                if ("http://start/".equals(url)) {
                    //在html代码中的按钮跳转地址需要同此地址一致
                    Toast.makeText(getApplicationContext(), "开始体验", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                webView.setVisibility(View.VISIBLE);
                entry.setVisibility(View.VISIBLE);
            }
        });
        webView.loadUrl(url);
    }




    @OnClick(R.id.tv_entry)
    public void onViewClicked() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
