package com.android.materialplayer.activities;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.materialplayer.R;
import com.android.materialplayer.adapters.TracksInAlbumAdapter;
import com.android.materialplayer.dao.AlbumDAO;
import com.android.materialplayer.dao.SongDAO;
import com.android.materialplayer.dao.impl.AlbumDAOImpl;
import com.android.materialplayer.dao.impl.SongDAOImpl;
import com.android.materialplayer.entity.Song;
import com.android.materialplayer.models.Album;
import com.android.materialplayer.provider.DBHelper;

import java.util.ArrayList;

/**
 * Created by dl1998 on 22.01.18.
 */

public class AlbumActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CollapsingToolbarLayout toolbarLayout;
    public static String ALBUM_ID = "album_id";

    private RecyclerView recyclerView;

    private Album album;
    private ArrayList<com.android.materialplayer.models.Song> songs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        Long album_id = getIntent().getExtras().getLong(ALBUM_ID);

        DBHelper dbHelper = new DBHelper(this);
        AlbumDAO albumDAO = new AlbumDAOImpl(dbHelper.getWritableDatabase());
        com.android.materialplayer.entity.Album albumEntity = albumDAO.findById(album_id);
        album = new Album(albumEntity);

        toolbarLayout = findViewById(R.id.toolbar_layout);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(album.getAlbumName());
        setSupportActionBar(toolbar);

        toolbarLayout.setBackground(Drawable.createFromPath(album.getAlbumCover()));

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlbumActivity.this.finish();
            }
        });

        recyclerView = findViewById(R.id.rvTracksInAlbum);
    }

    @Override
    protected void onStart() {
        super.onStart();

        DBHelper dbHelper = new DBHelper(AlbumActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SongDAO songDAO = new SongDAOImpl(db);
        ArrayList<Song> songs = new ArrayList<>(songDAO.getAllByAlbum(album.getId()));
        this.songs = new ArrayList<>();
        for (com.android.materialplayer.entity.Song song : songs) {
            this.songs.add(com.android.materialplayer.models.Song.getFromSong(song));
        }

        TracksInAlbumAdapter adapter = new TracksInAlbumAdapter(this, this.songs);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
