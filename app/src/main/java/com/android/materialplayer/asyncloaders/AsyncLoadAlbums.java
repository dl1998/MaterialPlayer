package com.android.materialplayer.asyncloaders;

import android.content.ContentResolver;
import android.content.Context;

import com.android.materialplayer.AsyncTaskEnded;
import com.android.materialplayer.Settings;
import com.android.materialplayer.dao.AlbumDAO;
import com.android.materialplayer.dao.impl.AlbumDAOImpl;
import com.android.materialplayer.dataloaders.LoadAlbums;
import com.android.materialplayer.entity.Album;
import com.android.materialplayer.provider.DBHelper;

import java.util.ArrayList;

/**
 * Created by dl1998 on 04.12.17.
 */

public class AsyncLoadAlbums extends AsyncTaskEnded<String, Void, ArrayList<Album>> {

    private ContentResolver musicResolver;
    private AlbumDAO albumDAO;

    public AsyncLoadAlbums(Context context) {
        this.musicResolver = context.getContentResolver();

        DBHelper dbHelper = new DBHelper(context);
        this.albumDAO = new AlbumDAOImpl(dbHelper.getWritableDatabase());
    }

    @Override
    protected ArrayList<Album> doInBackground(String... params) {
        LoadAlbums loadAlbums = new LoadAlbums(musicResolver);

        ArrayList<Album> albums = new ArrayList<>(loadAlbums.getAlbumsForCursor());

        for (Album album : albums) {
            if (params[0].equals(Settings.DB_ADD)) albumDAO.add(album);
            if (params[0].equals(Settings.DB_UPDATE)) albumDAO.update(album);
        }

        return albums;
    }
}
