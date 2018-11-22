
package com.android.materialplayer.json_objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Stats {

    @SerializedName("listeners")
    @Expose
    private String listeners;
    @SerializedName("playcount")
    @Expose
    private String playcount;

    /**
     * No args constructor for use in serialization
     */
    public Stats() {
    }

    /**
     * @param listeners
     * @param playcount
     */
    public Stats(String listeners, String playcount) {
        super();
        this.listeners = listeners;
        this.playcount = playcount;
    }

    public String getListeners() {
        return listeners;
    }

    public void setListeners(String listeners) {
        this.listeners = listeners;
    }

    public String getPlaycount() {
        return playcount;
    }

    public void setPlaycount(String playcount) {
        this.playcount = playcount;
    }

}
