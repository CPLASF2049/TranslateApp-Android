package com.example.translationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.translationapp.R;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        // 初始化组件
        CardView cardAccount = findViewById(R.id.card_account);
        CardView cardNotifications = findViewById(R.id.card_notifications);
        CardView cardPrivacy = findViewById(R.id.card_privacy);
        Switch switchNotifications = findViewById(R.id.switch_notifications);
        Button btnSubmit = findViewById(R.id.btn_submit);

        // 账号信息点击查看事件
        cardAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开账号信息详情页面或执行其他操作
                Toast.makeText(SettingActivity.this, "查看账号信息", Toast.LENGTH_SHORT).show();
                // 例如：Intent intent = new Intent(this, AccountDetailsActivity.class);
                // startActivity(intent);
            }
        });

        // 通知设置切换事件
        switchNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 根据开关状态处理通知设置
                Toast.makeText(SettingActivity.this, "接收通知: " + (isChecked ? "开启" : "关闭"), Toast.LENGTH_SHORT).show();
                // 这里可以添加保存通知设置的代码
            }
        });

        // 隐私设置点击查看事件
        cardPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开隐私政策详情页面或执行其他操作
                Toast.makeText(SettingActivity.this, "查看隐私政策", Toast.LENGTH_SHORT).show();
                // 例如：Intent intent = new Intent(this, PrivacyPolicyActivity.class);
                // startActivity(intent);
            }
        });

        // 提交或保存按钮点击事件
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 执行保存设置的操作
                Toast.makeText(SettingActivity.this, "保存设置", Toast.LENGTH_SHORT).show();

                // 假设保存设置的逻辑已经完成，接下来跳转到另一个Activity
                Intent intent = new Intent(SettingActivity.this, ProfileActivity.class);
                startActivity(intent); // 启动Intent指向的Activity
                finish(); // 结束当前Activity
            }
        });
    }
}