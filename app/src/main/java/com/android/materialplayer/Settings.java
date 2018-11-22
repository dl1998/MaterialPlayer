package com.android.materialplayer;

import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.MediaPlayer;

import com.android.materialplayer.models.ExtendedSong;
import com.android.materialplayer.services.MusicService;

import java.util.ArrayList;

/**
 * Created by dl1998 on 09.12.17.
 */

public class Settings {
    public static String DB_ADD = "add";
    public static String DB_UPDATE = "update";

    public static String SHARED_PREFERENCES = "preferences";
    public static SharedPreferences preferences = null;

    public static String SP_FIRST_RUN = "run";

    //String uri = "http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&artist=%s&api_key=5e51c3a25256e2f5f7396c9db2786b21&format=json";
    private static String PATH = "http://ws.audioscrobbler.com/2.0/?method=";
    private static String API_KEY = "5e51c3a25256e2f5f7396c9db2786b21";
    private static String FORMAT = "json";
    public static String URI = PATH + "%s&artist=%s&api_key=" + API_KEY + "&format=" + FORMAT;

    public static MusicService musicService = null;
    public static ServiceConnection serviceConnection = null;
    public static Intent playIntent;

    public static ExtendedSong song = null;
    public static ArrayList<ExtendedSong> songs = null;
    public static MediaPlayer player = null;

    public static String SONG_ORDER = "song order";
    public static String SONG_REVERSED = "song reversed";

    public static String ALBUM_ORDER = "album order";
    public static String ALBUM_REVERSED = "album reversed";

    public static String ARTIST_ORDER = "artist order";
    public static String ARTIST_REVERSED = "artist reversed";

    public interface Methods {
        String ARTIST_INFO = "artist.getinfo";
    }

    public enum SongOrder {
        TitleSong, Album, Artist, Year, Duration
    }

    public enum AlbumOrder {
        TitleAlbum, NoOfSongsAlbum
    }

    public enum ArtistOrder {
        TitleArtist, NoOfAlbums, NoOfSongsArtist
    }

    public interface Action {
        public static String MAIN_ACTION = "com.android.materialplayer.services.main";
        public static String PREV_ACTION = "com.android.materialplayer.services.prev";
        public static String PLAY_ACTION = "com.android.materialplayer.services.play";
        public static String NEXT_ACTION = "com.android.materialplayer.services.next";
        public static String STARTFOREGROUND_ACTION = "com.android.materialplayer.services.startforeground";
        public static String STOPFOREGROUND_ACTION = "com.android.materialplayer.services.stopforeground";
    }

    public interface NOTIFICATION_ID {
        public static Integer FOREGROUND_SERVICE = 100;
    }
}
