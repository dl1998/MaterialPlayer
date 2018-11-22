
package com.android.materialplayer.json_objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bio {

    @SerializedName("links")
    @Expose
    private Links links;
    @SerializedName("published")
    @Expose
    private String published;
    @SerializedName("summary")
    @Expose
    private String summary;
    @SerializedName("content")
    @Expose
    private String content;

    /**
     * No args constructor for use in serialization
     */
    public Bio() {
    }

    /**
     * @param content
     * @param summary
     * @param links
     * @param published
     */
    public Bio(Links links, String published, String summary, String content) {
        super();
        this.links = links;
        this.published = published;
        this.summary = summary;
        this.content = content;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
