package com.android.materialplayer.models;

/**
 * Created by dl1998 on 02.01.18.
 */

public class ExtendedSong extends Song {

    private String albumName;
    private String artistName;
    private Integer year;
    private String artPath;

    public ExtendedSong() {
    }

    public ExtendedSong(Long songId, String songName, Integer trackNumber, String albumName, String artistName, Integer year, Integer duration, String artPath) {
        super(songId, songName, duration, trackNumber);
        this.albumName = albumName;
        this.artistName = artistName;
        this.year = year;
        this.artPath = artPath;
    }

    public ExtendedSong(Song song) {
        super(song.getSongId(), song.getSongName(), song.getDuration(), song.getTrackNumber());
        this.albumName = null;
        this.artistName = null;
        this.year = null;
        this.artPath = null;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getArtPath() {
        return artPath;
    }

    public void setArtPath(String artPath) {
        this.artPath = artPath;
    }
}
