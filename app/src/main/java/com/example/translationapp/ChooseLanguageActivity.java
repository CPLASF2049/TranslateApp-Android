package com.example.translationapp;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

import com.example.translation.R;

public class ChooseLanguageActivity extends AppCompatActivity {

    private ListView listView;
    private String[] languages = {"中文","English" };
    private int[] languageResIds = { R.string.chinese,R.string.english};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_language); // 使用您的布局文件名

        listView = findViewById(R.id.list_view_languages);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, languages);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                changeLanguage(languageResIds[position]);
            }
        });
    }

    private void changeLanguage(int languageResId) {
        // 获取语言代码
        String languageCode = getResources().getString(languageResId);
        // 创建Locale对象
        Locale newLocale = new Locale(languageCode);
        // 更改配置
        Configuration config = new Configuration(getResources().getConfiguration());
        config.setLocale(newLocale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // 重载Activity
        recreate();
    }
}
