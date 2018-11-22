package com.afirez.lib.player;

public class One {
    static {
        System.loadLibrary("one");
        System.loadLibrary("avcodec-57");
        System.loadLibrary("avdevice-57");
        System.loadLibrary("avfilter-6");
        System.loadLibrary("avformat-57");
        System.loadLibrary("avutil-55");
        System.loadLibrary("postproc-54");
        System.loadLibrary("swresample-2");
        System.loadLibrary("swscale-4");
    }

    public native String helloWorld();

    public native String producerCustomer();

    public native String callbackFromC();

    public void onError(int code, String msg) {
        if (onErrorListener != null) {
            onErrorListener.onError(code, msg);
        }
    }

    private OnErrorListener onErrorListener;

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    public interface OnErrorListener {
        void onError(int code, String msg);
    }
}
