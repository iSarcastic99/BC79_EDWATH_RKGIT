package com.example.sih.Jobs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import com.example.sih.R;

public class studyprogress extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studyprogress);
        WebView mywebview = findViewById(R.id.webview);
        mywebview.loadUrl("https://www.swayam.gov.in/");
    }
}