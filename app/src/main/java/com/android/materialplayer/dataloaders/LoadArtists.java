package com.android.materialplayer.dataloaders;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.android.materialplayer.entity.Artist;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dl1998 on 09.12.17.
 */

public class LoadArtists {

    private ContentResolver musicResolver;

    public LoadArtists(ContentResolver musicResolver) {
        this.musicResolver = musicResolver;
    }

    public List<Artist> getArtistsForCursor() {
        List<Artist> artists = new LinkedList<>();

        Uri uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
        Cursor cursor = musicResolver.query(uri, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Long artistId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Artists._ID));
                String artistName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
                Integer albumsCount = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS));
                Integer songCount = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS));

                artists.add(new Artist(artistId, artistName, albumsCount, songCount));
            } while (cursor.moveToNext());
        }

        return artists;
    }
}
