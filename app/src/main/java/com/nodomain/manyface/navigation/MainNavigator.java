package com.nodomain.manyface.navigation;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.nodomain.manyface.model.Profile;
import com.nodomain.manyface.ui.activities.AuthorizationActivity;
import com.nodomain.manyface.ui.fragments.ChatFragment;
import com.nodomain.manyface.ui.fragments.ContactDetailsFragment;
import com.nodomain.manyface.ui.fragments.ContactsFragment;
import com.nodomain.manyface.ui.fragments.CreateProfileFragment;
import com.nodomain.manyface.ui.fragments.EditProfileFragment;
import com.nodomain.manyface.ui.fragments.ProfilesFragment;

import javax.inject.Inject;


public class MainNavigator extends BaseNavigator
        implements ProfilesNavigator, ContactsNavigator, ChatNavigator, EditableProfileNavigator {


    @Inject
    public MainNavigator(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void navigateToChatView(Profile currentProfile, Profile contact) {
        replaceFragmentAndAddToBackStack(ChatFragment.newInstance(currentProfile, contact));
    }

    @Override
    public void navigateToContactDetailsView(Profile contact) {
        showDialogFragment(ContactDetailsFragment.newInstance(contact));
    }

    @Override
    public void navigateToPreviousView() {
        popBackStack();
    }

    @Override
    public void navigateToContactsView(Profile profile) {
        replaceFragmentAndAddToBackStack(ContactsFragment.newInstance(profile));
    }

    @Override
    public void navigateToAuthorizationView() {
        Intent intent = createIntent(AuthorizationActivity.class);
        startNewActivityAndFinishCurrent(intent);
    }

    @Override
    public void navigateToCreateProfileView() {
        replaceFragment(CreateProfileFragment.newInstance());
    }

    @Override
    public void navigateToEditProfileView(Profile editableProfile) {
        replaceFragment(EditProfileFragment.newInstance(editableProfile));
    }

    public void navigateToProfilesView() {
        replaceFragment(ProfilesFragment.newInstance());
    }
}
