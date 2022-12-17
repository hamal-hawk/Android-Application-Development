package com.example.newsaggregator;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder>{

    private final ArrayList<Article> articleList;
    private Context context;
    private int picture_placeholder;
    private int broken_picture;
    private int no_picture;
    private ViewPagerClick viewPagerClick;


    public NewsAdapter(ArrayList<Article> articleList, Context context, ViewPagerClick viewPagerClick) {
        this.articleList = articleList;
        this.context = context;
        this.viewPagerClick = viewPagerClick;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsViewHolder(
                LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.news_entry, parent, false), viewPagerClick);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        Article article = articleList.get(position);


        holder.headline.setText(article.getTitle().equals("null")?"":article.getTitle());
        holder.date.setText(article.getPublishedAt().equals("null")?"":article.getPublishedAt());
        holder.author.setText(article.getAuthor().equals("null")?"":article.getAuthor());

        picture_placeholder = context.getResources().getIdentifier("loading", "drawable", context.getPackageName());
        broken_picture = context.getResources().getIdentifier("brokenimage", "drawable", context.getPackageName());
        no_picture = context.getResources().getIdentifier("noimage", "drawable", context.getPackageName());

        Glide.with(context)
                .load(articleList.get(position).getUrlToImage())
                .placeholder(picture_placeholder)
                .error(articleList.get(position).getUrlToImage().equals("")?no_picture:broken_picture)
                .addListener(new RequestListener<Drawable>() {

                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(holder.image);

        holder.description.setText(article.getDescription().equals("null")?"":article.getDescription());
        holder.count.setText(""+(position+1)+" of "+articleList.size());

    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

}
