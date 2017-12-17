package com.android.materialplayer.models;

/**
 * Created by dl1998 on 03.12.17.
 */

public class Song {

    private Long songId;
    private String songName;
    private String artistName;
    private String artPath;

    public Song() {
    }

    public Song(Long songId, String songName, String artistName, String artPath) {
        this.songId = songId;
        this.songName = songName;
        this.artistName = artistName;
        this.artPath = artPath;
    }

    public Long getSongId() {
        return songId;
    }

    public void setSongId(Long songId) {
        this.songId = songId;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtPath() {
        return this.artPath;
    }

    public void setArtPath(String artPath) {
        this.artPath = artPath;
    }
}
