package com.randomize.redmadrobots.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Links {

    @SerializedName("self")
    @Expose
    private String self;

    @SerializedName("html")
    @Expose
    private String html;

    @SerializedName("photos")
    @Expose
    private String photos;

    @SerializedName("likes")
    @Expose
    private String likes;

    @SerializedName("portfolio")
    @Expose
    private String portfolio;

    @SerializedName("download")
    @Expose
    private String download;

    @SerializedName("download_location")
    @Expose
    private String downloadLocation;

    public String getSelf() {
        return self;
    }

    public String getHtml() {
        return html;
    }

    public String getPhotos() {
        return photos;
    }

    public String getLikes() {
        return likes;
    }

    public String getPortfolio() {
        return portfolio;
    }

    public String getDownload() {
        return download;
    }

    public String getDownloadLocation() {
        return downloadLocation;
    }
}
