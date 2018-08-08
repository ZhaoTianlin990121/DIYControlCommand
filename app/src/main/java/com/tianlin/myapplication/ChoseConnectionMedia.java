package com.tianlin.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChoseConnectionMedia extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_connection_media);
        Button button = (Button) findViewById(R.id.wifi);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String instructionPlanName = intent.getStringExtra("ChosenPlan");
                Intent intent2 = new Intent(ChoseConnectionMedia.this,ChooseWifiIP.class);
                intent2.putExtra("ChosenPlan",instructionPlanName);
                startActivity(intent2);
            }
        });
    }
}
