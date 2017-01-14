package com.nodomain.manyface.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.nodomain.manyface.data.datasources.remote.impl.dtos.ProfileDto;

public class ContactDetailsFragment extends DialogFragment {

    private static final String ARG_CONTACT = "contact";

    public static ContactDetailsFragment newInstance(ProfileDto contact) {
        ContactDetailsFragment fragment = new ContactDetailsFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_CONTACT, contact);
        fragment.setArguments(args);

        return fragment;
    }
}
