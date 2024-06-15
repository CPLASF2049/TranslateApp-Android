package com.example.translationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.translationapp.R;

public class NoAccountManagementActivity extends AppCompatActivity {

    private CardView cardViewWithLogButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_management_noaccount);


        // 初始化组件
        CardView cardViewWithLogButton = findViewById(R.id.cardViewWithLogButton); // 假设CardView的ID

        // 为CardView设置点击事件
        cardViewWithLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到登录界面
                Intent intent = new Intent(NoAccountManagementActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
