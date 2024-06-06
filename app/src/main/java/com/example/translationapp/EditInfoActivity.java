package com.example.translationapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.translation.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class EditInfoActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PICK_IMAGE_REQUEST = 2;
    private TextInputLayout tilName;
    private TextInputLayout tilEmail;
    private TextInputLayout tilPhone;
    private FrameLayout layoutAvatar;
    private ImageView imgAvatar;
    private ImageButton btnChangeAvatar;
    private Button btnSubmit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_info);

        // 初始化组件
        tilName = findViewById(R.id.til_name);
        tilEmail = findViewById(R.id.til_email);
        tilPhone = findViewById(R.id.til_phone);
        layoutAvatar = findViewById(R.id.layout_avatar);
        imgAvatar = findViewById(R.id.img_avatar);
        btnChangeAvatar = findViewById(R.id.btn_change_avatar);
        btnSubmit = findViewById(R.id.btn_submit);

        // 头像更改按钮点击事件
        btnChangeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        // 提交按钮点击事件
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitProfileInfo();
            }
        });
    }

    private void openImageChooser() {
        CharSequence[] options = {"拍照", "从相册选择", "取消"};
        AlertDialog.Builder builder = new AlertDialog.Builder(EditInfoActivity.this);
        builder.setTitle("选择图片");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                } else if (which == 1) {
                    Intent pickPictureIntent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPictureIntent, PICK_IMAGE_REQUEST);
                }
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imgAvatar.setImageBitmap(imageBitmap);
        } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap imageBitmap = BitmapFactory.decodeFile(getRealPathFromURI(selectedImage));
            imgAvatar.setImageBitmap(imageBitmap);
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentURI, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }

    private void submitProfileInfo() {
        // 获取用户输入的信息
        String name = ((TextInputEditText) tilName.getEditText()).getText().toString();
        String email = ((TextInputEditText) tilEmail.getEditText()).getText().toString();
        String phone = ((TextInputEditText) tilPhone.getEditText()).getText().toString();

        // 这里应该是提交用户信息的逻辑
        // 例如，保存到数据库或者发送到服务器等

        // 验证输入是否合法，然后执行保存操作
        if (!name.isEmpty() && !email.isEmpty() && !phone.isEmpty()) {
            // 保存用户信息，例如更新数据库或发送到服务器
            // saveUserInfo(name, email, phone);

            Toast.makeText(this, "用户信息已更新", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "请填写所有字段", Toast.LENGTH_SHORT).show();
        }
    }

    // 以下是可能需要的其他方法，例如权限请求等
    // ...
}
