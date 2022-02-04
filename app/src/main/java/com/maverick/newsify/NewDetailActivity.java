package com.maverick.newsify;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class NewDetailActivity extends AppCompatActivity {

    String title, desc, content, imageUrl,url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_detail);
        title = getIntent().getStringExtra("title");
        desc = getIntent().getStringExtra("desc");
        content = getIntent().getStringExtra("content");
        imageUrl = getIntent().getStringExtra("imageUrl");
        url = getIntent().getStringExtra("url");
    }
}