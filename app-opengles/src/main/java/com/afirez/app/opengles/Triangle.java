package com.afirez.app.opengles;

public class Triangle {
    static {
        System.loadLibrary("triangle");
    }
    public static native boolean init();

    public static native void resize(int width, int height);

    public static native void draw();
}
