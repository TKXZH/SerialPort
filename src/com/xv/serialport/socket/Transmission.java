package com.xv.serialport.socket;
import com.xv.serialport.processor.controldata.BuzzerDataProcessor;
import com.xv.serialport.processor.controldata.LedProcessor;
import com.xv.serialport.processor.sensordata.WifiDataProcessor;
import com.xv.serialport.processor.sensordata.BlueToothProcessor;
import com.xv.serialport.processor.sensordata.Ipv6Processor;
import java.io.*;
import java.net.Socket;

public class Transmission {
    /*Socket及输入输出流*/
    private Socket socket;
    private PrintStream ps;
    private BufferedReader br;

    private byte controlMessage1[] = null;
    private byte controlMessage2[] = null;

    /*控制数据处理器*/
    private LedProcessor ledprocessor = new LedProcessor();
    private BuzzerDataProcessor buzzerDataProcessor = new BuzzerDataProcessor();

    /*传感器信息处理*/
    private WifiDataProcessor wifiDataProcessor = new WifiDataProcessor();
    private Ipv6Processor ipv6Processor  = new Ipv6Processor();
    private BlueToothProcessor blueToothProcessor = new BlueToothProcessor();

    public Transmission() {
        try {
            /*初始化Socket相关信息*/
            socket = new Socket("192.168.1.192",8085);
            ps = new PrintStream(socket.getOutputStream());
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getControlMessage1() {
        return controlMessage1;
    }

    public byte[] getControlMessage2() {
        return controlMessage2;
    }

    /*重置控制信息*/
    public void resetControlMessage1() {
        this.controlMessage1 = null;
    }

    public void resetControlMessage2() {
        this.controlMessage2 = null;
    }

    /*向手机端发送传感器数据*/
    public void sendMessage(String data) {
        ps.println(data);
    }
    /*检测并分类数据包，向手机端发送数据*/
    public void sendMessage(DataInputStream dis) {
        try {
            dis.readUnsignedByte();
            switch (dis.readUnsignedByte()) {
                case 0x0B:
                    int Byte2 = dis.readUnsignedByte();
                    int packageDataArray[] = new int[14];
                    packageDataArray[0] = 2;
                    packageDataArray[1] = 0x0B;
                    packageDataArray[2] = Byte2;
                    if(Byte2==0xBA) {
                        for(int i=3; i<packageDataArray.length; i++) {
                            packageDataArray[i] = dis.readUnsignedByte();
                        }

                        switch (packageDataArray[9]) {
                            case 0x07:
                                ps.println(wifiDataProcessor.getThreeAxisAcceleration_X(packageDataArray));
                                ps.println(wifiDataProcessor.getThreeAxisAcceleration_Y(packageDataArray));
                                ps.println(wifiDataProcessor.getThreeAxisAcceleration_Z(packageDataArray));
                                break;
                            case 0x03:
                                ps.println(wifiDataProcessor.getShake(packageDataArray));

                        }


                    }
                    if(Byte2 ==0xBB) {
                        for(int i=3; i<packageDataArray.length; i++) {
                            packageDataArray[i] = dis.readUnsignedByte();
                        }

                        switch (packageDataArray[9]) {
                            case 0x00:
                                ps.println(blueToothProcessor.getTemperature(packageDataArray));
                                ps.println(blueToothProcessor.getHumidity(packageDataArray));
                                break;
                            case 0x02:
                                ps.println(blueToothProcessor.getIlluminance(packageDataArray));
                                break;
                            case 0x04:
                                ps.println(blueToothProcessor.getSmog(packageDataArray));
                        }
                    }
                    break;

                case 0x0A:
                    break;
                case 0x19:
                    int Byte_2 = dis.readUnsignedByte();
                    int DataArray[] = new int[28];
                    DataArray[0] = 2;
                    DataArray[1] = 0x19;
                    DataArray[2] = Byte_2;
                    for(int i=3; i<DataArray.length; i++) {
                        DataArray[i] = dis.readUnsignedByte();
                    }

                    if(DataArray[25]==0x01) {
                        ps.println(ipv6Processor.getBodyStatu(DataArray));
                        break;
                    }

                    if(DataArray[25]==0x09) {
                        ps.println(ipv6Processor.getRainStatu(DataArray));
                        break;
                    }

                    if(DataArray[25]==0x0E) {
                        ps.println(ipv6Processor.getPressure(DataArray));
                        break;
                    }
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*开启socket控制信息监听线程*/
    public void startControl() {
        Listener l = new Listener();
        new Thread(l).start();
    }

    /*socket控制信息识别*/
    public class Listener implements Runnable {

        public void run() {
            while (true) {
                try {
                    String message = br.readLine();

                    if(message.startsWith("蜂鸣器")) {
                        message = message.trim();
                        String str2="";
                        if(message != null && !"".equals(message)) {
                            for (int i = 0; i < message.length(); i++) {
                                if (message.charAt(i) >= 48 && message.charAt(i) <= 57) {
                                    str2 += message.charAt(i);
                                }
                            }
                            System.out.println("蜂鸣器"+str2);
                            if(str2.equals("1")) {
                                controlMessage1 = buzzerDataProcessor.getBuzzerData();
                            }

                            System.out.println("蜂鸣器");
                        }
                    }

                    else if(message.startsWith("数码管")) {
                        message = message.trim();
                        String str3="";
                        if(message != null && !"".equals(message)) {
                            for (int i = 0; i < message.length(); i++) {
                                if (message.charAt(i) >= 48 && message.charAt(i) <= 57) {
                                    str3 += message.charAt(i);
                                }
                            }

                            int tag = Integer.parseInt(str3);
                            controlMessage2 = ledprocessor.getLEDData(tag);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
