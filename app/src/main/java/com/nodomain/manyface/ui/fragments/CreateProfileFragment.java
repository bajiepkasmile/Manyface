package com.nodomain.manyface.ui.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nodomain.manyface.R;
import com.nodomain.manyface.domain.Error;
import com.nodomain.manyface.mvp.presenters.CreateProfileMvpPresenter;
import com.nodomain.manyface.mvp.views.CreateProfileMvpView;
import com.nodomain.manyface.ui.activities.MainActivity;

import butterknife.BindView;
import butterknife.OnClick;


public class CreateProfileFragment extends BaseDialogFragment<CreateProfileMvpPresenter>
        implements CreateProfileMvpView {

    private static final int ITEM_CAMERA = 0;
    private static final int ITEM_GALLERY = 1;

    public static CreateProfileFragment newInstance() {
        return new CreateProfileFragment();
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
    private int errorColor;
    private String photoFilePath;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity.getActivitySubComponent(getActivity()).inject(this);
        initErrorColor(getActivity());
        getDialog().setTitle("Create new profile");
        setCancelable(false);
        return null;
    }

    @Override
    public void showSaveSuccess() {

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
//        navigator.navigateToBack();
        dismiss(); //TODO: hmm..
    }

    @Override
    public void showError(Error error) {    //TODO: set focus on username?
        String errorMessage = null;

        switch (error) {
            case USER_ALREADY_EXISTS:
                errorMessage = getString(R.string.error_user_already_exists);
                break;
            case CONNECTION_FAILED:
                errorMessage = getString(R.string.error_connection_failed);
                break;
            case EMPTY_USERNAME:
                errorMessage = getString(R.string.error_empty_username);
                highlightEditTextWithError(etUsername);
                break;
            case ILLEGAL_CHARACTERS_IN_USERNAME:
                highlightEditTextWithError(etUsername);
                errorMessage = getString(R.string.error_illegal_characters);
                break;
            case NETWORK_IS_NOT_AVAILABLE:
                errorMessage = getString(R.string.error_network_is_not_avaliable);
                break;
            case TOO_MANY_USERS:
                errorMessage = getString(R.string.error_too_many_users);
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
                        case ITEM_CAMERA:
                            break;
                        case ITEM_GALLERY:
                            break;
                    }
                })
                .show();
    }

//    @OnClick(R.id.tv_save)
//    public void onSaveClick() {
//        String username = etUsername.getText().toString();
//        String description = etDescription.getText().toString();
//        mvpPresenter.createUser(username, description, photoFilePath);
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
}
