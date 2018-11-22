
package com.android.materialplayer.json_objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Links {

    @SerializedName("link")
    @Expose
    private Link link;

    /**
     * No args constructor for use in serialization
     */
    public Links() {
    }

    /**
     * @param link
     */
    public Links(Link link) {
        super();
        this.link = link;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }

}
