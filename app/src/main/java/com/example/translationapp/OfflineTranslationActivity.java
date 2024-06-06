package com.example.translationapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.textfield.TextInputLayout;

public class OfflineTranslationActivity extends AppCompatActivity {

    private Spinner spinnerSourceLanguage;
    private Spinner spinnerTargetLanguage;
    private TextView txtTranslationResult;
    private Button btnTranslate;
    private TextInputLayout tilInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_translate);

        // 初始化组件
        spinnerSourceLanguage = findViewById(R.id.spinner_source_language);
        spinnerTargetLanguage = findViewById(R.id.spinner_target_language);
        tilInput = findViewById(R.id.til_input);
        txtTranslationResult = findViewById(R.id.txt_translation_result);
        btnTranslate = findViewById(R.id.btn_translate);

        // 设置源语言和目标语言的下拉列表数据
        setupLanguageSpinners();

        // 设置翻译按钮的点击事件
        btnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取用户选择的源语言和目标语言
                String sourceLanguage = spinnerSourceLanguage.getSelectedItem().toString();
                String targetLanguage = spinnerTargetLanguage.getSelectedItem().toString();
                // 获取用户输入的文本
                String inputText = tilInput.getEditText().getText().toString();

                // 执行翻译操作（这里仅作演示，实际需要调用翻译API或服务）
                String translatedText = performTranslation(sourceLanguage, targetLanguage, inputText);

                // 显示翻译结果
                txtTranslationResult.setText(translatedText);
            }
        });
    }

    private void setupLanguageSpinners() {
        // 示例：设置下拉列表数据
        String[] languages = {"English", "Spanish", "French", "German"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSourceLanguage.setAdapter(adapter);
        spinnerTargetLanguage.setAdapter(adapter);
    }

    private String performTranslation(String sourceLanguage, String targetLanguage, String inputText) {
        // 这里应该是调用翻译库或API的代码
        // 目前我们只是简单地返回输入文本作为演示
        return "Translated text from " + sourceLanguage + " to " + targetLanguage + ": " + inputText;
    }
}
