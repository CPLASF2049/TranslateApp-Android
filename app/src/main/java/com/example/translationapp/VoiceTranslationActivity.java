package com.example.translationapp;

import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;

public class VoiceTranslationActivity extends AppCompatActivity {

    private SpeechRecognizer speechRecognizer;
    private TextView txtTranslationResult;
    private ImageView voiceInputButton;
    private Spinner spinnerSourceLanguage, spinnerTargetLanguage;

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
                    txtTranslationResult.setText("识别结果: " + recognizedText);
                    // 这里可以添加翻译逻辑
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

        // 初始化语言选择下拉框（示例数据）
        // 实际开发中应从资源文件或API获取语言列表
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.language_names, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSourceLanguage.setAdapter(adapter);
        spinnerTargetLanguage.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }
}
