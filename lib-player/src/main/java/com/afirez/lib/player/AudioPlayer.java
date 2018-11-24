package com.afirez.lib.player;

import android.text.TextUtils;
import timber.log.Timber;

public class AudioPlayer {
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


    private String dataSource;
    private OnPreparedListener onPreparedListener;


    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public void setOnPreparedListener(OnPreparedListener onPreparedListener) {
        this.onPreparedListener = onPreparedListener;
    }

    public void prepare() {
        if (TextUtils.isEmpty(dataSource)) {
            Timber.w("dataSource == null");
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareN(dataSource);
            }
        }).start();
    }

    public void start() {
        if (TextUtils.isEmpty(dataSource)) {
            Timber.w("dataSource == null");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                startN();
            }
        }).start();
    }

    public native void prepareN(String url);

    public native void startN();

    public void onPrepared() {
        if (onPreparedListener != null) {
            onPreparedListener.onPrepared();
        }
    }

    public interface OnPreparedListener {
        void onPrepared();
    }
}
