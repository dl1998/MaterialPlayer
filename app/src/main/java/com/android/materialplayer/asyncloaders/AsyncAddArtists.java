package com.android.materialplayer.asyncloaders;

import android.content.Context;

import com.android.materialplayer.AsyncTaskEnded;
import com.android.materialplayer.Settings;
import com.android.materialplayer.adapters.ArtistsAdapter;
import com.android.materialplayer.comparators.ArtistComparator;
import com.android.materialplayer.dao.ArtistDAO;
import com.android.materialplayer.dao.impl.ArtistDAOImpl;
import com.android.materialplayer.models.Artist;
import com.android.materialplayer.provider.DBHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by dl1998 on 12.12.17.
 */

public class AsyncAddArtists extends AsyncTaskEnded<String, Void, ArrayList<Artist>> {

    private Context context;
    private ArtistsAdapter adapter;

    private ArtistDAO artistDAO;

    public AsyncAddArtists(Context context, ArtistsAdapter adapter) {
        this.context = context;
        this.adapter = adapter;

        DBHelper dbHelper = new DBHelper(context);
        this.artistDAO = new ArtistDAOImpl(dbHelper.getWritableDatabase());
    }

    @Override
    protected ArrayList<Artist> doInBackground(String... strings) {

        ArrayList<com.android.materialplayer.entity.Artist> artistsEntity = new ArrayList<>(artistDAO.getAll());
        ArrayList<Artist> artistsModel = new ArrayList<>();

        for (com.android.materialplayer.entity.Artist artist : artistsEntity) {
            Long artistId = artist.getArtistId();
            String artistName = artist.getArtistName();
            Integer albumCount = artist.getAlbumCount();
            Integer songCount = artist.getSongCount();
            String artPath = "";

            artistsModel.add(new Artist(artistId, artistName, albumCount, songCount, artPath));
        }

        final Integer ordinal = Settings.preferences.getInt(Settings.ARTIST_ORDER, 0);
        final Boolean reversed = Settings.preferences.getBoolean(Settings.ARTIST_REVERSED, false);

        Collections.sort(artistsModel, new Comparator<Artist>() {
            @Override
            public int compare(Artist artist, Artist artist1) {
                return ArtistComparator.compareArtist(artist, artist1, ArtistComparator.getOrderByOrdinal(ordinal));
            }
        });

        if (reversed) Collections.reverse(artistsModel);

        return artistsModel;
    }

    @Override
    public void onPostExecute(ArrayList<Artist> artists) {
        this.adapter.setAdapter(artists);
        super.onPostExecute(artists);

    }
}
