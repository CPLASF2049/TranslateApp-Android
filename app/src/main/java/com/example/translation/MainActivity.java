import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextInput;
    private TextView textViewTranslationResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化控件
        editTextInput = findViewById(R.id.editText_input);
        textViewTranslationResult = findViewById(R.id.textView_translation_result);
        Button buttonVoiceInput = findViewById(R.id.button_voice_input);
        Button buttonImageInput = findViewById(R.id.button_image_input);
        Button buttonVoiceOutput = findViewById(R.id.button_voice_output);

        // 设置语音输入按钮点击事件
        buttonVoiceInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceInput();
            }
        });

        // 设置图像输入按钮点击事件
        buttonImageInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里处理图像输入逻辑
                Toast.makeText(MainActivity.this, "Image input functionality coming soon!", Toast.LENGTH_SHORT).show();
            }
        });

        // 设置语音输出按钮点击事件
        buttonVoiceOutput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里处理语音输出逻辑
                String translation = textViewTranslationResult.getText().toString();
                if (!translation.isEmpty()) {
                    speakTranslation(translation);
                } else {
                    Toast.makeText(MainActivity.this, "No translation available to speak!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // 启动语音输入
    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak something...");
        startActivityForResult(intent, 1);
    }




}
