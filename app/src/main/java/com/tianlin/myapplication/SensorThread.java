package com.tianlin.myapplication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Created by HHenry on 2016/5/29.
 */
public class SensorThread extends Thread {
    public static ArrayList<String> ipReachableList = new ArrayList<String>();

    private CountDownLatch threadsSignal;
    private String ip;
    private int port;

    public SensorThread(CountDownLatch threadsSignal, String ip, int port) {
        this.threadsSignal = threadsSignal;
        this.ip = ip;
        this.port = port;
    }
    public void run() {
        if(isReachable(ip,port)){
            System.out.println("可达："+ip);
            synchronized (this) {
                ipReachableList.add(ip);
            }
        }
        threadsSignal.countDown();// 线程结束时计数器减1
    }

    private boolean isReachable(String desIp, int desPort) {
        boolean isReachable = false;
        DatagramSocket ds = null; // 建立套间字udpsocket服务
        try {
            ds = new DatagramSocket(); // 实例化套间字，指定自己的port

            byte[] buf = "sensor:sensor".getBytes(); // 数据
            InetAddress destination = null;

            destination = InetAddress.getByName(desIp); // 需要发送的地址

            DatagramPacket dp = new DatagramPacket(buf, buf.length,
                    destination, desPort);

            ds.send(dp); // 发送数据

            byte[] buffer = new byte[100];
            DatagramPacket dpReceive = new DatagramPacket(buffer,
                    buffer.length);
            // 设置超时时间,2秒
            ds.setSoTimeout(2000);
            ds.receive(dpReceive);
            byte data[] = dpReceive.getData();// 接收的数据
            String recString = new String(data);
            System.out.println("接收的文本:::" + recString.trim());
            if (recString!=null && recString.trim().equals("sensorok")) {
                System.out.println("主机: " + desIp + " 可用");
                isReachable = true;
            }else {
                System.out.println("主机: " + desIp + " 不可用");
                isReachable = false;
            }
            return isReachable;
        } catch (SocketException e) {
            System.out.println("Cannot open port!");
            isReachable = false;
            return isReachable;
        } catch (UnknownHostException e) {
            System.out.println("Cannot open findhost!");
            isReachable = false;
            return isReachable;
        } catch (IOException e) {
            //System.out.println("TimeOut!");
            isReachable = false;
            return isReachable;
        } finally{
            ds.close();
        }
    }

    public static ArrayList<String> getIpReachableList(){
        return ipReachableList;
    }
}