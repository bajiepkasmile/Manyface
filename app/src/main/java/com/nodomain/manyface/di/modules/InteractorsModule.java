package com.nodomain.manyface.di.modules;


import android.os.Handler;

import com.nodomain.manyface.data.datasources.remote.AccountManager;
import com.nodomain.manyface.data.repositories.ContactsRepository;
import com.nodomain.manyface.data.repositories.MessagesRepository;
import com.nodomain.manyface.data.repositories.ProfilesRepository;
import com.nodomain.manyface.domain.interactors.CreateProfileInteractor;
import com.nodomain.manyface.domain.interactors.DeleteProfileInteractor;
import com.nodomain.manyface.domain.interactors.EditProfileInteractor;
import com.nodomain.manyface.domain.interactors.GetContactsInteractor;
import com.nodomain.manyface.domain.interactors.GetDialogMessagesInteractor;
import com.nodomain.manyface.domain.interactors.GetProfilesInteractor;
import com.nodomain.manyface.domain.interactors.SearchInteractor;
import com.nodomain.manyface.domain.interactors.SendMessageInteractor;
import com.nodomain.manyface.domain.interactors.SetProfilePictureInteractor;
import com.nodomain.manyface.domain.interactors.SignOutInteractor;
import com.nodomain.manyface.domain.interactors.SignInInteractor;
import com.nodomain.manyface.domain.interactors.SignUpInteractor;
import com.nodomain.manyface.utils.AuthInfoValidator;
import com.nodomain.manyface.utils.NetworkUtil;

import java.util.concurrent.ExecutorService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class InteractorsModule {

    @Singleton
    @Provides
    SignInInteractor provideSignInInteractor(Handler mainThreadHandler,
                                             AccountManager accountManager,
                                             NetworkUtil networkUtil,
                                             AuthInfoValidator authInfoValidator) {
        return new SignInInteractor(mainThreadHandler, accountManager, networkUtil, authInfoValidator);
    }

    @Singleton
    @Provides
    SignUpInteractor provideSignUpInteractor(Handler mainThreadHandler,
                                             AccountManager accountManager,
                                             NetworkUtil networkUtil,
                                             AuthInfoValidator authInfoValidator) {
        return new SignUpInteractor(mainThreadHandler, accountManager, networkUtil, authInfoValidator);
    }

    @Singleton
    @Provides
    CreateProfileInteractor provideCreateUserInteractor(Handler mainThreadHandler,
                                                        ProfilesRepository profilesRepository,
                                                        NetworkUtil networkUtil,
                                                        AuthInfoValidator authInfoValidator) {
        return new CreateProfileInteractor(mainThreadHandler, profilesRepository, networkUtil, authInfoValidator);
    }

    @Singleton
    @Provides
    EditProfileInteractor provideEditUserInteractor(Handler mainThreadHandler,
                                                    ProfilesRepository profilesRepository,
                                                    NetworkUtil networkUtil) {
        return new EditProfileInteractor(mainThreadHandler, profilesRepository, networkUtil);
    }

    @Singleton
    @Provides
    GetProfilesInteractor provideGetUsersInteractor(Handler mainThreadHandler,
                                                    ProfilesRepository profilesRepository,
                                                    NetworkUtil networkUtil) {
        return new GetProfilesInteractor(mainThreadHandler, profilesRepository, networkUtil);
    }

    @Singleton
    @Provides
    SignOutInteractor provideSignOutInteractor(Handler mainThreadHandler,
                                               ContactsRepository contactsRepository,
                                               ProfilesRepository profilesRepository,
                                               MessagesRepository messagesRepository,
                                               AccountManager accountManager) {
        return new SignOutInteractor(mainThreadHandler,
                profilesRepository, contactsRepository, messagesRepository, accountManager);
    }

    @Singleton
    @Provides
    SetProfilePictureInteractor provideSetUserPhotoInteractor(Handler mainThreadHandler,
                                                              ProfilesRepository profilesRepository,
                                                              NetworkUtil networkUtil) {
        return new SetProfilePictureInteractor(mainThreadHandler, profilesRepository, networkUtil);
    }

    @Singleton
    @Provides
    DeleteProfileInteractor provideDeleteUserInteractor(Handler mainThreadHandler,
                                                        ProfilesRepository profilesRepository,
                                                        NetworkUtil networkUtil) {
        return new DeleteProfileInteractor(mainThreadHandler, profilesRepository, networkUtil);
    }

    @Singleton
    @Provides
    GetContactsInteractor provideGetContactsInteractor(Handler mainThreadHandler,
                                                       ContactsRepository contactsRepository,
                                                       NetworkUtil networkUtil) {
        return new GetContactsInteractor(mainThreadHandler, contactsRepository, networkUtil);
    }

    @Singleton
    @Provides
    SearchInteractor provideSearchInteractor(Handler mainThreadHandler,
                                             ContactsRepository contactsRepository,
                                             NetworkUtil networkUtil) {
        return new SearchInteractor(mainThreadHandler, contactsRepository, networkUtil);
    }

    @Singleton
    @Provides
    GetDialogMessagesInteractor provideGetDialogMessagesInteractor(Handler mainThreadHandler,
                                                                   MessagesRepository messagesRepository,
                                                                   NetworkUtil networkUtil) {
        return new GetDialogMessagesInteractor(mainThreadHandler, messagesRepository, networkUtil);
    }

    @Singleton
    @Provides
    SendMessageInteractor provideSendMessageInteractor(Handler mainThreadHandler,
                                                       ExecutorService executorService,
                                                       MessagesRepository messagesRepository,
                                                       NetworkUtil networkUtil) {
        return new SendMessageInteractor(mainThreadHandler, executorService, messagesRepository, networkUtil);
    }
}
