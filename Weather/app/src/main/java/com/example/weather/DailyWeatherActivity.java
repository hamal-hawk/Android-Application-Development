package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

public class DailyWeatherActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DailyRecyclerAdapter dailyRecyclerAdapter;
    ArrayList<DailyWeather> dailyWeathers;
    String resolvedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_weather);
        recyclerView = findViewById(R.id.recyclerViewDaily);
        Intent intent = getIntent();
        dailyWeathers = (ArrayList<DailyWeather>) intent.getSerializableExtra("incoming");
        resolvedLocation = intent.getStringExtra("location");
        getSupportActionBar().setTitle(resolvedLocation+" 15 day");
        setAdapter();
        dailyRecyclerAdapter.notifyDataSetChanged();

    }

    private void setAdapter() {
        dailyRecyclerAdapter = new DailyRecyclerAdapter(dailyWeathers);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(dailyRecyclerAdapter);
    }
}