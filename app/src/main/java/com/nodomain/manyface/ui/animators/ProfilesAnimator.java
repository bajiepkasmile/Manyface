package com.nodomain.manyface.ui.animators;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.nodomain.manyface.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ProfilesAnimator {

//    @BindView(R.id.viewLogoBackground)
//    View viewLogoBackground;
//    @BindView(R.id.ivLogo)
//    ImageView ivLogo;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Unbinder unbinder;

    @Inject
    public ProfilesAnimator() {
    }

    public void bind(View view) {
        unbinder = ButterKnife.bind(this, view);
    }

    public void unbind() {
//        unbinder.unbind();
    }

    public void animateLogoFadeOut() {
//        viewLogoBackground.animate()
//                .alpha(0)
//                .setDuration(1000)
//                .setInterpolator(new AccelerateInterpolator())
//                .start();
//
//        ivLogo.animate()
//                .alpha(0)
//                .setDuration(1000)
//                .setInterpolator(new AccelerateInterpolator())
//                .setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        viewLogoBackground.setVisibility(View.GONE);
//                        ivLogo.setVisibility(View.GONE);
//                    }
//        }).start();
    }

    public void animateNavigationToAuthorizationView(Runnable afterAnimationEnd) {
        afterAnimationEnd.run();
//        viewLogoBackground.setVisibility(View.VISIBLE);
//        ivLogo.setVisibility(View.VISIBLE);
//
//        viewLogoBackground.animate()
//                .alpha(1)
//                .setDuration(1000)
//                .setInterpolator(new AccelerateInterpolator())
//                .start();
//
//        ivLogo.animate()
//                .alpha(1)
//                .setDuration(1000)
//                .setInterpolator(new AccelerateInterpolator())
//                .setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        afterAnimationEnd.run();
//                    }
//                })
//                .start();
    }
}
