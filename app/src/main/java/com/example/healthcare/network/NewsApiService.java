package com.example.healthcare.network;

import com.example.healthcare.Models.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface NewsApiService {

    @GET("news/everything")
    Call<NewsResponse> getNews(
            @Header("X-API-Key") String apiKey,
            @Query("per_page") int perPage,
            @Query("title") String title,
            @Query("language.code") String language,
            @Query("category.id") String categoryId
    );
}
