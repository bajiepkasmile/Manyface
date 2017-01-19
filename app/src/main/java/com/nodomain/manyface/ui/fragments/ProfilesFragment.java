package com.nodomain.manyface.ui.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.nodomain.manyface.R;
import com.nodomain.manyface.domain.Error;
import com.nodomain.manyface.model.Profile;
import com.nodomain.manyface.mvp.presenters.ProfilesMvpPresenter;
import com.nodomain.manyface.mvp.views.ProfilesMvpView;
import com.nodomain.manyface.navigation.ProfilesNavigator;
import com.nodomain.manyface.ui.activities.MainActivity;
import com.nodomain.manyface.ui.animators.ProfilesAnimator;
import com.nodomain.manyface.ui.listeners.OnItemClickListener;
import com.nodomain.manyface.ui.listeners.OnItemLongClickListener;
import com.nodomain.manyface.ui.recyclerviews.adapters.ProfilesAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class ProfilesFragment extends BaseFragment<ProfilesMvpPresenter>
        implements ProfilesMvpView, OnItemClickListener, OnItemLongClickListener {

    private static final int DIALOG_ITEM_EDIT = 0;
    private static final int DIALOG_ITEM_DELETE = 1;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_profiles)
    RecyclerView rvProfiles;
    @BindView(R.id.ll_no_profiles)
    LinearLayout llNoProfiles;
    @BindView(R.id.pb_getting_profiles)
    ProgressBar pbGettingProfiles;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Inject
    ProfilesNavigator navigator;
    @Inject
    ProfilesAnimator animator;

    private ProgressDialog pdSignOut;
    private ProgressDialog pdDeletingProgress;
    private ProfilesAdapter profilesAdapter;

    public static ProfilesFragment newInstance() {
        return new ProfilesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity.getActivitySubComponent(getActivity()).inject(this);
        return inflater.inflate(R.layout.fragment_profiles, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        animator.bind(view);
//        if (savedInstanceState == null) {
//            animator.animateLogoFadeOut();
//        }
        mvpPresenter.getProfiles();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        animator.unbind();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profiles, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sign_out) {
            mvpPresenter.signOut();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showProfiles(List<Profile> profiles) {
        if (profiles.size() == 0) {
            llNoProfiles.setVisibility(View.VISIBLE);
            return;
        }

        llNoProfiles.setVisibility(View.GONE);

        profilesAdapter = new ProfilesAdapter(profiles, this, this);
        rvProfiles.setAdapter(profilesAdapter);
    }

    @Override
    public void hideProfile(Profile profile) {
        profilesAdapter.removeItem(profile);

        if (profilesAdapter.getItemCount() == 0)
            llNoProfiles.setVisibility(View.VISIBLE);
    }

    @Override
    public void showGetProfilesProgress() {
        pbGettingProfiles.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideGetProfilesProgress() {
        pbGettingProfiles.setVisibility(View.GONE);
    }

    @Override
    public void showDeleteProfileProgress() {
        pdDeletingProgress = createProgressDialog(R.string.deleting);
        pdDeletingProgress.show();
    }

    @Override
    public void hideDeleteProfileProgress() {
        pdDeletingProgress.hide();
    }

    @Override
    public void showSignOutProgress() {
        pdSignOut = createProgressDialog(R.string.sign_out);
        pdSignOut.show();
    }

    @Override
    public void hideSignOutProgress() {
        pdSignOut.hide();
    }

    @Override
    public void showCreateProfileView() {
        navigator.navigateToCreateProfileView();
    }

    @Override
    public void showEditProfileView(Profile editableProfile) {
        navigator.navigateToEditProfileView(editableProfile);
    }

    @Override
    public void showContactsView(Profile profile) {
        navigator.navigateToContactsView(profile);
    }

    @Override
    public void showAuthorizationView() {
        animator.animateNavigationToAuthorizationView(navigator::navigateToAuthorizationView);
    }

    @Override
    public void showError(Error error) {
        String errorMessage = getErrorMessage(error);
        createToast(errorMessage).show();
    }

    @OnClick(R.id.fab)
    public void onFabClick() {
        mvpPresenter.navigateToCreateProfile();
    }

    @Override
    public void onItemClick(int position) {
        Profile profile = profilesAdapter.getItem(position);
        mvpPresenter.navigateToContacts(profile);
    }

    @Override
    public void onItemLongClick(int position) {
        Profile profile = profilesAdapter.getItem(position);
        createProfileActionsAlertDialog(profile).show();
    }

    private AlertDialog createProfileActionsAlertDialog(Profile profile) {
        return new AlertDialog.Builder(getContext())
                .setTitle(profile.getName())
                .setItems(R.array.profile_actions, (dialogInterface, index) -> {
                    switch (index) {
                        case DIALOG_ITEM_EDIT:
                            onProfileItemEdit(profile);
                            break;
                        case DIALOG_ITEM_DELETE:
                            onProfileItemDelete(profile);
                            break;
                    }
                }).create();
    }

    private void onProfileItemEdit(Profile profile) {
        mvpPresenter.navigateToEditProfile(profile);
    }

    private void onProfileItemDelete(Profile profile) {
        new AlertDialog.Builder(getContext())
                .setMessage(R.string.deleting_confirmation)
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> mvpPresenter.deleteProfile(profile))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}
