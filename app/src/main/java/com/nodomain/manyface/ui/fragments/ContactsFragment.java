package com.nodomain.manyface.ui.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nodomain.manyface.R;
import com.nodomain.manyface.domain.Error;
import com.nodomain.manyface.model.Message;
import com.nodomain.manyface.model.Profile;
import com.nodomain.manyface.mvp.presenters.ContactsMvpPresenter;
import com.nodomain.manyface.mvp.views.ContactsMvpView;
import com.nodomain.manyface.navigation.ContactsNavigator;
import com.nodomain.manyface.ui.activities.MainActivity;
import com.nodomain.manyface.ui.listeners.OnItemClickListener;
import com.nodomain.manyface.ui.listeners.OnItemLongClickListener;
import com.nodomain.manyface.ui.recyclerviews.adapters.ContactsAdapter;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class ContactsFragment extends BaseFragment<ContactsMvpPresenter>
        implements ContactsMvpView, OnItemClickListener, OnItemLongClickListener {

    private static final String ARG_CURRENT_PROFILE = "current_profile";

    public static ContactsFragment newInstance(Profile profile) {
        ContactsFragment fragment = new ContactsFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_CURRENT_PROFILE, profile);
        fragment.setArguments(args);

        return fragment;
    }

    @BindView(R.id.pb_getting_contacts)
    ProgressBar pbGettingContacts;
    @BindView(R.id.tv_no_contacts)
    TextView tvNoContacts;
    @BindView(R.id.rv_contacts)
    RecyclerView rvContacts;

    @Inject
    ContactsNavigator navigator;

    private ContactsAdapter contactsAdapter;
    private ProgressDialog pdSearching;
    private boolean isSearchingForContacts = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        MainActivity.getActivitySubComponent(getActivity()).inject(this);
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter.init(getCurrentProfileFromArgs());
        mvpPresenter.getContacts();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.contacts, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        setSearchViewListeners(searchView);
    }

    @Override
    public void showCurrentProfile(Profile currentProfile) {
        String title = String.format(getString(R.string.contacts_of), currentProfile.getName());
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
    }

    @Override
    public void showContacts(List<Profile> contacts) {
        if (contacts.size() == 0)
            tvNoContacts.setVisibility(View.VISIBLE);
        else
            tvNoContacts.setVisibility(View.GONE);

        if (!isSearchingForContacts)
            setContacts(contacts);
    }

    @Override
    public void showUnreadMessagesForContacts(Map<Profile, List<Message>> unreadMessagesForContacts) {
        contactsAdapter.setUnreadMessagesForContacts(unreadMessagesForContacts);
    }

    @Override
    public void showFoundedContacts(List<Profile> foundedContacts) {
        if (isSearchingForContacts)
            setContacts(foundedContacts);
    }

    @Override
    public void showGetContactsProgress() {
        pbGettingContacts.setVisibility(View.VISIBLE);
        rvContacts.setVisibility(View.GONE);
    }

    @Override
    public void hideGetContactsProgress() {
        pbGettingContacts.setVisibility(View.GONE);
        rvContacts.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSearchProgress() {
        pdSearching = createProgressDialog(R.string.saving_changes);
        pdSearching.show();
    }

    @Override
    public void hideSearchProgress() {
        pdSearching.hide();
    }

    @Override
    public void showChatView(Profile currentProfile, Profile contact) {
        navigator.navigateToChatView(currentProfile, contact);
    }

    @Override
    public void showContactDetailsView(Profile contact) {
        navigator.navigateToContactDetailsView(contact);
    }

    @Override
    public void showPreviousView() {
        navigator.navigateToPreviousView();
    }

    @Override
    public void showError(Error error) {
        String errorMessage = getErrorMessage(error);
        createToast(errorMessage).show();
    }

    @Override
    public void onItemClick(int position) {
        Profile contact = contactsAdapter.getContact(position);
        mvpPresenter.navigateToChat(contact);
    }

    @Override
    public void onItemLongClick(int position) {
        Profile contact = contactsAdapter.getContact(position);
        mvpPresenter.navigateToContactDetails(contact);
    }

    @OnClick(R.id.iv_home)
    public void onHomeClick() {
        mvpPresenter.navigateToBack();
    }

    private void setSearchViewListeners(SearchView searchView) {
        searchView.setOnSearchClickListener((v) -> {
            tvNoContacts.setVisibility(View.GONE);
            isSearchingForContacts = true;
        });

        searchView.setOnCloseListener(() -> {
            mvpPresenter.getContacts();
            isSearchingForContacts = false;
            return false;
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mvpPresenter.searchForContacts(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private Profile getCurrentProfileFromArgs() {
        return getArguments().getParcelable(ARG_CURRENT_PROFILE);
    }

    private void setContacts(List<Profile> contacts) {
        if (contactsAdapter == null) {
            contactsAdapter = new ContactsAdapter(contacts, this, this);
            rvContacts.setAdapter(contactsAdapter);
        } else {
            contactsAdapter.setContacts(contacts);
        }
    }
}
