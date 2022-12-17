package com.example.weather;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class HourlyRecyclerAdapter extends RecyclerView.Adapter<HourlyRecyclerAdapter.MyViewHolder>{
    public ArrayList<HourlyWeather> hourlyWeathers;

    public HourlyRecyclerAdapter(ArrayList<HourlyWeather> hourlyWeathers) {
        this.hourlyWeathers = hourlyWeathers;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView hourly_day;
        private TextView hourly_time;
        private ImageView hourly_icon;
        private TextView hourly_temp;
        private TextView hourly_desc;
        public MyViewHolder(final View itemView) {
            super(itemView);
            hourly_day = itemView.findViewById(R.id.hourly_day);
            hourly_time = itemView.findViewById(R.id.hourly_time);
            hourly_icon = itemView.findViewById(R.id.hourly_icon);
            hourly_temp = itemView.findViewById(R.id.hourly_temp);
            hourly_desc = itemView.findViewById(R.id.hourly_desc);
        }
    }
    @NonNull
    @Override
    public HourlyRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyRecyclerAdapter.MyViewHolder holder, int position) {
        String hourly_day = hourlyWeathers.get(position).getDay();
        String hourly_time = hourlyWeathers.get(position).getTime();
        int hourly_icon = hourlyWeathers.get(position).getIcon_id();
        String hourly_temp = hourlyWeathers.get(position).getTemperature();
        String hourly_desc = hourlyWeathers.get(position).getDescription();
        holder.hourly_day.setText(hourly_day);
        holder.hourly_time.setText(hourly_time);
        holder.hourly_icon.setImageResource(hourly_icon);
        holder.hourly_temp.setText(hourly_temp+MainActivity.unit_value);
        holder.hourly_desc.setText(hourly_desc);

    }

    @Override
    public int getItemCount() {
        return hourlyWeathers.size();
    }
}
