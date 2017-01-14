package com.nodomain.manyface.ui.animators;


import android.view.View;

public interface SignInAnimator {

    void bind(View view);

    void unbind();

    void showIntroductoryAnimation();

    void animateErrorAppearance();

    void animateNavigationToUsersView(Runnable afterAnimationEnded);
}
