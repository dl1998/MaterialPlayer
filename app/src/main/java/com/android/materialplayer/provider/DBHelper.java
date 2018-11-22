package com.android.materialplayer.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dl1998 on 05.12.17.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "material_player_db";
    public static final Integer DB_VERSION = 1;

    private Context context;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE song(" +
                "song_id integer primary key," +
                "album_id integer," +
                "artist_id integer," +
                "song_name text," +
                "duration integer," +
                "track_number integer)");

        db.execSQL("CREATE TABLE artist(" +
                "artist_id integer primary key," +
                "artist_name text," +
                "album_count integer," +
                "song_count integer)");

        db.execSQL("CREATE TABLE album(" +
                "album_id integer primary key," +
                "artist_id integer," +
                "album_name text," +
                "album_art_path text," +
                "album_year integer," +
                "song_count integer)");

        RecentlyPlayed.getInstance().onCreate(db);
        SongPlayCount.getInstance().onCreate(db);
        SearchHistory.getInstance().onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
