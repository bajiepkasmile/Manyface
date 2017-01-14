package com.nodomain.manyface.domain.interactors.base;


import android.os.Handler;


public class BaseSingleTaskInteractor extends BaseInteractor {

    private Thread thread = new Thread();

    protected BaseSingleTaskInteractor(Handler mainThreadHandler) {
        super(mainThreadHandler);
    }

    @Override
    protected void runInBackground(Runnable runnable) {
        if (!thread.isAlive()) {
            thread = new Thread(runnable);
            thread.start();
        }
    }
}
