package com.xv. serialport;
import javax.comm.*;
import java.util.*;
import com.sun.comm.*;

public class ComList {
    public static void main(String[] args) {
        int i = 0;

        Win32Driver w32Driver = new Win32Driver();
        // 这两行修正javax.comm的BUG  

        w32Driver.initialize();
        // 这才正常初始化，才会正常找出所有port  

        Enumeration e = CommPortIdentifier.getPortIdentifiers();
        while (e.hasMoreElements()) {
            i++;
            // 打出所有port的名字  
            System.out.println(((CommPortIdentifier) e.nextElement()).getName());

        }
        System.out.println(i);
    }

}  