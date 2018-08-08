package com.tianlin.myapplication;

import java.util.ArrayList;

public class RecognizeManager {
    public static final int AUDIO_MODE = 0;
    public static final int BUTTON_MODE = 1;
    public static final int WRITE_MODE = 0;
    
    public static String instruction;
    public static ArrayList<String> instructions = new ArrayList<String>();
    public static ArrayList<Integer> outputs = new ArrayList<Integer>();
    public static int socketNum;
    public static String ipNum;
    public static ArrayList<String> planNameList = new ArrayList<String>();
    public static int mode = AUDIO_MODE;
}
