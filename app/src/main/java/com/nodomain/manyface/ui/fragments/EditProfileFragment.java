package com.nodomain.manyface.ui.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nodomain.manyface.R;
import com.nodomain.manyface.data.datasources.remote.impl.dtos.ProfileDto;
import com.nodomain.manyface.domain.Error;
import com.nodomain.manyface.model.Profile;
import com.nodomain.manyface.mvp.presenters.EditUserMvpPresenter;
import com.nodomain.manyface.mvp.views.EditProfileMvpView;
import com.nodomain.manyface.ui.activities.MainActivity;
import com.nodomain.manyface.utils.AppConstants;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.OnClick;


public class EditProfileFragment extends EditableProfileFragment<EditUserMvpPresenter>
        implements EditProfileMvpView {

    private static final String ARG_PROFILE = "editable_profile";

    public static EditProfileFragment newInstance(Profile editableProfile) {
        EditProfileFragment fragment = new EditProfileFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_PROFILE, editableProfile);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity.getActivitySubComponent(getActivity()).inject(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_create_profile);
        mvpPresenter.init(getEditableProfileFromArgs());
    }

    @Override
    public void showEditableProfile(Profile editableProfile) {
        Picasso.with(getContext())
                .load(editableProfile.getPictureUrl())
                .placeholder(R.drawable.take_picture_placeholder)
                .into(ivPicture);
        etProfileName.setText(editableProfile.getName());
        etDescription.setText(editableProfile.getDescription());
    }

    @OnClick(R.id.btn_save)
    public void onSaveClick() {
        String description = etDescription.getText().toString();
        mvpPresenter.editProfile(description, pictureFilePath);
    }

    protected AlertDialog createCancelConfirmationAlertDialog() {
        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.cancel_editing_title)
                .setMessage(R.string.cancel_editing_message)
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> mvpPresenter.navigateToBack())
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    private Profile getEditableProfileFromArgs() {
        return getArguments().getParcelable(ARG_PROFILE);
    }
}
