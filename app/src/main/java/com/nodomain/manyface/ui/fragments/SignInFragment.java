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
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nodomain.manyface.R;
import com.nodomain.manyface.domain.Error;
import com.nodomain.manyface.mvp.presenters.SignInMvpPresenter;
import com.nodomain.manyface.mvp.views.SignInMvpView;
import com.nodomain.manyface.navigation.SignInNavigator;
import com.nodomain.manyface.ui.activities.AuthorizationActivity;
import com.nodomain.manyface.ui.animators.SignInAnimator;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class SignInFragment extends BaseFragment<SignInMvpPresenter> implements SignInMvpView {

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @BindView(R.id.til_login)
    TextInputLayout tilLogin;
    @BindView(R.id.et_login)
    EditText etLogin;
    @BindView(R.id.til_password)
    TextInputLayout tilPassword;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.pb_sign_in)
    ProgressBar pbSignIn;
    @BindView(R.id.tv_error)
    TextView tvError;
    @BindView(R.id.btn_sign_in)
    Button btnSignIn;
    @BindView(R.id.tv_forgot_password)
    TextView tvForgotPassword;

    @Inject
    SignInNavigator navigator;
    @Inject
    SignInAnimator animator;

    private int errorColor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        AuthorizationActivity.getActivitySubComponent(getActivity()).inject(this);
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initErrorColor();
        animator.bind(view);
        setOnFirstGlobalLayoutListener(view);
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
                highlightEditTextWithErrorColor(etLogin);
                break;
            case EMPTY_PASSWORD:
            case ILLEGAL_CHARACTERS_IN_PASSWORD:
                highlightEditTextWithErrorColor(etPassword);
                break;
        }

        String errorMessage = getErrorMessage(error);
        tvError.setText(errorMessage);
        animator.animateErrorAppearance();
    }

    @Override
    public void showSignInProgress() {
        tvError.setVisibility(View.GONE);
        pbSignIn.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSignInProgress() {
        pbSignIn.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.btn_sign_in)
    void onSignInClick() {
        String login = etLogin.getText().toString();
        String password = etPassword.getText().toString();
        mvpPresenter.signIn(login, password);
    }

    @OnClick(R.id.tv_forgot_password)
    void onForgotPasswordClick() {
        Toast.makeText(getContext(), getString(R.string.it_happens), Toast.LENGTH_SHORT).show();
    }

    private void initErrorColor() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            errorColor = getResources().getColor(R.color.accent);
        } else {
            errorColor = getResources().getColor(R.color.accent, getActivity().getTheme());
        }
    }

    private void highlightEditTextWithErrorColor(EditText et) {
        et.getBackground().setColorFilter(errorColor, PorterDuff.Mode.SRC_ATOP);
    }

    private void setOnFirstGlobalLayoutListener(View view) {
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();

        if (!viewTreeObserver.isAlive())
            return;

        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                onFirstGlobalLayout();
            }
        });
    }

    private void onFirstGlobalLayout() {
        animator.showIntroductoryAnimation();
    }
}
