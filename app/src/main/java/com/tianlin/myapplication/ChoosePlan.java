package com.tianlin.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class ChoosePlan extends AppCompatActivity {

    private ArrayList<String> instructionNames = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println(1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_plan);
        System.out.println(2);
        List<InstructionPlan> instructionPlans = DataSupport.findAll(InstructionPlan.class);
        System.out.println(3);
        ListView listView = (ListView) findViewById(R.id.choose_list);
        System.out.println(4);
        instructionNames = new ArrayList<String>();
        System.out.println(5);
        for (InstructionPlan instructionPlan:instructionPlans){
            instructionNames.add(instructionPlan.getName());
        }
        System.out.println(6);
        adapter = new ArrayAdapter<String>(ChoosePlan.this, android.R.layout.simple_list_item_1, instructionNames);
        System.out.println(7);
        listView.setAdapter(adapter);
        System.out.println(8);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println(9);
                List<InstructionPlan> instructionPlansss = DataSupport.findAll(InstructionPlan.class);
                System.out.println(10);
                InstructionPlan instructionPlan = instructionPlansss.get(i);
                System.out.println(11);
                Intent intent3 = new Intent(ChoosePlan.this,ChoseConnectionMedia.class);
                intent3.putExtra("ChosenPlan",instructionNames.get(i));
                System.out.println("q");
                startActivity(intent3);
            }
        });
    }
}
