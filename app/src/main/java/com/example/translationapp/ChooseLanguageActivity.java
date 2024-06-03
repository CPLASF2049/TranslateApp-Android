package com.example.translationapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ChooseLanguageActivity extends AppCompatActivity {

    private ListView listViewLanguages;
    private TextView txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_language);

        // 初始化组件
        txtTitle = findViewById(R.id.txt_title);
        listViewLanguages = findViewById(R.id.list_view_languages);

        // 设置语言列表适配器
        String[] languageNames = {"English", "Español", "Français", "Deutsch", "中文"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, languageNames);
        listViewLanguages.setAdapter(adapter);

        // 设置列表项点击事件
        listViewLanguages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取被点击的项
                String selectedLanguage = (String) parent.getItemAtPosition(position);

                // 处理语言选择，例如更新应用语言或跳转到其他页面
                // 这里仅演示Toast消息
                Toast.makeText(ChooseLanguageActivity.this, "已选择语言: " + selectedLanguage, Toast.LENGTH_SHORT).show();

                // 示例：根据选择的语言更新应用语言设置
                // updateAppLanguage(selectedLanguage);

                // 完成后关闭当前Activity
                // finish();
            }
        });
    }

    // 示例：更新应用语言的方法（需要根据实际情况实现）
    /*
    private void updateAppLanguage(String languageCode) {
        // 根据选择的语言代码更新应用语言设置
        // 这可能涉及到Locale设置和重启Activity等操作
    }
    */
}
