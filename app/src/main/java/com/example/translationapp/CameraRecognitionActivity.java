package com.example.translationapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.example.translation.R;

public class CameraRecognitionActivity extends AppCompatActivity {

    private static final String TAG = "CameraRecognitionActivity";
    private static final int REQUEST_CODE_PERMISSIONS = 10;
    private static final String[] REQUIRED_PERMISSIONS = {Manifest.permission.CAMERA};

    private TextView recognizedTextView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private Executor executor = Executors.newSingleThreadExecutor();
    private TextRecognizer textRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_recognition);

        recognizedTextView = findViewById(R.id.txt_recognized_text);
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
        // 将 Bitmap 转换为 ImageProxy 对象
        InputImage inputImage = InputImage.fromBitmap(bitmap, 0);

        // 调用 performOCR 方法并传递 ImageProxy 对象
        performOCR(imageProxy);
    }

    @ExperimentalGetImage
    private void performOCR(ImageProxy image, OCRResultListener listener) {
        // 将ImageProxy转换为InputImage
        InputImage inputImage = InputImage.fromMediaImage(image.getImage(), image.getImageInfo().getRotationDegrees());

        // 进行OCR识别
        textRecognizer.process(inputImage)
                .addOnSuccessListener(text -> {
                    // OCR成功
                    // 处理识别结果
                    String recognizedText = text.getText();
                    Log.d(TAG, "Recognized text: " + recognizedText);
                    listener.onOCRCompleted(recognizedText);
                })
                .addOnFailureListener(e -> {
                    // OCR失败
                    Log.e(TAG, "Error during OCR: " + e.getMessage(), e);
                    listener.onOCRFailed(e.getMessage());
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
                // Implement OCR logic here
                // You can access the image data using image.getImage() method
                // Process the image for OCR
                // Once OCR is done, update the recognized text in the TextView
                String recognizedText = performOCR(image);
                runOnUiThread(() -> recognizedTextView.setText(recognizedText));
                // Don't forget to close the image proxy to release resources
                image.close();
            }
        });

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
        cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis);
    }


    // Placeholder method for OCR implementation
    private String performOCR(ImageProxy image) {
        // Implement OCR logic here and return the recognized text
        // For example:
        // OCR ocr = new OCR();
        // String recognizedText = ocr.recognize(image);
        // return recognizedText;
        return "Sample recognized text"; // Placeholder text
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
