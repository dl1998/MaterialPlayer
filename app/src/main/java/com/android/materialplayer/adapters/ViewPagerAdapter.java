package com.android.materialplayer.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;

import com.android.materialplayer.fragments.AbstractTabFragment;
import com.android.materialplayer.fragments.FragmentAlbums;
import com.android.materialplayer.fragments.FragmentArtists;
import com.android.materialplayer.fragments.FragmentTracks;

/**
 * Created by dl1998 on 02.12.17.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private SparseArray<AbstractTabFragment> tabs;
    private Context context;

    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        initTabsMap();
    }

    @Override
    public Fragment getItem(int position) {
        return tabs.get(position);
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position).getTitle();
    }

    private void initTabsMap() {
        tabs = new SparseArray<>();
        tabs.put(0, FragmentTracks.newInstance(context));
        tabs.put(1, FragmentAlbums.newInstance(context));
        tabs.put(2, FragmentArtists.newInstance(context));
    }

}
