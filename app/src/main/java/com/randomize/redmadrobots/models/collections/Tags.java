package com.randomize.redmadrobots.models.collections;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tags {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("title")
    @Expose
    private String title;

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }
}
