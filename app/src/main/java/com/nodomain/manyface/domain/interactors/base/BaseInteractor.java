package com.nodomain.manyface.domain.interactors.base;


import android.os.Handler;
import android.support.annotation.NonNull;

import com.nodomain.manyface.domain.Error;

import org.greenrobot.eventbus.EventBus;


public abstract class BaseInteractor {

    private final Handler mainThreadHandler;

    protected BaseInteractor(Handler mainThreadHandler) {
        this.mainThreadHandler = mainThreadHandler;
    }

    protected abstract void runInBackground(Runnable runnable);

    protected void postOnMainThread(Runnable runnable) {
        mainThreadHandler.post(runnable);
    }

    protected void postEvent(@NonNull Object event) {
        EventBus.getDefault().postSticky(event);
    }

    public static class BaseFailureEvent {

        private Error error;

        protected BaseFailureEvent(Error error) {
            this.error = error;
        }

        public Error getError() {
            return error;
        }
    }
}
