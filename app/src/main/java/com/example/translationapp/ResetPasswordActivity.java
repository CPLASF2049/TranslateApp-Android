package com.example.translationapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.translationapp.R;

import java.util.regex.Pattern;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText etAccount; // 账号输入框
    private EditText etNewPassword; // 新密码输入框
    private EditText etConfirmPassword; // 确认新密码输入框
    private Button btnSubmit; // 提交按钮
    private LinearLayout navigationBar; // 导航栏

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_key);

        initializeViews();
        setListeners();
    }

    private void initializeViews() {
        // 初始化视图组件
        etAccount = findViewById(R.id.et_account);
        etNewPassword = findViewById(R.id.et_new_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnSubmit = findViewById(R.id.btn_submit);
        navigationBar = findViewById(R.id.navigation_bar);

        // 这里可以添加更多初始化代码，比如设置导航栏的返回按钮等
    }

    private void setListeners() {
        // 设置按钮的点击事件监听器
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

        // 如果导航栏中有其他交互元素，也可以在这里设置监听器
    }

    private void resetPassword() {
        String account = etAccount.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // 检查输入是否有效
        if (!isValidInput(account, newPassword, confirmPassword)) {
            Toast.makeText(this, "输入有误，请重新输入", Toast.LENGTH_SHORT).show();
            return;
        }

        // 调用重置密码的逻辑，例如调用后端API
        boolean isResetSuccess = resetPasswordFromServer(account, newPassword);

        if (isResetSuccess) {
            Toast.makeText(this, "密码重置成功", Toast.LENGTH_SHORT).show();
            // 可以在这里添加逻辑，比如跳转到登录界面等
        } else {
            Toast.makeText(this, "密码重置失败，请重试", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidInput(String account, String newPassword, String confirmPassword) {
        // 这里可以添加具体的验证逻辑，例如检查邮箱格式、密码强度等
        return !account.isEmpty() && newPassword.equals(confirmPassword) && newPassword.matches(".{6,}"); // 至少6位密码
    }

    private boolean resetPasswordFromServer(String account, String newPassword) {
        // 这里应该是调用后端API的逻辑，返回操作结果
        // 以下为示例代码，实际开发中需要替换为真实的网络请求
        return true; // 假设重置总是成功
    }
}
