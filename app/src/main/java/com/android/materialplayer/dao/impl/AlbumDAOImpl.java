package com.android.materialplayer.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.android.materialplayer.dao.AlbumDAO;
import com.android.materialplayer.entity.Album;

import java.util.List;

/**
 * Created by dl1998 on 06.12.17.
 */

public class AlbumDAOImpl extends MainDAOImpl<Album> implements AlbumDAO {

    private static final String TABLE_NAME = "album";

    private SQLiteDatabase db;

    public AlbumDAOImpl(SQLiteDatabase db) {
        super(db, TABLE_NAME);
        this.db = db;
    }

    @Override
    Album getByCursor(Cursor cursor) {
        Album album = new Album();

        album.setAlbumId(cursor.getLong(cursor.getColumnIndex(cursor.getColumnName(0))));
        album.setArtistId(cursor.getLong(cursor.getColumnIndex(cursor.getColumnName(1))));
        album.setAlbumName(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2))));
        album.setAlbumArtPath(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(3))));
        album.setAlbumYear(cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(4))));
        album.setSongCount(cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(5))));

        return album;
    }

    @Override
    SQLiteStatement bindAdd(SQLiteStatement statement, Album album) {
        statement.clearBindings();

        statement.bindLong(1, album.getAlbumId());
        statement.bindLong(2, album.getArtistId());
        statement.bindString(3, album.getAlbumName());
        if (album.getAlbumArtPath() != null) statement.bindString(4, album.getAlbumArtPath());
        statement.bindLong(5, album.getAlbumYear());
        statement.bindLong(6, album.getSongCount());

        return statement;
    }

    @Override
    SQLiteStatement bindUpdate(SQLiteStatement statement, Album album) {
        statement.clearBindings();

        statement.bindLong(1, album.getArtistId());
        statement.bindString(2, album.getAlbumName());
        if (album.getAlbumArtPath() != null) statement.bindString(3, album.getAlbumArtPath());
        statement.bindLong(4, album.getAlbumYear());
        statement.bindLong(5, album.getSongCount());
        statement.bindLong(6, album.getAlbumId());

        return statement;
    }

    @Override
    public void add(Album album) {
        String SQL = String.format("INSERT INTO %s (album_id, artist_id, album_name, album_art_path, album_year, song_count) VALUES(?, ?, ?, ?, ?, ?)", TABLE_NAME);
        super.add(album, SQL);
    }

    @Override
    public void update(Album album) {
        String SQL = String.format("UPDATE %s SET artist_id = ?, album_name = ?, album_art_path = ?, album_year = ?, song_count = ? WHERE album_id = ?", TABLE_NAME);
        super.update(album, SQL);
    }

    @Override
    public void remove(Long id) {
        super.remove(String.format("DELETE FROM %s WHERE album_id = %d", TABLE_NAME, id));
    }

    @Override
    public void remove(Album album) {
        remove(album.getAlbumId());
    }

    @Override
    public void removeAll() {
        super.remove(String.format("DELETE FROM %s", TABLE_NAME));
    }

    @Override
    public Album findById(Long id) {
        return super.findById("album_id = ?", id);
    }

    @Override
    public List<Album> getAll() {
        return super.getAll();
    }
}
