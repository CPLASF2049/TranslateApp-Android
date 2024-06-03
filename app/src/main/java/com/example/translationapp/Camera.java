package com.example.translationapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Camera;
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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

public class Camera extends AppCompatActivity {
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
        setContentView(R.layout.activity_main);

        previewView = findViewById(R.id.textureView);
    }

    /* 检查摄像头的状态 onResume在activity启动后执行 */
    @Override
    protected void onResume() {
        super.onResume();

        startCameraThread();

        if(!previewView.isActivated()){
            // 添加监听
            previewView.setSurfaceTextureListener(textureListener);
        }
        else{
            startPreview();  //开始预览
        }
    }

    // 创建一个监听器的全局变量
    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        // 摄像头控件准备好
        public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
            try {
                setUpCamera(width, height);
            }
            catch (CameraAccessException e){
                throw new RuntimeException(e);
            }
            openCamera();
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

        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        // 遍历手机系统的摄像头，拿到摄像头ID
        for(String CameraID : cameraManager.getCameraIdList()){
            // 获取当前摄像头的一些参数
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraID);
            Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
            // 如果摄像头朝前就换一个
            if(facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT){
                continue;
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
            cameraID = CameraID;
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
            // 拍照完成
            public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                Toast.makeText(getApplicationContext(), "Photo has been saved!", Toast.LENGTH_LONG).show();
                unLockFouces();
                super.onCaptureCompleted(session, request, result);
            }
        };

        cameraCaptureSession.stopRepeating();
        cameraCaptureSession.capture(cameraBuilder.build(), captureCallback, cameraHandler);
    }

    private void unLockFouces(){

        try {
            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
            cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), null, cameraHandler);
        }
        catch (CameraAccessException e){
            throw new RuntimeException(e);
        }
    }

    // 建立缓冲区，准备保存图片，参数2表示最多存2张
    private void setUpImegeReader(){

        imageReader = ImageReader.newInstance(captureSize.getWidth(), captureSize.getHeight(), ImageFormat.JPEG, 2);

        // 当图片已经准备好了
        imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                cameraHandler.post(new MainActivity.ImageSaver(reader.acquireLatestImage()));
            }
        }, cameraHandler);
    }

    private class ImageSaver implements Runnable{
        Image image;
        public ImageSaver(Image anImage){
            image = anImage;
        }

        public void run(){

            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            String path = Environment.getExternalStorageDirectory()+"/DCIM/CameraV2";
            File imegeFile = new File(path);
            if(!imegeFile.exists()){
                imegeFile.mkdir();
            }
            // 文件命名方式：年月日_时分秒
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = path + "IMG_" + timeStamp + ".jpg";
            try {
                FileOutputStream fos = new FileOutputStream(fileName);
                fos.write(data, 0, data.length);
            }
            catch (FileNotFoundException e){
                throw new RuntimeException(e);
            }
            catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }
}
