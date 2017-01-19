package com.nodomain.manyface.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nodomain.manyface.R;
import com.nodomain.manyface.data.datasources.remote.impl.dtos.ProfileDto;
import com.nodomain.manyface.domain.Error;
import com.nodomain.manyface.model.Profile;
import com.nodomain.manyface.mvp.presenters.ContactDetailsMvpPresenter;
import com.nodomain.manyface.mvp.views.ContactDetailsMvpView;
import com.nodomain.manyface.ui.activities.MainActivity;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

public class ContactDetailsFragment extends BaseDialogFragment<ContactDetailsMvpPresenter>
        implements ContactDetailsMvpView{

    private static final String ARG_CONTACT = "contact";

    @BindView(R.id.iv_contact_picture)
    ImageView ivContactPicture;
    @BindView(R.id.tv_contact_name)
    TextView tvContactName;
    @BindView(R.id.tv_contact_description)
    TextView tvContactDescription;

    public static ContactDetailsFragment newInstance(Profile contact) {
        ContactDetailsFragment fragment = new ContactDetailsFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_CONTACT, contact);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity.getActivitySubComponent(getActivity()).inject(this);
        return inflater.inflate(R.layout.fragment_contact_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter.init(getContactFromArgs());
    }

    @Override
    public void showContactDetails(Profile contact) {
        Picasso.with(getContext())
                .load(contact.getPictureUrl())
                .placeholder(R.drawable.picture_placeholder)
                .into(ivContactPicture);
        tvContactName.setText(contact.getName());
        tvContactDescription.setText(contact.getDescription());
    }

    @Override
    public void showError(Error error) {
    }

    private Profile getContactFromArgs() {
        return getArguments().getParcelable(ARG_CONTACT);
    }
}
