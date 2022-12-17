package com.example.weather;

public class HourlyWeather {
    String day;
    String time;
    int icon_id;
    String temperature;
    String description;

    public HourlyWeather(String day, String time, int icon_id, String temperature, String description) {
        this.day = day;
        this.time = time;
        this.icon_id = icon_id;
        this.temperature = temperature;
        this.description = description;
    }

    public String getDay() {
        return day;
    }

    public String getTime() {
        return time;
    }

    public int getIcon_id() {
        return icon_id;
    }

    public String getTemperature() {
        return temperature;
    }

    @Override
    public String toString() {
        return "HourlyWeather{" +
                "day='" + day + '\'' +
                ", time='" + time + '\'' +
                ", icon_id='" + icon_id + '\'' +
                ", temperature='" + temperature + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public String getDescription() {
        return description;
    }
}
