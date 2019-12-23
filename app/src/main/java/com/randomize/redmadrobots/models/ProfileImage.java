package com.randomize.redmadrobots.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileImage {

    @SerializedName("small")
    @Expose
    private String small;

    @SerializedName("medium")
    @Expose
    private String medium;

    @SerializedName("large")
    @Expose
    private String large;

    public String getSmall() {
        return small;
    }

    public String getMedium() {
        return medium;
    }

    public String getLarge() {
        return large;
    }
}
