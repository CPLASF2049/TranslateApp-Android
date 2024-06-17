package com.example.translationapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "InnoTranslate";
    private static final String IS_LOGGED_IN = "isLoggedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);

        // 找到按钮并设置点击事件
        Button btnRegister = findViewById(R.id.registerButton);
        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onRegisterClick(view);
            }
        });
    }

    public void onLoginClick(View view) {
        // 从EditText获取用户名和密码
        EditText usernameEditText = findViewById(R.id.usernameEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        String username = usernameEditText.getText().toString().trim();
        String inputPassword = passwordEditText.getText().toString().trim();

        // 验证用户名和密码是否为空
        if (username.isEmpty() || inputPassword.isEmpty()) {
            Toast.makeText(this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // 获取UserAccount实例，传递当前Activity的Context
        UserAccount userAccount = UserAccount.getInstance(this);
        String storedPassword = userAccount.getPassword(username);

        // 验证用户名和密码
        if (storedPassword != null && storedPassword.equals(inputPassword)) {
            // 登录成功，设置当前用户名
            UserAccount.setCurrentUsername(username);
            performLoginSuccess();
        } else {
            // 登录失败，显示错误提示
            Toast.makeText(this, "用户名或密码错误，请重试", Toast.LENGTH_SHORT).show();
        }
    }

    private void performLoginSuccess() {
        // 更新SharedPreferences以标记用户已登录
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_LOGGED_IN, true);
        // 确保提交更改
        editor.apply(); // 使用apply()代替commit()，因为它是异步的
        Log.d("LoginActivity", "SharedPreferences updated: isLoggedIn set to true");

        // 跳转到MainActivity
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // 结束当前活动
    }

    // 这个方法将作为按钮点击事件的处理器
    public void onForgotPasswordClick(View view) {
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        // 启动ResetPasswordActivity
        startActivity(intent);
    }

    public void onRegisterClick(View view) {
        // 创建Intent以启动RegisterActivity
        Intent intent = new Intent(this, RegisterActivity.class);
        // 启动RegisterActivity
        startActivity(intent);
    }

}
