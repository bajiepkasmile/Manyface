package com.nodomain.manyface.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.nodomain.manyface.mvp.presenters.MvpPresenter;
import com.nodomain.manyface.mvp.views.MvpView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseDialogFragment<P extends MvpPresenter> extends DialogFragment implements MvpView {

    @Inject
    protected P mvpPresenter;

    private Unbinder unbinder;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        mvpPresenter.attachMvpView(this);
    }

    @Override
    public void onDestroyView() {
        mvpPresenter.detachMvpView();
        unbinder.unbind();
        super.onDestroyView();
    }
}
