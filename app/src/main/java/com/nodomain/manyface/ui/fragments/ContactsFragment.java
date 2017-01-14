package com.nodomain.manyface.ui.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nodomain.manyface.R;
import com.nodomain.manyface.data.datasources.remote.impl.dtos.ProfileDto;
import com.nodomain.manyface.domain.Error;
import com.nodomain.manyface.mvp.presenters.ContactsMvpPresenter;
import com.nodomain.manyface.mvp.views.ContactsMvpView;
import com.nodomain.manyface.navigation.ContactsNavigator;
import com.nodomain.manyface.ui.activities.MainActivity;
import com.nodomain.manyface.ui.listeners.OnItemClickListener;
import com.nodomain.manyface.ui.recyclerviews.adapters.ContactsAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class ContactsFragment extends BaseFragment<ContactsMvpPresenter>
        implements ContactsMvpView, OnItemClickListener {

    private static final String ARG_CURRENT_USER = "current_user";

    public static ContactsFragment newInstance(ProfileDto user) {
        ContactsFragment fragment = new ContactsFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_CURRENT_USER, user);
        fragment.setArguments(args);

        return fragment;
    }

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.sv_contacts)
    SearchView svContacts;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity.getActivitySubComponent(getActivity()).inject(this);
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        svContacts.setOnSearchClickListener((v) -> {
            tvTitle.setVisibility(View.GONE);
            tvNoContacts.setVisibility(View.GONE);
        });

        svContacts.setOnCloseListener(() -> {
            tvTitle.setVisibility(View.VISIBLE);
            contactsAdapter.setData(null);
            mvpPresenter.getContacts();
            return false;
        });

        svContacts.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        mvpPresenter.init(getCurrentUserFromArgs());
        mvpPresenter.getContacts();
    }

    @Override
    public void showCurrentUser(ProfileDto currentUser) {
        String title = String.format(getString(R.string.contacts_of), currentUser.getUsername());
        tvTitle.setText(title);
    }

    @Override
    public void showContacts(List<ProfileDto> contacts) {
        if (contacts.size() == 0) {
            tvNoContacts.setVisibility(View.VISIBLE);
        } else {
            tvNoContacts.setVisibility(View.GONE);
        }

        setContacts(contacts);
    }

    @Override
    public void showFoundedContacts(List<ProfileDto> foundedContacts) {
        setContacts(foundedContacts);
    }

    @Override
    public void showGetContactsProgress() {
        pbGettingContacts.setVisibility(View.VISIBLE);
        svContacts.setEnabled(false); //TODO: hmmm
    }

    @Override
    public void hideGetContactsProgress() {
        pbGettingContacts.setVisibility(View.GONE);
        svContacts.setEnabled(true);  //TODO: hmmmm
    }

    @Override
    public void showSearchProgress() {
        pdSearching = new ProgressDialog(getContext());
        pdSearching.setMessage(getString(R.string.saving_changes));
        pdSearching.setCancelable(false);
        pdSearching.show();
    }

    @Override
    public void hideSearchProgress() {
        pdSearching.hide();
    }

    @Override
    public void showChatView(ProfileDto currentUser, ProfileDto contact) {
        navigator.navigateToChatView(currentUser, contact);
    }

    @Override
    public void showContactDetailsView(ProfileDto contact) {
        navigator.navigateToContactDetailsView(contact);
    }

    @Override
    public void showPreviousView() {
        navigator.navigateToPreviousView();
    }

    @Override
    public void showError(Error error) {
        Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(int position) {
        ProfileDto contact = contactsAdapter.getItem(position);
        mvpPresenter.navigateToChat(contact);
    }

    @OnClick(R.id.iv_home)
    public void onHomeClick() {
        mvpPresenter.navigateToBack();
    }

    private ProfileDto getCurrentUserFromArgs() {
        return getArguments().getParcelable(ARG_CURRENT_USER);
    }

    private void setContacts(List<ProfileDto> contacts) { //TODO: refactor this shit
//        if (contactsAdapter == null) {
            contactsAdapter = new ContactsAdapter(contacts);
            contactsAdapter.setOnItemClickListener(this);
            rvContacts.setAdapter(contactsAdapter);
//        } else {
//            contactsAdapter.setData(contacts);
//        }
    }
}
