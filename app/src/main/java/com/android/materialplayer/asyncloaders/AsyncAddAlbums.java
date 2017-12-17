package com.android.materialplayer.asyncloaders;

import android.content.Context;
import android.os.AsyncTask;

import com.android.materialplayer.DBHelper;
import com.android.materialplayer.adapters.AlbumsAdapter;
import com.android.materialplayer.dao.AlbumDAO;
import com.android.materialplayer.dao.ArtistDAO;
import com.android.materialplayer.dao.impl.AlbumDAOImpl;
import com.android.materialplayer.dao.impl.ArtistDAOImpl;
import com.android.materialplayer.models.Album;

import java.util.ArrayList;

/**
 * Created by dl1998 on 17.12.17.
 */

public class AsyncAddAlbums extends AsyncTask<String, Void, ArrayList<Album>> {

    private Context context;
    private AlbumsAdapter adapter;

    private AlbumDAO albumDAO;
    private ArtistDAO artistDAO;

    public AsyncAddAlbums(Context context, AlbumsAdapter adapter) {
        this.context = context;
        this.adapter = adapter;

        DBHelper dbHelper = new DBHelper(context);
        this.albumDAO = new AlbumDAOImpl(dbHelper.getWritableDatabase());
        this.artistDAO = new ArtistDAOImpl(dbHelper.getWritableDatabase());
    }

    @Override
    protected ArrayList<Album> doInBackground(String... strings) {

        ArrayList<com.android.materialplayer.entity.Album> albumsEntity = new ArrayList<>(albumDAO.getAll());
        ArrayList<Album> albumsModel = new ArrayList<>();

        for (com.android.materialplayer.entity.Album album : albumsEntity) {

            Long albumId = album.getAlbumId();
            String albumName = album.getAlbumName();
            String artistName = artistDAO.findById(album.getArtistId()).getArtistName();
            String artPath = album.getAlbumArtPath();

            albumsModel.add(new Album(albumId, albumName, artistName, artPath));
        }

        return albumsModel;
    }

    @Override
    protected void onPostExecute(ArrayList<Album> albums) {
        super.onPostExecute(albums);

        this.adapter.setAdapter(albums);
    }
}
