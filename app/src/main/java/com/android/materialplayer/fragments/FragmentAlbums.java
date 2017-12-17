package com.android.materialplayer.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.materialplayer.R;
import com.android.materialplayer.adapters.AlbumsAdapter;
import com.android.materialplayer.asyncloaders.AsyncAddAlbums;
import com.android.materialplayer.models.Album;

import java.util.ArrayList;

/**
 * Created by dl1998 on 02.12.17.
 */

public class FragmentAlbums extends AbstractTabFragment {

    private AlbumsAdapter adapter;

    private RecyclerView listAlbums;

    public static FragmentAlbums newInstance(Context context) {
        FragmentAlbums fragment = new FragmentAlbums();

        fragment.context = context;
        fragment.setArguments(new Bundle());
        fragment.setTitle("Albums");

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_artists_albums_list, parent, false);

        listAlbums = view.findViewById(R.id.rvArtistsAlbums);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        adapter = new AlbumsAdapter(getActivity(), new ArrayList<Album>());
        int numColumn = 2;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            numColumn = 2;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            numColumn = 3;
        listAlbums.setLayoutManager(new GridLayoutManager(getActivity(), numColumn));
        listAlbums.setAdapter(adapter);

        new AsyncAddAlbums(getActivity(), adapter).execute();
    }

    /*@Override
    public void onStart() {
        super.onStart();
        LoadAlbums loadAlbums = new LoadAlbums(getActivity().getContentResolver());

        ArrayList<Album> albumsList = new ArrayList<>(loadAlbums.getAlbumsForCursor());

        for(Album album : albumsList){
            Log.d("Info", album.getAlbumTitle() + ", " + album.getArtistName() + ", " + album.getAlbumYear() + ", " + album.getSongCount());
        }

        AlbumsAdapter adapter = new AlbumsAdapter(getActivity(), R.layout.adapter_albums, albumsList);

        listView.setAdapter(adapter);
    }*/
}
