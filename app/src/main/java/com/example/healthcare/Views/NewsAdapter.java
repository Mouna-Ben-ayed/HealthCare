package com.example.healthcare.Views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcare.Models.NewsArticle;
import com.example.healthcare.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private final List<NewsArticle> articles;

    public NewsAdapter(List<NewsArticle> articles) {
        this.articles = articles;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_article_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsArticle article = articles.get(position);

        // Add a null check for the article object itself
        if (article == null) {
            return;
        }

        if (article.getTitle() != null) {
            holder.title.setText(article.getTitle());
        }

        if (article.getDescription() != null) {
            holder.description.setText(article.getDescription());
        }

        if (article.getImageUrl() != null && !article.getImageUrl().isEmpty()) {
            Picasso.get().load(article.getImageUrl()).into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;
        TextView description;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.article_image);
            title = itemView.findViewById(R.id.article_title);
            description = itemView.findViewById(R.id.article_description);
        }
    }
}
