package com.android.materialplayer.models;

/**
 * Created by dl1998 on 29.12.17.
 */

public class Album {

    private Long id;
    private String albumName;
    private String albumCover;
    private Integer songsCount;

    public Album() {
    }

    public Album(Long id, String albumName, String albumCover, Integer songsCount) {
        this.id = id;
        this.albumName = albumName;
        this.albumCover = albumCover;
        this.songsCount = songsCount;
    }

    public Album(com.android.materialplayer.entity.Album album) {
        this(album.getAlbumId(), album.getAlbumName(), album.getAlbumArtPath(), album.getSongCount());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumCover() {
        return albumCover;
    }

    public void setAlbumCover(String albumCover) {
        this.albumCover = albumCover;
    }

    public Integer getSongsCount() {
        return songsCount;
    }

    public void setSongsCount(Integer songsCount) {
        this.songsCount = songsCount;
    }
}
