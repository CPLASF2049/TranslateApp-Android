package com.example.translationapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.translationapp.R;

public class LoginActivity extends AppCompatActivity {

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

        // 检索存储的密码
        UserAccount userAccount = UserAccount.getInstance();
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
        // 模拟网络请求成功后的操作
        new Handler().postDelayed(() -> {
            // 登录成功提示
            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
            // 跳转到主界面
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // 关闭当前登录界面
        }, 2000);
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
