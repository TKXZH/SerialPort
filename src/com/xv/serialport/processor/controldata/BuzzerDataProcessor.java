package com.xv.serialport.processor.controldata;

/**
 * Created by Administrator on 2016/6/24.
 */
public class BuzzerDataProcessor {
    private byte controlData[];
    public BuzzerDataProcessor() {
        this.controlData = new byte[]{0x02,0x0B,(byte)0xBA,(byte)0x45,0x05,0x00,0x03,0x00,0x00,0x10,0x02,0x00,0x32,(byte)0xD2};
    }

    public byte[] getBuzzerData() {
        return this.controlData;
    }
}
