package com.android.materialplayer.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.android.materialplayer.dao.ArtistDAO;
import com.android.materialplayer.entity.Artist;

import java.util.List;

/**
 * Created by dl1998 on 06.12.17.
 */

public class ArtistDAOImpl extends MainDAOImpl<Artist> implements ArtistDAO {

    private static final String TABLE_NAME = "artist";

    private SQLiteDatabase db;

    public ArtistDAOImpl(SQLiteDatabase db) {
        super(db, TABLE_NAME);
        this.db = db;
    }

    @Override
    Artist getByCursor(Cursor cursor) {
        Artist artist = new Artist();

        artist.setArtistId(cursor.getLong(cursor.getColumnIndex(cursor.getColumnName(0))));
        artist.setArtistName(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));
        artist.setAlbumCount(cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(2))));
        artist.setSongCount(cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(3))));

        return artist;
    }

    @Override
    SQLiteStatement bindAdd(SQLiteStatement statement, Artist artist) {
        statement.clearBindings();

        statement.bindLong(1, artist.getArtistId());
        statement.bindString(2, artist.getArtistName());
        statement.bindLong(3, artist.getAlbumCount());
        statement.bindLong(4, artist.getSongCount());

        return statement;
    }

    @Override
    SQLiteStatement bindUpdate(SQLiteStatement statement, Artist artist) {
        statement.clearBindings();

        statement.bindString(1, artist.getArtistName());
        statement.bindLong(2, artist.getAlbumCount());
        statement.bindLong(3, artist.getSongCount());
        statement.bindLong(4, artist.getArtistId());

        return statement;
    }

    @Override
    public void add(Artist artist) {
        String SQL = String.format("INSERT INTO %s (artist_id, artist_name, album_count, song_count) VALUES(?, ?, ?, ?)", TABLE_NAME);
        super.add(artist, SQL);
    }

    @Override
    public void update(Artist artist) {
        String SQL = String.format("UPDATE %s SET artist_name = ?, album_count = ?, song_count = ? WHERE artist_id = ?", TABLE_NAME);
        super.update(artist, SQL);
    }

    @Override
    public void remove(Long id) {
        super.remove(String.format("DELETE FROM %s WHERE artist_id = %d", TABLE_NAME, id));
    }

    @Override
    public void remove(Artist artist) {
        remove(artist.getArtistId());
    }

    @Override
    public void removeAll() {
        super.remove(String.format("DELETE FROM %s", TABLE_NAME));
    }

    @Override
    public Artist findById(Long id) {
        return super.findById("artist_id = ?", id);
    }

    @Override
    public List<Artist> getAll() {
        return super.getAll();
    }
}
