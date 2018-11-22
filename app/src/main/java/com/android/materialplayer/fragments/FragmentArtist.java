package com.android.materialplayer.fragments;

import android.database.sqlite.SQLiteDatabase;
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
import com.android.materialplayer.adapters.MiniAlbumAdapter;
import com.android.materialplayer.adapters.TracksAdapter;
import com.android.materialplayer.dao.AlbumDAO;
import com.android.materialplayer.dao.SongDAO;
import com.android.materialplayer.dao.impl.AlbumDAOImpl;
import com.android.materialplayer.dao.impl.SongDAOImpl;
import com.android.materialplayer.entity.Song;
import com.android.materialplayer.models.Album;
import com.android.materialplayer.models.Artist;
import com.android.materialplayer.models.ExtendedSong;
import com.android.materialplayer.provider.DBHelper;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dl1998 on 15.02.18.
 */

public class FragmentArtist extends Fragment {

    private RecyclerView rvAlbums;
    private RecyclerView rvTracks;

    private MiniAlbumAdapter miniAlbumAdapter;
    private TracksAdapter tracksAdapter;

    private AlbumDAO albumDAO;
    private SongDAO songDAO;

    private Artist artist;

    private List<Album> albums;
    private List<ExtendedSong> songs;

    public static FragmentArtist getInstance(Artist artist) {
        FragmentArtist fragmentArtist = new FragmentArtist();
        Bundle bundle = new Bundle();
        fragmentArtist.artist = artist;

        fragmentArtist.setArguments(bundle);

        return fragmentArtist;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist, container, false);

        rvAlbums = view.findViewById(R.id.rvAlbums);
        rvTracks = view.findViewById(R.id.rvTracks);

        MainActivity activity = (MainActivity) getActivity();
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(artist.getArtistName());
        activity.setSupportActionBar(toolbar);

        CollapsingToolbarLayout toolbarLayout = view.findViewById(R.id.toolbar_layout);
        if (artist.getArtPath() != null)
            toolbarLayout.setBackground(Drawable.createFromPath(artist.getArtistName()));
        else toolbarLayout.setBackground(getResources().getDrawable(R.drawable.unknown_artist));

        setHasOptionsMenu(false);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        initFromDB();

        miniAlbumAdapter = new MiniAlbumAdapter(getActivity(), albums);
        tracksAdapter = new TracksAdapter(getActivity(), songs);

        rvAlbums.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvAlbums.setAdapter(miniAlbumAdapter);
        rvTracks.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvTracks.setAdapter(tracksAdapter);

        tracksAdapter.setOnItemClickListener(new ItemClickListener() {

            private Boolean firstStart = true;

            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (!isLongClick) {
                    if (firstStart) {
                        Settings.musicService.setList(FragmentArtist.this.songs);
                        firstStart = false;
                    }
                    if (Settings.musicService != null) {
                        Settings.musicService.stopSong();
                        Settings.musicService.setSong(position);
                        Settings.musicService.playSong();
                        MainActivity activity = (MainActivity) FragmentArtist.this.getActivity();
                        activity.initSeekBar();
                    }
                }
            }
        });
    }

    private void initFromDB() {
        DBHelper dbHelper = new DBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        albumDAO = new AlbumDAOImpl(db);
        songDAO = new SongDAOImpl(db);

        initAlbums(albumDAO.getAllByArtistId(artist.getArtistId()));
        initSongs(songDAO.getAllByArtist(artist.getArtistId()));
    }

    private void initAlbums(List<com.android.materialplayer.entity.Album> albums) {
        this.albums = new LinkedList<>();
        for (com.android.materialplayer.entity.Album album : albums) {
            this.albums.add(new Album(album));
        }
    }

    private void initSongs(List<Song> songs) {
        this.songs = new LinkedList<>();
        for (Song song : songs) {
            this.songs.add(getSong(song));
        }
    }

    private ExtendedSong getSong(Song song) {
        ExtendedSong songModel = new ExtendedSong();
        songModel.setSongId(song.getSongId());
        songModel.setSongName(song.getSongName());
        songModel.setDuration(song.getDuration());
        songModel.setTrackNumber(song.getTrackNumber());
        songModel.setYear(song.getYear());
        songModel.setArtistName(artist.getArtistName());
        com.android.materialplayer.entity.Album album = albumDAO.findById(song.getAlbumId());
        songModel.setAlbumName(album.getAlbumName());
        songModel.setArtPath(album.getAlbumArtPath());

        return songModel;
    }

}
