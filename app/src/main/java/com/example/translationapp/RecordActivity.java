package com.example.translationapp;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.translation.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class RecordActivity extends AppCompatActivity{

    private Button buttonRecord;
    private int isRecording = 0;
    private int isPlaying = 0;
    private String FilePath = null;
    private Timer timer;
    private ImageView imageView;
    private TextView textViewRecordTime;
    private ImageView recordWaveLeft, recordWaveRight;
    private AnimationDrawable animationDrawableLeft, animationDrawableRight;
    private MediaPlayer mediaPlayer = null;
    private MediaRecorder mediaRecorder = null;
    Button buttonSave, buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        buttonSave = findViewById(R.id.bt_save);
        buttonSave.setOnClickListener(new ClickEvent());
        buttonBack = (Button) findViewById(R.id.bt_back);
        buttonBack.setOnClickListener(new ClickEvent());
        buttonRecord = (Button) findViewById(R.id.bt_record);
        buttonRecord.setOnClickListener(new ClickEvent());
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(new ClickEvent());
        recordWaveLeft = (ImageView) findViewById(R.id.recordWaveLeft);
        recordWaveRight = (ImageView) findViewById(R.id.recordWaveRight);
        recordWaveLeft.setImageResource(R.id.recordWaveLeft);
        recordWaveRight.setImageResource(R.id.recordWaveRight);
        animationDrawableLeft = ((AnimationDrawable) recordWaveLeft.getDrawable());
        animationDrawableRight = ((AnimationDrawable) recordWaveRight.getDrawable());
        textViewRecordTime = (TextView) findViewById(R.id.textViewRecordTime);
    }

    final Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 1 :
                    String time[] = textViewRecordTime.getText().toString().split(":");
                    int hour = Integer.parseInt(time[0]);
                    int minute = Integer.parseInt(time[1]);
                    int second = Integer.parseInt(time[2]);

                    if(second < 59){
                        second++;
                    }
                    else if(second == 59 && minute < 59){
                        minute++;
                        second = 0;
                    }
                    if(second == 59 && minute == 59 && hour < 98){
                        hour++;
                        minute = 0;
                        second = 0;
                    }

                    time[0] = hour + "";
                    time[1] = minute + "";
                    time[2] = second + "";
                    //调整格式显示到屏幕上
                    if(second < 10)
                        time[2] = "0" + second;
                    if(minute < 10)
                        time[1] = "0" + minute;
                    if(hour < 10)
                        time[0] = "0" + hour;

                    //显示在TextView中
                    textViewRecordTime.setText(time[0]+":"+time[1]+":"+time[2]);

                    break;


            }
        }
    };

    class ClickEvent implements View.OnClickListener{

        @Override
        public void onClick(View view){
            switch(view.getId()){
                case R.id.buttonRecord :
                    if(isRecording == 0){
                        if(FilePath != null){
                            File oldFile = new File(FilePath);
                            oldFile.delete();
                        }

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                        Date curDate = new Date(System.currentTimeMillis());
                        String str = simpleDateFormat.format(curDate);

                        str = str + "record.amr";
                        File dir = new File("/sdcard/notes/");
                        File file = new File("/sdcard/notes/", str);
                        if(!dir.exists()){
                            dir.mkdir();
                        }
                        else{
                            if(file.exists()){
                                file.delete();
                            }
                        }

                        FilePath = dir.getPath() + "/" + str;
                        timer = new Timer();

                        imageView.setClickable(false);
                        textViewRecordTime.setText("00:00:00");
                        isRecording = 1;
                        buttonRecord.setBackgroundResource(R.drawable.tabbar_record_stop);

                        mediaRecorder = new MediaRecorder();
                        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                        mediaRecorder.setOutputFile(FilePath);
                        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

                        try{
                            mediaRecorder.prepare();
                        } catch (IllegalStateException e){
                            e.printStackTrace();
                        } catch (IOException e){
                            e.printStackTrace();
                        }

                        mediaRecorder.start();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                Message message = new Message();
                                message.what = 1;
                                handler.sendMessage(message);
                            }
                        },1000, 1000);

                        animationDrawableLeft.start();
                        animationDrawableRight.start();
                    }
                    else{
                        isRecording = 0;
                        buttonRecord.setBackgroundResource(R.drawable.tabbar_record_start);
                        mediaRecorder.stop();
                        timer.cancel();
                        timer = null;

                        mediaRecorder.release();
                        mediaRecorder = null;

                        imageView.setClickable(true);
                        animationDrawableLeft.stop();
                        animationDrawableRight.stop();
                        Toast.makeText(RecordActivity.this, "单击麦克风图标试听，再次点击结束试听", Toast.LENGTH_LONG).show();
                    }
                    break;

                case R.id.iv_microphone :
                    if(FilePath == null){
                        Toast.makeText(RecordActivity.this, "请先录音", Toast.LENGTH_LONG).show();
                    }
                    else {
                        if(isPlaying == 0){
                            isPlaying = 1;
                            mediaPlayer = new MediaPlayer();
                            textViewRecordTime.setText("00:00:00");
                            timer = new Timer();
                            mediaPlayer .setOnCompletionListener(new MediaCompletion());
                            try{
                                mediaPlayer.setDataSource(FilePath);
                                mediaPlayer.prepare();
                                mediaPlayer.start();
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            } catch (SecurityException e) {
                                e.printStackTrace();
                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    Message message = new Message();
                                    message.what = 1;
                                    handler.sendMessage(message);
                                }
                            }, 1000, 1000);

                            animationDrawableLeft.start();
                            animationDrawableRight.start();
                        }
                        else {
                            isPlaying = 0;
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            mediaPlayer = null;
                            timer.cancel();
                            timer = null;

                            animationDrawableLeft.stop();
                            animationDrawableRight.stop();
                        }
                    }
                    break;

                case R.id.bt_save :
                    Intent intent = getIntent();
                    Bundle bundle = new Bundle();
                    bundle.putString("Audio", FilePath);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);

                    RecordActivity.this.finish();
                    break;

                case R.id.bt_back :
                    if(FilePath != null){
                        File oldFile =new File(FilePath);
                        oldFile.delete();
                    }
                    RecordActivity.this.finish();
                    break;
            }
        }
    }

    class MediaCompletion implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            timer.cancel();
            timer = null;
            isPlaying = 0;
            animationDrawableLeft.stop();
            animationDrawableRight.stop();
            Toast.makeText(RecordActivity.this, "播放完毕", Toast.LENGTH_LONG).show();
            textViewRecordTime.setText("00:00:00");
        }
    }
}
