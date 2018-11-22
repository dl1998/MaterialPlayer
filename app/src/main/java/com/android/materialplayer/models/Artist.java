package com.android.materialplayer.models;

/**
 * Created by dl1998 on 04.12.17.
 */

public class Artist {

    private Long artistId;
    private String artistName;
    private Integer albumCount;
    private Integer songCount;
    private String artPath;

    public Artist() {
    }

    public Artist(Long artistId, String artistName, Integer albumCount, Integer songCount, String artPath) {
        this.artistId = artistId;
        this.artistName = artistName;
        this.albumCount = albumCount;
        this.songCount = songCount;
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

    public Integer getAlbumCount() {
        return albumCount;
    }

    public void setAlbumCount(Integer albumCount) {
        this.albumCount = albumCount;
    }

    public Integer getSongCount() {
        return songCount;
    }

    public void setSongCount(Integer songCount) {
        this.songCount = songCount;
    }

    public String getArtPath() {
        return artPath;
    }

    public void setArtPath(String artPath) {
        this.artPath = artPath;
    }
}
