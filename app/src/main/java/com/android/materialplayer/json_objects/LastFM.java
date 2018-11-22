
package com.android.materialplayer.json_objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LastFM {

    @SerializedName("artist")
    @Expose
    private Artist artist;

    /**
     * No args constructor for use in serialization
     */
    public LastFM() {
    }

    /**
     * @param artist
     */
    public LastFM(Artist artist) {
        super();
        this.artist = artist;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

}
