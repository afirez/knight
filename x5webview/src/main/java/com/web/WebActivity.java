package com.web;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import com.example.test_webview_demo.R;
import com.web.widget.X5WebView;

public class WebActivity extends AppCompatActivity {

    private X5WebView webView;
    private Button btnBack;

    public static final String URL_EMPTY_0 = "https://robot-lib-achieve.zuoshouyisheng.com/?app_id=5cc197e8b60c48171066f0e7";
    public static final String URL_EMPTY_1 = "https://robot-lib-achieve.zuoshouyisheng.com/?app_id=5cd3d5cbb60c48343fafe493";

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableFullScreen(true);
        url = getIntent().getStringExtra("url");
        setContentView(R.layout.activity_web);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        webView = findViewById(R.id.x5WebView);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TbsInitHelper.setListener(new TbsInitHelper.Listener() {
            @Override
            public void onInitComplete() {
                if (url != null && !url.isEmpty()) {
                    webView.loadUrl(url);
                } else {
                    webView.loadUrl(URL_EMPTY_0);
                }
            }

            @Override
            public void onInitError() {
                Toast.makeText(WebActivity.this.getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enableFullScreen(boolean enable) {
        if (enable) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(lp);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            WindowManager.LayoutParams attr = getWindow().getAttributes();
            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attr);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    @Override
    protected void onDestroy() {
        TbsInitHelper.setListener(null);
        super.onDestroy();
    }
}
