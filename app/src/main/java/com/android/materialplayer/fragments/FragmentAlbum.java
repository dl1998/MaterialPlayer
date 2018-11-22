package com.android.materialplayer.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.materialplayer.R;
import com.android.materialplayer.Settings;
import com.android.materialplayer.activities.MainActivity;
import com.android.materialplayer.adapters.ItemClickListener;
import com.android.materialplayer.adapters.TracksInAlbumAdapter;
import com.android.materialplayer.dao.SongDAO;
import com.android.materialplayer.dao.impl.SongDAOImpl;
import com.android.materialplayer.models.ExtendedAlbum;
import com.android.materialplayer.models.ExtendedSong;
import com.android.materialplayer.models.Song;
import com.android.materialplayer.provider.DBHelper;

import java.util.ArrayList;

/**
 * Created by dl1998 on 02.01.18.
 */

public class FragmentAlbum extends Fragment {

    private RecyclerView recyclerView;

    private ExtendedAlbum album;

    private ArrayList<Song> songs;

    private Toolbar toolbar;
    private CollapsingToolbarLayout toolbarLayout;

    public FragmentAlbum() {
    }

    public static FragmentAlbum getInstance(ExtendedAlbum album) {
        FragmentAlbum fragment = new FragmentAlbum();
        Bundle bundle = new Bundle();
        fragment.album = album;

        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album, parent, false);

        recyclerView = view.findViewById(R.id.rvTracksInAlbum);

        final MainActivity activity = (MainActivity) getActivity();
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(album.getAlbumName());
        activity.setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.initFragmentWithViewPager();
                //activity.onBackPressed();
            }
        });

        toolbarLayout = view.findViewById(R.id.toolbar_layout);
        if (album.getAlbumCover() != null)
            toolbarLayout.setBackground(Drawable.createFromPath(album.getAlbumCover()));
        else
            toolbarLayout.setBackground(getResources().getDrawable(R.drawable.cover_not_available));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        DBHelper dbHelper = new DBHelper(getActivity());
        SongDAO songDAO = new SongDAOImpl(dbHelper.getWritableDatabase());
        final ArrayList<com.android.materialplayer.entity.Song> songs = new ArrayList<>(songDAO.getAllByAlbum(album.getId()));
        this.songs = new ArrayList<>();
        for (com.android.materialplayer.entity.Song song : songs) {
            this.songs.add(Song.getFromSong(song));
        }

        TracksInAlbumAdapter adapter = new TracksInAlbumAdapter(getActivity(), this.songs);
        adapter.setOnItemClickListener(new ItemClickListener() {

            private Boolean firstStart = true;

            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (!isLongClick) {
                    if (firstStart) {
                        ArrayList<ExtendedSong> extendedSongs = getSongs();
                        Settings.musicService.setList(extendedSongs);
                        firstStart = false;
                    }
                    if (Settings.musicService != null) {
                        Settings.musicService.stopSong();
                        Settings.musicService.setSong(position);
                        Settings.musicService.playSong();
                        MainActivity activity = (MainActivity) FragmentAlbum.this.getActivity();
                        activity.initSeekBar();
                    }
                }
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    private ArrayList<ExtendedSong> getSongs() {
        ArrayList<ExtendedSong> extendedSongs = new ArrayList<>();
        for (Song song : FragmentAlbum.this.songs) {
            ExtendedSong extendedSong = new ExtendedSong(song);
            extendedSong.setAlbumName(album.getAlbumName());
            extendedSong.setArtPath(album.getAlbumCover());
            extendedSong.setArtistName(album.getArtistName());
            extendedSongs.add(extendedSong);
        }

        return extendedSongs;
    }
}
