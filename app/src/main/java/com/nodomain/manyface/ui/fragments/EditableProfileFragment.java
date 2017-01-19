package com.nodomain.manyface.ui.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nodomain.manyface.R;
import com.nodomain.manyface.domain.Error;
import com.nodomain.manyface.mvp.presenters.MvpPresenter;
import com.nodomain.manyface.mvp.views.EditableProfileMvpView;
import com.nodomain.manyface.navigation.EditableProfileNavigator;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public abstract class EditableProfileFragment<T extends MvpPresenter> extends BaseFragment<T>
        implements EditableProfileMvpView {

    public static final int ACTIVITY_REQUEST_CODE_CAMERA = 0;
    public static final int ACTIVITY_REQUEST_CODE_GALLERY = 1;
    private static final int DIALOG_ITEM_CAMERA = 0;
    private static final int DIALOG_ITEM_GALLERY = 1;

    @BindView(R.id.iv_picture)
    ImageView ivPicture;
    @BindView(R.id.et_profile_name)
    EditText etProfileName;
    @BindView(R.id.et_description)
    EditText etDescription;

    @Inject
    EditableProfileNavigator navigator;

    protected String pictureFilePath;
    private ProgressDialog pdSavingProgress;
    private int errorColor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        initErrorColor(getActivity());
        return inflater.inflate(R.layout.fragment_editable_user, container, false);
    }

    protected abstract AlertDialog createCancelConfirmationAlertDialog();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            createCancelConfirmationAlertDialog().show();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ivPicture.setImageURI(data.getData());
//        pictureFilePath = getRealPathFromURI(data.getData());
    }

    @Override
    public void showSaveProgress() {
        pdSavingProgress = createProgressDialog(R.string.saving_changes);
        pdSavingProgress.show();
    }

    @Override
    public void hideSaveProgress() {
        pdSavingProgress.hide();
    }

    @Override
    public void showPreviousView() {
        navigator.navigateToPreviousView();
    }

    @Override
    public void showError(Error error) {
        if (error == Error.EMPTY_PROFILE_NAME || error == Error.ILLEGAL_CHARACTERS_IN_PROFILE_NAME) {
            highlightEditTextWithError(etProfileName);
            //TODO: set focus on profile name
        }

        String errorMessage = getErrorMessage(error);
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.iv_picture)
    public void onPictureClick() {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.select_photo)
                .setItems(R.array.photo_selections, (dialogInterface, index) -> {
                    switch (index) {
                        case DIALOG_ITEM_CAMERA: //TODO: to navigator
                            Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  //TODO: resolve activity
                            startActivityForResult(takePhoto, ACTIVITY_REQUEST_CODE_CAMERA);
                            break;
                        case DIALOG_ITEM_GALLERY:
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, ACTIVITY_REQUEST_CODE_GALLERY);
                            break;
                    }
                })
                .show();
    }

    private void initErrorColor(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            errorColor = getResources().getColor(R.color.accent);
        } else {
            errorColor = getResources().getColor(R.color.accent, activity.getTheme());
        }
    }

    private void highlightEditTextWithError(EditText et) {
        et.getBackground().setColorFilter(errorColor, PorterDuff.Mode.SRC_ATOP);
    }

//    private String getRealPathFromURI(Uri contentURI) {
//        String result;
//        Cursor cursor = getContext().getContentResolver().query(contentURI, null, null, null, null);
//        if (cursor == null) {
//            result = contentURI.getPath();
//        } else {
//            cursor.moveToFirst();
//            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//            result = cursor.getString(idx);
//            cursor.close();
//        }
//        return result;
//    }
}
