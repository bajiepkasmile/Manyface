package com.nodomain.manyface.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.nodomain.manyface.R;
import com.nodomain.manyface.mvp.presenters.CreateProfileMvpPresenter;
import com.nodomain.manyface.mvp.views.CreateProfileMvpView;
import com.nodomain.manyface.ui.activities.MainActivity;

import butterknife.OnClick;


public class CreateProfileFragment extends EditableProfileFragment<CreateProfileMvpPresenter>
        implements CreateProfileMvpView {

    public static CreateProfileFragment newInstance() {
        return new CreateProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        MainActivity.getActivitySubComponent(getActivity()).inject(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_edit_profile);
    }

    @OnClick(R.id.btn_save)
    public void onSaveClick() {
        String profileName = etProfileName.getText().toString();
        String description = etDescription.getText().toString();
        mvpPresenter.createProfile(profileName, description, pictureFilePath);
    }

    protected AlertDialog createCancelConfirmationAlertDialog() {
        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.cancel_creation_title)
                .setMessage(R.string.cancel_creation_message)
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> mvpPresenter.navigateToBack())
                .setNegativeButton(R.string.cancel, null)
                .create();
    }
}
