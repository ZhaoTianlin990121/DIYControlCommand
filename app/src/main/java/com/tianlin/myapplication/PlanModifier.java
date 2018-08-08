package com.tianlin.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;


public class PlanModifier extends AppCompatActivity {

    private EditText editText_name;
    private EditText editText_output;
    private String name;
    private String output;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_modifier);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        output = intent.getStringExtra("output");
        editText_name = (EditText) findViewById(R.id.instr_name);
        editText_output = (EditText) findViewById(R.id.output);
        editText_name.setText(name);editText_output.setText(output);
        editText_output.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        editText_output.setKeyListener(DigitsKeyListener.getInstance("0123456789"));

        //OK 按钮
        Button set = (Button) findViewById(R.id.okay);
        set.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getIntent = getIntent();
                ArrayList<String> existingNames = getIntent.getStringArrayListExtra("existingNames");
                ArrayList<Integer> existingOutputs = getIntent.getIntegerArrayListExtra("existingOutputs");
                try {
                    existingNames.remove(name);
                    existingOutputs.remove(output);
                } catch (Exception e){
                    e.printStackTrace();
                }
                if (existingNames!=null) {
                    if (existingNames.contains(editText_name.getText().toString()) || editText_name == null || editText_name.equals("")) {
                        Toast.makeText(PlanModifier.this, "无效的指令名，请重新输入", Toast.LENGTH_SHORT).show();
                    } else if (existingOutputs.contains(editText_output.getText().toString()) || editText_output == null || editText_output.equals("")) {
                        Toast.makeText(PlanModifier.this, "无效的指令名，请重新输入", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intentback = new Intent();
                        intentback.putExtra("name", editText_name.getText().toString());
                        intentback.putExtra("output", editText_output.getText().toString());
                        setResult(RESULT_OK, intentback);
                        finish();
                    }
                } else {
                    if (editText_name == null || editText_name.equals("")) {
                        Toast.makeText(PlanModifier.this, "无效的指令名，请重新输入", Toast.LENGTH_SHORT).show();
                    } else if (editText_output == null || editText_output.equals("")) {
                        Toast.makeText(PlanModifier.this, "无效的指令名，请重新输入", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intentback = new Intent();
                        intentback.putExtra("name", editText_name.getText().toString());
                        intentback.putExtra("output", editText_output.getText().toString());
                        setResult(RESULT_OK, intentback);
                        finish();
                    }
                }
            }
        });

        //删除 按钮
        Button remove = (Button) findViewById(R.id.remove);
        remove.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(4);
                Intent intentrem = new Intent();
                System.out.println(5);
                intentrem.putExtra("name",editText_name.getText().toString());
                intentrem.putExtra("output",editText_output.getText().toString());
                setResult(RESULT_CANCELED, intentrem);
                System.out.println(6);
                finish();
            }
        });
    }
}
