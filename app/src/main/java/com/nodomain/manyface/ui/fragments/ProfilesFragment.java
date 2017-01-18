package com.nodomain.manyface.ui.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nodomain.manyface.R;
import com.nodomain.manyface.data.datasources.remote.impl.dtos.ProfileDto;
import com.nodomain.manyface.domain.Error;
import com.nodomain.manyface.mvp.presenters.ProfilesMvpPresenter;
import com.nodomain.manyface.mvp.views.ProfilesMvpView;
import com.nodomain.manyface.navigation.UsersNavigator;
import com.nodomain.manyface.ui.activities.MainActivity;
import com.nodomain.manyface.ui.animators.UsersAnimator;
import com.nodomain.manyface.ui.listeners.OnItemClickListener;
import com.nodomain.manyface.ui.listeners.OnItemLongClickListener;
import com.nodomain.manyface.ui.recyclerviews.adapters.UsersAdapter;

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
    @BindView(R.id.rv_users)
    RecyclerView rvUsers;
    @BindView(R.id.tv_no_users)
    TextView tvNoUsers;
    @BindView(R.id.pb_getting_users)
    ProgressBar pbGettingUsers;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Inject
    UsersNavigator navigator;
    @Inject
    UsersAnimator animator;

    private ProgressDialog pdSignOut;
    private ProgressDialog pdDeletingProgress;
    private UsersAdapter usersAdapter;

    public static ProfilesFragment newInstance() {
        return new ProfilesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity.getActivitySubComponent(getActivity()).inject(this);
        return inflater.inflate(R.layout.fragment_users, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupActionBar();
        setHasOptionsMenu(true);

//        animator.bind(view);
//        if (savedInstanceState == null) {
//            animator.animateLogoFadeOut();
//        }

        mvpPresenter.getUsers();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        animator.unbind();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.users, menu);
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
    public void showUsers(List<ProfileDto> users) {
        if (users.size() == 0) {
            tvNoUsers.setVisibility(View.VISIBLE);
            return;
        }

        tvNoUsers.setVisibility(View.GONE);

        usersAdapter = new UsersAdapter(users);
        usersAdapter.setOnItemClickListener(this);
        usersAdapter.setOnItemLongClickListener(this);
        rvUsers.setAdapter(usersAdapter);
    }

    @Override
    public void hideUser(ProfileDto user) {
        usersAdapter.removeItem(user);

        if (usersAdapter.getItemCount() == 0) {
            tvNoUsers.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showGetUsersProgress() {
        pbGettingUsers.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideGetUsersProgress() {
        pbGettingUsers.setVisibility(View.GONE);
    }

    @Override
    public void showDeleteUserProgress() {
        pdDeletingProgress = createProgressDialog(R.string.deleting);
        pdDeletingProgress.show();
    }

    @Override
    public void hideDeleteUserProgress() {
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
    public void showCreateUserView() {

    }

    @Override
    public void showEditUserView(ProfileDto editableUser) {
//        animator.animateTitleChanging(R.string.title_edit_user);
    }

    @Override
    public void showContactsView(ProfileDto user) {
        navigator.navigateToContactsView(user);
    }

    @Override
    public void showAuthorizationView() {
        animator.animateNavigationToAuthorizationView(navigator::navigateToAuthorizationView);
    }

    @Override
    public void showError(Error error) {
        String errorMessage = "";

        switch (error) {
            case NETWORK_IS_NOT_AVAILABLE:
                errorMessage = getString(R.string.error_network_is_not_avaliable);
                break;
            case CONNECTION_FAILED:
                errorMessage = getString(R.string.error_connection_failed);
                break;
        }

        createToast(errorMessage).show();
    }

    @OnClick(R.id.fab)
    public void onFabClick() {
        mvpPresenter.navigateToCreateUser();
    }

    @Override
    public void onItemClick(int position) {
        ProfileDto user = usersAdapter.getItem(position);
        mvpPresenter.navigateToContacts(user);
    }

    @Override
    public void onItemLongClick(int position) {
        ProfileDto user = usersAdapter.getItem(position);

        new AlertDialog.Builder(getContext())
                .setTitle(user.getUsername())
                .setItems(R.array.user_actions, (dialogInterface, index) -> {
                    switch (index) {
                        case DIALOG_ITEM_EDIT:
                            onUserItemEdit(user);
                            break;
                        case DIALOG_ITEM_DELETE:
                            onUserItemDelete(user);
                            break;
                    }
                })
                .show();
    }

    private void setupActionBar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }

    private void onUserItemEdit(ProfileDto user) {
        mvpPresenter.navigateToEditUser(user);
    }

    private void onUserItemDelete(ProfileDto user) {
        new AlertDialog.Builder(getContext())
                .setMessage(R.string.deleting_confirmation)
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> mvpPresenter.deleteUser(user))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private ProgressDialog createProgressDialog(@StringRes int messageResId) {
        ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage(getString(messageResId));
        pd.setCancelable(false);
        pd.show();
        return pd;
    }

    private Toast createToast(String errorMessage) {
        Toast toast = Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        return toast;
    }
}
