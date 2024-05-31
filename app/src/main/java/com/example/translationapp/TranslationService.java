package com.example.translationapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TranslationService {
    @GET("api/trans/vip/translate")
    Call<com.example.translationapp.TranslationResponse> translate(
            @Query("q") String query,
            @Query("from") String sourceLang,
            @Query("to") String targetLang,
            @Query("appid") String appId,
            @Query("salt") String salt,
            @Query("sign") String sign
    );
}
