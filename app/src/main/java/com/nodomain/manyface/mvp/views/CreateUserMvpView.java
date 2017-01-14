package com.nodomain.manyface.mvp.views;


public interface CreateUserMvpView extends MvpView {

    void showSaveProgress();

    void hideSaveProgress();

    void showSaveSuccess();

    void showPreviousView();
}
