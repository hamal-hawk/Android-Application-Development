package com.example.notes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private ArrayList<Note> notesList;
    private RecyclerViewClick recyclerViewClick;
    public RecyclerAdapter(ArrayList<Note> notesList, RecyclerViewClick recyclerViewClick){
        this.notesList=notesList;
        this.recyclerViewClick=recyclerViewClick;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView date;
        private TextView text;
        public MyViewHolder(final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.list_title);
            date = itemView.findViewById(R.id.list_date);
            text = itemView.findViewById(R.id.list_text);
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.items, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
        String title = notesList.get(position).getTitle();
        String text = notesList.get(position).getText();
        String date = notesList.get(position).getDate().toString();
        holder.title.setText(title);
        holder.date.setText(date);
        if(text.length() > 80){
            holder.text.setText(text.substring(0, 80)+"...");
        }
        else{
            holder.text.setText(text);
        }
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }
}