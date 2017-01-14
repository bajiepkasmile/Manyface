package com.nodomain.manyface.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nodomain.manyface.R;
import com.nodomain.manyface.data.datasources.remote.impl.dtos.ProfileDto;
import com.nodomain.manyface.domain.Error;
import com.nodomain.manyface.data.datasources.remote.impl.dtos.MessageDto;
import com.nodomain.manyface.mvp.presenters.ChatMvpPresenter;
import com.nodomain.manyface.mvp.views.ChatMvpView;
import com.nodomain.manyface.navigation.ChatNavigator;
import com.nodomain.manyface.ui.activities.MainActivity;
import com.nodomain.manyface.ui.recyclerviews.adapters.MessagesAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class ChatFragment extends BaseFragment<ChatMvpPresenter> implements ChatMvpView {

    private static final String ARG_CURRENT_USER = "current_user";
    private static final String ARG_CONTACT = "contact";

    public static ChatFragment newInstance(ProfileDto currentUser, ProfileDto contact) {
        ChatFragment fragment = new ChatFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_CURRENT_USER, currentUser);
        args.putParcelable(ARG_CONTACT, contact);
        fragment.setArguments(args);

        return fragment;
    }

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_messages)
    RecyclerView rvMessages;
    @BindView(R.id.et_message)
    EditText etMessage;

    @Inject
    ChatNavigator navigator;

    private MessagesAdapter messagesAdapter;

    private int lastScrollPosition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity.getActivitySubComponent(getActivity()).inject(this);
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter.init(getCurrentUserFromArgs(), getContactFromArgs());
        mvpPresenter.getMessages();
    }

    @Override
    public void showChatMembers(ProfileDto currentUser, ProfileDto contact) {
//        String title = String.format(getString(R.string.with), new String[]{currentUser.getUsername(), contact.getUsername()});
        String title = currentUser.getUsername() + " with " + contact.getUsername();
        tvTitle.setText(title);
    }

    @Override
    public void showMessages(List<MessageDto> messages) {
        messagesAdapter = new MessagesAdapter(getCurrentUserFromArgs().getId(), messages);
        rvMessages.setAdapter(messagesAdapter);
    }

    @Override
    public void showMessage(MessageDto message) {
        if (messagesAdapter == null) {
            messagesAdapter = new MessagesAdapter(getCurrentUserFromArgs().getId(), new ArrayList<>());
            rvMessages.setAdapter(messagesAdapter);
        }

        messagesAdapter.addItem(message);
        if (lastScrollPosition != messagesAdapter.getItemCount()-1) {
            rvMessages.scrollToPosition(messagesAdapter.getItemCount()-1);
            lastScrollPosition = messagesAdapter.getItemCount()-1;
        }
    }

    @Override
    public void showGetMessagesProgress() {

    }

    @Override
    public void hideGetMessagesProgress() {

    }

    @Override
    public void showPreviousView() {
        navigator.navigateToPreviousView();
    }

    @Override
    public void showError(Error error) {
        Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.iv_home)
    public void onHomeClick() {
        mvpPresenter.navigateToBack();
    }

    @OnClick(R.id.iv_send)
    public void onSendClick() {
        String text = etMessage.getText().toString();
        mvpPresenter.sendMessage(text);

        etMessage.setText("");
    }

    private ProfileDto getCurrentUserFromArgs() {
        return getArguments().getParcelable(ARG_CURRENT_USER);
    }

    private ProfileDto getContactFromArgs() {
        return getArguments().getParcelable(ARG_CONTACT);
    }
}
