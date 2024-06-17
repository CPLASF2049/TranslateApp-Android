package com.example.translationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText usernameEditText;
    EditText passwordEditText;
    EditText confirmPasswordEditText;
    Button mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        mRegisterButton = findViewById(R.id.registerButton);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser(usernameEditText, passwordEditText, confirmPasswordEditText);
            }
        });
    }

    private void registerUser(EditText usernameEditText, EditText passwordEditText, EditText confirmPasswordEditText) {
        // 获取用户输入的用户名和密码
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // 检查用户名和密码是否为空
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "请输入完整的用户名和密码", Toast.LENGTH_SHORT).show();
            return;
        }

        // 检查用户名长度（6-10个字符）
        if (username.length() < 6 || username.length() > 10) {
            Toast.makeText(this, "用户名不符合要求", Toast.LENGTH_SHORT).show();
            return;
        }

        // 检查密码长度（6-16个字符）和复杂性（至少包含1个数字和1个字母）
        if (password.length() < 6 || password.length() > 16 ||
                !password.matches(".*[0-9].*") || !password.matches(".*[a-zA-Z].*")) {
            Toast.makeText(this, "密码不符合要求", Toast.LENGTH_SHORT).show();
            return;
        }

        // 检查两次输入的密码是否一致
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        // 检查用户名是否已存在
        UserAccount userAccount = UserAccount.getInstance(this); // 直接在此处传递Context
        if (userAccount.containsUsername(username)) {
            Toast.makeText(this, "用户名已存在，请使用其他用户名", Toast.LENGTH_SHORT).show();
            return;
        }

        // 继续使用相同的userAccount实例来存储用户名和密码
        userAccount.setCredentials(username, password);

        // 保存凭证到SharedPreferences
        userAccount.saveCredentials();

        // 注册成功提示
        Toast.makeText(this, "用户注册成功，即将跳转到登录页面...", Toast.LENGTH_SHORT).show();

        // 注册成功后跳转到登录界面
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // 关闭当前注册界面
    }
}
