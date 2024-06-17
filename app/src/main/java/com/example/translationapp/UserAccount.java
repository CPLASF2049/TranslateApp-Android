package com.example.translationapp;

import java.util.HashMap;
import java.util.Map;

public class UserAccount {
    private static UserAccount instance;
    private Map<String, String> userCredentials;

    private UserAccount() {
        userCredentials = new HashMap<>();
    }

    public static UserAccount getInstance() {
        if (instance == null) {
            synchronized (UserAccount.class) {
                if (instance == null) {
                    instance = new UserAccount();
                }
            }
        }
        return instance;
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

    public static void setCurrentUsername(String username) {
        currentUsername = username;
    }

    public static String getCurrentUsername() {
        return currentUsername;
    }

    public void removeUser(String username) {
        userCredentials.remove(username);
    }

    public void clearAllUsers() {
        userCredentials.clear();
    }
}