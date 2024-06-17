package com.example.translationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PrivacyPolicy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_policy); // 确保这里的布局文件名是正确的

        // 初始化退出按钮并设置点击事件
        Button btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToSettings(view);
            }
        });
    }

    // 按钮点击事件处理
    public void backToSettings(View view) {
        Intent intent = new Intent(PrivacyPolicy.this, SettingActivity.class);
        startActivity(intent);
    }
}