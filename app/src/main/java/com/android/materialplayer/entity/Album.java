package com.android.materialplayer.entity;

/**
 * Created by dl1998 on 05.12.17.
 */

public class Album {

    private Long albumId;
    private Long artistId;
    private String albumName;
    private String albumArtPath;
    private Integer albumYear;
    private Integer songCount;

    public Album() {
    }

    public Album(Long albumId, Long artistId, String albumName, String albumArtPath, Integer albumYear, Integer songCount) {
        this.albumId = albumId;
        this.artistId = artistId;
        this.albumName = albumName;
        this.albumArtPath = albumArtPath;
        this.albumYear = albumYear;
        this.songCount = songCount;
    }

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public Long getArtistId() {
        return artistId;
    }

    public void setArtistId(Long artistId) {
        this.artistId = artistId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumArtPath() {
        return albumArtPath;
    }

    public void setAlbumArtPath(String albumArtPath) {
        this.albumArtPath = albumArtPath;
    }

    public Integer getAlbumYear() {
        return albumYear;
    }

    public void setAlbumYear(Integer albumYear) {
        this.albumYear = albumYear;
    }

    public Integer getSongCount() {
        return songCount;
    }

    public void setSongCount(Integer songCount) {
        this.songCount = songCount;
    }
}
