package com.afirez.app;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * A fragment that displays a WebView.
 * <p>
 * The WebView is automically paused or resumed when the Fragment is paused or resumed.
 */
public class WebFragment extends LazyFragment {
    private WebView mWebView;
    private boolean mIsWebViewAvailable;
    private String url = "";

    public WebFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            url = (String) arguments.getString("url", "");
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        if (mWebView != null) {
            mWebView.destroy();
        }
        mWebView = new WebView(getContext());
        mIsWebViewAvailable = true;
        return mWebView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WebView webView = getWebView();
        if (webView != null && !TextUtils.isEmpty(url)) {


            WebSettings settings = webView.getSettings();
            settings.setJavaScriptEnabled(true);

            settings.setUseWideViewPort(true);
            settings.setLoadWithOverviewMode(true);

            webView.loadUrl(url);
        }
    }

    @Override
    public void onPageResume() {
        mWebView.onResume();
        super.onResume();
    }

    @Override
    public void onPagePause() {
        super.onPause();
        mWebView.onPause();
    }


    @Override
    public void onDestroyView() {
        mIsWebViewAvailable = false;
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        if (mWebView != null) {
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    public WebView getWebView() {
        return mIsWebViewAvailable ? mWebView : null;
    }
}

