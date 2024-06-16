package com.example.translationapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_history);

        // 获取底部导航栏的按钮
        Button homeButton = findViewById(R.id.home_button);
        Button voiceButton = findViewById(R.id.voice_button);
        Button cameraButton = findViewById(R.id.camera_button);
        Button historyButton = findViewById(R.id.history_button);
        Button myButton = findViewById(R.id.my_button);

        // 为首页按钮设置点击事件

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击首页按钮后的操作，这里只是一个示例，弹出Toast消息
                Toast.makeText(HistoryActivity.this, "首页按钮被点击", Toast.LENGTH_SHORT).show();
                // 这里可以添加跳转到首页的代码
            }
        });

        // 为语音按钮设置点击事件
        voiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击语音按钮后的操作
                Toast.makeText(HistoryActivity.this, "语音按钮被点击", Toast.LENGTH_SHORT).show();
                // 这里可以添加语音识别功能的代码
            }
        });

        // 为拍照按钮设置点击事件
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击拍照按钮后的操作
                Toast.makeText(HistoryActivity.this, "拍照按钮被点击", Toast.LENGTH_SHORT).show();
                // 这里可以添加打开相机拍照的代码
            }
        });

        // 为历史按钮设置点击事件（当前页面，无需操作）
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击历史按钮后的操作
                Toast.makeText(HistoryActivity.this, "历史按钮被点击", Toast.LENGTH_SHORT).show();
                // 这里可以添加查看历史记录的代码
            }
        });

        // 为我的按钮设置点击事件
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击我的按钮后的操作
                Toast.makeText(HistoryActivity.this, "我的按钮被点击", Toast.LENGTH_SHORT).show();
                // 这里可以添加跳转到用户个人页面的代码
            }
        });

        // 为清除历史按钮设置点击事件
        Button clearHistoryButton = findViewById(R.id.btn_clear_history);
        clearHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击清除历史按钮后的操作
                Toast.makeText(HistoryActivity.this, "清除历史记录", Toast.LENGTH_SHORT).show();
                // 这里可以添加清除历史记录的代码
            }
        });
    }
}