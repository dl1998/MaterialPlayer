package com.android.materialplayer.models;

/**
 * Created by dl1998 on 04.12.17.
 */

public class Album {

    private Long albumId;
    private String albumName;
    private String artistName;
    private String artPath;

    public Album() {
    }

    public Album(Long albumId, String albumName, String artistName, String artPath) {
        this.albumId = albumId;
        this.albumName = albumName;
        this.artistName = artistName;
        this.artPath = artPath;
    }

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
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

    public String getArtPath() {
        return artPath;
    }

    public void setArtPath(String artPath) {
        this.artPath = artPath;
    }
}
