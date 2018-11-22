package com.android.materialplayer.asyncloaders;

import android.content.Context;
import android.os.AsyncTask;

import com.android.materialplayer.Settings;
import com.android.materialplayer.adapters.AlbumsAdapter;
import com.android.materialplayer.comparators.AlbumComparator;
import com.android.materialplayer.dao.AlbumDAO;
import com.android.materialplayer.dao.ArtistDAO;
import com.android.materialplayer.dao.impl.AlbumDAOImpl;
import com.android.materialplayer.dao.impl.ArtistDAOImpl;
import com.android.materialplayer.models.ExtendedAlbum;
import com.android.materialplayer.provider.DBHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by dl1998 on 17.12.17.
 */

public class AsyncAddAlbums extends AsyncTask<String, Void, ArrayList<ExtendedAlbum>> {

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
    protected ArrayList<ExtendedAlbum> doInBackground(String... strings) {

        ArrayList<com.android.materialplayer.entity.Album> albumsEntity = new ArrayList<>(albumDAO.getAll());
        ArrayList<ExtendedAlbum> albumsModel = new ArrayList<>();

        for (com.android.materialplayer.entity.Album album : albumsEntity) {

            Long albumId = album.getAlbumId();
            String albumName = album.getAlbumName();
            String artistName = artistDAO.findById(album.getArtistId()).getArtistName();
            String artPath = album.getAlbumArtPath();

            Integer songCount = album.getSongCount();

            albumsModel.add(new ExtendedAlbum(albumId, albumName, artistName, songCount, artPath));
        }

        final Integer ordinal = Settings.preferences.getInt(Settings.ALBUM_ORDER, 0);
        final Boolean reversed = Settings.preferences.getBoolean(Settings.ALBUM_REVERSED, false);

        Collections.sort(albumsModel, new Comparator<ExtendedAlbum>() {
            @Override
            public int compare(ExtendedAlbum album, ExtendedAlbum album1) {
                return AlbumComparator.compareAlbum(album, album1, AlbumComparator.getOrderByOrdinal(ordinal));
            }
        });

        if (reversed) Collections.reverse(albumsModel);

        return albumsModel;
    }

    @Override
    protected void onPostExecute(ArrayList<ExtendedAlbum> albums) {
        super.onPostExecute(albums);

        this.adapter.setAdapter(albums);
    }
}
