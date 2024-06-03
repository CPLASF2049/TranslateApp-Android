package com.example.translationapp;

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

        // 检查两次输入的密码是否一致
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        // 假设这里进行网络请求注册用户
        // 以下代码仅为示例，您需要根据实际API和服务器情况实现网络请求
        // new UserRegistrationTask().execute(username, password);

        // 注册成功提示
        Toast.makeText(this, "用户注册成功，即将跳转到登录页面...", Toast.LENGTH_SHORT).show();

        // 注册成功后的逻辑处理，比如跳转到登录界面等
        // 这里需要您根据实际情况编写代码
    }
}
