package com.nodomain.manyface.ui.fragments;


import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nodomain.manyface.R;
import com.nodomain.manyface.domain.Error;
import com.nodomain.manyface.mvp.presenters.SignUpMvpPresenter;
import com.nodomain.manyface.mvp.views.SignUpMvpView;
import com.nodomain.manyface.navigation.SignUpNavigator;
import com.nodomain.manyface.ui.activities.AuthorizationActivity;
import com.nodomain.manyface.ui.animators.SignInAnimator;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class SignUpFragment extends BaseFragment<SignUpMvpPresenter> implements SignUpMvpView {

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @BindView(R.id.til_login)
    TextInputLayout tilLogin;
    @BindView(R.id.et_login)
    EditText etLogin;
    @BindView(R.id.til_password)
    TextInputLayout tilPassword;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.til_password_confirmation)
    TextInputLayout tilPasswordConfirmation;
    @BindView(R.id.et_password_confirmation)
    EditText etPasswordConfirmation;
    @BindView(R.id.pb_sign_up)
    ProgressBar pbSignUp;
    @BindView(R.id.tv_error)
    TextView tvError;

    @Inject
    SignUpNavigator navigator;
    @Inject
    SignInAnimator animator;

    private int errorColor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        AuthorizationActivity.getActivitySubComponent(getActivity()).inject(this);
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initErrorColor();
        animator.bind(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        animator.unbind();
    }

    @Override
    public void showProfilesView() {
        animator.animateNavigationToUsersView(navigator::navigateToProfilesView);
    }

    @Override
    public void showError(Error error) {
        switch (error) {
            case EMPTY_LOGIN:
            case ILLEGAL_CHARACTERS_IN_LOGIN:
                highlightEditTextWithError(etLogin);
                break;
            case EMPTY_PASSWORD:
            case ILLEGAL_CHARACTERS_IN_PASSWORD:
                highlightEditTextWithError(etPassword);
                break;
            case EMPTY_PASSWORD_CONFIRMATION:
            case ILLEGAL_CHARACTERS_IN_PASSWORD_CONFIRMATION:
            case INCORRECT_PASSWORD_CONFIRMATION:
                highlightEditTextWithError(etPasswordConfirmation);
                break;
        }

        String errorMessage = getErrorMessage(error);
        tvError.setText(errorMessage);
        animator.animateErrorAppearance();
    }

    @Override
    public void showSignUpProgress() {
        tvError.setVisibility(View.GONE);
        pbSignUp.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSignUpProgress() {
        pbSignUp.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.btn_sign_up)
    void onSignUpClick() {
        String login = etLogin.getText().toString();
        String password = etPassword.getText().toString();
        String passwordConfirmation = etPasswordConfirmation.getText().toString();
        mvpPresenter.signUp(login, password, passwordConfirmation);
    }

    private void initErrorColor() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            errorColor = getResources().getColor(R.color.accent);
        } else {
            errorColor = getResources().getColor(R.color.accent, getActivity().getTheme());
        }
    }

    private void highlightEditTextWithError(EditText et) {
        et.getBackground().setColorFilter(errorColor, PorterDuff.Mode.SRC_ATOP);
    }
}
