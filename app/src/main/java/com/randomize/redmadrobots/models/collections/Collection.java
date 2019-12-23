package com.randomize.redmadrobots.models.collections;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.randomize.redmadrobots.models.User;

import java.util.List;

public class Collection {

    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("total_photos")
    @Expose
    private int totalPhotos;
    @SerializedName("tags")
    @Expose
    private List<Tags> tags;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("preview_photos")
    @Expose
    private List<PreviewPhotos> previewPhotos;

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getTotalPhotos() {
        return totalPhotos;
    }

    public List<Tags> getTags() {
        return tags;
    }

    public User getUser() {
        return user;
    }

    public List<PreviewPhotos> getPreviewPhotos() {
        return previewPhotos;
    }
}
