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
    public void showUsersView() {
        animator.animateNavigationToUsersView(navigator::navigateToUsersView);
    }

    @Override
    public void showError(Error error) {
        switch (error) {
            case NETWORK_IS_NOT_AVAILABLE:
                showErrorMessageFromStringRes(R.string.error_network_is_not_avaliable);
                break;
            case CONNECTION_FAILED:
                showErrorMessageFromStringRes(R.string.error_connection_failed);
                break;
            case EMPTY_LOGIN:
                highlightEditTextWithError(etLogin);
                showErrorMessageFromStringRes(R.string.error_empty_field);
                break;
            case EMPTY_PASSWORD:
                highlightEditTextWithError(etPassword);
                showErrorMessageFromStringRes(R.string.error_empty_field);
                break;
            case EMPTY_PASSWORD_CONFIRMATION:
                highlightEditTextWithError(etPasswordConfirmation);
                showErrorMessageFromStringRes(R.string.error_empty_field);
                break;
            case ILLEGAL_CHARACTERS_IN_LOGIN:
                highlightEditTextWithError(etLogin);
                showErrorMessageFromStringRes(R.string.error_illegal_characters);
                break;
            case ILLEGAL_CHARACTERS_IN_PASSWORD:
                highlightEditTextWithError(etPassword);
                showErrorMessageFromStringRes(R.string.error_illegal_characters);
                break;
            case ILLEGAL_CHARACTERS_IN_PASSWORD_CONFIRMATION:
                highlightEditTextWithError(etPasswordConfirmation);
                showErrorMessageFromStringRes(R.string.error_illegal_characters);
                break;
            case INCORRECT_PASSWORD_CONFIRMATION:
                highlightEditTextWithError(etPasswordConfirmation);
                showErrorMessageFromStringRes(R.string.error_incorrect_password_confirmation);
                break;
            case ACCOUNT_ALREADY_EXISTS:
                tvError.setText(getString(R.string.error_account_already_exists));
                break;
        }

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

    private void showErrorMessageFromStringRes(@StringRes int resId) {
        tvError.setText(getString(resId));
    }
}
