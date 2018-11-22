package com.android.materialplayer.activities;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.materialplayer.R;
import com.android.materialplayer.dao.AlbumDAO;
import com.android.materialplayer.dao.ArtistDAO;
import com.android.materialplayer.dao.SongDAO;
import com.android.materialplayer.dao.impl.AlbumDAOImpl;
import com.android.materialplayer.dao.impl.ArtistDAOImpl;
import com.android.materialplayer.dao.impl.SongDAOImpl;
import com.android.materialplayer.models.Album;
import com.android.materialplayer.models.Artist;
import com.android.materialplayer.models.Song;
import com.android.materialplayer.provider.DBHelper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by dl1998 on 06.01.18.
 */

public class SearchActivity extends AppCompatActivity implements MenuItem.OnActionExpandListener {

    ArrayList<Song> modelSong;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchActivity.this.finish();
            }
        });

        final SearchView searchView = findViewById(R.id.search_view);

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SongDAO songDAO = new SongDAOImpl(db);
        AlbumDAO albumDAO = new AlbumDAOImpl(db);
        ArtistDAO artistDAO = new ArtistDAOImpl(db);

        ArrayList<com.android.materialplayer.entity.Song> songs = new ArrayList<>(songDAO.getAll());
        ArrayList<com.android.materialplayer.entity.Album> albums = new ArrayList<>(albumDAO.getAll());
        ArrayList<com.android.materialplayer.entity.Artist> artists = new ArrayList<>(artistDAO.getAll());

        modelSong = new ArrayList<>();

        for (com.android.materialplayer.entity.Song song : songs) {
            modelSong.add(new Song(song.getSongId(), song.getSongName(), song.getDuration(), song.getTrackNumber()));
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Song> newSongs = new ArrayList<>(filterSong(modelSong, newText));

                for (Song song : newSongs) {
                    Log.d("Song", song.getSongName());
                }

                return true;
            }
        });

        /*MenuItem item;
        item.setOnActionExpandListener()*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem menuItem) {
        //menuItem.set
        return false;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        return false;
    }

    private List<Song> filterSong(List<Song> songs, String query) {
        query = query.toLowerCase();
        List<Song> songsResult = new LinkedList<>();
        for (Song song : songs) {
            if (song.getSongName().toLowerCase().contains(query)) {
                songsResult.add(song);
            }
        }
        return songsResult;
    }

    private List<Album> filterAlbum(List<Album> albums, String query) {
        query = query.toLowerCase();
        List<Album> albumsResult = new LinkedList<>();
        for (Album album : albums) {
            if (album.getAlbumName().toLowerCase().contains(query)) {
                albumsResult.add(album);
            }
        }
        return albumsResult;
    }

    private List<Artist> filterArtist(List<Artist> artists, String query) {
        query = query.toLowerCase();
        List<Artist> artistsResult = new LinkedList<>();
        for (Artist artist : artists) {
            if (artist.getArtistName().toLowerCase().contains(query)) {
                artistsResult.add(artist);
                ;
            }
        }
        return artistsResult;
    }
}
