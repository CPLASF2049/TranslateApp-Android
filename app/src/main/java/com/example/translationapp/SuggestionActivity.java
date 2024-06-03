package com.example.translationapp;

import static android.app.DownloadManager.COLUMN_DESCRIPTION;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SuggestionActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private TextInputLayout tilDescription;
    private TextInputLayout tilContact;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggestions);

        databaseHelper = new DatabaseHelper(this);

        tilDescription = findViewById(R.id.til_description);
        tilContact = findViewById(R.id.til_contact);
        btnSubmit = findViewById(R.id.btn_submit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etDescription.getEditText().getText().toString();
                String contact = etContact.getEditText().getText().toString();
                saveSuggestion(description, contact);
            }
        });
    }

    private void saveSuggestion(String description, String contact) {
        // 这里可以添加数据验证逻辑

        // 将数据保存到数据库
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_CONTACT, contact);
        long result = db.insert(TABLE_SUGGESTIONS, null, values);

        if (result != -1) {
            Toast.makeText(this, "建议已成功保存", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "保存失败，请重试", Toast.LENGTH_SHORT).show();
        }
    }
}
