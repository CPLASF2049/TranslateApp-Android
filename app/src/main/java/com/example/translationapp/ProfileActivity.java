package com.example.translationapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import com.example.translationapp.R;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_management_account);

        // 初始化按钮
        ImageView profileImage = findViewById(R.id.profile_image);
        TextView userName = findViewById(R.id.user_name);
        TextView emailAddress = findViewById(R.id.email_address);
        Button editButton = findViewById(R.id.edit_button);
        Button offlineTranslationButton = findViewById(R.id.offline_translation_button);
        Button privacySettingsButton = findViewById(R.id.privacy_settings_button);
        Button selectButton = findViewById(R.id.select_button);
        Button feedbackButton = findViewById(R.id.feedback_button);
        Button logoutButton = findViewById(R.id.logout_button);

        // 为编辑资料按钮设置点击事件
        // 设置点击事件
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditInfoActivity.class);
                startActivity(intent);
            }
        });

        offlineTranslationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, OfflineTranslationActivity.class);
                startActivity(intent);
            }
        });

        // 设置隐私设置按钮的点击事件
        privacySettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        // 设置选择语言按钮的点击事件
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ChooseLanguageActivity.class);
                startActivity(intent);
            }
        });

        // 设置投诉建议按钮的点击事件
        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, SuggestionActivity.class);
                startActivity(intent);
            }
        });

        // 设置退出账号按钮的点击事件
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. 清除用户登录状态
                SharedPreferences sharedPreferences = getSharedPreferences("YourAppPreferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("isLoggedIn"); // 假设您的登录状态保存在"isLoggedIn"键中
                editor.remove("username"); // 清除用户名或其他相关信息
                editor.apply();

                // 2. 跳转到无账户界面，并关闭当前活动
                Intent intent = new Intent(ProfileActivity.this, NoAccountManagementActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish(); // 结束当前Activity
            }
        });
    }
}
