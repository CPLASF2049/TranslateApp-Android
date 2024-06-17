package com.example.translationapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class CameraTranslationActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PICK_IMAGE_REQUEST = 2;
    private ImageView imgSelectedPhoto;
    private Button btnSubmitTranslation;
    private ImageButton btnCapture, btnGallery;
    private Bitmap imageBitmap = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_translation);

        // 初始化组件
        imgSelectedPhoto = findViewById(R.id.img_selected_photo);
        btnSubmitTranslation = findViewById(R.id.btn_submit_translation);
        btnCapture = findViewById(R.id.btn_capture);
        btnGallery = findViewById(R.id.btn_gallery);


        // 设置拍照按钮的点击事件
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CameraTranslationActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });

        // 设置图库选择按钮的点击事件
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromGallery();
            }
        });


        // 设置提交翻译按钮的点击事件
        btnSubmitTranslation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitTranslation();
            }
        });

        // 初始化底部导航栏的按钮
        LinearLayout bottomNavigation = findViewById(R.id.bottom_navigation);
        LinearLayout homeButton = findViewById(R.id.home_button);
        LinearLayout voiceButton = findViewById(R.id.voice_button);
        LinearLayout historyButton = findViewById(R.id.history_button);
        LinearLayout myButton = findViewById(R.id.my_button);
        LinearLayout cameraButton = findViewById(R.id.camera_button);

        // 为首页按钮设置点击事件
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到HomeActivity
                Intent intent = new Intent(CameraTranslationActivity.this, TextTranslationActivity.class);
                startActivity(intent);
            }
        });
        // 为语音按钮设置点击事件
        voiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到语音Activity
                Intent intent = new Intent(CameraTranslationActivity.this, VoiceTranslationActivity.class);
                startActivity(intent);
            }
        });
        // 为历史按钮设置点击事件
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到历史Activity
                Intent intent = new Intent(CameraTranslationActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

        // 为“我的”按钮设置点击事件
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到我的Activity
                Intent intent = new Intent(CameraTranslationActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void pickImageFromGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return; // 如果结果不是RESULT_OK，则直接返回
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            // 从相机Intent的extras中获取Bitmap
            Bundle extras = data.getExtras();
            if (extras != null) {
                imageBitmap = (Bitmap) extras.get("data");
                if (imageBitmap != null) {
                    imgSelectedPhoto.setImageBitmap(imageBitmap);
                }
            }
        } else if (requestCode == PICK_IMAGE_REQUEST) {
            // 从图库中获取图片的Uri
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                try {
                    // 使用ContentResolver和InputStream来获取Bitmap
                    InputStream is = getContentResolver().openInputStream(selectedImageUri);
                    imageBitmap = BitmapFactory.decodeStream(is);
                    if (imageBitmap != null) {
                        imgSelectedPhoto.setImageBitmap(imageBitmap);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    // 处理错误，例如显示Toast消息
                    Toast.makeText(this, "Error retrieving image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void submitTranslation() {
        // 创建Intent对象，指向CameraRecognitionActivity
        Intent intent = new Intent(this, CameraRecognitionActivity.class);

        // 将Bitmap对象作为额外数据传递
        intent.putExtra("imageBitmap", imageBitmap);

        // 启动CameraRecognitionActivity
        startActivity(intent);
    }

}
