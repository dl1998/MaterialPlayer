package com.android.materialplayer.entity;

/**
 * Created by dl1998 on 05.12.17.
 */

public class Song {

    private Long songId;
    private Long albumId;
    private Long artistId;
    private String songName;
    private Integer duration;
    private Integer trackNumber;

    public Song() {
    }

    public Song(Long songId, Long albumId, Long artistId, String songName, Integer duration, Integer trackNumber) {
        this.songId = songId;
        this.albumId = albumId;
        this.artistId = artistId;
        this.songName = songName;
        this.duration = duration;
        this.trackNumber = trackNumber;
    }

    public Long getSongId() {
        return songId;
    }

    public void setSongId(Long songId) {
        this.songId = songId;
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

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(Integer trackNumber) {
        this.trackNumber = trackNumber;
    }
}
