
package com.android.materialplayer.json_objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image_ {

    @SerializedName("#text")
    @Expose
    private String text;
    @SerializedName("size")
    @Expose
    private String size;

    /**
     * No args constructor for use in serialization
     */
    public Image_() {
    }

    /**
     * @param text
     * @param size
     */
    public Image_(String text, String size) {
        super();
        this.text = text;
        this.size = size;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

}
