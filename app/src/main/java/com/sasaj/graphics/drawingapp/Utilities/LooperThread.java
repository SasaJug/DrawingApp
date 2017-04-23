package com.sasaj.graphics.drawingapp.Utilities;

/**
 * Created by DS on 4/22/2017.
 */


import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class LooperThread extends Thread {
    public Handler handler;
    private final Object handlerSyncObject = new Object();

    public LooperThread(final String threadName) {
        setName(threadName);
        start();
    }

    private void sendMessage(Message msg, int delay) {
        if (handler == null) {
            try {
                synchronized (handlerSyncObject) {
                    handlerSyncObject.wait();
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        if (handler != null) {
            if (delay <= 0) {
                handler.sendMessage(msg);
            } else {
                handler.sendMessageDelayed(msg, delay);
            }
        }
    }

    public void postRunnable(Runnable runnable) {
        postRunnable(runnable, 0);
    }

    public void postRunnable(Runnable runnable, int delay) {
        if (handler == null) {
            try {
                synchronized (handlerSyncObject) {
                    handlerSyncObject.wait();
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        if (handler != null) {
            if (delay <= 0) {
                handler.post(runnable);
            } else {
                handler.postDelayed(runnable, delay);
            }
        }
    }


    public void deleteAndPostRunnable(Runnable runnable, int delay) {
        if (handler == null) {
            try {
                synchronized (handlerSyncObject) {
                    handlerSyncObject.wait();
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        if (handler != null) {
            if (delay <= 0) {
                handler.post(runnable);
            } else {
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(runnable, delay);
            }
        }
    }

    public void run() {
        Looper.prepare();
        handler = new Handler();
        synchronized (handlerSyncObject) {
            handlerSyncObject.notify();
        }
        Looper.loop();
    }
}