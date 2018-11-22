package com.android.materialplayer.asyncloaders;

import android.content.ContentResolver;
import android.content.Context;

import com.android.materialplayer.AsyncTaskEnded;
import com.android.materialplayer.Settings;
import com.android.materialplayer.dao.ArtistDAO;
import com.android.materialplayer.dao.impl.ArtistDAOImpl;
import com.android.materialplayer.dataloaders.LoadArtists;
import com.android.materialplayer.entity.Artist;
import com.android.materialplayer.provider.DBHelper;

import java.util.ArrayList;

/**
 * Created by dl1998 on 09.12.17.
 */

public class AsyncLoadArtists extends AsyncTaskEnded<String, Void, ArrayList<Artist>> {

    private ContentResolver musicResolver;
    private ArtistDAO artistDAO;

    public AsyncLoadArtists(Context context) {
        this.musicResolver = context.getContentResolver();

        DBHelper dbHelper = new DBHelper(context);
        this.artistDAO = new ArtistDAOImpl(dbHelper.getWritableDatabase());
    }

    @Override
    protected ArrayList<Artist> doInBackground(String... params) {
        LoadArtists loadArtists = new LoadArtists(musicResolver);

        ArrayList<Artist> artists = new ArrayList<>(loadArtists.getArtistsForCursor());

        for (Artist artist : artists) {
            if (params[0].equals(Settings.DB_ADD)) artistDAO.add(artist);
            if (params[0].equals(Settings.DB_UPDATE)) artistDAO.update(artist);
        }

        return artists;
    }
}
