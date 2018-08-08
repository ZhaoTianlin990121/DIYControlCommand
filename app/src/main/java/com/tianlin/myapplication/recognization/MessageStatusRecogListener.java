package com.tianlin.myapplication.recognization;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.tianlin.myapplication.RecognizeManager;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by fujiayi on 2017/6/16.
 */

public class MessageStatusRecogListener extends StatusRecogListener {

    private DatagramSocket socket;

    private Handler handler;

    private long speechEndTime;

    private boolean needTime = true;

    private static final String TAG = "MesStatusRecogListener";

    public MessageStatusRecogListener(Handler handler) {
        this.handler = handler;
    }


    @Override
    public void onAsrReady() {
        super.onAsrReady();
        sendStatusMessage("引擎就绪，可以开始说话。");
    }

    @Override
    public void onAsrBegin() {
        super.onAsrBegin();
        sendStatusMessage("检测到用户说话");
    }

    @Override
    public void onAsrEnd() {
        super.onAsrEnd();
        speechEndTime = System.currentTimeMillis();
        sendMessage("检测到用户说话结束");
    }

    @Override
    public void onAsrPartialResult(String[] results, RecogResult recogResult) {
        sendStatusMessage("临时识别结果，结果是“" + results[0] + "”；原始json：" + recogResult.getOrigalJson());
        super.onAsrPartialResult(results, recogResult);
    }

    @Override
    public void onAsrFinalResult(String[] results, RecogResult recogResult) {
        super.onAsrFinalResult(results, recogResult);
        String message = "识别结束，结果是”" + results[0] + "”";
        sendStatusMessage(message + "“；原始json：" + recogResult.getOrigalJson());
        if (speechEndTime > 0) {
            long diffTime = System.currentTimeMillis() - speechEndTime;
            message += "；说话结束到识别结束耗时【" + diffTime + "ms】";

        }
        speechEndTime = 0;
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RecognizeManager.instruction = results[0];
        int numInst = RecognizeManager.instructions.indexOf(RecognizeManager.instruction);
        if (numInst != -1) {
            sendInstruction(RecognizeManager.outputs.get(numInst).toString());
            message = message + "\n发送指令："+RecognizeManager.outputs.get(numInst).toString();
            message = message + "\n给端口："+RecognizeManager.socketNum;
        } else {
            message = message + "\n不在既定指令中";
        }
        sendMessage(message, status, true);
    }

    @Override
    public void onAsrFinishError(int errorCode, int subErrorCode, String errorMessage, String descMessage,
                                 RecogResult recogResult) {
        super.onAsrFinishError(errorCode, subErrorCode, errorMessage, descMessage, recogResult);
        String message = "识别错误, 错误码：" + errorCode + " ," + subErrorCode + " ; " + descMessage;
        sendStatusMessage(message + "；错误消息:" + errorMessage + "；描述信息：" + descMessage);
        if (speechEndTime > 0) {
            long diffTime = System.currentTimeMillis() - speechEndTime;
            message += "。说话结束到识别结束耗时【" + diffTime + "ms】";
        }
        speechEndTime = 0;
        sendMessage(message, status, true);
        speechEndTime = 0;
    }

    @Override
    public void onAsrOnlineNluResult(String nluResult) {
        super.onAsrOnlineNluResult(nluResult);
        if (!nluResult.isEmpty()) {
            sendStatusMessage("原始语义识别结果json：" + nluResult);
        }
    }

    @Override
    public void onAsrFinish(RecogResult recogResult) {
        super.onAsrFinish(recogResult);
        sendStatusMessage("识别一段话结束。如果是长语音的情况会继续识别下段话。");

    }

    /**
     * 长语音识别结束
     */
    @Override
    public void onAsrLongFinish() {
        super.onAsrLongFinish();
        sendStatusMessage("长语音识别结束。");
    }


    /**
     * 使用离线命令词时，有该回调说明离线语法资源加载成功
     */
    @Override
    public void onOfflineLoaded() {
        sendStatusMessage("【重要】asr.loaded：离线资源加载成功。没有此回调可能离线语法功能不能使用。");
    }

    /**
     * 使用离线命令词时，有该回调说明离线语法资源加载成功
     */
    @Override
    public void onOfflineUnLoaded() {
        sendStatusMessage(" 离线资源卸载成功。");
    }

    @Override
    public void onAsrExit() {
        super.onAsrExit();
        sendStatusMessage("识别引擎结束并空闲中");
    }

    private void sendStatusMessage(String message) {
        sendMessage(message, status);
    }

    private void sendMessage(String message) {
        sendMessage(message, WHAT_MESSAGE_STATUS);
    }

    private void sendMessage(String message, int what) {
        sendMessage(message, what, false);
    }


    private void sendMessage(String message, int what, boolean highlight) {


        if (needTime && what != STATUS_FINISHED) {
            message += "  ;time=" + System.currentTimeMillis();
        }
        if (handler == null){
            Log.i(TAG, message );
            return;
        }
        Message msg = Message.obtain();
        msg.what = what;
        msg.arg1 = status;
        if (highlight) {
            msg.arg2 = 1;
        }
        msg.obj = message + "\n";
        handler.sendMessage(msg);
    }

    private void sendInstruction(String str) {
        try {
            // 首先创建一个DatagramSocket对象

            // 创建一个InetAddree
            InetAddress serverAddress = InetAddress.getByName(RecognizeManager.ipNum);
            byte data[] = str.getBytes();
            // 创建一个DatagramPacket对象，并指定要讲这个数据包发送到网络当中的哪个地址，以及端口号
            DatagramPacket packet = new DatagramPacket(data, data.length,
                    serverAddress, RecognizeManager.socketNum);
            // 调用socket对象的send方法，发送数据
            socket.send(packet);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
