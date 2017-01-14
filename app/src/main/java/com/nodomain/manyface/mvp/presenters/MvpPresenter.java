package com.nodomain.manyface.mvp.presenters;


import com.nodomain.manyface.mvp.views.MvpView;


public interface MvpPresenter<V extends MvpView> {

    void attachMvpView(V mvpView);

    void detachMvpView();
}
