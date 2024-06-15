package com.example.translationapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.translationapp.R;
import com.example.translationapp.TranslationManager;

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
        setContentView(R.layout.home_translation);

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

        // 使用 TranslationManager 进行翻译
        TranslationManager translationManager = new TranslationManager();
        translationManager.translate(originalText, "en", "zh", "20240531002066446", "123456", "25a51c8844e4bc77929a307511480008", new TranslationManager.TranslationCallback() {
            @Override
            public void onSuccess(String translatedText) {
                // 显示翻译结果
                translatedTextTextView.setText("Translated Text: " + translatedText);

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

            @Override
            public void onFailure(String errorMessage) {
                System.out.println("翻译失败：" + errorMessage);
                // 处理翻译失败的情况
            }
        });
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
