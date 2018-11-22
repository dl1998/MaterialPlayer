package com.android.materialplayer.comparators;

import android.view.MenuItem;

import com.android.materialplayer.R;
import com.android.materialplayer.Settings;
import com.android.materialplayer.models.ExtendedAlbum;

/**
 * Created by dl1998 on 01.01.18.
 */

public class AlbumComparator {

    public static Settings.AlbumOrder getOrder(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_album_in_album:
                return Settings.AlbumOrder.TitleAlbum;
            case R.id.sort_by_no_tracks:
                return Settings.AlbumOrder.NoOfSongsAlbum;
            default:
                return null;
        }
    }

    public static int compareAlbum(ExtendedAlbum album, ExtendedAlbum album1, Settings.AlbumOrder order) {
        if (order.equals(Settings.AlbumOrder.TitleAlbum)) {
            return album.getAlbumName().compareTo(album1.getAlbumName());
        } else if (order.equals(Settings.AlbumOrder.NoOfSongsAlbum)) {
            return album.getSongsCount().compareTo(album1.getSongsCount());
        } else {
            return 0;
        }
    }

    public static Settings.AlbumOrder getOrderByOrdinal(Integer ordinal) {
        if (ordinal.equals(Settings.AlbumOrder.TitleAlbum.ordinal())) {
            return Settings.AlbumOrder.TitleAlbum;
        } else if (ordinal.equals(Settings.AlbumOrder.NoOfSongsAlbum.ordinal())) {
            return Settings.AlbumOrder.NoOfSongsAlbum;
        }
        return null;
    }
}
