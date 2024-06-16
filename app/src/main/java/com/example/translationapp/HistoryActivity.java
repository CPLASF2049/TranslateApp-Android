package com.example.translationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity {

    private ListView listSearchHistory;
    private Button btnClearHistory;
    private ArrayAdapter<String> adapter; // 适配器用于展示历史记录

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_history);

        // 初始化组件
        listSearchHistory = findViewById(R.id.list_search_history);
        btnClearHistory = findViewById(R.id.btn_clear_history);


        // 设置列表适配器（假设你已经有了一个适配器）
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, yourDataArray);
        listSearchHistory.setAdapter(adapter);

        // 设置列表项点击事件
        listSearchHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 处理列表项点击事件
                Toast.makeText(HistoryActivity.this, "Clicked on item: " + position, Toast.LENGTH_SHORT).show();
            }
        });

        // 设置清除历史记录按钮的点击事件
        // 设置清除历史记录按钮的点击事件
        btnClearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 清除历史记录的逻辑
                clearHistory();
            }
        });
    }


    private void clearHistory() {
        // 清空历史记录数据
        for (int i = 0; i < historyData.length; i++) {
            historyData[i] = ""; // 或者可以根据需要进行其他处理
        }

        // 清空适配器中的数据
        adapter.clear();

        // 通知适配器数据已更改
        adapter.notifyDataSetChanged();

        // 显示操作结果
        Toast.makeText(HistoryActivity.this, "History cleared", Toast.LENGTH_SHORT).show();
    }


    private void clearHistory() {
        // 假设有一个名为AppDatabase的数据库类，其中包含一个名为historyDao的接口
        AppDatabase database = AppDatabase.getInstance(this);
        historyDao = database.historyDao();

        // 异步执行数据库清除操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 在后台线程中执行数据库操作
                historyDao.deleteAllHistory();

                // 数据库操作完成后，回到主线程更新UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 清空适配器中的数据
                        adapter.clear();

                        // 通知适配器数据已更改
                        adapter.notifyDataSetChanged();

                        // 显示操作结果
                        Toast.makeText(HistoryActivity.this, "History cleared", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    // 假设这是底部导航栏的组件引用
    private Button homeButton, voiceButton, cameraButton, myButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_history); // 确保使用正确的布局文件名

        // 初始化底部导航栏的按钮
        homeButton = findViewById(R.id.home_button);
        voiceButton = findViewById(R.id.voice_button);
        cameraButton = findViewById(R.id.camera_button);
        // historyButton 已经在这个界面上，不需要设置跳转
        myButton = findViewById(R.id.my_button);

        // 为首页按钮设置点击事件
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到首页Activity
                Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
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
                Intent intent = new Intent(HistoryActivity.this, CameraActivity.class);
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


    }
}
