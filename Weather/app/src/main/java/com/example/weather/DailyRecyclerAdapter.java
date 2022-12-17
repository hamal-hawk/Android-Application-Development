package com.example.weather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DailyRecyclerAdapter extends RecyclerView.Adapter<DailyRecyclerAdapter.MyViewHolder>{
    public ArrayList<DailyWeather> dailyWeathers;

    public DailyRecyclerAdapter(ArrayList<DailyWeather> dailyWeathers) {
        this.dailyWeathers = dailyWeathers;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView daily_date;
        private TextView daily_temperature;
        private ImageView daily_icon;
        private TextView daily_description;
        private TextView daily_precip;
        private TextView daily_uv;
        private TextView daily_morning;
        private TextView daily_noon;
        private TextView daily_evening;
        private TextView daily_night;

        public MyViewHolder(final View itemView) {
            super(itemView);
            daily_date = itemView.findViewById(R.id.daily_date);
            daily_temperature = itemView.findViewById(R.id.daily_temperature);
            daily_icon = itemView.findViewById(R.id.daily_icon);
            daily_description = itemView.findViewById(R.id.daily_description);
            daily_precip = itemView.findViewById(R.id.daily_precip);
            daily_uv = itemView.findViewById(R.id.daily_uv);
            daily_morning = itemView.findViewById(R.id.daily_morning);
            daily_noon = itemView.findViewById(R.id.daily_noon);
            daily_evening = itemView.findViewById(R.id.daily_evening);
            daily_night = itemView.findViewById(R.id.daily_night);
        }
    }

    @NonNull
    @Override
    public DailyRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_daily, parent, false);
        return new DailyRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyRecyclerAdapter.MyViewHolder holder, int position) {
        String daily_date = dailyWeathers.get(position).getDate();
        String temp_max = dailyWeathers.get(position).getTemp_max();
        String temp_min = dailyWeathers.get(position).getTemp_min();
        int daily_icon = dailyWeathers.get(position).getIcon_id();
        String daily_description = dailyWeathers.get(position).getDescription();
        String daily_precip = dailyWeathers.get(position).getPrecip();
        String daily_uv = dailyWeathers.get(position).getUv();
        String daily_morning = dailyWeathers.get(position).getMorning();
        String daily_noon = dailyWeathers.get(position).getAfternoon();
        String daily_evening = dailyWeathers.get(position).getEvening();
        String daily_night = dailyWeathers.get(position).getNight();
        holder.daily_date.setText(daily_date);
        holder.daily_temperature.setText(temp_min+MainActivity.unit_value+"/"+temp_max+MainActivity.unit_value);
        holder.daily_icon.setImageResource(daily_icon);
        holder.daily_description.setText(daily_description);
        holder.daily_precip.setText("("+daily_precip+"% precip.)");
        holder.daily_uv.setText("UV Index: "+daily_uv);
        holder.daily_morning.setText(daily_morning+MainActivity.unit_value);
        holder.daily_noon.setText(daily_noon+MainActivity.unit_value);
        holder.daily_evening.setText(daily_evening+MainActivity.unit_value);
        holder.daily_night.setText(daily_night+MainActivity.unit_value);
    }

    @Override
    public int getItemCount() {
        return dailyWeathers.size();
    }

}
