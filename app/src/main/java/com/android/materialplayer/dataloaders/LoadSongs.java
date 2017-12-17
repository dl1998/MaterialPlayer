package com.android.materialplayer.dataloaders;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.android.materialplayer.entity.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dl1998 on 04.12.17.
 */

public class LoadSongs {

    private ContentResolver musicResolver;

    public LoadSongs(ContentResolver musicResolver) {
        this.musicResolver = musicResolver;
    }

    public List<Song> getSongsForCursor() {

        ArrayList<Song> songs = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.ARTIST_ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.TRACK
        };
        Cursor cursor = musicResolver.query(uri, projection, "duration > 20000", null, null);

        if (cursor != null && cursor.moveToFirst()) {

            do {
                Long songId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                Long albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                Long artistId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));
                String songName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                Integer duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                Integer trackNumber = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.TRACK));

                songs.add(new Song(songId, albumId, artistId, songName, duration, trackNumber));
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return songs;
    }
}
