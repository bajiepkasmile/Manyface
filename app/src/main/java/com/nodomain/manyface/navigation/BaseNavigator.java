package com.nodomain.manyface.navigation;


import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;


public class BaseNavigator {

    private final AppCompatActivity activity;
    private final FragmentManager fragmentManager;

    protected BaseNavigator(AppCompatActivity activity) {
        this.activity = activity;
        fragmentManager = activity.getSupportFragmentManager();
    }

    protected void replaceFragment(Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(android.R.id.content, fragment)
                .commit();
    }

    protected void replaceFragmentAndAddToBackStack(Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(android.R.id.content, fragment)
                .addToBackStack(null)
                .commit();
    }

    protected void showDialogFragment(DialogFragment dialogFragment) {
        dialogFragment.show(fragmentManager, null);
    }

    protected void startActivity(Intent intent) {
        activity.startActivity(intent);
    }

    protected void startNewActivityAndFinishCurrent(Intent intent) {
        activity.startActivity(intent);
        activity.finish();
    }

    protected Intent createIntent(Class activityClass) {
        return new Intent(activity, activityClass);
    }

    protected void popBackStack() {
        fragmentManager.popBackStack();
    }
}
