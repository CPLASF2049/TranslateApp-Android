package com.example.translationapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "InnoTranslate";
    private static final String IS_LOGGED_IN = "isLoggedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 检查用户是否登录
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean(IS_LOGGED_IN, false);
        Log.d("MainActivity", "isLoggedIn: " + isLoggedIn);

        Intent intent;
        if (isLoggedIn) {
            // 用户已登录，跳转到已登录用户的主界面
            intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        } else {
            // 用户未登录，跳转到访客模式界面
            intent = new Intent(this, NoAccountManagementActivity.class);
            startActivity(intent);
            finish(); // 仅在用户未登录时完成MainActivity
        }
    }
}
