package com.android.materialplayer.dataloaders;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.android.materialplayer.entity.Album;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dl1998 on 04.12.17.
 */

public class LoadAlbums {

    private ContentResolver musicResolver;

    public LoadAlbums(ContentResolver musicResolver) {
        this.musicResolver = musicResolver;
    }

    public List<Album> getAlbumsForCursor() {

        ArrayList<Album> albums = new ArrayList<>();

        Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        Cursor cursor = musicResolver.query(uri, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {

            do {

                Long albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Albums._ID));
                Long artistId = cursor.getLong(cursor.getColumnIndex("artist_id"));
                String albumName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
                String albumArtPath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                Integer albumYear = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Albums.LAST_YEAR));
                Integer songsCount = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS));

                albums.add(new Album(albumId, artistId, albumName, albumArtPath, albumYear, songsCount));
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return albums;
    }

    public String getAlbumArt(Long albumId) {
        Cursor cursor = musicResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                String.format("%s = ?", MediaStore.Audio.Albums._ID),
                new String[]{String.valueOf(albumId)},
                null);

        String drawablePath = null;

        if (cursor != null && cursor.moveToFirst()) {
            drawablePath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
        }

        if (cursor != null) {
            cursor.close();
        }

        return drawablePath;
    }
}
