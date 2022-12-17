package com.example.newsaggregator;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsViewHolder extends RecyclerView.ViewHolder {
    TextView headline;
    TextView date;
    TextView author;
    ImageView image;
    TextView description;
    TextView count;
    ViewPagerClick viewPagerClick;

    public NewsViewHolder(@NonNull View itemView, ViewPagerClick viewPagerClick) {
        super(itemView);
        headline = itemView.findViewById(R.id.headline);
        date = itemView.findViewById(R.id.date);
        author = itemView.findViewById(R.id.author);
        image = itemView.findViewById(R.id.image);
        description = itemView.findViewById(R.id.description);
        count = itemView.findViewById(R.id.count);
        this.viewPagerClick = viewPagerClick;

        headline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPagerClick.onItemClick(getAdapterPosition());
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPagerClick.onItemClick(getAdapterPosition());
            }
        });

        description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPagerClick.onItemClick(getAdapterPosition());
            }
        });

        headline.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                viewPagerClick.onItemLongClick(getAdapterPosition());
                return true;
            }
        });
    }
}
