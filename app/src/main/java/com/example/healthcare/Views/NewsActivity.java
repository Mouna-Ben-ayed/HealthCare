package com.example.healthcare.Views;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsActivity extends AppCompatActivity {

    private static final String TAG = "NewsActivity";
    private static final String HEALTH_CATEGORY_ID = "medtop:07000000";
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private final List<NewsArticle> articles = new ArrayList<>();
    private EditText searchEditText;
    private Button searchButton;
    private ProgressBar loading;
    private TextView emptyState;
    private NewsApiService newsApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        recyclerView = findViewById(R.id.news_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NewsAdapter(articles);
        recyclerView.setAdapter(adapter);

        searchEditText = findViewById(R.id.search_edit_text);
        searchButton = findViewById(R.id.search_button);
        loading = findViewById(R.id.news_loading);
        emptyState = findViewById(R.id.news_empty_state);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.apitube.io/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        newsApiService = retrofit.create(NewsApiService.class);

        searchButton.setOnClickListener(v -> {
            performSearch();
        });

        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            boolean isSearchAction = actionId == EditorInfo.IME_ACTION_SEARCH;
            boolean isEnterKey = event != null
                    && event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getKeyCode() == KeyEvent.KEYCODE_ENTER;

            if (isSearchAction || isEnterKey) {
                performSearch();
                return true;
            }
            return false;
        });

        // Fetch initial news with default English query
        fetchNews(null, false);
    }

    private void performSearch() {
        hideKeyboard();
        String rawQuery = searchEditText.getText().toString().trim();

        // Optional keyword filter: empty means "show latest health news".
        if (rawQuery.isEmpty()) {
            fetchNews(null, false);
            return;
        }

        fetchNews(rawQuery, true);
    }

    private void setLoading(boolean isLoading) {
        if (loading != null) {
            loading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
        if (searchButton != null) {
            searchButton.setEnabled(!isLoading);
        }
    }

    private void showEmptyState(String message) {
        if (emptyState != null) {
            emptyState.setText(message);
            emptyState.setVisibility(View.VISIBLE);
        }
    }

    private void hideEmptyState() {
        if (emptyState != null) {
            emptyState.setVisibility(View.GONE);
        }
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view == null) {
            view = searchEditText;
        }
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    private void fetchNews(String userQuery, boolean allowFallbackToLatest) {
        String titleQuery = (userQuery == null || userQuery.trim().isEmpty()) ? null : userQuery.trim();

        hideEmptyState();
        setLoading(true);

        // Fetch health-category news; use title search within health category
        Call<NewsResponse> call = newsApiService.getNews(BuildConfig.API_KEY, 10, titleQuery, "en", HEALTH_CATEGORY_ID);

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                setLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    List<NewsArticle> fetchedArticles = response.body().getArticles();

                    // Filter out nulls (defensive; some APIs can return sparse arrays)
                    List<NewsArticle> validArticles = new ArrayList<>();
                    if (fetchedArticles != null) {
                        for (NewsArticle article : fetchedArticles) {
                            if (article != null) {
                                validArticles.add(article);
                            }
                        }
                    }

                    articles.clear();
                    if (!validArticles.isEmpty()) {
                        articles.addAll(validArticles);
                        adapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(0);
                    } else {
                        // If user provided a keyword and it yields no results, fall back to latest health news.
                        if (allowFallbackToLatest && titleQuery != null) {
                            Toast.makeText(NewsActivity.this, "No matches for this keyword. Showing latest health news.", Toast.LENGTH_SHORT).show();
                            fetchNews(null, false);
                            return;
                        }

                        // If category-only yields nothing, try a single fallback with title=health.
                        if (titleQuery == null) {
                            fetchNews("health", false);
                            return;
                        }

                        adapter.notifyDataSetChanged();
                        showEmptyState("No health news available right now.");
                    }
                } else {
                    Log.e(TAG, "onResponse: HTTP " + response.code() + " - " + response.message());
                    Toast.makeText(NewsActivity.this, "Error: " + response.code() + " " + response.message(), Toast.LENGTH_SHORT).show();
                    showEmptyState("Could not load news. Try again.");
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                setLoading(false);
                Log.e(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(NewsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                showEmptyState("Network error. Check connection and retry.");
            }
        });
    }
}
