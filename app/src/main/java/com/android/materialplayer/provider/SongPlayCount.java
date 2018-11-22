package com.android.materialplayer.provider;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by dl1998 on 31.12.17.
 */

public class SongPlayCount {

    private static SongPlayCount instance = null;

    public static SongPlayCount getInstance() {
        if (instance == null) {
            return new SongPlayCount();
        }
        return instance;
    }

    private SongPlayCount() {
    }

    public void onCreate(SQLiteDatabase db) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS ");
        builder.append(SongPlayCountColumns.TABLE_NAME);
        builder.append(" (");
        builder.append(SongPlayCountColumns.ID);
        builder.append("integer unique not null, ");
        builder.append(SongPlayCountColumns.PLAY_COUNT);
        builder.append("integer not null)");

        db.execSQL(builder.toString());
    }

    interface SongPlayCountColumns {
        String TABLE_NAME = "song_play_count";

        String ID = "id";

        String PLAY_COUNT = "play_count";
    }
}
