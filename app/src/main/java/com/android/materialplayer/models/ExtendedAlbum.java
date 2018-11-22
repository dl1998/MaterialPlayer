package com.android.materialplayer.models;

/**
 * Created by dl1998 on 02.01.18.
 */

public class ExtendedAlbum extends Album {

    private String artistName;

    public ExtendedAlbum() {
    }

    public ExtendedAlbum(Long id, String albumName, String artistName, Integer songsCount, String cover) {
        super(id, albumName, cover, songsCount);
        this.artistName = artistName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
}
