package com.android.materialplayer.asyncloaders;

import android.content.Context;
import android.os.AsyncTask;

import com.android.materialplayer.DBHelper;
import com.android.materialplayer.adapters.TracksAdapter;
import com.android.materialplayer.dao.AlbumDAO;
import com.android.materialplayer.dao.ArtistDAO;
import com.android.materialplayer.dao.SongDAO;
import com.android.materialplayer.dao.impl.AlbumDAOImpl;
import com.android.materialplayer.dao.impl.ArtistDAOImpl;
import com.android.materialplayer.dao.impl.SongDAOImpl;
import com.android.materialplayer.models.Song;

import java.util.ArrayList;

/**
 * Created by dl1998 on 09.12.17.
 */

public class AsyncAddSongs extends AsyncTask<String, Void, ArrayList<Song>> {

    private Context context;
    private TracksAdapter adapter;

    private SongDAO songDAO;
    private AlbumDAO albumDAO;
    private ArtistDAO artistDAO;

    public AsyncAddSongs(Context context, TracksAdapter adapter) {
        this.context = context;
        this.adapter = adapter;

        DBHelper dbHelper = new DBHelper(context);
        this.songDAO = new SongDAOImpl(dbHelper.getWritableDatabase());
        this.albumDAO = new AlbumDAOImpl(dbHelper.getWritableDatabase());
        this.artistDAO = new ArtistDAOImpl(dbHelper.getWritableDatabase());
    }

    @Override
    protected ArrayList<Song> doInBackground(String... params) {

        ArrayList<com.android.materialplayer.entity.Song> songsEntity = new ArrayList<>(songDAO.getAll());
        ArrayList<Song> songsModel = new ArrayList<>();

        for (com.android.materialplayer.entity.Song song : songsEntity) {

            Long songId = song.getSongId();
            String songName = song.getSongName();

            String artistName = artistDAO.findById(song.getArtistId()).getArtistName();
            String artPath = albumDAO.findById(song.getAlbumId()).getAlbumArtPath();

            songsModel.add(new Song(songId, songName, artistName, artPath));
        }

        return songsModel;
    }

    @Override
    protected void onPostExecute(ArrayList<Song> songs) {
        super.onPostExecute(songs);

        this.adapter.setAdapter(songs);
    }
}
