package com.android.materialplayer.provider;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by dl1998 on 31.12.17.
 */

public class RecentlyPlayed {

    private static RecentlyPlayed instance = null;

    public static RecentlyPlayed getInstance() {
        if (instance == null) {
            return new RecentlyPlayed();
        }
        return instance;
    }

    private RecentlyPlayed() {
    }

    public void onCreate(final SQLiteDatabase db) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS ");
        builder.append(RecentlyPlayedColumns.TABLE_NAME);
        builder.append(" (");
        builder.append(RecentlyPlayedColumns.ID);
        builder.append(" integer unique not null, ");
        builder.append(RecentlyPlayedColumns.TIME_PLAYED);
        builder.append(" integer not null)");

        db.execSQL(builder.toString());
    }

    public interface RecentlyPlayedColumns {
        String TABLE_NAME = "recently_played";

        String ID = "id";

        String TIME_PLAYED = "time_played";
    }
}
