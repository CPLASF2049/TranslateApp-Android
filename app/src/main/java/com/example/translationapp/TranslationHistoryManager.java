package com.example.translationapp;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TranslationHistoryManager {
    private static TranslationHistoryManager instance;
    private Context context; // 用于初始化SharedPreferences
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "TranslationHistoryPrefs"; // SharedPreferences的文件名
    private static final String HISTORY_KEY = "history_data"; // 历史记录的数据键

    private List<String> historyData; // 用于存储历史记录数据

    // 私有构造函数，防止外部直接实例化
    private TranslationHistoryManager(Context context) {
        this.context = context.getApplicationContext(); // 使用应用的上下文
        sharedPreferences = this.context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        loadHistoryData(); // 加载历史数据
    }

    // 单例获取实例的方法，传入Context
    public static TranslationHistoryManager getInstance(Context context) {
        if (instance == null) {
            synchronized (TranslationHistoryManager.class) {
                if (instance == null) {
                    instance = new TranslationHistoryManager(context);
                }
            }
        }
        return instance;
    }

    // 加载历史数据的方法
    private void loadHistoryData() {
        String savedHistory = sharedPreferences.getString(HISTORY_KEY, null);
        if (savedHistory != null) {
            String[] entries = savedHistory.split(",");
            historyData = new ArrayList<>(Arrays.asList(entries));
        } else {
            historyData = new ArrayList<>();
        }
    }

    // 保存历史数据的方法
    private void saveHistoryData() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < historyData.size(); i++) {
            builder.append(historyData.get(i));
            if (i < historyData.size() - 1) {
                builder.append(",");
            }
        }
        sharedPreferences.edit().putString(HISTORY_KEY, builder.toString()).apply();
    }

    // 添加历史记录项的方法
    public void addHistoryEntry(String entry) {
        if (!historyData.contains(entry)) { // 避免添加重复的记录
            historyData.add(0, entry); // 将新记录插入到列表的开头
            saveHistoryData(); // 保存更新后的历史记录
        }
    }

    // 获取历史数据的方法
    public List<String> getHistoryData() {
        return historyData;
    }

    // 清除历史记录的方法
    public void clearHistory() {
        historyData.clear();
        saveHistoryData(); // 更新SharedPreferences
    }
}
