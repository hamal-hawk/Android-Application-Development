package com.example.civiladvocacy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{
    public ArrayList<Official> officialsList;
    private RecyclerViewClick recyclerViewClick;
    public Context context;
    static int picture_placeholder;
    static int broken_picture;

    public RecyclerAdapter(ArrayList<Official> officialsList, RecyclerViewClick recyclerViewClick, Context context) {
        this.officialsList = officialsList;
        this.recyclerViewClick = recyclerViewClick;
        this.context = context;
        picture_placeholder = context.getResources().getIdentifier("missing", "drawable", context.getPackageName());
        broken_picture = context.getResources().getIdentifier("brokenimage", "drawable", context.getPackageName());
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView name_party;
        private ImageView picture;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.officeTitle_main);
            name_party = itemView.findViewById(R.id.name_main);
            picture = itemView.findViewById(R.id.picture_main);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerViewClick.onItemClick(getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    recyclerViewClick.onItemLongClick(getAdapterPosition());
                    return true;
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new RecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
        holder.title.setText(officialsList.get(position).getOffice());
        holder.name_party.setText(officialsList.get(position).getName()+" ("+officialsList.get(position).getParty()+")");
        Glide.with(context)
                .load(officialsList.get(position).photo_url)
                .placeholder(picture_placeholder)
                .error(officialsList.get(position).photo_url.equals("")?picture_placeholder:broken_picture)
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
                .into(holder.picture);

    }

    @Override
    public int getItemCount() {
        return officialsList.size();
    }

}
