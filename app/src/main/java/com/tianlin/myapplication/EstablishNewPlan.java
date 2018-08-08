package com.tianlin.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

public class EstablishNewPlan extends AppCompatActivity {

    private static ArrayAdapter<String> adapter;
    private ArrayList<String> instructionNames = new ArrayList<String>();
    private ArrayList<Integer> instructionOutputs = new ArrayList<Integer>();
    private EditText planName;
    private static ListView listView;
    private int judge=0;
    private String theplanName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("h");
        super.onCreate(savedInstanceState);
        System.out.println("r");
        setContentView(R.layout.activity_establish_new_plan);
        System.out.println("w");
        Intent getIntent = getIntent();
        System.out.println("y");
        judge = getIntent.getIntExtra("order",-1);
        theplanName = getIntent.getStringExtra("planName");
        System.out.println(theplanName);
        instructionNames = getIntent.getStringArrayListExtra("InstructionNames");
        System.out.println(7.2);
        instructionOutputs = getIntent.getIntegerArrayListExtra("InstructionOutputs");
        System.out.println(7.3);
        System.out.println(instructionNames);System.out.println(instructionOutputs);
        System.out.println(7.4);
        if (instructionNames!=null) {
            adapter = new ArrayAdapter<String>(
                    EstablishNewPlan.this, android.R.layout.simple_list_item_1, instructionNames);
        } else {
            adapter = new ArrayAdapter<String>(
                    EstablishNewPlan.this, android.R.layout.simple_list_item_1, new ArrayList<String>());
        }
        System.out.println(8);
        System.out.println(9);
        listView = (ListView) findViewById(R.id.instruction_list);
        listView.setAdapter(adapter);
        System.out.println(10);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String inst = instructionNames.get(i);
                int output = instructionOutputs.get(i);
                Intent intent = new Intent(EstablishNewPlan.this, PlanModifier.class);
                intent.putExtra("existingNames",instructionNames);
                intent.putExtra("existingOutputs",instructionOutputs);
                intent.putExtra("name",inst);
                intent.putExtra("output",Integer.toString(output));
                startActivityForResult(intent, 0);
            }
        });
        planName = (EditText) findViewById(R.id.name_plan);
        planName.setText(theplanName);
        System.out.println(11);
        Button destroyInstructions = (Button) findViewById(R.id.destroy_instructions);
        destroyInstructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(EstablishNewPlan.this);
                dialog.setTitle("删除指令集");
                dialog.setMessage("您确定要删除这条指令集吗");
                dialog.setCancelable(true);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removePlan();
                        finish();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
            }
        });
        Button returnInstructions = (Button) findViewById(R.id.return_instructions);
        System.out.println(12);
        returnInstructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InstructionPlan instructionPlan = new InstructionPlan();
                System.out.println(15);
                String inputText = planName.getText().toString();
                System.out.println(16);
                Intent intent = new Intent();
                intent.putExtra("planName", instructionPlan.getName());
                if ((RecognizeManager.planNameList.contains(inputText)&&!inputText.equals(theplanName))||inputText.equals("")||inputText.equals(null)) {
                    Toast.makeText(EstablishNewPlan.this,"无效的名字，请输入有效名称",Toast.LENGTH_SHORT).show();
                } else if (instructionNames.size()==0){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(EstablishNewPlan.this);
                    dialog.setTitle("指令集为空");
                    dialog.setMessage("此指令集没有元素，继续则删除该指令集");
                    dialog.setCancelable(true);
                    dialog.setPositiveButton("继续", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            removePlan();
                            finish();
                        }
                    });
                    dialog.setNegativeButton("返回", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dialog.show();
                } else {
                    if (judge != -1) {
                        System.out.println("dsafdha");
                        System.out.println(instructionNames);
                        instructionPlan.setInstructionNames(instructionNames);
                        instructionPlan.setInstructionOutputs(instructionOutputs);
                        instructionPlan.setName(inputText);
                        instructionPlan.updateAll("name = ?", theplanName);
                        System.out.println("Updated");
                        setResult(0, intent);
                    } else {
                        instructionPlan.setName(inputText);
                        instructionPlan.setInstructionNames(instructionNames);
                        instructionPlan.setInstructionOutputs(instructionOutputs);
                        instructionPlan.save();
                        setResult(1, intent);
                    }
                    System.out.println(17);
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.instructionscheme, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_plan:
                Intent intent = new Intent(EstablishNewPlan.this, PlanModifier.class);
                intent.putExtra("existingNames",instructionNames);
                intent.putExtra("existingOutputs",instructionOutputs);
                startActivityForResult(intent, 0);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                try {
                    String instructionName = data.getStringExtra("name");
                    Integer instructionOutput = Integer.parseInt(data.getStringExtra("output"));
                    System.out.println(instructionOutput);
                    System.out.println("wa");
                    System.out.println(instructionOutputs);
                    if (resultCode == RESULT_OK) {
                        System.out.println("a");
                        if (instructionNames!=null&&instructionNames.contains(instructionName)) {
                            System.out.println("b");
                            instructionOutputs.set(instructionNames.indexOf(instructionName), instructionOutput);
                        } else if (instructionNames==null){
                            instructionNames = new ArrayList<String>();instructionOutputs = new ArrayList<Integer>();
                            instructionNames.add(instructionName);
                            instructionOutputs.add(instructionOutput);
                            adapter = new ArrayAdapter<String>(EstablishNewPlan.this, android.R.layout.simple_list_item_1, instructionNames);
                            adapter.notifyDataSetChanged();
                            listView.setAdapter(adapter);
                        } else {
                            instructionNames.add(instructionName);
                            instructionOutputs.add(instructionOutput);
                            System.out.println("c");
                            adapter.add(instructionName);
                            adapter.remove(instructionName);
                            adapter.notifyDataSetChanged();
                            System.out.println("d");
                            System.out.println(instructionOutputs.size());
                            System.out.println(instructionNames);
                            System.out.println("ka");
                            System.out.println(instructionOutputs);
                        }
                    } else if (resultCode == RESULT_CANCELED) {
                        System.out.println("d");
                        instructionNames.remove(instructionName);
                        instructionOutputs.remove(instructionOutput);
                        adapter.notifyDataSetChanged();
                        adapter.remove(instructionName);
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }

        }
    }

    public void removePlan(){
        DataSupport.deleteAll(InstructionPlan.class,"name = ?", theplanName);
//        finish();
    }
}
