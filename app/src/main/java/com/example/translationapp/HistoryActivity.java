package com.example.translationapp;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.content.ClipboardManager;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private ListView listSearchHistory;
    private Button btnClearHistory;
    private ArrayAdapter<String> adapter;
    private List<String> historyData; // 用于存储历史记录数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_history);

        // 初始化底部导航栏的按钮
        LinearLayout bottomNavigation = findViewById(R.id.bottom_navigation);
        LinearLayout homeButton = findViewById(R.id.home_button);
        LinearLayout voiceButton = findViewById(R.id.voice_button);
        LinearLayout historyButton = findViewById(R.id.history_button);
        LinearLayout myButton = findViewById(R.id.my_button);
        LinearLayout cameraButton = findViewById(R.id.camera_button);

        // 为首页按钮设置点击事件
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到首页Activity
                Intent intent = new Intent(HistoryActivity.this, TextTranslationActivity.class);
                startActivity(intent);
            }
        });

        // 为语音按钮设置点击事件
        voiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到语音Activity
                Intent intent = new Intent(HistoryActivity.this, VoiceTranslationActivity.class);
                startActivity(intent);
            }
        });

        // 为拍照按钮设置点击事件
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到拍照Activity
                Intent intent = new Intent(HistoryActivity.this, CameraTranslationActivity.class);
                startActivity(intent);
            }
        });

        // 为“我的”按钮设置点击事件
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到我的Activity
                Intent intent = new Intent(HistoryActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        // 初始化组件
        listSearchHistory = findViewById(R.id.list_search_history);
        btnClearHistory = findViewById(R.id.btn_clear_history);

        // 获取翻译历史管理器实例，传递当前Activity的Context
        TranslationHistoryManager historyManager = TranslationHistoryManager.getInstance(this);

        // 初始化历史数据列表
        historyData = historyManager.getHistoryData();

        // 设置列表适配器
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, historyData);
        listSearchHistory.setAdapter(adapter);

        listSearchHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取点击的项
                String clickedItem = historyData.get(position);

                // 创建一个ClipData对象并添加点击的项
                ClipData clip = ClipData.newPlainText("simple text", clickedItem);

                // 获取系统剪贴板服务
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

                // 将ClipData设置到剪贴板
                clipboard.setPrimaryClip(clip);

                // 显示复制成功的提示
                Toast.makeText(HistoryActivity.this, "复制到剪贴板: " + clickedItem, Toast.LENGTH_SHORT).show();
            }
        });

        // 设置清除历史记录按钮的点击事件
        btnClearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 调用 TranslationHistoryManager 的 clearHistory 方法
                historyManager.clearHistory();
                adapter.notifyDataSetChanged(); // 通知适配器数据已更改
                Toast.makeText(HistoryActivity.this, "翻译历史已清空！", Toast.LENGTH_SHORT).show();
            }
        });
    }
}