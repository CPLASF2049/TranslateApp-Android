package com.example.translationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PrivacyPolicy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_policy); // 确保这里的布局文件名是正确的


        // 获取TextView并设置文本
        TextView textViewPrivacyPolicy = findViewById(R.id.textViewPrivacyPolicy);
        textViewPrivacyPolicy.setText("                                           翻译应用程序隐私政策\\n\n" +
                "\n" +
                "引言\\n\n" +
                "欢迎使用我们的翻译应用程序InnoTranslate（以下简称“应用”）。我们重视您的隐私，并致力于保护您的个人信息。本隐私政策解释了当您使用我们的应用时，我们如何收集、使用、存储和保护您的数据。\n" +
                "\\n\\n\n" +
                "1. 信息收集\\n\n" +
                "我们可能收集以下类型的信息：\\n\n" +
                "\n" +
                "个人身份信息：包括但不限于您的姓名、电子邮件地址、电话号码等。\\n\n" +
                "使用数据：包括您使用应用时的行为数据，例如查询的翻译内容、使用频率等。\\n\n" +
                "设备信息：可能包括您的设备型号、操作系统版本、IP地址等。\\n\\n\n" +
                "2. 信息使用\\n\n" +
                "我们使用收集的信息来：\\n\n" +
                "提供和改进我们的服务。\\n\n" +
                "定制和个性化您的用户体验。\\n\\n\n" +
                "3. 信息共享\\n\n" +
                "我们不会与任何第三方共享您的个人信息，除非：\\n\n" +
                "为了提供服务，例如使用第三方服务进行翻译处理。\\n\n" +
                "法律要求或保护我们的权益。\\n\\n\n" +
                "4. 信息安全\\n\n" +
                "我们采取适当的安全措施来保护您的个人信息免受未经授权的访问和泄露。然而，由于互联网的性质，我们无法保证信息的绝对安全。\\n\n" +
                "\\n\n" +
                "5. 用户权利\\n\n" +
                "您有权：访问、更正或删除您的个人信息。\n");

        // 初始化退出按钮并设置点击事件
        Button btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToSettings(view);
            }
        });
    }

    // 按钮点击事件处理
    public void backToSettings(View view) {
        Intent intent = new Intent(PrivacyPolicy.this, SettingActivity.class);
        startActivity(intent);
    }
}