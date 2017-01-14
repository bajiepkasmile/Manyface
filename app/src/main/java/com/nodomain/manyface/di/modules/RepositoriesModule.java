package com.nodomain.manyface.di.modules;


import com.nodomain.manyface.data.datasources.cache.ContactsCache;
import com.nodomain.manyface.data.datasources.cache.MessagesCache;
import com.nodomain.manyface.data.datasources.cache.ProfilesCache;
import com.nodomain.manyface.data.datasources.local.ContactsLocalStorage;
import com.nodomain.manyface.data.datasources.local.MessagesLocalStorage;
import com.nodomain.manyface.data.datasources.local.ProfilesLocalStorage;
import com.nodomain.manyface.data.datasources.remote.ContactsRemoteStorage;
import com.nodomain.manyface.data.datasources.remote.MessagesRemoteStorage;
import com.nodomain.manyface.data.datasources.remote.ProfilesRemoteStorage;
import com.nodomain.manyface.data.repositories.ContactsRepository;
import com.nodomain.manyface.data.repositories.MessagesRepository;
import com.nodomain.manyface.data.repositories.ProfilesRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class RepositoriesModule {

    @Singleton
    @Provides
    ProfilesRepository providesUsersRepository(ProfilesRemoteStorage remoteStorage,
                                               ProfilesLocalStorage localStorage,
                                               ProfilesCache cache) {
        return new ProfilesRepository(remoteStorage, localStorage, cache);
    }

    @Singleton
    @Provides
    ContactsRepository provideContactsRepository(ContactsRemoteStorage remoteStorage,
                                                 ContactsLocalStorage localStorage,
                                                 ContactsCache cache) {
        return new ContactsRepository(remoteStorage, localStorage, cache);
    }

    @Singleton
    @Provides
    MessagesRepository provideMessagesRepository(MessagesRemoteStorage remoteStorage,
                                                 MessagesLocalStorage localStorage,
                                                 MessagesCache cache) {
        return new MessagesRepository(remoteStorage, localStorage, cache);
    }
}
