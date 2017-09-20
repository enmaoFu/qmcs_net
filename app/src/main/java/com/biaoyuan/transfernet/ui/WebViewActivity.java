package com.biaoyuan.transfernet.ui;

import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.base.BaseAty;

import butterknife.Bind;

/**
 * Title  :
 * Create : 2016/12/19
 * Author ：chen
 */

/**
 * 加载网页的
 */
public class WebViewActivity extends BaseAty {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.webView)
    WebView mWebView;
    private boolean isOnPause = false;


    public static final int TYPE_OTHER = 3;
    private int type = 1;

    private String url;


    @Override
    public int getLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    public void initData() {

        type=getIntent().getIntExtra("type",1);

        switch (type){

            case TYPE_OTHER:
                initToolbar(mToolbar, getIntent().getStringExtra("title"));
                url =getIntent().getStringExtra("url");
                break;
        }





      hardwareAccelerate();
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setBuiltInZoomControls(false);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        //以下两句和硬件加速有关
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebView.getSettings().setAllowFileAccess(true);

   //     mWebView.getSettings().setUseWideViewPort(true);

        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });
        mWebView.setWebViewClient(
                new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                      view.loadUrl(url);
                        return true;
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        dismissLoadingContentDialog();
                        super.onPageFinished(view, url);
                    }

                    @Override
                    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                        super.onReceivedHttpError(view, request, errorResponse);
                    }

                    @Override
                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                        super.onReceivedError(view, errorCode, description, failingUrl);
                        showErrorToast("errorCode=="+errorCode);
                    }
                });

    }

    @Override
    public void requestData() {
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                showLoadingContentDialog();
                mWebView.loadUrl(url);
            }
        });
    }

    @Override
    public boolean setIsInitRequestData() {
        return true;
    }

    //硬件加速
    private void hardwareAccelerate() {
        if (this.getPhoneSDKInt() >= 14) {
            getWindow().setFlags(0x1000000, 0x1000000);
        }
    }

    public int getPhoneSDKInt() {
        int version = 0;
        try {
            version = Integer.valueOf(Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return version;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 当Activity执行onPause()时让WebView执行pause
     */
    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (mWebView != null) {
                mWebView.onPause();
                isOnPause = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 当Activity执行onResume()时让WebView执行resume
     */
    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (isOnPause) {
                if (mWebView != null) {
                    mWebView.onResume();
                }
                isOnPause = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            ViewGroup view = (ViewGroup) getWindow().getDecorView();
            view.removeAllViews();
            mWebView.destroy();
        }
    }
}
