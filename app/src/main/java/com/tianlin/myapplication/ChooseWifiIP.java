package com.tianlin.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class ChooseWifiIP extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_wifi_ip);
        findIP();
    }

    public void findIP(){
        String strHost = null;
        ArrayList<String> reachableIps = new ArrayList<String>();
        try {

            //strHost = InetAddress.getLocalHost().getHostAddress();
            //获取wifi服务
            WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            //判断wifi是否开启
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            strHost = intToIp(ipAddress);
            System.out.println("dahsdjfhak");
            System.out.println(strHost);

            reachableIps.add(strHost);
//            int strPort = Integer.parseInt(getResources().getString(R.string.default_socket));
//            System.out.println("===========" + strPort);
//            reachableIps = getReachableIP(strHost, strPort, 3000);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //找到所有ip
        final LinearLayout ipListLy = (LinearLayout) findViewById(R.id.ipList);

        //获取linearlayout子view的个数
        int count = ipListLy.getChildCount();
        //研究整个LAYOUT布局，第0位的是含add和remove两个button的layout
        //第count-1个是那个文字被置中的textview
        //因此，在remove的时候，只能操作的是0<location<count-1这个范围的
        //在执行每次remove时，我们从count-2的位置即textview上面的那个控件开始删除~
        while (count  > 0) {
            //count-2>0用来判断当前linearlayout子view数多于2个，即还有我们点add增加的button
            ipListLy.removeViewAt(count - 1);
            count--;
        }

        for (int i = 0; i < reachableIps.size(); i++) {
            String ipTmp = reachableIps.get(i);
            final Button btnTmp = new Button(ChooseWifiIP.this);
            btnTmp.setText(ipTmp);
            btnTmp.setTag(ipTmp);
            btnTmp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent get = getIntent();
                    String planName = get.getStringExtra("ChosenPlan");
                    if (RecognizeManager.mode==RecognizeManager.AUDIO_MODE) {
                        Intent intent = new Intent(ChooseWifiIP.this, ActivityOffline.class);
                        intent.putExtra("ChosenPlan", planName);
                        RecognizeManager.ipNum = String.valueOf(v.getTag());
                        EditText editText = (EditText) findViewById(R.id.socket_edit);
                        editText.setInputType(EditorInfo.TYPE_CLASS_PHONE);
                        editText.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                        System.out.println("sjkfdasjkfhjkah");
                        System.out.println(editText.getText().toString());
                        try {
                            int socketNumber = Integer.parseInt(editText.getText().toString());
                            RecognizeManager.socketNum = socketNumber;
                            ChooseWifiIP.this.startActivity(intent);
                            ChooseWifiIP.this.finish();
                        } catch (Exception e) {
                            Toast.makeText(ChooseWifiIP.this, "请输入正确的端口号", Toast.LENGTH_SHORT).show();
                        }
                    } else if (RecognizeManager.mode==RecognizeManager.BUTTON_MODE) {
                        Toast.makeText(ChooseWifiIP.this, "按钮模式即将上线！请先使用语音模式", Toast.LENGTH_SHORT).show();
                    } else if (RecognizeManager.mode==RecognizeManager.WRITE_MODE) {
                        Toast.makeText(ChooseWifiIP.this, "键盘输入模式即将上线！请先使用语音模式", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            ipListLy.addView(btnTmp);
        }
    }

    private ArrayList<String> getReachableIP(String ip, int port, int timeout) throws InterruptedException{
        String ipHead = ip.substring(0, ip.lastIndexOf(".") + 1);
        CountDownLatch threadSignal = new CountDownLatch(254);//初始化countDown
        SensorThread.ipReachableList.clear();
        for (int i = 1; i < 255; i++) {
            String ipTmp = ipHead + i;
            Thread sensorThread = new SensorThread(threadSignal,ipTmp,port);
            sensorThread.start();
        }
        threadSignal.await();
        return SensorThread.getIpReachableList();
    }

    private String intToIp(int i) {
        return (i & 0xFF ) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ( i >> 24 & 0xFF) ;
    }
}
