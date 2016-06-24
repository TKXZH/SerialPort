package com.xv.serialport.processor.sensordata;


import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Ipv6Processor {

    public String getBodyStatu(int data[]) {
        String statu;
        if(data[26]==0x0) {
            statu = "无人";
            System.out.println(statu);
        }

        else {
            statu = "有人";
            System.out.println(statu);
        }
        return statu;
    }

    public String getRainStatu(int data[]) {
        String statu;
        if(data[26]==0x9) {
            statu = "无雨水";
            System.out.println(statu);
        }

        else {
            statu = "有雨水";
            System.out.println(statu);
        }
        return statu;
    }

    public String getPressure(int data[]) {


        System.out.println("压力为:"+data[26]);
        return "压力为:"+data[26];
    }
}
