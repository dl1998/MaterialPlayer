package com.android.materialplayer.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.materialplayer.R;
import com.android.materialplayer.activities.MainActivity;
import com.android.materialplayer.adapters.ViewPagerAdapter;

/**
 * Created by dl1998 on 28.01.18.
 */

public class FragmentMain extends Fragment {

    private MainActivity activity;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        activity = (MainActivity) getActivity();

        toolbar = view.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);

        viewPager = view.findViewById(R.id.vpCategories);
        tabLayout = view.findViewById(R.id.tabLayout);

        initTabs();

        activity.initActionBarDrawerToggle(toolbar);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initTabs() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager(), getContext());
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
    }
}
