package com.example.healthcare.Models;

import com.google.gson.annotations.SerializedName;

public class NewsArticle {

    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("href")
    private String url;
    @SerializedName("image")
    private String image;

    // Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getImageUrl() {
        return image;
    }
}
