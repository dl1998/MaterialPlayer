package com.android.materialplayer.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.materialplayer.R;
import com.android.materialplayer.adapters.ArtistsAdapter;
import com.android.materialplayer.asyncloaders.AsyncAddArtists;
import com.android.materialplayer.models.Artist;

import java.util.ArrayList;

/**
 * Created by dl1998 on 02.12.17.
 */

public class FragmentArtists extends AbstractTabFragment {

    private ArtistsAdapter adapter;

    private RecyclerView listArtists;

    public static FragmentArtists newInstance(Context context) {
        FragmentArtists fragment = new FragmentArtists();

        fragment.context = context;
        fragment.setArguments(new Bundle());
        fragment.setTitle(context.getString(R.string.artists));

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_artists_albums_list, parent, false);

        listArtists = view.findViewById(R.id.rvArtistsAlbums);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        adapter = new ArtistsAdapter(getActivity(), new ArrayList<Artist>());
        int numColumn = 2;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            numColumn = 2;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            numColumn = 3;
        listArtists.setLayoutManager(new GridLayoutManager(getActivity(), numColumn));
        listArtists.setAdapter(adapter);

        new AsyncAddArtists(getActivity(), adapter).execute();
    }
}
