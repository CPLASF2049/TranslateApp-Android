package com.example.translationapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.translation.R;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_management_account);

        // 初始化按钮
        Button editButton = findViewById(R.id.edit_button);
        Button offlineTranslationButton = findViewById(R.id.offline_translation_button);
        Button privacySettingsButton = findViewById(R.id.privacy_settings_button);
        Button selectButton = findViewById(R.id.select_button);
        Button feedbackButton = findViewById(R.id.feedback_button);
        Button logoutButton = findViewById(R.id.logout_button);

        // 为编辑资料按钮设置点击事件
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开编辑资料的Activity或Fragment
                Toast.makeText(ProfileActivity.this, "编辑资料按钮被点击", Toast.LENGTH_SHORT).show();
            }
        });

        // 为离线翻译按钮设置点击事件
        offlineTranslationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开离线翻译的Activity或Fragment
                Toast.makeText(ProfileActivity.this, "离线翻译按钮被点击", Toast.LENGTH_SHORT).show();
            }
        });

        // 为隐私设置按钮设置点击事件
        privacySettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开隐私设置的Activity或Fragment
                Toast.makeText(ProfileActivity.this, "账户设置按钮被点击", Toast.LENGTH_SHORT).show();
            }
        });

        // 为选择语言按钮设置点击事件
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开选择语言的Activity或Fragment
                Toast.makeText(ProfileActivity.this, "选择语言按钮被点击", Toast.LENGTH_SHORT).show();
            }
        });

        // 为投诉建议按钮设置点击事件
        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开投诉建议的Activity或Fragment
                Toast.makeText(ProfileActivity.this, "投诉建议按钮被点击", Toast.LENGTH_SHORT).show();
            }
        });

        // 为退出账号按钮设置点击事件
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理用户退出登录的逻辑
                Toast.makeText(ProfileActivity.this, "退出账号按钮被点击", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
