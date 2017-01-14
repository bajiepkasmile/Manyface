package com.nodomain.manyface.mvp.presenters.impl;


import com.nodomain.manyface.mvp.presenters.MvpPresenter;
import com.nodomain.manyface.mvp.views.MvpView;

import org.greenrobot.eventbus.EventBus;


public class BaseMvpPresenterImpl<V extends MvpView> implements MvpPresenter<V> {

    protected V mvpView;

    protected BaseMvpPresenterImpl() {
    }

    @Override
    public void attachMvpView(V mvpView) {
        this.mvpView = mvpView;
        EventBus.getDefault().register(this);
    }

    @Override
    public void detachMvpView() {
        EventBus.getDefault().unregister(this);
        mvpView = null;
    }
}
