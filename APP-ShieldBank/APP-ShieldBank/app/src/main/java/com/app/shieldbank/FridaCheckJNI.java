package com.app.shieldbank;

public class FridaCheckJNI {
    private static final String soName =  "frida-check";

    static
    {
        System.loadLibrary(soName);
    }

    public native int fridaCheck();
}
