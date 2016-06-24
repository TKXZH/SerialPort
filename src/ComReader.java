import com.sun.comm.Win32Driver;

import javax.comm.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

public class ComReader {
    static CommPortIdentifier portId;


    public static void main(String args[]) {
        InputStream is;
        CommPort serialPort;
        Win32Driver w32Driver = new Win32Driver();
        w32Driver.initialize();
        try {
            portId = CommPortIdentifier.getPortIdentifier("COM3");
        } catch (NoSuchPortException e) {
            e.printStackTrace();
        }
    }
}


//纠正一些问题


