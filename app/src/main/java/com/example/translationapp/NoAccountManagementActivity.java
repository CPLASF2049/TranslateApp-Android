package com.example.translationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.translationapp.R;

public class NoAccountManagementActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_management_noaccount);


        // 初始化登录按钮并设置点击事件
        Button logButton = findViewById(R.id.log_button);
        setupLoginButton(logButton); // 确保这个方法被调用
        // 初始化底部导航栏的LinearLayout
        LinearLayout bottomNavigation = findViewById(R.id.bottom_navigation);
        LinearLayout homeButton = findViewById(R.id.home_button);
        LinearLayout voiceButton = findViewById(R.id.voice_button);
        LinearLayout cameraButton = findViewById(R.id.camera_button);
        LinearLayout historyButton = findViewById(R.id.history_button);
        LinearLayout myButton = findViewById(R.id.my_button);

        // 为首页按钮设置点击事件，可以跳转到首页
        homeButton.setOnClickListener(v -> redirectToHomeActivity());

        // 为其他按钮设置点击事件，未登录时不能跳转
        setUnauthenticatedButtonClickListener(voiceButton, cameraButton, historyButton, myButton);
    }

    private void setUnauthenticatedButtonClickListener(LinearLayout... buttons) {
        for (LinearLayout button : buttons) {
            button.setOnClickListener(v -> {
                // 显示未登录提示
                Toast.makeText(NoAccountManagementActivity.this, "您尚未登录，请先登录!", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void redirectToHomeActivity() {
        Intent intent = new Intent(NoAccountManagementActivity.this, MainActivity.class);
        startActivity(intent);
        // 如果需要关闭当前Activity，可以添加以下代码：
        finish();
    }


    // 设置登录按钮的点击事件
    private void setupLoginButton(Button loginButton) {
        loginButton.setOnClickListener(v -> {
            // 调用跳转到登录界面的函数
            redirectToLoginActivity();
        });
    }

    // 跳转到登录界面的函数
    private void redirectToLoginActivity() {
        Intent intent = new Intent(NoAccountManagementActivity.this, LoginActivity.class);
        startActivity(intent);
        // 如果需要关闭当前Activity，取消注释下面的代码
         finish();
    }
}




