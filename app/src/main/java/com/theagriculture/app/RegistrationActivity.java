package com.theagriculture.app;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        webView = findViewById(R.id.reg_webview);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        boolean isAdo = intent.getBooleanExtra("isAdo", false);
        if (isAdo)
            getSupportActionBar().setTitle("Sign Up as ADO");
        else
            getSupportActionBar().setTitle("Sign Up as DDA");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack())
            webView.goBack();
        else
            super.onBackPressed();
    }
}
