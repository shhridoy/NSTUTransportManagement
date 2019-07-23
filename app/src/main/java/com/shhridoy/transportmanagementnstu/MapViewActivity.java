package com.shhridoy.transportmanagementnstu;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MapViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        loadMap();

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void loadMap() {
        WebView webview = findViewById(R.id.webView);
        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        //webview.loadUrl("http://maps.google.com/maps?" + "saddr=43.0054446,-87.9678884" + "&daddr=42.9257104,-88.0508355");
        webview.loadUrl("https://www.google.com/maps/place/Noakhali+Science+and+Technology+University/@22.7939158,91.0978028,16.25z/data=!4m5!3m4!1s0x3754af712aaac0b7:0x4bab3d112f6b6f3f!8m2!3d22.792886!4d91.1003307");
    }

}
