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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class EditInfoActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PICK_IMAGE_REQUEST = 2;
    private TextInputLayout tilName;
    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
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
        tilPassword = findViewById(R.id.til_phone);
        layoutAvatar = findViewById(R.id.layout_avatar);
        imgAvatar = findViewById(R.id.img_avatar);
        btnChangeAvatar = findViewById(R.id.btn_change_avatar);
        btnSubmit = findViewById(R.id.btn_submit);
        Button btnGiveUp = findViewById(R.id.btn_giveup);

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


        btnGiveUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建Intent，指向ProfileActivity
                Intent intent = new Intent(EditInfoActivity.this, ProfileActivity.class);
                // 启动ProfileActivity
                startActivity(intent);
                // 可以选择性添加：关闭当前Activity
                finish();
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
        String email = ((TextInputEditText) tilEmail.getEditText()).getText().toString();
        String username = ((TextInputEditText) tilName.getEditText()).getText().toString();
        String password = ((TextInputEditText) tilPassword.getEditText()).getText().toString();

        // 验证输入是否合法
        if (!email.isEmpty() && !username.isEmpty() && !password.isEmpty()) {
            // 验证用户名长度
            if (username.length() < 6 || username.length() > 10) {
                Toast.makeText(this, "用户名必须6-10个字符", Toast.LENGTH_LONG).show();
                return;
            }

            // 验证密码长度和复杂性
            if (!isValidPassword(password)) {
                Toast.makeText(this, "密码必须6-16个字符，且必须包含至少1个数字和1个字母", Toast.LENGTH_LONG).show();
                return;
            }

            // 简单的邮箱格式验证
            if (!isValidEmail(email)) {
                Toast.makeText(this, "邮箱格式不正确", Toast.LENGTH_SHORT).show();
                return;
            }

            // 获取UserAccount单例
            UserAccount userAccount = UserAccount.getInstance(this);

            // 如果用户名已存在，先删除旧的凭证
            if (userAccount.containsUsername(username)) {
                userAccount.removeCredentials(username);
            }

            // 更新用户名和密码
            userAccount.setCredentials(username, password);
            userAccount.saveCredentials();

            // 可选：更新当前登录的用户名
            UserAccount.setCurrentUsername(username);

            // 显示信息已更新的提示
            Toast.makeText(this, "用户信息已更新", Toast.LENGTH_SHORT).show();

            // 创建Intent来启动MyManagementAccountActivity
            Intent intent = new Intent(EditInfoActivity.this, ProfileActivity.class);
            startActivity(intent); // 启动Activity
        } else {
            // 显示请填写所有字段的提示
            Toast.makeText(this, "请填写所有字段", Toast.LENGTH_SHORT).show();
        }
    }

    // 密码验证辅助方法
    private boolean isValidPassword(String password) {
        return password.length() >= 6 && password.length() <= 16 && password.matches(".*[a-zA-Z].*") && password.matches(".*[0-9].*");
    }

    // 邮箱验证辅助方法
    private boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }
}
