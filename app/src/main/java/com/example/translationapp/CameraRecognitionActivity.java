package com.example.translationapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.DoNotInline;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.ImageProxy.PlaneProxy;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


@OptIn(markerClass = ExperimentalGetImage.class)
public class CameraRecognitionActivity extends AppCompatActivity {

    private static final String TAG = "CameraRecognitionActivity";
    private static final int REQUEST_CODE_PERMISSIONS = 10;
    private static final String[] REQUIRED_PERMISSIONS = {Manifest.permission.CAMERA};

    private TextView recognizedTextView;
    private TextView translatedTextTextView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private Executor executor = Executors.newSingleThreadExecutor();
    private TextRecognizer textRecognizer;
    String sourceLanguage = "auto";
    String targetLanguage = "zh";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_recognition);

        recognizedTextView = findViewById(R.id.txt_recognized_text);
        translatedTextTextView = findViewById(R.id.txt_translated_text);
        initOCR();

        // 获取传递过来的图片数据
        Bitmap bitmap = getIntent().getParcelableExtra("imageBitmap");

        // 执行 OCR 识别操作
        performOCR(bitmap);

        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }

    private void initOCR() {
        textRecognizer = TextRecognition.getClient();
    }

    private void performOCR(Bitmap bitmap) {
        InputImage inputImage = InputImage.fromBitmap(bitmap, 0);
        textRecognizer.process(inputImage)
                .addOnSuccessListener(text -> {
                    // OCR成功
                    String recognizedText = text.getText();
                    Log.d(TAG, "Recognized text: " + recognizedText);
                    // 这里需要更新UI，例如使用 runOnUiThread
                    runOnUiThread(() -> recognizedTextView.setText(recognizedText));
                    performTranslation(recognizedText);
                })
                .addOnFailureListener(e -> {
                    // OCR失败
                    Log.e(TAG, "Error during OCR: " + e.getMessage(), e);
                    // 更新UI
                    runOnUiThread(() -> recognizedTextView.setText("OCR failed: " + e.getMessage()));
                });
    }

    @ExperimentalGetImage
    @OptIn(markerClass = ExperimentalGetImage.class)
    private void performOCR(ImageProxy image, OCRResultListener listener) {
        InputImage inputImage = InputImage.fromMediaImage(image.getImage(), image.getImageInfo().getRotationDegrees());
        textRecognizer.process(inputImage)
                .addOnSuccessListener(text -> {
                    // OCR成功
                    String recognizedText = text.getText();
                    Log.d(TAG, "Recognized text: " + recognizedText);
                    listener.onOCRCompleted(recognizedText);
                    performTranslation(recognizedText);
                })
                .addOnFailureListener(e -> {
                    // OCR失败
                    Log.e(TAG, "Error during OCR: " + e.getMessage(), e);
                    listener.onOCRFailed(e.getMessage());
                })
                .addOnCompleteListener(task -> {
                    // 关闭ImageProxy以释放资源
                    image.close();
                });
    }

    interface OCRResultListener {
        void onOCRCompleted(String result);
        void onOCRFailed(String errorMessage);
    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void performTranslation(String originalText) {
        String appId = "20240531002066446";
        String salt = "123456"; // 随机数
        String sign = md5(appId, originalText, salt, "GoAyiiiaGuLFmrY4F54O");// 根据文档计算得出的签名

        // 使用 TranslationManager 进行翻译
        TranslationManager translationManager = new TranslationManager();
        translationManager.translate(originalText, sourceLanguage, targetLanguage, appId, salt, sign, new TranslationManager.TranslationCallback() {
            @Override
            public void onSuccess(String translatedText) {
                // 显示翻译结果
                translatedTextTextView.setText(translatedText);
            }

            @Override
            public void onFailure(String errorMessage) {
                System.out.println("翻译失败：" + errorMessage);
                // 处理翻译失败的情况
            }
        });
    }

    public static String md5(String fixedString, String originalText, String salt, String password) {
        // 将三个字符串按顺序连接起来
        String combinedString = fixedString + originalText + salt + password;
        // 计算组合后的字符串的 MD5 值并返回
        return DigestUtils.md5Hex(combinedString);
    }

    private void startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindPreview(ProcessCameraProvider cameraProvider) throws ExecutionException, InterruptedException {
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        imageAnalysis.setAnalyzer(executor, new ImageAnalysis.Analyzer() {
            @Override
            public void analyze(@NonNull ImageProxy image) {
                // 调用 performOCR 方法并传递 ImageProxy 对象和OCRResultListener
                performOCR(image, new OCRResultListener() {
                    @Override
                    public void onOCRCompleted(String result) {
                        // 更新UI
                        runOnUiThread(() -> recognizedTextView.setText(result));
                    }

                    @Override
                    public void onOCRFailed(String errorMessage) {
                        // 更新UI
                        runOnUiThread(() -> recognizedTextView.setText("OCR failed: " + errorMessage));
                    }
                });
            }
        });

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
        cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放OCR资源
        closeOCR();
    }

    // 释放OCR资源的方法
    private void closeOCR() {
        if (textRecognizer != null) {
            textRecognizer.close();
            textRecognizer = null;
        }
    }
}
