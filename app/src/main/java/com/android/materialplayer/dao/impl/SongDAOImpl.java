package com.android.materialplayer.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.android.materialplayer.dao.SongDAO;
import com.android.materialplayer.entity.Song;

import java.util.List;

/**
 * Created by dl1998 on 05.12.17.
 */

public class SongDAOImpl extends MainDAOImpl<Song> implements SongDAO {

    private static final String TABLE_NAME = "song";

    private SQLiteDatabase db;

    public SongDAOImpl(SQLiteDatabase db) {
        super(db, TABLE_NAME);
        this.db = db;
    }

    @Override
    Song getByCursor(Cursor cursor) {
        Song song = new Song();

        song.setSongId(cursor.getLong(cursor.getColumnIndex(cursor.getColumnName(0))));
        song.setAlbumId(cursor.getLong(cursor.getColumnIndex(cursor.getColumnName(1))));
        song.setArtistId(cursor.getLong(cursor.getColumnIndex(cursor.getColumnName(2))));
        song.setSongName(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(3))));
        song.setDuration(cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(4))));
        song.setTrackNumber(cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(5))));

        return song;
    }

    @Override
    SQLiteStatement bindAdd(SQLiteStatement statement, Song song) {
        statement.clearBindings();

        statement.bindLong(1, song.getSongId());
        statement.bindLong(2, song.getAlbumId());
        statement.bindLong(3, song.getArtistId());
        statement.bindString(4, song.getSongName());
        statement.bindLong(5, song.getDuration());
        statement.bindLong(6, song.getTrackNumber());

        return statement;
    }

    @Override
    SQLiteStatement bindUpdate(SQLiteStatement statement, Song song) {
        statement.clearBindings();

        statement.bindLong(1, song.getAlbumId());
        statement.bindLong(2, song.getArtistId());
        statement.bindString(3, song.getSongName());
        statement.bindLong(4, song.getDuration());
        statement.bindLong(5, song.getTrackNumber());
        statement.bindLong(6, song.getSongId());

        return statement;
    }

    @Override
    public void add(Song song) {
        String SQL = String.format("INSERT INTO %s (song_id, album_id, artist_id, song_name, duration, track_number) VALUES(?, ?, ?, ?, ?, ?)", TABLE_NAME);
        try {
            super.add(song, SQL);
        } catch (Exception e) {
            update(song);
        }
    }

    @Override
    public void update(Song song) {
        String SQL = String.format("UPDATE %s SET album_id = ?, artist_id = ?, song_name = ?, duration = ?, track_number = ? WHERE song_id = ?", TABLE_NAME);
        super.update(song, SQL);
    }

    @Override
    public void remove(Long id) {
        super.remove(String.format("DELETE FROM %s WHERE song_id = %d", TABLE_NAME, id));
    }

    @Override
    public void remove(Song song) {
        remove(song.getSongId());
    }

    @Override
    public void removeAll() {
        super.remove(String.format("DELETE FROM %s", TABLE_NAME));
    }

    @Override
    public Song findById(Long id) {
        return super.findById("song_id = ?", id);
    }

    @Override
    public List<Song> getAll() {
        return super.getAll();
    }
}
