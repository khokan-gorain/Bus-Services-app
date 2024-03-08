package com.khokan_gorain_nsubusservices_app.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.khokan_gorain_nsubusservices_app.AdapterClass.MyBusFindAd;
import com.khokan_gorain_nsubusservices_app.R;
import com.khokan_gorain_nsubusservices_app.databinding.FragmentMyBusBinding;

public class MyBusFragment extends Fragment {

    FragmentMyBusBinding binding;

    public MyBusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyBusBinding.inflate(inflater,container,false);


        binding.tabLyt.setTabGravity(TabLayout.GRAVITY_FILL);

        MyBusFindAd myBusFindAd = new MyBusFindAd(getChildFragmentManager());
        binding.viewPager.setAdapter(myBusFindAd);
        binding.tabLyt.setupWithViewPager(binding.viewPager);

        return binding.getRoot();
    }
}