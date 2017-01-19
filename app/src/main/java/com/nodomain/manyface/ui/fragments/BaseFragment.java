package com.nodomain.manyface.ui.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.nodomain.manyface.R;
import com.nodomain.manyface.domain.Error;
import com.nodomain.manyface.mvp.presenters.MvpPresenter;
import com.nodomain.manyface.mvp.views.MvpView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseFragment<P extends MvpPresenter> extends Fragment implements MvpView {

    @Nullable @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Inject
    protected P mvpPresenter;

    private Unbinder unbinder;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        if (toolbar != null) {
            setupActionBar();
            setHasOptionsMenu(true);
        }

        mvpPresenter.attachMvpView(this);
    }

    @Override
    public void onDestroyView() {
        mvpPresenter.detachMvpView();
        unbinder.unbind();
        super.onDestroyView();
    }

    private void setupActionBar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }

    protected ProgressDialog createProgressDialog(@StringRes int messageResId) {
        ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage(getString(messageResId));
        pd.setCancelable(false);
        pd.show();
        return pd;
    }

    protected Toast createToast(String errorMessage) {
        Toast toast = Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        return toast;
    }

    protected String getErrorMessage(Error error) {
        switch (error) {
            case NETWORK_IS_NOT_AVAILABLE:
                return getString(R.string.error_network_is_not_avaliable);
            case CONNECTION_FAILED:
                return getString(R.string.error_connection_failed);
            case EMPTY_LOGIN:
            case EMPTY_PASSWORD:
            case EMPTY_PASSWORD_CONFIRMATION:
            case EMPTY_PROFILE_NAME:
                return getString(R.string.error_empty_field);
            case ILLEGAL_CHARACTERS_IN_LOGIN:
            case ILLEGAL_CHARACTERS_IN_PASSWORD:
            case ILLEGAL_CHARACTERS_IN_PASSWORD_CONFIRMATION:
            case ILLEGAL_CHARACTERS_IN_PROFILE_NAME:
                return getString(R.string.error_illegal_characters);
            case INCORRECT_PASSWORD_CONFIRMATION:
                return getString(R.string.error_incorrect_password_confirmation);
            case ACCOUNT_ALREADY_EXISTS:
                return getString(R.string.error_account_already_exists);
            case PROFILE_ALREADY_EXISTS:
                return getString(R.string.error_profile_already_exists);
            case INCORRECT_LOGIN_AND_PASSWORD_COMBINATION:
                return getString(R.string.error_incorrect_login_and_password_combination);
            case TOO_MANY_PROFILES:
                return getString(R.string.error_too_many_profiles);
            default:
                return "";
        }
    }
}
