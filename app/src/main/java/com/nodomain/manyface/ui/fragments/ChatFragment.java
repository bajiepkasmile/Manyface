package com.nodomain.manyface.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.nodomain.manyface.R;
import com.nodomain.manyface.domain.Error;
import com.nodomain.manyface.model.Message;
import com.nodomain.manyface.model.Profile;
import com.nodomain.manyface.mvp.presenters.ChatMvpPresenter;
import com.nodomain.manyface.mvp.views.ChatMvpView;
import com.nodomain.manyface.navigation.ChatNavigator;
import com.nodomain.manyface.ui.activities.MainActivity;
import com.nodomain.manyface.ui.listeners.OnItemClickListener;
import com.nodomain.manyface.ui.recyclerviews.adapters.MessagesAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class ChatFragment extends BaseFragment<ChatMvpPresenter> implements ChatMvpView, OnItemClickListener {

    private static final int DIALOG_ITEM_RETRY = 0;
    private static final int DIALOG_ITEM_DELETE = 1;
    private static final String ARG_CURRENT_PROFILE = "current_profile";
    private static final String ARG_CONTACT = "contact";

    public static ChatFragment newInstance(Profile currentProfile, Profile contact) {
        ChatFragment fragment = new ChatFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_CURRENT_PROFILE, currentProfile);
        args.putParcelable(ARG_CONTACT, contact);
        fragment.setArguments(args);

        return fragment;
    }

    @BindView(R.id.rv_messages)
    RecyclerView rvMessages;
    @BindView(R.id.et_message)
    EditText etMessage;
    @BindView(R.id.pb_getting_messages)
    ProgressBar pbGettingMessages;

    @Inject
    ChatNavigator navigator;

    private Profile currentProfile;
    private MessagesAdapter messagesAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        MainActivity.getActivitySubComponent(getActivity()).inject(this);
        currentProfile = getCurrentProfileFromArgs();
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter.init(getCurrentProfileFromArgs(), getContactFromArgs());
        mvpPresenter.getMessages();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mvpPresenter.navigateToBack();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void showChatMembers(Profile currentProfile, Profile contact) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(contact.getName());
    }

    @Override
    public void showMessages(List<Message> messages) {
        messagesAdapter = new MessagesAdapter(currentProfile, messages, this);
        rvMessages.setAdapter(messagesAdapter);
    }

    @Override
    public void showSendingMessage(Message message) {
        messagesAdapter.addItem(message);
    }

    @Override
    public void showSendMessageSuccess(Message sentMessage) {
        messagesAdapter.updateItem(sentMessage);
    }

    @Override
    public void showSendMessageError(Message unsentMessage) {
        messagesAdapter.updateItem(unsentMessage);
    }

    @Override
    public void hideMessage(Message message) {
        messagesAdapter.removeItem(message);
    }

    @Override
    public void showGetMessagesProgress() {
        pbGettingMessages.setVisibility(View.VISIBLE);
        rvMessages.setVisibility(View.GONE);
    }

    @Override
    public void hideGetMessagesProgress() {
        pbGettingMessages.setVisibility(View.GONE);
        rvMessages.setVisibility(View.VISIBLE);
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
        Message message = messagesAdapter.getItem(position);
        if (message.getStatus() == Message.Status.UNSENT)
            createUnsentMessageActionsAlertDialog(message).show();
    }

    @OnClick(R.id.iv_send)
    public void onSendClick() {
        String text = etMessage.getText().toString();
        mvpPresenter.sendMessage(text);
        etMessage.setText("");
    }

    private AlertDialog createUnsentMessageActionsAlertDialog(Message message) {
        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.message)
                .setItems(R.array.unsent_message_actions, (dialogInterface, index) -> {
                    switch (index) {
                        case DIALOG_ITEM_RETRY:
                            mvpPresenter.resendMessage(message);
                            break;
                        case DIALOG_ITEM_DELETE:
                            mvpPresenter.deleteMessage(message);
                            break;
                    }
                })
                .create();
    }

    private Profile getCurrentProfileFromArgs() {
        return getArguments().getParcelable(ARG_CURRENT_PROFILE);
    }

    private Profile getContactFromArgs() {
        return getArguments().getParcelable(ARG_CONTACT);
    }
}
