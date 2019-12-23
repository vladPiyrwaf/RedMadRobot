package com.randomize.redmadrobots.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("first_name")
    @Expose
    private String firstName;

    @SerializedName("last_name")
    @Expose
    private String lastName;

    @SerializedName("instagram_username")
    @Expose
    private String instagramUsername;

    @SerializedName("twitter_username")
    @Expose
    private String twitterUsername;

    @SerializedName("portfolio_url")
    @Expose
    private Object portfolioUrl;

    @SerializedName("bio")
    @Expose
    private String bio;

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("total_likes")
    @Expose
    private Integer totalLikes;

    @SerializedName("total_photos")
    @Expose
    private Integer totalPhotos;

    @SerializedName("total_collections")
    @Expose
    private Integer totalCollections;

    @SerializedName("followed_by_user")
    @Expose
    private Boolean followedByUser;

    @SerializedName("followers_count")
    @Expose
    private Integer followersCount;

    @SerializedName("following_count")
    @Expose
    private Integer followingCount;

    @SerializedName("downloads")
    @Expose
    private Integer downloads;

    @SerializedName("profile_image")
    @Expose
    private ProfileImage profileImage;
    @SerializedName("links")
    @Expose
    private Links links;

    public String getId() {
        return id;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getInstagramUsername() {
        return instagramUsername;
    }

    public String getTwitterUsername() {
        return twitterUsername;
    }

    public Object getPortfolioUrl() {
        return portfolioUrl;
    }

    public String getBio() {
        return bio;
    }

    public String getLocation() {
        return location;
    }

    public Integer getTotalLikes() {
        return totalLikes;
    }

    public Integer getTotalPhotos() {
        return totalPhotos;
    }

    public Integer getTotalCollections() {
        return totalCollections;
    }

    public Boolean getFollowedByUser() {
        return followedByUser;
    }

    public Integer getFollowersCount() {
        return followersCount;
    }

    public Integer getFollowingCount() {
        return followingCount;
    }

    public Integer getDownloads() {
        return downloads;
    }

    public ProfileImage getProfileImage() {
        return profileImage;
    }

    public Links getLinks() {
        return links;
    }
}
