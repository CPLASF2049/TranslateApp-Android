package com.example.translationapp;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.RecognitionListener;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
public class VoiceRecognition extends AppCompatActivity implements RecognitionListener{
    private static final int SPEECH_REQUEST_CODE = 123;
    private Activity mActivity;
    private SpeechRecognizer mSpeechRecognizer;

    public VoiceRecognition(Activity activity) {
        mActivity = activity;
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(activity);
        mSpeechRecognizer.setRecognitionListener(this);
    }

    public void startSpeechRecognition() {
        if (mActivity.checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // 如果未授予录音权限，则请求权限
            mActivity.requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, SPEECH_REQUEST_CODE);
        } else {
            // 启动语音识别
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US"); // 设置语言为英文，可以根据需求更改
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak something...");
            try {
                mActivity.startActivityForResult(intent, SPEECH_REQUEST_CODE);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(mActivity, "Speech recognition not supported", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        // 识别器准备就绪，可以开始说话了
        // 这里可以执行一些准备工作，比如显示一个提示或者改变UI状态
        Toast.makeText(mActivity, "Speak now...", Toast.LENGTH_SHORT).show();
        // 你还可以在这里改变UI状态，比如启用或禁用其他按钮
    }

    @Override
    public void onBeginningOfSpeech() {
        // 开始说话，可以执行一些与语音输入相关的操作
        // 这里可以显示一个正在录音的指示符号，告诉用户正在录音
        // 你还可以在这里改变UI状态，比如禁用其他按钮以防止中断语音输入
    }

    @Override
    public void onEndOfSpeech() {
        // 语音输入结束，可以执行一些与语音输入结束相关的操作
        // 这里可以停止显示录音指示符号，告诉用户录音已结束
        // 你还可以在这里改变UI状态，比如重新启用其他按钮
    }

    @Override
    public void onError(int error) {
        // 识别过程中发生错误，可以在这里处理错误并向用户提供适当的反馈
        String errorMessage;
        switch (error) {
            case SpeechRecognizer.ERROR_AUDIO:
                errorMessage = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                errorMessage = "Client side error";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                errorMessage = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                errorMessage = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                errorMessage = "No match found";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                errorMessage = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                errorMessage = "Server error";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                errorMessage = "No speech input";
                break;
            default:
                errorMessage = "Unknown error";
                break;
        }
        // 在这里向用户显示错误消息，可以使用Toast或者其他方式
        Toast.makeText(mActivity, errorMessage, Toast.LENGTH_SHORT).show();
        // 你还可以在这里执行一些错误处理逻辑，比如重新启动语音识别器
    }

    @Override
    public void onResults(Bundle results) {
        // 识别结果可用
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (matches != null && matches.size() > 0) {
            String spokenText = matches.get(0);
            // 处理识别结果
            // 可以将识别结果传递给其他方法或回调函数进行处理
        }
    }


}
