package com.example.translationapp;

import java.util.ArrayList;
import java.util.List;

public class TranslationHistoryManager {
    private static TranslationHistoryManager instance;
    private List<String> historyData;

    private TranslationHistoryManager() {
        historyData = new ArrayList<>();
    }

    public static synchronized TranslationHistoryManager getInstance() {
        if (instance == null) {
            instance = new TranslationHistoryManager();
        }
        return instance;
    }

    public void addHistoryEntry(String entry) {
        historyData.add(entry);
    }

    public List<String> getHistoryData() {
        return historyData;
    }
}
