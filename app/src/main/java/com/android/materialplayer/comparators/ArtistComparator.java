package com.android.materialplayer.comparators;

import android.view.MenuItem;

import com.android.materialplayer.R;
import com.android.materialplayer.Settings;
import com.android.materialplayer.models.Artist;

/**
 * Created by dl1998 on 01.01.18.
 */

public class ArtistComparator {

    public static Settings.ArtistOrder getOrder(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_artist_in_artist:
                return Settings.ArtistOrder.TitleArtist;
            case R.id.sort_by_no_albums:
                return Settings.ArtistOrder.NoOfAlbums;
            case R.id.sort_by_no_tracks:
                return Settings.ArtistOrder.NoOfSongsArtist;
            default:
                return null;
        }
    }

    public static int compareArtist(Artist artist, Artist artist1, Settings.ArtistOrder order) {
        if (order.equals(Settings.ArtistOrder.TitleArtist)) {
            return artist.getArtistName().compareTo(artist1.getArtistName());
        } else if (order.equals(Settings.ArtistOrder.NoOfAlbums)) {
            return artist.getAlbumCount().compareTo(artist1.getAlbumCount());
        } else if (order.equals(Settings.ArtistOrder.NoOfSongsArtist)) {
            return artist.getSongCount().compareTo(artist1.getSongCount());
        } else {
            return 0;
        }
    }

    public static Settings.ArtistOrder getOrderByOrdinal(Integer ordinal) {
        if (ordinal.equals(Settings.ArtistOrder.TitleArtist.ordinal()))
            return Settings.ArtistOrder.TitleArtist;
        else if (ordinal.equals(Settings.ArtistOrder.NoOfAlbums.ordinal()))
            return Settings.ArtistOrder.NoOfAlbums;
        else if (ordinal.equals(Settings.ArtistOrder.NoOfSongsArtist.ordinal()))
            return Settings.ArtistOrder.NoOfSongsArtist;
        return null;
    }
}
