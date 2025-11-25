package com.example.healthcare.Views;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcare.BuildConfig;
import com.example.healthcare.Models.NewsArticle;
import com.example.healthcare.Models.NewsResponse;
import com.example.healthcare.R;
import com.example.healthcare.network.NewsApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsActivity extends AppCompatActivity {

    private static final String TAG = "NewsActivity";
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private final List<NewsArticle> articles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        recyclerView = findViewById(R.id.news_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NewsAdapter(articles);
        recyclerView.setAdapter(adapter);

        fetchNews();
    }

    private void fetchNews() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.apitube.io/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NewsApiService newsApiService = retrofit.create(NewsApiService.class);

        Call<NewsResponse> call = newsApiService.getNews(BuildConfig.API_KEY, 10, "health");

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<NewsArticle> fetchedArticles = response.body().getArticles();
                    if (fetchedArticles != null) {
                        // Filter out any null articles before adding them to the list
                        List<NewsArticle> validArticles = fetchedArticles.stream()
                                .filter(java.util.Objects::nonNull)
                                .collect(Collectors.toList());
                        articles.addAll(validArticles);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Log.e(TAG, "onResponse: " + response.message());
                    Toast.makeText(NewsActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(NewsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
