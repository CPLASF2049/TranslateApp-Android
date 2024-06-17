package com.example.translationapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.media.ImageWriter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Size;
import android.util.SparseArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.translationapp.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CameraActivity extends AppCompatActivity {
    TextureView previewView;

    HandlerThread handlerThread;
    Handler cameraHandler;

    CameraManager cameraManager;

    Size previewSize;  //预览尺寸
    Size captureSize;  //拍照尺寸
    String cameraID;

    CameraDevice cameraDevice;

    CaptureRequest captureRequest;
    CaptureRequest.Builder captureRequestBuilder;

    CameraCaptureSession cameraCaptureSession;

    ImageReader imageReader;

    //旋转角度
    private static final SparseArray ORIENTATION =new SparseArray();

    static{
        ORIENTATION.append(Surface.ROTATION_0,90);
        ORIENTATION.append(Surface.ROTATION_90,0);
        ORIENTATION.append(Surface.ROTATION_180,270);
        ORIENTATION.append(Surface.ROTATION_270,180);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_photo);
        previewView = findViewById(R.id.texture_view);
        previewView.setSurfaceTextureListener(textureListener);
        FrameLayout previewContainer = findViewById(R.id.camera_preview_container);
        Button quitButton = findViewById(R.id.btn_back);
        Button captureButton = findViewById(R.id.btn_capture);

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CameraActivity.this, CameraTranslationActivity.class);
                startActivity(intent);
            }
        });

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Capture(v); // 调用Capture方法
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                    // 处理异常，例如显示错误信息
                }
            }
        });

    }

    /* 检查摄像头的状态 onResume在activity启动后执行 */
    @Override
    protected void onResume() {
        super.onResume();
        startCameraThread();

        if (previewView != null && previewView.isAvailable()) {
            int width = previewView.getWidth();
            int height = previewView.getHeight();
            if (width > 0 && height > 0) {
                try {
                    setUpCamera(width, height);
                    startPreview();
                } catch (CameraAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            // 处理TextureView未准备好的情况
        }
    }

    // 创建一个监听器的全局变量
    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
            try {
                setUpCamera(width, height);
                openCamera();
            } catch (CameraAccessException e) {
                // 处理CameraAccessException，例如显示错误消息或关闭Activity
                e.printStackTrace();
                cameraDevice.close();
                cameraDevice = null;
            }
        }

        @Override
        // 尺寸改变
        public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

        }

        @Override
        // 销毁
        public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
            return false;
        }

        @Override
        // 更新
        public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

        }
    };

    private void startCameraThread() {

        handlerThread = new HandlerThread("CameraThread");
        handlerThread.start();
        cameraHandler = new Handler(handlerThread.getLooper());

    }

    private void setUpCamera(int width, int height) throws CameraAccessException{

        if (cameraManager == null) {
            cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        }

        String[] cameraIdList = cameraManager.getCameraIdList();
        if (cameraIdList == null || cameraIdList.length == 0) {
            // 抛出带有原因的CameraAccessException
            throw new CameraAccessException(CameraAccessException.CAMERA_DISABLED, "No cameras available on this device.");
        }

        for (String cameraId : cameraIdList) {
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
            Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
            if (facing != null && facing != CameraCharacteristics.LENS_FACING_BACK) {
                continue; // 只选择后置摄像头
            }

            // 确保尺寸有效
            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("Invalid preview size");
            }

            // 获取当前摄像头的分辨率
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            if(map != null){
                previewSize = getOptimalSize(map.getOutputSizes(SurfaceTexture.class), width, height);
                captureSize = Collections.max(Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)), new Comparator<Size>() {
                    @Override
                    public int compare(Size o1, Size o2) {
                        return Long.signum(o1.getWidth() * o1.getHeight() - o2.getWidth() * o2.getHeight());
                    }
                });
            }

            // 建立缓冲区
            setUpImegeReader();
            cameraID = cameraId;
            break;
        }
    }

    private Size getOptimalSize(Size[] sizeMap, int width, int height){

        List<Size> sizeList = new ArrayList<Size>(); //一个存放分辨率的列表
        for(Size option : sizeMap){
            if(width > height){ // 横屏
                if(option.getWidth() > width && option.getHeight() > height){
                    sizeList.add(option);
                }
            }
            else{ //竖屏
                if(option.getWidth() > height && option.getHeight() > width) {
                    sizeList.add(option);
                }
            }
        }
        if(sizeList.size() > 1){
            return Collections.min(sizeList, new Comparator<Size>() {
                @Override
                public int compare(Size o1, Size o2) {
                    return Long.signum(o1.getWidth() * o1.getHeight() - o2.getWidth() * o2.getHeight());
                }
            });
        }
        return sizeMap[0];
    }

    private void openCamera(){

        // 动态获取权限
        String [] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        // 遍历权限
        int i = 0;
        for(String permission : permissions){
            if(checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                this.requestPermissions(permissions, i++);
                return;
            }
        }
        try {
            cameraManager.openCamera(cameraID, stateCallback, cameraHandler);
        }
        catch (CameraAccessException e){
            throw new RuntimeException(e);
        }
    }

    CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        // 摄像头打开
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice = camera;
            startPreview();
        }

        @Override
        // 摄像头关闭
        public void onDisconnected(@NonNull CameraDevice camera) {
            cameraDevice.close();
            cameraDevice = null;
        }

        @Override
        // 摄像头发生错误
        public void onError(@NonNull CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };

    private void startPreview(){

        // 建立图像缓冲区
        SurfaceTexture surfaceTexture = previewView.getSurfaceTexture();
        surfaceTexture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());

        // 得到界面显示对象
        Surface previewSurface = new Surface(surfaceTexture);
        try {
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(previewSurface);

            cameraDevice.createCaptureSession(Arrays.asList(previewSurface, imageReader.getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    captureRequest = captureRequestBuilder.build();
                    cameraCaptureSession = session;

                    try {
                        cameraCaptureSession.setRepeatingRequest(captureRequest, null, cameraHandler);
                    }
                    catch (CameraAccessException e){
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                }
            }, cameraHandler);
        }
        catch (CameraAccessException e){
            throw new RuntimeException(e);
        }
    }

    // 拍照按钮的响应函数
    public void Capture(View view) throws CameraAccessException{

        // 获取请求
        CaptureRequest.Builder cameraBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
        cameraBuilder.addTarget(imageReader.getSurface());

        // 获取摄像头方向
        int rotation = getWindowManager().getDefaultDisplay().getRotation();

        // 获取拍照方向
        cameraBuilder.set(CaptureRequest.JPEG_ORIENTATION, (Integer) ORIENTATION.get(rotation));

        // 回调函数
        CameraCaptureSession.CaptureCallback captureCallback = new CameraCaptureSession.CaptureCallback() {
            @Override
            public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                // 拍照完成，显示Toast消息
                Toast.makeText(getApplicationContext(), "Photo capture successful", Toast.LENGTH_SHORT).show();

                // 拍照完成后，保存图片
                Image image = imageReader.acquireLatestImage();
                if (image != null) {
                    String imagePath = saveImageToPublicDirectory(image);
                    image.close();
                    if (imagePath != null) {
                        // 将图片路径传递给CameraTranslationActivity
                        Intent intent = new Intent(CameraActivity.this, CameraTranslationActivity.class);
                        intent.putExtra("photo_path", imagePath);
                        startActivity(intent);
                    }
                }

                // 恢复预览
                startPreview();
            }
        };

        cameraCaptureSession.stopRepeating();
        cameraCaptureSession.capture(cameraBuilder.build(), captureCallback, cameraHandler);
    }

    private void saveAndShareImage() {
        Image image = imageReader.acquireLatestImage();
        if (image != null) {
            String imagePath = saveImageToPublicDirectory(image);
            if (imagePath != null) {
                // 将图片路径传递给CameraTranslationActivity
                Intent intent = new Intent(CameraActivity.this, CameraTranslationActivity.class);
                intent.putExtra("photo_path", imagePath);
                startActivity(intent);
            }
            image.close();
        }
    }

    private String saveImageToPublicDirectory(Image image) {
        if (image == null) return null;

        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);

        // 获取公共图片目录
        File photoDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        // 创建时间戳作为文件名
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File photoFile = new File(photoDir, "IMG_" + timeStamp + ".jpg");

        try (FileOutputStream fos = new FileOutputStream(photoFile)) {
            fos.write(bytes);
            fos.flush();
            return photoFile.getAbsolutePath(); // 返回图片文件的绝对路径
        } catch (IOException e) {
            e.printStackTrace();
            return null; // 如果保存失败，返回null
        }
    }

    // 建立缓冲区，准备保存图片，参数2表示最多存2张
    private void setUpImegeReader(){
        imageReader = ImageReader.newInstance(
                captureSize.getWidth(),
                captureSize.getHeight(),
                ImageFormat.JPEG,
                2
        );

        // 当图片已经准备好了
        imageReader.setOnImageAvailableListener(
                new ImageReader.OnImageAvailableListener() {
                    @Override
                    public void onImageAvailable(ImageReader reader) {
                        // 直接处理最新的图片
                        Image image = reader.acquireLatestImage();
                        if (image != null) {
                            // 保存图片并处理成功后的逻辑
                            String imagePath = saveImageToPublicDirectory(image);
                            image.close(); // 释放Image资源

                            // 根据图片保存结果进行UI更新或其他操作
                            if (imagePath != null) {
                                // 图片保存成功，可以在这里更新UI或执行其他操作
                                // 例如，使用runOnUiThread来更新UI
                                runOnUiThread(() -> {
                                    // 这里可以更新UI，例如显示一个提示信息
                                    Toast.makeText(CameraActivity.this, "Image saved to: " + imagePath, Toast.LENGTH_LONG).show();
                                });
                            } else {
                                // 图片保存失败，可以在这里处理错误，例如显示一个错误消息
                                runOnUiThread(() -> {
                                    Toast.makeText(CameraActivity.this, "Failed to save image.", Toast.LENGTH_LONG).show();
                                });
                            }
                        }
                    }
                },
                cameraHandler
        );
    }

}

