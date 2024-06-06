package com.example.translationapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.translation.R;
import com.example.translationapp.TranslationManager;

public class TranslationActivity extends AppCompatActivity {
    private TextView translationResultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TranslationManager translationManager = new TranslationManager();
        translationManager.translate("Hello, how are you", "en", "zh", "20240531002066446", "123456", "25a51c8844e4bc77929a307511480008", new TranslationManager.TranslationCallback() {
            @Override
            public void onSuccess(String translatedText) {
                translationResultTextView.setText("Translated Text: " + translatedText);
            }

            @Override
            public void onFailure(String errorMessage) {
                System.out.println("翻译失败：" + errorMessage);
            }
        });
    }
}

