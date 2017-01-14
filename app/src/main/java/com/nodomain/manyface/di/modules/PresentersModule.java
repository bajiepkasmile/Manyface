package com.nodomain.manyface.di.modules;


import com.nodomain.manyface.mvp.presenters.AuthorizationMvpPresenter;
import com.nodomain.manyface.mvp.presenters.ChatMvpPresenter;
import com.nodomain.manyface.mvp.presenters.ContactsMvpPresenter;
import com.nodomain.manyface.mvp.presenters.CreateUserMvpPresenter;
import com.nodomain.manyface.mvp.presenters.EditUserMvpPresenter;
import com.nodomain.manyface.mvp.presenters.SignInMvpPresenter;
import com.nodomain.manyface.mvp.presenters.SignUpMvpPresenter;
import com.nodomain.manyface.mvp.presenters.UsersMvpPresenter;
import com.nodomain.manyface.mvp.presenters.impl.AuthorizationMvpPresenterImpl;
import com.nodomain.manyface.mvp.presenters.impl.ChatMvpPresenterImpl;
import com.nodomain.manyface.mvp.presenters.impl.ContactsMvpPresenterImpl;
import com.nodomain.manyface.mvp.presenters.impl.CreateUserMvpPresenterImpl;
import com.nodomain.manyface.mvp.presenters.impl.EditUserMvpPresenterImpl;
import com.nodomain.manyface.mvp.presenters.impl.SignInMvpPresenterImpl;
import com.nodomain.manyface.mvp.presenters.impl.SignUpMvpPresenterImpl;
import com.nodomain.manyface.mvp.presenters.impl.UsersMvpPresenterImpl;

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
    UsersMvpPresenter provideUsersMvpPresenter(UsersMvpPresenterImpl presenterImpl) {
        return presenterImpl;
    }

    @Provides
    CreateUserMvpPresenter provideCreateUserMvpPresenter(CreateUserMvpPresenterImpl presenterImpl) {
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
