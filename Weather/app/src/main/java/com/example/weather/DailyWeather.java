package com.example.weather;

import java.io.Serializable;

public class DailyWeather implements Serializable {
    String date;
    String temp_max;
    String temp_min;
    String description;
    String precip;
    String uv;
    String morning;
    String afternoon;
    String evening;
    String night;
    int icon_id;

    public DailyWeather(String date, String temp_max, String temp_min, String description, String precip, String uv, String morning, String afternoon, String evening, String night, int icon_id) {
        this.date = date;
        this.temp_max = temp_max;
        this.temp_min = temp_min;
        this.description = description;
        this.precip = precip;
        this.uv = uv;
        this.morning = morning;
        this.afternoon = afternoon;
        this.evening = evening;
        this.night = night;
        this.icon_id = icon_id;
    }

    public String getDate() {
        return date;
    }

    public String getTemp_max() {
        return temp_max;
    }

    public String getTemp_min() {
        return temp_min;
    }

    public String getDescription() {
        return description;
    }

    public String getPrecip() {
        return precip;
    }

    public String getUv() {
        return uv;
    }

    public String getMorning() {
        return morning;
    }

    public String getAfternoon() {
        return afternoon;
    }

    public String getEvening() {
        return evening;
    }

    public String getNight() {
        return night;
    }

    public int getIcon_id() {
        return icon_id;
    }

    @Override
    public String toString() {
        return "DailyWeather{" +
                "date='" + date + '\'' +
                ", temp_max='" + temp_max + '\'' +
                ", temp_min='" + temp_min + '\'' +
                ", description='" + description + '\'' +
                ", precip='" + precip + '\'' +
                ", uv='" + uv + '\'' +
                ", morning='" + morning + '\'' +
                ", afternoon='" + afternoon + '\'' +
                ", evening='" + evening + '\'' +
                ", night='" + night + '\'' +
                ", icon_id=" + icon_id +
                '}';
    }
}
