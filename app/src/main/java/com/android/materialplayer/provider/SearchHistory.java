package com.android.materialplayer.provider;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by dl1998 on 31.12.17.
 */

public class SearchHistory {

    private static SearchHistory instance = null;

    public static SearchHistory getInstance() {
        if (instance == null) {
            return new SearchHistory();
        }
        return instance;
    }

    private SearchHistory() {
    }

    public void onCreate(SQLiteDatabase db) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS ");
        builder.append(SearchHistoryColumns.TABLE_NAME);
        builder.append(" (");
        builder.append(SearchHistoryColumns.REQUEST);
        builder.append(" text not null, ");
        builder.append(SearchHistoryColumns.TIME_SEARCH);
        builder.append(" integer not null)");

        db.execSQL(builder.toString());
    }

    interface SearchHistoryColumns {
        String TABLE_NAME = "search_history";

        String REQUEST = "request";

        String TIME_SEARCH = "time_search";
    }
}
