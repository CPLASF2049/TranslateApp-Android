package com.example.translationapp;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TranslationManager {

    private TranslationService translationService;

    public TranslationManager() {
        translationService = TranslationRetrofitClient.getClient().create(TranslationService.class);
    }

    public void translate(String textToTranslate, String sourceLanguage, String targetLanguage, String appId, String salt, String sign, final TranslationCallback callback) {
        Call<TranslationResponse> call = translationService.translate(textToTranslate, sourceLanguage, targetLanguage, appId, salt, sign);
        call.enqueue(new Callback<TranslationResponse>() {
            @Override
            public void onResponse(Call<TranslationResponse> call, Response<TranslationResponse> response) {
                if (response.isSuccessful()) {
                    TranslationResponse translationResponse = response.body();
                    if (translationResponse != null && translationResponse.trans_result != null && !translationResponse.trans_result.isEmpty()) {
                        // 获取第一个翻译结果
                        TranslationResult result = translationResponse.trans_result.get(0);
                        // 将翻译结果传递给回调函数
                        callback.onSuccess(result.dst);
                    } else {
                        // 翻译结果为空，通知回调函数
                        callback.onFailure("翻译结果为空");
                    }
                } else {
                    // 翻译请求失败，通知回调函数
                    callback.onFailure("翻译请求失败");
                }
            }

            @Override
            public void onFailure(Call<TranslationResponse> call, Throwable t) {
                // 请求失败，通知回调函数
                callback.onFailure("翻译请求失败：" + t.getMessage());
            }
        });
    }

    public static interface TranslationCallback {
        void onSuccess(String translatedText);

        void onFailure(String errorMessage);
    }
}
