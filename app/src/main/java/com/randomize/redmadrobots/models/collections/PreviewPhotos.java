package com.randomize.redmadrobots.models.collections;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.randomize.redmadrobots.models.Urls;

public class PreviewPhotos {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("urls")
    @Expose
    private Urls urls;

    public String getId() {
        return id;
    }

    public Urls getUrls() {
        return urls;
    }
}
