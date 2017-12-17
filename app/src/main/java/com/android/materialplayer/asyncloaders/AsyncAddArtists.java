package com.android.materialplayer.asyncloaders;

import android.content.Context;
import android.os.AsyncTask;

import com.android.materialplayer.DBHelper;
import com.android.materialplayer.adapters.ArtistsAdapter;
import com.android.materialplayer.dao.ArtistDAO;
import com.android.materialplayer.dao.impl.ArtistDAOImpl;
import com.android.materialplayer.models.Artist;

import java.util.ArrayList;

/**
 * Created by dl1998 on 12.12.17.
 */

public class AsyncAddArtists extends AsyncTask<String, Void, ArrayList<Artist>> {

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
            String artPath = "";

            artistsModel.add(new Artist(artistId, artistName, artPath));
        }

        return artistsModel;
    }

    @Override
    public void onPostExecute(ArrayList<Artist> artists) {
        super.onPostExecute(artists);

        this.adapter.setAdapter(artists);
    }
}
