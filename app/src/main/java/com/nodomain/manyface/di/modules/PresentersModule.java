package com.nodomain.manyface.di.modules;


import com.nodomain.manyface.mvp.presenters.AuthorizationMvpPresenter;
import com.nodomain.manyface.mvp.presenters.ChatMvpPresenter;
import com.nodomain.manyface.mvp.presenters.ContactDetailsMvpPresenter;
import com.nodomain.manyface.mvp.presenters.ContactsMvpPresenter;
import com.nodomain.manyface.mvp.presenters.CreateProfileMvpPresenter;
import com.nodomain.manyface.mvp.presenters.EditUserMvpPresenter;
import com.nodomain.manyface.mvp.presenters.SignInMvpPresenter;
import com.nodomain.manyface.mvp.presenters.SignUpMvpPresenter;
import com.nodomain.manyface.mvp.presenters.ProfilesMvpPresenter;
import com.nodomain.manyface.mvp.presentersimpl.AuthorizationMvpPresenterImpl;
import com.nodomain.manyface.mvp.presentersimpl.ChatMvpPresenterImpl;
import com.nodomain.manyface.mvp.presentersimpl.ContactDetailsMvpPresenterImpl;
import com.nodomain.manyface.mvp.presentersimpl.ContactsMvpPresenterImpl;
import com.nodomain.manyface.mvp.presentersimpl.CreateProfileMvpPresenterImpl;
import com.nodomain.manyface.mvp.presentersimpl.EditUserMvpPresenterImpl;
import com.nodomain.manyface.mvp.presentersimpl.SignInMvpPresenterImpl;
import com.nodomain.manyface.mvp.presentersimpl.SignUpMvpPresenterImpl;
import com.nodomain.manyface.mvp.presentersimpl.ProfilesMvpPresenterImpl;

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
    ProfilesMvpPresenter provideProfilesMvpPresenter(ProfilesMvpPresenterImpl presenterImpl) {
        return presenterImpl;
    }

    @Provides
    CreateProfileMvpPresenter provideCreateProfileMvpPresenter(CreateProfileMvpPresenterImpl presenterImpl) {
        return presenterImpl;
    }

    @Provides
    EditUserMvpPresenter provideEditProfileMvpPresenter(EditUserMvpPresenterImpl presenterImpl) {
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

    @Provides
    ContactDetailsMvpPresenter provideContactDetailsMvpPresenter(ContactDetailsMvpPresenterImpl presenterImpl) {
        return presenterImpl;
    }
}
