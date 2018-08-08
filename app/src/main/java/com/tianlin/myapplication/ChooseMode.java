package com.tianlin.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseMode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_mode);
        TextView textView = (TextView) findViewById(R.id.current_mode);
        if (RecognizeManager.mode == RecognizeManager.AUDIO_MODE) textView.setText("当前为语音控制模式");
        else if (RecognizeManager.mode == RecognizeManager.BUTTON_MODE) textView.setText("当前为按钮控制模式");
        else if (RecognizeManager.mode == RecognizeManager.WRITE_MODE) textView.setText("当前为键盘输入控制模式");
        Button audio = (Button) findViewById(R.id.audio_mode);
        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecognizeManager.mode = RecognizeManager.AUDIO_MODE;
                Toast.makeText(ChooseMode.this,"选中语音控制模式",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        Button button = (Button) findViewById(R.id.button_mode);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecognizeManager.mode = RecognizeManager.BUTTON_MODE;
                Toast.makeText(ChooseMode.this,"选中按钮控制模式",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        Button write = (Button) findViewById(R.id.write_mode);
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecognizeManager.mode = RecognizeManager.WRITE_MODE;
                Toast.makeText(ChooseMode.this,"选中键盘输入控制模式",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
