package com.xv.serialport.processor.sensordata;

public class WifiDataProcessor {

    public String getThreeAxisAcceleration_X(int[] data) {
        System.out.println("X:"+data[10]);
        return "X:"+data[10];
    }

    public String getThreeAxisAcceleration_Y(int[] data) {
        System.out.println("Y:"+data[11]);
        return "Y:"+data[11];
    }

    public String getThreeAxisAcceleration_Z(int[] data) {
        System.out.println("Z:"+data[12]);
        return "Z:"+data[12];
    }

    public String getShake(int[] data) {
        String statu = "无震动";
        if(data[10]==1) {
            statu = "有震动";
        }
        System.out.println(statu);
        return statu;
    }
}
