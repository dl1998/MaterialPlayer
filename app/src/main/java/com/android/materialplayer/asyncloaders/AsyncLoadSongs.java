package com.android.materialplayer.asyncloaders;

import android.content.ContentResolver;
import android.content.Context;

import com.android.materialplayer.AsyncTaskEnded;
import com.android.materialplayer.DBHelper;
import com.android.materialplayer.Settings;
import com.android.materialplayer.dao.SongDAO;
import com.android.materialplayer.dao.impl.SongDAOImpl;
import com.android.materialplayer.dataloaders.LoadSongs;
import com.android.materialplayer.entity.Song;

import java.util.ArrayList;

/**
 * Created by dl1998 on 04.12.17.
 */

public class AsyncLoadSongs extends AsyncTaskEnded<String, Void, ArrayList<Song>> {

    private ContentResolver musicResolver;
    private SongDAO songDAO;

    public AsyncLoadSongs(Context context) {
        this.musicResolver = context.getContentResolver();

        DBHelper dbHelper = new DBHelper(context);
        this.songDAO = new SongDAOImpl(dbHelper.getWritableDatabase());
    }

    @Override
    protected ArrayList<Song> doInBackground(String... params) {

        LoadSongs loadSongs = new LoadSongs(musicResolver);

        ArrayList<Song> songs = new ArrayList<>(loadSongs.getSongsForCursor());

        for (Song song : songs) {
            if (params[0].equals(Settings.DB_ADD)) songDAO.add(song);
            if (params[0].equals(Settings.DB_UPDATE)) songDAO.update(song);
        }

        return songs;
    }
}
