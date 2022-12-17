package com.example.newsaggregator;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CustomAdapter extends ArrayAdapter {
    public CustomAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public CustomAdapter(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public CustomAdapter(@NonNull Context context, int resource, @NonNull Object[] objects) {
        super(context, resource, objects);
    }

    public CustomAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull Object[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public CustomAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
    }

    public CustomAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = super.getView(position, convertView, parent);
        // Here we get the textview and set the color
        TextView tv = (TextView) row.findViewById(R.id.list_item);
        if(MainActivity.flag){
            int index=0;
            for(Source source : MainActivity.sourcesList){
                if(source.getName().equals(MainActivity.sourcesNameList.get(position))){
                    index = MainActivity.sourcesList.indexOf(source);
                }
            }
            Source source = MainActivity.sourcesList.get(index);
            tv.setTextColor(MainActivity.colorMap.get(source.getTopic()));
        }
        return row;
    }
}
