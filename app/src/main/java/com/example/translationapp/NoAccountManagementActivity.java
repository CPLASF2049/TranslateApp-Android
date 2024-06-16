package com.example.translationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.translationapp.R;

public class NoAccountManagementActivity extends AppCompatActivity {

    private CardView cardViewWithLogButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_management_noaccount);


        // 初始化组件
        //CardView cardViewWithLogButton = findViewById(R.id.cardViewWithLogButton); // 假设CardView的ID

        // 为CardView设置点击事件
        cardViewWithLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到登录界面
                Intent intent = new Intent(NoAccountManagementActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        // 初始化底部导航栏的按钮
        setBottomNavigationButtons();
    }
    // 设置底部导航栏按钮的点击事件
    private void setBottomNavigationButtons() {
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
                // 跳转到首页Activity的逻辑
                Intent intent = new Intent(NoAccountManagementActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // 设置语音按钮点击事件
        voiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 显示未登录提示
                Toast.makeText(NoAccountManagementActivity.this, "您尚未登录，请先登录!", Toast.LENGTH_SHORT).show();
            }
        });

// 设置拍照按钮点击事件
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 同上，显示未登录提示
                Toast.makeText(NoAccountManagementActivity.this, "您尚未登录，请先登录!", Toast.LENGTH_SHORT).show();
            }
        });

        // 设置历史按钮点击事件
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 同上，显示未登录提示
                Toast.makeText(NoAccountManagementActivity.this, "您尚未登录，请先登录!", Toast.LENGTH_SHORT).show();
            }
        });

        // 找到登录按钮
        Button logButton = findViewById(R.id.log_button);
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到LoginActivity
                redirectToLoginActivity();
            }
            // 跳转到LoginActivity的通用方法
            private void redirectToLoginActivity() {
                Intent intent = new Intent(NoAccountManagementActivity.this, LoginActivity.class);
                startActivity(intent);
                // 如果需要关闭当前Activity，可以添加以下代码：
                // finish();
            }
        });


    }
}
