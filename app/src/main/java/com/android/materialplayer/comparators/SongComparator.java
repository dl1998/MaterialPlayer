package com.android.materialplayer.comparators;

import android.view.MenuItem;

import com.android.materialplayer.R;
import com.android.materialplayer.Settings;
import com.android.materialplayer.models.ExtendedSong;

/**
 * Created by dl1998 on 31.12.17.
 */

public class SongComparator {

    public static Settings.SongOrder getOrder(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_title:
                return Settings.SongOrder.TitleSong;
            case R.id.sort_by_album:
                return Settings.SongOrder.Album;
            case R.id.sort_by_artist:
                return Settings.SongOrder.Artist;
            case R.id.sort_by_year:
                return Settings.SongOrder.Year;
            case R.id.sort_by_duration:
                return Settings.SongOrder.Duration;
            default:
                return null;
        }
    }

    public static int compareSong(ExtendedSong song, ExtendedSong song1, Settings.SongOrder order) {
        if (order.equals(Settings.SongOrder.TitleSong)) {
            return song.getSongName().compareTo(song1.getSongName());
        } else if (order.equals(Settings.SongOrder.Album)) {
            return song.getAlbumName().compareTo(song1.getAlbumName());
        } else if (order.equals(Settings.SongOrder.Artist)) {
            return song.getArtistName().compareTo(song1.getArtistName());
        } else if (order.equals(Settings.SongOrder.Year)) {
            return song.getYear().compareTo(song1.getYear());
        } else if (order.equals(Settings.SongOrder.Duration)) {
            return song.getDuration().compareTo(song1.getDuration());
        } else {
            return 0;
        }
    }

    public static Settings.SongOrder getOrderByOrdinal(Integer ordinal) {
        if (ordinal.equals(Settings.SongOrder.TitleSong.ordinal()))
            return Settings.SongOrder.TitleSong;
        else if (ordinal.equals(Settings.SongOrder.Album.ordinal()))
            return Settings.SongOrder.Album;
        else if (ordinal.equals(Settings.SongOrder.Artist.ordinal()))
            return Settings.SongOrder.Artist;
        else if (ordinal.equals(Settings.SongOrder.Year.ordinal())) return Settings.SongOrder.Year;
        else if (ordinal.equals(Settings.SongOrder.Duration.ordinal()))
            return Settings.SongOrder.Duration;
        return null;
    }

}
