package com.example.healthcare.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

public class NewsResponse {

    @SerializedName("results")
    private final List<NewsArticle> results;

    public NewsResponse(List<NewsArticle> results) {
        this.results = results;
    }

    public List<NewsArticle> getArticles() {
        return results != null ? results : Collections.emptyList();
    }
}
