package com.nodomain.manyface.navigation;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.nodomain.manyface.data.datasources.remote.impl.dtos.ProfileDto;
import com.nodomain.manyface.ui.activities.AuthorizationActivity;
import com.nodomain.manyface.ui.fragments.ChatFragment;
import com.nodomain.manyface.ui.fragments.ContactDetailsFragment;
import com.nodomain.manyface.ui.fragments.ContactsFragment;
import com.nodomain.manyface.ui.fragments.ProfilesFragment;

import javax.inject.Inject;


public class MainNavigator extends BaseNavigator implements UsersNavigator, ContactsNavigator, ChatNavigator {


    @Inject
    public MainNavigator(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void navigateToChatView(ProfileDto currentUser, ProfileDto contact) {
        replaceFragmentAndAddToBackStack(ChatFragment.newInstance(currentUser, contact));
    }

    @Override
    public void navigateToContactDetailsView(ProfileDto contact) {
        showDialogFragment(ContactDetailsFragment.newInstance(contact));
    }

    @Override
    public void navigateToPreviousView() {
        popBackStack();
    }

    @Override
    public void navigateToContactsView(ProfileDto selectedUser) {
        replaceFragmentAndAddToBackStack(ContactsFragment.newInstance(selectedUser));
    }

    @Override
    public void navigateToAuthorizationView() {
        Intent intent = createIntent(AuthorizationActivity.class);
        startNewActivityAndFinishCurrent(intent);
    }

    public void navigateToUsersView() {
        replaceFragment(ProfilesFragment.newInstance());
    }
}
