
package com.android.materialplayer.json_objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Similar {

    @SerializedName("artist")
    @Expose
    private List<Artist_> artist = null;

    /**
     * No args constructor for use in serialization
     */
    public Similar() {
    }

    /**
     * @param artist
     */
    public Similar(List<Artist_> artist) {
        super();
        this.artist = artist;
    }

    public List<Artist_> getArtist() {
        return artist;
    }

    public void setArtist(List<Artist_> artist) {
        this.artist = artist;
    }

}
