package com.example.translationapp; // 替换为您的应用程序的包名

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.translationapp.R;

public class SuggestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggestions);

        // 初始化控件
        final ConstraintLayout constraintLayout = findViewById(R.id.suggestions_root);
        final Button btnSubmit = findViewById(R.id.btn_submit);

        // 设置按钮的点击监听器
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击按钮时保存反馈
                saveFeedback();
            }
        });
    }

    private void saveFeedback() {
        // 获取用户输入的描述和联系方式
        EditText etDescription = findViewById(R.id.et_description);
        EditText etContact = findViewById(R.id.et_contact);

        String description = etDescription.getText().toString();
        String contact = etContact.getText().toString();

        // 验证输入是否有效
        if (description.isEmpty()) {
            Toast.makeText(this, "描述不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // 验证邮箱（简单验证，实际使用时需要更复杂的验证）
        if (!contact.isEmpty() && !contact.contains("@")) {
            Toast.makeText(this, "邮箱格式不正确", Toast.LENGTH_SHORT).show();
            return;
        }

        // 假设我们这里直接保存到数据库，实际使用时需要替换为数据库保存逻辑
        boolean isSaved = saveToDatabase(description, contact);

        if (isSaved) {
            // 显示成功消息并清空输入框
            Toast.makeText(this, "感谢您的反馈", Toast.LENGTH_SHORT).show();
            etDescription.setText("");
            etContact.setText("");
        } else {
            Toast.makeText(this, "保存失败，请重试", Toast.LENGTH_SHORT).show();
        }
    }
    // 模拟数据库保存逻辑
    private boolean saveToDatabase(String description, String contact) {
        // 这里应该是保存数据到数据库的逻辑
        // 目前我们只是返回true以表示“保存成功”
        // 实际使用时需要替换为真实的数据库操作
        return true;
    }
}
