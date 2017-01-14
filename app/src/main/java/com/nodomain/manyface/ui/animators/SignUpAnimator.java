package com.nodomain.manyface.ui.animators;


import android.view.View;

public interface SignUpAnimator {

    void bind(View view);

    void unbind();

    void animateErrorAppearance();

    void animateNavigationToUsersView(Runnable afterAnimationEnded);
}
