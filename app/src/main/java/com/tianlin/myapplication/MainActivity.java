package com.tianlin.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    String initRecon = RecognizeManager.instruction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LitePal.getDatabase();
        List<InstructionPlan> instructionPlans = DataSupport.findAll(InstructionPlan.class);
        for (InstructionPlan instructionPlan:instructionPlans) {
            RecognizeManager.planNameList.add(instructionPlan.getName());
        }
        Button monitor_mode = (Button) findViewById(R.id.monitor_mode);
        monitor_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ChoosePlan.class);
                startActivity(intent);
            }
        });
        Button choose_moniter_mode = (Button) findViewById(R.id.choose_monitor_mode);
        choose_moniter_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ChooseMode.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.scheme:
                Intent intent = new Intent(MainActivity.this,InstructionSchemes.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }
}
