package com.nodomain.manyface.di.modules;


import com.nodomain.manyface.mvp.presenters.AuthorizationMvpPresenter;
import com.nodomain.manyface.mvp.presenters.ChatMvpPresenter;
import com.nodomain.manyface.mvp.presenters.ContactsMvpPresenter;
import com.nodomain.manyface.mvp.presenters.CreateProfileMvpPresenter;
import com.nodomain.manyface.mvp.presenters.EditUserMvpPresenter;
import com.nodomain.manyface.mvp.presenters.SignInMvpPresenter;
import com.nodomain.manyface.mvp.presenters.SignUpMvpPresenter;
import com.nodomain.manyface.mvp.presenters.ProfilesMvpPresenter;
import com.nodomain.manyface.mvp.presenters.impl.AuthorizationMvpPresenterImpl;
import com.nodomain.manyface.mvp.presenters.impl.ChatMvpPresenterImpl;
import com.nodomain.manyface.mvp.presenters.impl.ContactsMvpPresenterImpl;
import com.nodomain.manyface.mvp.presenters.impl.CreateProfileMvpPresenterImpl;
import com.nodomain.manyface.mvp.presenters.impl.EditUserMvpPresenterImpl;
import com.nodomain.manyface.mvp.presenters.impl.SignInMvpPresenterImpl;
import com.nodomain.manyface.mvp.presenters.impl.SignUpMvpPresenterImpl;
import com.nodomain.manyface.mvp.presenters.impl.ProfilesMvpPresenterImpl;

import dagger.Module;
import dagger.Provides;


@Module
public class PresentersModule {

    @Provides
    AuthorizationMvpPresenter provideAuthorizationMvpPresenter(AuthorizationMvpPresenterImpl presenterImpl) {
        return presenterImpl;
    }

    @Provides
    SignInMvpPresenter provideSignInMvpPresenter(SignInMvpPresenterImpl presenterImpl) {
        return presenterImpl;
    }

    @Provides
    SignUpMvpPresenter provideSignUpMvpPresenter(SignUpMvpPresenterImpl presenterImpl) {
        return presenterImpl;
    }

    @Provides
    ProfilesMvpPresenter provideUsersMvpPresenter(ProfilesMvpPresenterImpl presenterImpl) {
        return presenterImpl;
    }

    @Provides
    CreateProfileMvpPresenter provideCreateUserMvpPresenter(CreateProfileMvpPresenterImpl presenterImpl) {
        return presenterImpl;
    }

    @Provides
    EditUserMvpPresenter provideEditUserMvpPresenter(EditUserMvpPresenterImpl presenterImpl) {
        return presenterImpl;
    }

    @Provides
    ContactsMvpPresenter provideContactsMvpPresenter(ContactsMvpPresenterImpl presenterImpl) {
        return presenterImpl;
    }

    @Provides
    ChatMvpPresenter provideChatMvpPresenter(ChatMvpPresenterImpl presenterImpl) {
        return presenterImpl;
    }
}
