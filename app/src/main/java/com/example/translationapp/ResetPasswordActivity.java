package com.example.translationapp;

import android.content.Intent;
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

        // 提交按钮的点击事件
        Button btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取输入的文本
                EditText etAccount = findViewById(R.id.et_account);
                EditText etNewPassword = findViewById(R.id.et_new_password);
                EditText etConfirmPassword = findViewById(R.id.et_confirm_password);

                // 执行重置密码的逻辑
                resetPassword(etAccount, etNewPassword, etConfirmPassword);
            }
        });

        // 放弃按钮的点击事件
        Button btnForget = findViewById(R.id.btn_forget);
        btnForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到LoginActivity
                redirectToLoginActivity();
            }
        });
    }

    // 跳转到LoginActivity的通用方法
    private void redirectToLoginActivity() {
        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
        // 如果需要关闭当前Activity，可以添加以下代码：
         finish();
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

    // 重置密码方法，接收EditText参数
    private void resetPassword(EditText etAccount, EditText etNewPassword, EditText etConfirmPassword) {
        String account = etAccount.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // 检查输入是否有效
        if (isValidInput(account, newPassword, confirmPassword)) {
            // 检查账户是否存在
            UserAccount userAccount = UserAccount.getInstance();
            if (userAccount.containsUsername(account)) {
                // 调用重置密码的逻辑，这里我们先模拟这个过程
                boolean isResetSuccess = resetPasswordFromServer(account, newPassword);

                if (isResetSuccess) {
                    // 如果重置成功，更新本地存储的密码
                    userAccount.setCredentials(account, newPassword);
                    Toast.makeText(this, "密码重置成功", Toast.LENGTH_SHORT).show();
                    // 跳转到登录界面
                    redirectToLoginActivity();
                } else {
                    Toast.makeText(this, "密码重置失败，请重试", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "账户不存在", Toast.LENGTH_SHORT).show();
            }
        } else {
            // 输入无效，isValidInput已经显示了Toast提示
            // 无需额外操作
        }
    }

    private boolean isValidInput(String account, String newPassword, String confirmPassword) {
        // 检查账户是否为空
        if (account.isEmpty()) {
            Toast.makeText(this, "账户名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        // 检查密码长度（6-16个字符）和复杂性（至少包含1个数字和1个字母）
        if (newPassword.length() < 6 || newPassword.length() > 16 ||
                !newPassword.matches(".*[0-9].*") || !newPassword.matches(".*[a-zA-Z].*")) {
            Toast.makeText(this, "密码不符合要求", Toast.LENGTH_SHORT).show();
            return false;
        }
        // 检查两次输入的密码是否一致
        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // 模拟调用后端API进行密码重置的过程
    private boolean resetPasswordFromServer(String account, String newPassword) {
        // 这里应该是网络请求的代码，现在我们直接返回true模拟重置成功
        // 实际开发中，你需要替换这里的代码，执行实际的API调用
        return true;
    }
}
