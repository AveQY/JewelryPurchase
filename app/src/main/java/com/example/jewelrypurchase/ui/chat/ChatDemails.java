package com.example.jewelrypurchase.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jewelrypurchase.R;

public class ChatDemails extends AppCompatActivity {

    private String Url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_demails);

        // 隐藏标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Intent intent = getIntent();
        if(intent.getStringExtra("id").equals("0")) {
            Url = "http://aweqy.asia/html/cozi/jp/index.html";
        }else if(intent.getStringExtra("id").equals("1")) {
            Url = "http://aweqy.asia/html/cozi/deepseekR1/index.html";
        }

        // 初始化配置
        WebView webView = findViewById(R.id.webview_coze);
        // 启用 JavaScript（可选）
        webView.getSettings().setJavaScriptEnabled(true);

        // 设置 WebViewClient，确保链接在 WebView 内打开
        webView.setWebViewClient(new WebViewClient());

        // 加载指定网页
        webView.loadUrl(Url);

    }
}