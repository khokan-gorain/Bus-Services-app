package com.khokan_gorain_nsubusservices_app.AdapterClass;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.khokan_gorain_nsubusservices_app.Fragments.FindBusWithAreaNameFragment;
import com.khokan_gorain_nsubusservices_app.Fragments.FindBusWithBusNumberFragment;

public class MyBusFindAd extends FragmentPagerAdapter {

    public MyBusFindAd(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return new FindBusWithAreaNameFragment();
        } else {
            return new FindBusWithBusNumberFragment();
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0){
            return "Area wise";
        } else {
            return "Number wise";
        }
    }
}
