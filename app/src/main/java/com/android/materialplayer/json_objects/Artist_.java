
package com.android.materialplayer.json_objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Artist_ {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("image")
    @Expose
    private List<Image_> image = null;

    /**
     * No args constructor for use in serialization
     */
    public Artist_() {
    }

    /**
     * @param name
     * @param image
     * @param url
     */
    public Artist_(String name, String url, List<Image_> image) {
        super();
        this.name = name;
        this.url = url;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Image_> getImage() {
        return image;
    }

    public void setImage(List<Image_> image) {
        this.image = image;
    }

}
