package com.example.translationapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.translation.R;

// API接口定义
interface ResetPasswordService {
    @POST("reset_password")
    Call<ResetPasswordResponse> resetPassword(@Body ResetPasswordBody body);
}
// 请求体
class ResetPasswordBody {
    private String account;
    private String newPassword;

    // 全参构造函数
    public ResetPasswordBody() {
        this.account = account;
        this.newPassword = newPassword;
    }

    // account的getter和setter
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    // newPassword的getter和setter
    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

// 响应体
class ResetPasswordResponse {
    private boolean success;
    private String message;

    // 全参构造函数
    public ResetPasswordResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // success的getter和setter
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    // message的getter和setter
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // Retrofit实例化
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://yourserver.com/api/") // 替换为您的服务器API基础URL
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    ResetPasswordService service = retrofit.create(ResetPasswordService.class);}

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText etAccount, etNewPassword, etConfirmPassword;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_key);

        // 初始化控件
        etAccount = findViewById(R.id.et_account);
        etNewPassword = findViewById(R.id.et_new_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnSubmit = findViewById(R.id.btn_submit);

        // 设置提交按钮的点击事件
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取用户输入
                String account = etAccount.getText().toString().trim();
                String newPassword = etNewPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();

                // 检查输入是否有效
                if (account.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(ResetPasswordActivity.this, "所有字段都是必填的", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(ResetPasswordActivity.this, "密码不匹配", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 这里添加重置密码的逻辑
                resetPassword(account, newPassword);

                // 提示用户密码重置成功
                Toast.makeText(ResetPasswordActivity.this, "密码重置成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 重置密码的方法，这里仅作为示例，实际应包含与服务器通信的代码
    private void resetPassword(String account, String newPassword) {
        // 创建请求体
        ResetPasswordBody body = new ResetPasswordBody();
        body.setAccount(account);
        body.setNewPassword(newPassword);

        // 发送请求
        Call<ResetPasswordResponse> call = service.resetPassword(body);
        call.enqueue(new Callback<ResetPasswordResponse>() {
            @Override
            public void onResponse(Call<ResetPasswordResponse> call, Response<ResetPasswordResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResetPasswordResponse resetPasswordResponse = response.body();
                    if (resetPasswordResponse.isSuccess()) {
                        // 更新数据库的逻辑（如果需要）
                        // 显示成功消息
                        Toast.makeText(ResetPasswordActivity.this, "密码重置成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 显示错误消息
                        Toast.makeText(ResetPasswordActivity.this, resetPasswordResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 服务器错误，显示错误消息
                    Toast.makeText(ResetPasswordActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResetPasswordResponse> call, Throwable t) {
                // 网络或其他错误，显示错误消息
                Toast.makeText(ResetPasswordActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }
}