package com.cwuom.YJSLFull;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.PopTip;
import com.kongzue.dialogx.interfaces.DialogXStyle;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;

public class LoginActivity extends AppCompatActivity {

    private WebView mWvLogin;
    private boolean firstLogin = true;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DialogX.init(this);
        PopTip.show("请先登录！", "我已知晓").noAutoDismiss();

        mWvLogin = findViewById(R.id.wv_login);
        mWvLogin.getSettings().setJavaScriptEnabled(true);
        mWvLogin.setWebViewClient(new MyWebViewClient());
        mWvLogin.loadUrl("https://passport.bilibili.com/login");

    }

    public class MyWebViewClient extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView webview, String url) {
            webview.loadUrl(url);
//            Log.d("url", "LOAD URL:" + url);
            if (url.contains("m.bilibili.com")){
                CookieManager cookieManager = CookieManager.getInstance();
                String CookieStr = cookieManager.getCookie(url);
                SharedPreferences share = getSharedPreferences("temp",MODE_PRIVATE);
                SharedPreferences.Editor edt = share.edit();
                edt.putString("cookie", CookieStr);
                edt.putBoolean("iCookie", true);
                edt.commit();


                new XPopup.Builder(LoginActivity.this).asConfirm("登录成功？", "检测到您已经登录成功，您可以点击确定关闭此页面",
                                new OnConfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        finish();
                                    }
                                })
                        .show();
            }
            return true;
        }

        public void onPageFinished(WebView view, String url) {
            CookieManager cookieManager = CookieManager.getInstance();
            String CookieStr = cookieManager.getCookie(url);
            Log.e("Cookies", "Cookies = " + CookieStr);

//            new XPopup.Builder(LoginActivity.this).asConfirm("cookie", CookieStr,
//                            new OnConfirmListener() {
//                                @Override
//                                public void onConfirm() {
//
//                                }
//                            })
//                    .show();

            super.onPageFinished(view, url);
        }

    }
}