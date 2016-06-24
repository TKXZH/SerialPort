package com.xv.serialport.processor.sensordata;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class BlueToothProcessor {


    public String getTemperature(int data[]) {
        System.out.print("温度为："+data[10]);
        return "温度为："+data[10];

    }
    public String getHumidity(int data[]) {
        System.out.println("湿度为："+data[11]+"   ");
        return "湿度为："+data[11];
    }

    public String getIlluminance(int data[]) {
        System.out.print("光照为："+data[10]+"   ");
        System.out.println();

        return "光照为："+data[10]+"   ";
    }

    public String getSmog(int data[]) {
        System.out.println("烟雾浓度: "+data[10]);
        return "烟雾浓度: "+data[10];
    }

}
