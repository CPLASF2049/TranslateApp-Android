package com.example.translationapp;


import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.translationapp.R;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class VoiceTranslationActivity extends AppCompatActivity {

    private SpeechRecognizer speechRecognizer;
    private TextView txtTranslationResult;
    private ImageView voiceInputButton;
    private Spinner spinnerSourceLanguage, spinnerTargetLanguage;
    private String sourceLanguage = "auto"; // 默认为自动识别
    private String targetLanguage;
    String[] sourceLanguages = new String[]{"自动识别", "英语", "中文", "西班牙语", "德语", "法语"};
    String[] targetLanguages = new String[]{"英语", "中文", "西班牙语", "德语", "法语"};
    String appId = "20240531002066446";
    String salt = "123456"; // 随机数


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voice_translation);

        // 初始化组件
        txtTranslationResult = findViewById(R.id.txt_translation_result);
        voiceInputButton = findViewById(R.id.voice_input_button);
        spinnerSourceLanguage = findViewById(R.id.spinner_source_language);
        spinnerTargetLanguage = findViewById(R.id.spinner_target_language);

        // 初始化语音识别器
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                // 准备就绪，可以开始语音输入

            }

            @Override
            public void onBeginningOfSpeech() {
                // 检测到用户开始说话
            }

            @Override
            public void onRmsChanged(float rmsdB) {
                // 录音的RMS值改变
            }

            @Override
            public void onBufferReceived(byte[] buffer) {
                // 接收到音频数据缓冲区
            }

            @Override
            public void onEndOfSpeech() {
                // 用户停止说话
            }

            @Override
            public void onError(int error) {
                // 出现错误
                Toast.makeText(VoiceTranslationActivity.this, "语音识别错误: " + error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResults(Bundle results) {
                // 返回识别结果
                ArrayList<String> phrases = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (phrases != null && !phrases.isEmpty()) {
                    String recognizedText = phrases.get(0);
                    String sign = md5(appId, recognizedText, salt, "GoAyiiiaGuLFmrY4F54O");// 根据文档计算得出的签名
                    // 使用 TranslationManager 进行翻译
                    TranslationManager translationManager = new TranslationManager();
                    translationManager.translate(recognizedText, sourceLanguage, targetLanguage, appId, salt, sign, new TranslationManager.TranslationCallback() {
                        @Override
                        public void onSuccess(String translatedText) {
                            // 显示翻译结果
                            txtTranslationResult.setText(translatedText);

                            // 保存翻译历史
                            saveToHistory(recognizedText, translatedText);
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            System.out.println("翻译失败：" + errorMessage);
                            // 处理翻译失败的情况
                        }
                    });
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                // 返回部分识别结果
            }

            @Override
            public void onEvent(int eventType, Bundle params) {
                // 其他事件
            }
        });

        // 设置语音输入按钮的点击事件
        voiceInputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 开始语音识别
                speechRecognizer.startListening(RecognizerIntent.getVoiceDetailsIntent(VoiceTranslationActivity.this));
            }
        });

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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
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
