package com.nodomain.manyface.domain;


import android.icu.util.TimeUnit;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

public class CheckMessages {

    volatile boolean check = false;

    public static CheckMessages instance;

    public static CheckMessages getInstance() {
        if (instance == null ) {
            instance = new CheckMessages();
        }
        return instance;
    }

    public void start() {
        check = true;

        new Thread(() -> {
            while (check) {
                try {
                    synchronized (this) {
                        wait(2000);
                    }
                } catch (Exception e) {}

                EventBus.getDefault().postSticky(new OnTimerEvent());
            }
        }).start();
    }

    public void stop() {
        check = false;
    }

    public static class OnTimerEvent {

    }
}
