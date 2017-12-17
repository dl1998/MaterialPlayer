package com.android.materialplayer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.materialplayer.R;
import com.android.materialplayer.adapters.TracksAdapter;
import com.android.materialplayer.asyncloaders.AsyncAddSongs;
import com.android.materialplayer.models.Song;

import java.util.ArrayList;

/**
 * Created by dl1998 on 02.12.17.
 */

public class FragmentTracks extends AbstractTabFragment {

    private TracksAdapter adapter;

    private RecyclerView listTracks;

    public static FragmentTracks newInstance(Context context) {
        FragmentTracks fragment = new FragmentTracks();

        fragment.context = context;
        fragment.setArguments(new Bundle());
        fragment.setTitle(context.getString(R.string.tracks));

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_tracks_list, parent, false);

        listTracks = view.findViewById(R.id.lvTracks);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        adapter = new TracksAdapter(getActivity(), new ArrayList<Song>());
        listTracks.setAdapter(adapter);
        listTracks.setLayoutManager(new LinearLayoutManager(getActivity()));

        new AsyncAddSongs(getActivity(), adapter).execute();
    }
}
