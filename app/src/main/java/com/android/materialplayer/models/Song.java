package com.android.materialplayer.models;

/**
 * Created by dl1998 on 02.01.18.
 */

public class Song {

    private Long songId;
    private String songName;
    private Integer duration;
    private Integer trackNumber;

    public static Song getFromSong(com.android.materialplayer.entity.Song song) {
        Song temp = new Song();

        temp.songId = song.getSongId();
        temp.songName = song.getSongName();
        temp.duration = song.getDuration();
        temp.trackNumber = song.getTrackNumber();

        return temp;
    }

    public Song() {
    }

    public Song(Long songId, String songName, Integer duration, Integer trackNumber) {
        this.songId = songId;
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
