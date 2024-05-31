package com.example.translationapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TranslationRetrofitClient {
    private static final String BASE_URL = "https://fanyi-api.baidu.com";

    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
