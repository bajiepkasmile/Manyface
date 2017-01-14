package com.nodomain.manyface.ui.animators;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.nodomain.manyface.R;
import com.nodomain.manyface.ui.listeners.AnimationListenerAdapter;
import com.nodomain.manyface.utils.DisplayUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class AuthorizationAnimator implements SignInAnimator, SignUpAnimator {

    static class RootViews {
        @BindView(R.id.iv_logo)
        ImageView ivLogo;
        @BindView(R.id.fl_logo_container)
        FrameLayout flLogoContainer;
        @BindView(R.id.tab_layout)
        TabLayout tabLayout;
    }

    static class Views {
        @BindView(R.id.til_login)
        TextInputLayout tilLogin;
        @BindView(R.id.til_password)
        TextInputLayout tilPassword;
        @BindView(R.id.tv_error)
        TextView tvError;
        @Nullable @BindView(R.id.btn_sign_in)
        Button btnSignIn;
        @Nullable @BindView(R.id.tv_forgot_password)
        TextView tvForgotPassword;
    }

    private RootViews rootViews = new RootViews();
    private Views views = new Views();

    private Unbinder rootUnbinder;
    private Unbinder unbinder;

    private LayoutParams logoLayoutParams;

    private float collapsedLogoContainerHeight;
    private float deltaLogoContainerHeight;
    private float collapsedLogoSize;
    private float deltaLogoSize;
    private float expandedLogoMarginBottom;

    @Inject
    public AuthorizationAnimator(AppCompatActivity activity, DisplayUtil displayUtil) {
        rootUnbinder = ButterKnife.bind(rootViews, activity);

        Resources resources = activity.getResources();

        collapsedLogoContainerHeight = resources.getDimension(R.dimen.login_toolbar_height);
        float expandedLogoContainerHeight = displayUtil.getDisplayHeight();
        deltaLogoContainerHeight = expandedLogoContainerHeight - collapsedLogoContainerHeight;
        collapsedLogoSize = resources.getDimension(R.dimen.logo_size);
        expandedLogoMarginBottom = resources.getDimension(R.dimen.logo_margin_bottom);
    }

    @Override
    public void bind(View view) {
        unbinder = ButterKnife.bind(views, view);
    }

    @Override
    public void unbind() {
        rootUnbinder.unbind();
        unbinder.unbind();
    }

    @Override
    public void showIntroductoryAnimation() {
        float expandedLogoSize = rootViews.ivLogo.getHeight();
        deltaLogoSize = expandedLogoSize - collapsedLogoSize;

        setAlphasForIntroductoryAnimation();
        showCollapseScaleAnimation(() -> showAlphaAnimation(1, null));
    }

    @Override
    public void animateErrorAppearance() {
        views.tvError.setAlpha(0);
        views.tvError.setVisibility(View.VISIBLE);
        views.tvError.animate().alpha(1).setDuration(200).start();
    }

    @Override
    public void animateNavigationToUsersView(Runnable afterAnimationEnded) {
        showAlphaAnimation(0, () -> showExpandScaleAnimation(afterAnimationEnded));
    }

    private void setAlphasForIntroductoryAnimation() {
        rootViews.tabLayout.setAlpha(0);

        views.tilLogin.setAlpha(0);
        views.tilPassword.setAlpha(0);
        views.btnSignIn.setAlpha(0);
        views.tvForgotPassword.setAlpha(0);
    }

    private void showCollapseScaleAnimation(Runnable afterAnimationEnd) {
        Animation collapseAnimation = createScaleAnimationAndSetListener(true, afterAnimationEnd);
        collapseAnimation.setDuration(1000);
        rootViews.flLogoContainer.startAnimation(collapseAnimation);
    }

    private void showExpandScaleAnimation(Runnable afterAnimationEnd) {
        Animation expandAnimation = createScaleAnimationAndSetListener(false, afterAnimationEnd);
        expandAnimation.setDuration(1000);
        rootViews.flLogoContainer.startAnimation(expandAnimation);
    }

    private void showAlphaAnimation(float targetAlpha, Runnable afterAnimationEnd) {
        rootViews.tabLayout.animate().alpha(targetAlpha).setDuration(250).start();

        views.tilLogin.animate().alpha(targetAlpha).setStartDelay(100).setDuration(250).start();
        views.tilPassword.animate().alpha(targetAlpha).setStartDelay(200).setDuration(250).start();
        views.btnSignIn.animate().alpha(targetAlpha).setStartDelay(300).setDuration(250).start();
        views.tvForgotPassword.animate().alpha(targetAlpha).setStartDelay(400).setDuration(250)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (afterAnimationEnd != null)
                            afterAnimationEnd.run();
                    }
                }).start();
    }

    private Animation createScaleAnimationAndSetListener(boolean collapse, Runnable afterAnimationEnd) {
        Animation scaleAnimation =  new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (collapse) {
                    interpolatedTime = 1 - interpolatedTime;
                }

                int logoContainerHeight = (int) calculateLogoContainedHeight(interpolatedTime);
                int logoSize = (int) calculateLogoSize(interpolatedTime);
                int logoMarginBottom = (int) calculateLogoMarginBottom(interpolatedTime);

                setLogoContainerHeight(logoContainerHeight);
                setLogoSize(logoSize);
                setLogoMarginBottom(logoMarginBottom);

                rootViews.flLogoContainer.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        scaleAnimation.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                if (afterAnimationEnd != null)
                    afterAnimationEnd.run();
            }
        });

        return scaleAnimation;
    }

    private float calculateLogoContainedHeight(float interpolatedTime) {
        return collapsedLogoContainerHeight + deltaLogoContainerHeight * interpolatedTime;
    }

    private float calculateLogoSize(float interpolatedTime) {
        return collapsedLogoSize + deltaLogoSize * interpolatedTime;
    }

    private float calculateLogoMarginBottom(float interpolatedTime) {
        return expandedLogoMarginBottom * (1 - interpolatedTime);
    }

    private void setLogoContainerHeight(int height) {
        rootViews.flLogoContainer.getLayoutParams().height = height;
    }

    private void setLogoSize(int size) {
        logoLayoutParams = new LayoutParams(size, size);
        logoLayoutParams.gravity = Gravity.CENTER;
    }

    private void setLogoMarginBottom(int marginBottom) {
        logoLayoutParams.setMargins(0, 0, 0, marginBottom);
        rootViews.ivLogo.setLayoutParams(logoLayoutParams);
    }
}
