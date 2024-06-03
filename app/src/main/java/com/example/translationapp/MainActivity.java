package com.example.translationapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private EditText originalTextEditText;
    private TextView translatedTextTextView;
    private ScrollView translationResultScrollView;
    private Button translateButton;
    private List<String> translationHistory; // 用于保存翻译历史
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.your_layout);

        originalTextEditText = findViewById(R.id.txt_original_text);
        translatedTextTextView = findViewById(R.id.txt_translated_text);
        translationResultScrollView = findViewById(R.id.scroll_translation_result);
        translateButton = findViewById(R.id.btn_translate);

        // 初始化翻译历史列表
        translationHistory = new ArrayList<>();

        // 设置翻译按钮的点击事件
        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performTranslation();
            }
        });
    }

    private void performTranslation() {
        // 获取原文
        String originalText = originalTextEditText.getText().toString();

        // 检查原文是否为空
        if (originalText.isEmpty()) {
            Toast.makeText(this, "请输入原文", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: 实现翻译逻辑，例如调用翻译API
        String translatedText = "翻译结果示例"; // 假设的翻译结果

        // 显示翻译结果
        translatedTextTextView.setText(translatedText);

        // 滚动到视图底部
        translationResultScrollView.post(new Runnable() {
            @Override
            public void run() {
                translationResultScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        // 保存翻译历史
        saveToHistory(originalText, translatedText);
    }

    private void saveToHistory(String originalText, String translatedText) {
        String historyRecord = "原文: " + originalText + "\n翻译结果: " + translatedText + "\n\n";

        // 将历史记录追加到文件
        try (FileOutputStream fos = openFileOutput("translation_history.txt", MODE_APPEND)) {
            fos.write(historyRecord.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "保存历史失败", Toast.LENGTH_SHORT).show();
        }
    }

    // 其他方法...
}
