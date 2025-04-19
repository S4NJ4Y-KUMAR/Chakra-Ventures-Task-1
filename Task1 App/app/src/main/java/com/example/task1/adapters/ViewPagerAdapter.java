package com.example.task1.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.task1.fragments.AppListFragment;
import com.example.task1.fragments.PermissionsFragment;
import com.example.task1.fragments.RestApiFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private static final int NUM_TABS = 3;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new RestApiFragment();
            case 1:
                return new PermissionsFragment();
            case 2:
                return new AppListFragment();
            default:
                throw new IllegalArgumentException("Invalid position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return NUM_TABS;
    }
}