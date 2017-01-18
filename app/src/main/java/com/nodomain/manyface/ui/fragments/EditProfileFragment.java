package com.nodomain.manyface.ui.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nodomain.manyface.R;
import com.nodomain.manyface.data.datasources.remote.impl.dtos.ProfileDto;
import com.nodomain.manyface.domain.Error;
import com.nodomain.manyface.mvp.presenters.EditUserMvpPresenter;
import com.nodomain.manyface.mvp.views.EditProfileMvpView;
import com.nodomain.manyface.ui.activities.MainActivity;
import com.nodomain.manyface.utils.AppConstants;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.OnClick;


public class EditProfileFragment extends BaseDialogFragment<EditUserMvpPresenter>
        implements EditProfileMvpView {

    private static final int ITEM_CAMERA = 0;
    private static final int ITEM_GALLERY = 1;
    private static final String ARG_USER = "editable_user";

    public static EditProfileFragment newInstance(ProfileDto editableUser) {
        EditProfileFragment fragment = new EditProfileFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_USER, editableUser);
        fragment.setArguments(args);

        return fragment;
    }

    @BindView(R.id.iv_photo)
    public ImageView ivPhoto;
    @BindView(R.id.et_username)
    public EditText etUsername;
    @BindView(R.id.et_description)
    public EditText etDescription;

//    @BindView(R.id.tv_save)
//    TextView tvSave;
//    @BindView(R.id.tv_cancel)
//    TextView tvCancel;

    private ProgressDialog pdSavingProgress;
    private String photoFilePath;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity.getActivitySubComponent(getActivity()).inject(this);
        return null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().setTitle("Edit profile");
        setCancelable(false);
        etUsername.setClickable(false);
        etUsername.setFocusable(false);

        mvpPresenter.init(getEditableUserFromArgs());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ivPhoto.setImageURI(data.getData());
        photoFilePath = getRealPathFromURI(data.getData());
    }

    @Override
    public void showEditableUser(ProfileDto editableUser) {
        Picasso.with(getContext())
                .load(editableUser.getPhotoLink())
                .placeholder(R.drawable.take_photo_placeholder)
                .into(ivPhoto);
        etUsername.setText(editableUser.getUsername());
        etDescription.setText(editableUser.getDescription());
    }

    @Override
    public void showSaveProgress() {
        pdSavingProgress = new ProgressDialog(getContext());
        pdSavingProgress.setMessage(getString(R.string.saving_changes));
        pdSavingProgress.setCancelable(false);
        pdSavingProgress.show();
    }

    @Override
    public void hideSaveProgress() {
        pdSavingProgress.hide();
    }

    @Override
    public void showPreviousView() {
//        navigator.navigateToPreviousView();
        dismiss();
    }

    @Override
    public void showError(Error error) {
        String errorMessage = null;

        switch (error) {
            case USER_ALREADY_EXISTS:
                errorMessage = getString(R.string.error_user_already_exists);
                break;
            case CONNECTION_FAILED:
                errorMessage = getString(R.string.error_connection_failed);
                break;
            case NETWORK_IS_NOT_AVAILABLE:
                errorMessage = getString(R.string.error_network_is_not_avaliable);
                break;
        }

        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.iv_photo)
    public void onPhotoClick() {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.select_photo)
                .setItems(R.array.photo_selections, (dialogInterface, index) -> {
                    switch (index) {
                        case ITEM_CAMERA: //TODO: to navigator
                            Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);         //TODO: resolve activity
                            startActivityForResult(takePhoto, AppConstants.ACTIVITY_REQUEST_CODE_CAMERA);
                            break;
                        case ITEM_GALLERY:
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, AppConstants.ACTIVITY_REQUEST_CODE_GALLERY);
                            break;
                    }
                })
                .show();
    }
//
//    @OnClick(R.id.tv_save)
//    public void onSaveClick() {
//        String username = etUsername.getText().toString();
//        String description = etDescription.getText().toString();
//        mvpPresenter.editUser(description, photoFilePath);
//    }
//
//    @OnClick(R.id.tv_cancel)
//    public void onCancelClick() {
//        new AlertDialog.Builder(getContext())
//                .setTitle(R.string.cancel_creation_title)
//                .setMessage(R.string.cancel_creation_message)
//                .setPositiveButton(R.string.ok, (dialogInterface, i) -> mvpPresenter.navigateToBack())
//                .setNegativeButton(R.string.cancel, null)
//                .show();
//    }

    private ProfileDto getEditableUserFromArgs() {
        return getArguments().getParcelable(ARG_USER);
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContext().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
}
