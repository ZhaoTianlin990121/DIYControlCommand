package com.tianlin.myapplication;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

public class InstructionPlan extends DataSupport{

    private String name;

    private ArrayList<String> instructionNames = new ArrayList<String>();

    private ArrayList<Integer> instructionOutputs = new ArrayList<Integer>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Integer> getInstructionOutputs() {
        return instructionOutputs;
    }

    public void setInstructionOutputs(ArrayList<Integer> instructionOutput) {
        this.instructionOutputs = instructionOutput;
    }

    public ArrayList<String> getInstructionNames() {
        return instructionNames;
    }

    public void setInstructionNames(ArrayList<String> instructionName) {
        this.instructionNames = instructionName;
    }
}
