package com.sgorokh.TimeSaver.controllers.helpers;

public class ByteWrapper {

    public static Byte[] bytesToBytes(byte[] bytes) {
        Byte[] outputBytes = new Byte[bytes.length];
        int i = 0;
        for (byte b : bytes)
            outputBytes[i++] = b;
        return outputBytes;
    }

    public static byte[] bytesToBytes(Byte[] bytes) {
        byte[] outputBytes = new byte[bytes.length];
        int i = 0;
        for (Byte b : bytes)
            outputBytes[i++] = b;
        return outputBytes;
    }
}
