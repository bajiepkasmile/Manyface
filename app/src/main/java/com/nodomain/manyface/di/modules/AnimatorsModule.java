package com.nodomain.manyface.di.modules;


import com.nodomain.manyface.ui.animators.AuthorizationAnimator;
import com.nodomain.manyface.ui.animators.SignInAnimator;
import com.nodomain.manyface.ui.animators.SignUpAnimator;

import dagger.Module;
import dagger.Provides;


@Module
public class AnimatorsModule {

    @Provides
    SignInAnimator provideSignInAnimator(AuthorizationAnimator authorizationAnimator) {
        return authorizationAnimator;
    }

    @Provides
    SignUpAnimator provideSignUpAnimator(AuthorizationAnimator authorizationAnimator) {
        return authorizationAnimator;
    }
}
