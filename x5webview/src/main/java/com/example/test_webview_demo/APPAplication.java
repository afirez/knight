package com.example.test_webview_demo;

import android.app.Application;
import com.web.TbsInitHelper;

public class APPAplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                TbsInitHelper.init(APPAplication.this);
            }
        }).start();
    }

}
