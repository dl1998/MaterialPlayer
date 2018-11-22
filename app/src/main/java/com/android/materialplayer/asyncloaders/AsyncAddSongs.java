package com.android.materialplayer.asyncloaders;

import android.content.Context;
import android.os.AsyncTask;

import com.android.materialplayer.Settings;
import com.android.materialplayer.adapters.TracksAdapter;
import com.android.materialplayer.comparators.SongComparator;
import com.android.materialplayer.dao.AlbumDAO;
import com.android.materialplayer.dao.ArtistDAO;
import com.android.materialplayer.dao.SongDAO;
import com.android.materialplayer.dao.impl.AlbumDAOImpl;
import com.android.materialplayer.dao.impl.ArtistDAOImpl;
import com.android.materialplayer.dao.impl.SongDAOImpl;
import com.android.materialplayer.entity.Album;
import com.android.materialplayer.models.ExtendedSong;
import com.android.materialplayer.provider.DBHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by dl1998 on 09.12.17.
 */

public class AsyncAddSongs extends AsyncTask<String, Void, ArrayList<ExtendedSong>> {

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
    protected ArrayList<ExtendedSong> doInBackground(String... params) {

        ArrayList<com.android.materialplayer.entity.Song> songsEntity = new ArrayList<>(songDAO.getAll());
        ArrayList<ExtendedSong> songsModel = new ArrayList<>();

        for (com.android.materialplayer.entity.Song song : songsEntity) {

            Long songId = song.getSongId();
            String songName = song.getSongName();
            Integer trackNumber = song.getTrackNumber();

            Album album = albumDAO.findById(song.getAlbumId());

            String albumName = null;
            String artPath = null;

            if (album != null) {
                albumName = album.getAlbumName();
                artPath = album.getAlbumArtPath();
            }

            String artistName = artistDAO.findById(song.getArtistId()).getArtistName();

            Integer year = song.getYear();
            if (year == null) year = 0;
            Integer duration = song.getDuration();

            songsModel.add(new ExtendedSong(songId, songName, trackNumber, albumName, artistName, year, duration, artPath));
        }

        final Integer ordinal = Settings.preferences.getInt(Settings.SONG_ORDER, 0);
        final Boolean reversed = Settings.preferences.getBoolean(Settings.SONG_REVERSED, false);

        Collections.sort(songsModel, new Comparator<ExtendedSong>() {
            @Override
            public int compare(ExtendedSong song, ExtendedSong song1) {
                return SongComparator.compareSong(song, song1, SongComparator.getOrderByOrdinal(ordinal));
            }
        });

        if (reversed) Collections.reverse(songsModel);

        return songsModel;
    }

    @Override
    protected void onPostExecute(ArrayList<ExtendedSong> songs) {
        super.onPostExecute(songs);

        this.adapter.setAdapter(songs);
        Settings.musicService.setList(songs);
        Settings.songs = songs;

    }
}
