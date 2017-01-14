package com.nodomain.manyface.domain.interactors.base;


import android.os.Handler;

import java.util.concurrent.ExecutorService;


public class BaseQueueTasksInteractor extends BaseInteractor {

    private final ExecutorService executorService;

    protected BaseQueueTasksInteractor(Handler mainThreadHandler, ExecutorService executorService) {
        super(mainThreadHandler);
        this.executorService = executorService;
    }

    @Override
    protected void runInBackground(Runnable runnable) {
        executorService.execute(runnable);
    }
}
