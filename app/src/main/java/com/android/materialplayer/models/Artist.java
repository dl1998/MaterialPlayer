package com.android.materialplayer.models;

/**
 * Created by dl1998 on 04.12.17.
 */

public class Artist {

    private Long artistId;
    private String artistName;
    private String artPath;

    public Artist() {
    }

    public Artist(Long artistId, String artistName, String artPath) {
        this.artistId = artistId;
        this.artistName = artistName;
        this.artPath = artPath;
    }

    public Long getArtistId() {
        return artistId;
    }

    public void setArtistId(Long artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtPath() {
        return artPath;
    }

    public void setArtPath(String artPath) {
        this.artPath = artPath;
    }
}
