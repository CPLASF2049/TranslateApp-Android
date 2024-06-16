package com.example.translationapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;

import androidx.appcompat.app.AppCompatActivity;

import com.example.translationapp.R;
import com.example.translationapp.TranslationManager;
import org.apache.commons.codec.digest.DigestUtils;

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
    String[] sourceLanguages = new String[]{"自动识别", "英语", "中文", "西班牙语", "德语", "法语"};
    String[] targetLanguages = new String[]{"英语", "中文", "西班牙语", "德语", "法语"};
    String sourceLanguage = "auto";
    String targetLanguage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_translation);

        // 初始化底部导航栏的按钮
        Button homeButton = findViewById(R.id.home_button);
        Button voiceButton = findViewById(R.id.voice_button);
        Button cameraButton = findViewById(R.id.camera_button);
        Button historyButton = findViewById(R.id.history_button);
        Button myButton = findViewById(R.id.my_button);

        // 为语音按钮设置点击事件
        voiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VoiceTranslationActivity.class);
                startActivity(intent);
            }
        });

        // 为拍照按钮设置点击事件
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CameraTranslationActivity.class);
                startActivity(intent);
            }
        });

        // 为历史按钮设置点击事件
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

        // 为“我的”按钮设置点击事件
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        originalTextEditText = findViewById(R.id.txt_original_text);
        translatedTextTextView = findViewById(R.id.txt_translated_text);
        translationResultScrollView = findViewById(R.id.scroll_translation_result);
        translateButton = findViewById(R.id.btn_translate);

        Spinner spinnerSourceLanguage = findViewById(R.id.spinner_source_language);
        Spinner spinnerTargetLanguage = findViewById(R.id.spinner_target_language);

        // 创建适配器
        ArrayAdapter<String> sourceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sourceLanguages);
        ArrayAdapter<String> targetAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, targetLanguages);
        sourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        targetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // 应用适配器
        spinnerSourceLanguage.setAdapter(sourceAdapter);
        spinnerTargetLanguage.setAdapter(targetAdapter);

        // 设置监听器
        spinnerSourceLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    sourceLanguage = "auto"; // 自动识别
                } else {
                    // 根据位置设置sourceLanguage的值
                    switch (position - 1) {
                        case 0:
                            sourceLanguage = "en";
                            break;
                        case 1:
                            sourceLanguage = "zh";
                            break;
                        case 2:
                            sourceLanguage = "spa";
                            break;
                        case 3:
                            sourceLanguage = "de";
                            break;
                        case 4:
                            sourceLanguage = "fra";
                            break;
                    }
                }
                // 这里可以添加其他逻辑，比如更新UI或开始翻译等
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 没有选择时的处理
                sourceLanguage = "auto";
            }
        });

        spinnerTargetLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 根据位置设置targetLanguage的值
                switch (position) {
                    case 0:
                        targetLanguage = "en";
                        break;
                    case 1:
                        targetLanguage = "zh";
                        break;
                    case 2:
                        targetLanguage = "spa";
                        break;
                    case 3:
                        targetLanguage = "de";
                        break;
                    case 4:
                        targetLanguage = "fra";
                        break;
                }
                // 这里可以添加其他逻辑，比如更新UI或开始翻译等
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 没有选择时的处理
                targetLanguage = "en";
            }
        });

        // 初始化翻译历史列表
        translationHistory = new ArrayList<>();

        // 设置翻译按钮的点击事件
        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performTranslation();
            }
        });

        LinearLayout voiceButton = findViewById(R.id.voice_button);
        voiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VoiceTranslationActivity.class);
                startActivity(intent);
            }
        });


        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CameraTranslationActivity.class);
                startActivity(intent);
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

        String appId = "20240531002066446";
        String salt = "123456"; // 随机数
        String sign = md5(appId, originalText, salt, "GoAyiiiaGuLFmrY4F54O");// 根据文档计算得出的签名

        // 使用 TranslationManager 进行翻译
        TranslationManager translationManager = new TranslationManager();
        translationManager.translate(originalText, sourceLanguage, targetLanguage, appId, salt, sign, new TranslationManager.TranslationCallback() {
            @Override
            public void onSuccess(String translatedText) {
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

            @Override
            public void onFailure(String errorMessage) {
                System.out.println("翻译失败：" + errorMessage);
                // 处理翻译失败的情况
            }
        });

    }

    public static String md5(String fixedString, String originalText, String salt, String password) {
        // 将三个字符串按顺序连接起来
        String combinedString = fixedString + originalText + salt + password;
        // 计算组合后的字符串的 MD5 值并返回
        return DigestUtils.md5Hex(combinedString);
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
}
