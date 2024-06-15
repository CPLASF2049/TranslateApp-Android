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


    // 这个方法将作为按钮点击事件的处理器
    public void onForgotPasswordClick(View view) {
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        // 启动ResetPasswordActivity
        startActivity(intent);
    }

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
    public void onRegisterClick(View view) {
        // 创建Intent以启动RegisterActivity
        Intent intent = new Intent(this, RegisterActivity.class);
        // 启动RegisterActivity
        startActivity(intent);
    }

}
