package com.xv.serialport.transceiver;
import com.xv.serialport.socket.Transmission;
import java.io.*;
import java.util.*;
import javax.comm.*;

public class SerialPort implements Runnable, SerialPortEventListener {
    /*socket消息收发器*/
    private Transmission ts = new Transmission();

    /*获取串口相关组件*/
    static CommPortIdentifier portId;
    static Enumeration portList;
    private InputStream inputStream;
    private OutputStream outputStream;
    private DataInputStream dis;
    private javax.comm.SerialPort serialPort;
    Thread readThread;

    public static void main(String[] args) {

        portList = CommPortIdentifier.getPortIdentifiers();

        /*获取指定名称的串口*/
        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (portId.getName().equals("COM4")) {
                    SerialPort reader = new SerialPort();
                }
            }
        }
    }

    public void startSendSensorMessage() {

    }

    public void startSendControlMessage() {

    }
    public SerialPort() {
        ts.startControl();
        try {
            serialPort = (javax.comm.SerialPort) portId.open("SimpleReadApp", 2000);
        } catch (PortInUseException e) {}
        try {
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
            dis = new DataInputStream(inputStream);

        } catch (IOException e) {}
	try {
            serialPort.addEventListener(this);
	} catch (TooManyListenersException e) {}
        serialPort.notifyOnDataAvailable(true);
        try {
            serialPort.setSerialPortParams(115200,
                javax.comm.SerialPort.DATABITS_8,
                javax.comm.SerialPort.STOPBITS_1,
                javax.comm.SerialPort.PARITY_NONE);
        } catch (UnsupportedCommOperationException e) {}
        readThread = new Thread(this);
        readThread.start();
    }

    public void run() {
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {}
    }

    public void serialEvent(SerialPortEvent event) {
        switch(event.getEventType()) {
        case SerialPortEvent.BI:
        case SerialPortEvent.OE:
        case SerialPortEvent.FE:
        case SerialPortEvent.PE:
        case SerialPortEvent.CD:
        case SerialPortEvent.CTS:
        case SerialPortEvent.DSR:
        case SerialPortEvent.RI:
        case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
            break;
        case SerialPortEvent.DATA_AVAILABLE:
            byte[] readBuffer = new byte[20];

            if(ts.getControlMessage1()!=null) {
                try {
                    outputStream.write(ts.getControlMessage1());
                    ts.resetControlMessage1();
                }   catch (IOException e) {
                    e.printStackTrace();
                    }
            }

            if(ts.getControlMessage2()!=null) {
                try {
                    outputStream.write(ts.getControlMessage2());
                    ts.resetControlMessage2();
                }   catch (IOException e) {
                    e.printStackTrace();
                    }
            }
            /*开始传感器信息监测和传输*/
            ts.sendMessage(dis);
        }
    }

}
