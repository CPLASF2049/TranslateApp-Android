package com.example.translationapp;

public class Translation {
    com.example.translationapp.TranslationManager translationManager = new com.example.translationapp.TranslationManager();
    String textToTranslate = "Hello, how are you?";
    String sourceLanguage = "en";
    String targetLanguage = "zh";
    String appId = "20240531002066446";
    String salt = "123456"; // 随机数
    String sign = "25a51c8844e4bc77929a307511480008"; // 根据文档计算得出的签名

        translationManager.translate("Hello, how are you", "en", "zh", "20240531002066446", "123456", "25a51c8844e4bc77929a307511480008", new com.example.translationapp.TranslationManager.TranslationCallback() {
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
