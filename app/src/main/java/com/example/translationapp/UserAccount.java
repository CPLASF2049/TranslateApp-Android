package com.example.translationapp;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class UserAccount {
    private static UserAccount instance;
    private Map<String, String> userCredentials;
    private static final String PREFS_NAME = "UserAccountPrefs";
    private static final String CREDENTIALS_MAP = "credentialsMap";

    private Context context; // 用于访问SharedPreferences

    private UserAccount(Context context) {
        this.context = context;
        userCredentials = new HashMap<>();
        loadCredentials(); // 加载已保存的凭证
    }

    public static UserAccount getInstance(Context context) {
        if (instance == null) {
            synchronized (UserAccount.class) {
                if (instance == null) {
                    instance = new UserAccount(context);
                }
            }
        }
        return instance;
    }

    private void loadCredentials() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        for (String key : sharedPreferences.getAll().keySet()) {
            userCredentials.put(key, sharedPreferences.getString(key, null));
        }
    }

    public void saveCredentials() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (Map.Entry<String, String> entry : userCredentials.entrySet()) {
            editor.putString(entry.getKey(), entry.getValue());
        }
        editor.apply();
    }

    public void setCredentials(String username, String password) {
        if (userCredentials.size() >= 100) {
            throw new IllegalStateException("已达到最大用户数限制");
        }
        userCredentials.put(username, password);
    }

    public String getPassword(String username) {
        return userCredentials.get(username);
    }

    public boolean containsUsername(String username) {
        return userCredentials.containsKey(username);
    }

    // 假设我们使用一个静态变量来存储当前登录的用户名
    private static String currentUsername;

    public void removeCredentials(String username) {
        // 从内存中的map删除旧的凭证
        userCredentials.remove(username);
        // 从SharedPreferences中删除旧的凭证
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(username);
        editor.apply();
    }

    public static void setCurrentUsername(String username) {
        currentUsername = username;
    }

    public static String getCurrentUsername() {
        return currentUsername;
    }

}