package com.example.translationapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageProxy;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.translation.R;

import java.io.IOException;

public class CameraTranslationActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PICK_IMAGE_REQUEST = 2;
    private ImageView imgSelectedPhoto;
    private Bitmap selectedImageBitmap;
    private Button btnSubmitTranslation;
    private ImageButton btnCapture, btnGallery;

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
                dispatchTakePictureIntent();
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
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void pickImageFromGallery() {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImageIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            selectedImageBitmap = (Bitmap) extras.get("data");
            imgSelectedPhoto.setImageBitmap(selectedImageBitmap);
        } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            Uri imageUri = data.getData();
            try {
                selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imgSelectedPhoto.setImageBitmap(selectedImageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void submitTranslation() {
        if (selectedImageBitmap != null) {
            // 创建一个新的 Intent 对象，将当前的 Activity 跳转到 CameraRecognitionActivity
            Intent intent = new Intent(this, CameraRecognitionActivity.class);
            // 将图片数据作为附加信息添加到 Intent 中
            intent.putExtra("imageBitmap", selectedImageBitmap); // bitmap 是您要传递的图片数据
            // 启动 CameraRecognitionActivity
            startActivity(intent);
        } else {
            // 如果未选择图片，显示提示信息或执行其他操作
            Toast.makeText(this, "请先选择图片", Toast.LENGTH_SHORT).show();
        }
    }
}
