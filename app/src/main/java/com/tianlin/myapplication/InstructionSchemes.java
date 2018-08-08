package com.tianlin.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class InstructionSchemes extends AppCompatActivity {

    private static ArrayAdapter<String> adapter;
    private ArrayList<String> data = new ArrayList<String>();
    private static List<InstructionPlan> instructionPlans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction_schemes);
        System.out.println(1);
        instructionPlans = DataSupport.findAll(InstructionPlan.class);
        int planNum = instructionPlans.size();
        System.out.println(2);
        if (planNum>0) {
            for (int i = 0; i <= planNum - 1; i++) {
                InstructionPlan instructionPlan = instructionPlans.get(i);
                String planName = instructionPlan.getName();
                data.add(planName);
            }
        }
        System.out.println(3);
        adapter = new ArrayAdapter<String>(
                InstructionSchemes.this,android.R.layout.simple_list_item_1, data);
        System.out.println(4);
        ListView listView = (ListView) findViewById(R.id.plan_list);
        listView.setAdapter(adapter);
        System.out.println(5);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("a");
                String planNmae = data.get(i);
                System.out.println("b");System.out.println(i);System.out.println(data);System.out.println(instructionPlans.get(i).getName());
                ArrayList<String> instructionNames = instructionPlans.get(i).getInstructionNames();
                ArrayList<Integer> instructionOutputs = instructionPlans.get(i).getInstructionOutputs();
                System.out.println(instructionNames);System.out.println(instructionOutputs);
                Intent intent = new Intent(InstructionSchemes.this,EstablishNewPlan.class);
                intent.putExtra("order",i);
                intent.putExtra("planName",planNmae);
                intent.putExtra("InstructionNames",instructionNames);
                intent.putExtra("InstructionOutputs",instructionOutputs);
                startActivityForResult(intent,0);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.instructionscheme,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.add_plan:
                Intent intent = new Intent(InstructionSchemes.this,EstablishNewPlan.class);
                intent.putExtra("order",-1);
                System.out.println(6);
                startActivityForResult(intent,0);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void  onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 0:
                finish();
        }
    }
}
