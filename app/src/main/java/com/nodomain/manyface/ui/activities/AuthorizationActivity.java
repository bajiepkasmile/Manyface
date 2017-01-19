package com.nodomain.manyface.ui.activities;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.nodomain.manyface.App;
import com.nodomain.manyface.di.components.AuthorizationActivitySubComponent;
import com.nodomain.manyface.di.modules.AuthorizationActivityModule;
import com.nodomain.manyface.domain.Error;
import com.nodomain.manyface.mvp.presenters.AuthorizationMvpPresenter;
import com.nodomain.manyface.mvp.views.AuthorizationMvpView;
import com.nodomain.manyface.navigation.AuthorizationNavigator;
import com.nodomain.manyface.ui.adapters.AuthorizationPagerAdapter;
import com.nodomain.manyface.ui.listeners.OnPageChangeListenerAdapter;
import com.nodomain.manyface.utils.DisplayUtil;
import com.nodomain.manyface.R;
import com.nodomain.manyface.utils.KeyboardUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AuthorizationActivity extends AppCompatActivity implements AuthorizationMvpView {

    @BindView(android.R.id.content)
    View activityContentView;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @Inject
    AuthorizationMvpPresenter mvpPresenter;
    @Inject
    AuthorizationNavigator navigator;
    @Inject
    DisplayUtil displayUtil;
    @Inject
    KeyboardUtil keyboardUtil;

    private AuthorizationActivitySubComponent authorizationActivitySubComponent;

    public static AuthorizationActivitySubComponent getActivitySubComponent(Activity activity) {
        return ((AuthorizationActivity) activity).authorizationActivitySubComponent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        initActivitySubComponent();
        authorizationActivitySubComponent.inject(this);

        mvpPresenter.attachMvpView(this);
        mvpPresenter.navigateToProfiles();

        ButterKnife.bind(this);
        initViewPager();
        setOnPageChangeListener();
        setupTabs();
    }

    @Override
    protected void onDestroy() {
        mvpPresenter.detachMvpView();
        super.onDestroy();
    }

    @Override
    public void showProfilesView() {
        navigator.navigateToProfilesView();
    }

    @Override
    public void showAuthorizationProgress() {
        viewPager.setEnabled(false);
        tabLayout.setEnabled(false);
    }

    @Override
    public void hideAuthorizationProgress() {
        viewPager.setEnabled(true);
        tabLayout.setEnabled(true);
    }

    @Override
    public void showError(Error error) {

    }

    private void initActivitySubComponent() {
        authorizationActivitySubComponent =
                App.getApplicationComponent(this)
                        .plusLoginActivitySubComponent(new AuthorizationActivityModule(this));
    }

    private void initViewPager() {
        AuthorizationPagerAdapter pagerAdapter =
                new AuthorizationPagerAdapter(getSupportFragmentManager(), getApplicationContext());
        viewPager.setAdapter(pagerAdapter);
    }

    private void setOnPageChangeListener() {
        viewPager.addOnPageChangeListener(new OnPageChangeListenerAdapter() {
            @Override
            public void onPageSelected(int position) {
                keyboardUtil.hideKeyboard();
            }
        });
    }

    private void setupTabs() {
        tabLayout.setupWithViewPager(viewPager);
    }
}
