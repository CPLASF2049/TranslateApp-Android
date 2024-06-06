package com.example.translationapp;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.translation.R;

public class LoginActivity extends AppCompatActivity {

    public void onLoginClick(View view) {
        // 从EditText获取用户名和密码
        EditText usernameEditText = findViewById(R.id.usernameEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // 验证用户名和密码（这里仅作示例，您需要根据实际情况进行验证）
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // 登录逻辑（例如，通过网络请求验证用户）
        performLogin(username, password);
    }

    private void performLogin(String username, String password) {
        // 模拟网络请求
        new Handler().postDelayed(() -> {
            // 假设登录成功
            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
            // 跳转到主界面或其他界面
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }, 2000);
    }

    public void onForgotPasswordClick(View view) {
        // 忘记密码的逻辑
        Toast.makeText(this, "点击了忘记密码", Toast.LENGTH_SHORT).show();
        // 可以在这里添加打开重置密码界面的逻辑
    }

    public void onRegisterClick(View view) {
        // 注册账号的逻辑
        Toast.makeText(this, "点击了注册账号", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, RegisterActivity.class);
        // 启动RegisterActivity
        startActivity(intent);
    }

    // 您的其他代码 ...
}
