package com.example.weather;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.LongDef;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private RequestQueue queue;
    public ArrayList<HourlyWeather> hourlyWeathers;
    public ArrayList<DailyWeather> dailyWeathers;
    public RecyclerView recyclerView;
    public HourlyRecyclerAdapter hourlyRecyclerAdapter;
    public TextView temperature;
    public TextView datetime;
    public TextView feelslike;
    public TextView description;
    public TextView winds;
    public TextView humidity;
    public TextView uvIndex;
    public TextView visibility;
    public ImageView weatherIcon;
    public TextView morning;
    public TextView afternoon;
    public TextView evening;
    public TextView night;
    public TextView morning_sub;
    public TextView afternoon_sub;
    public TextView evening_sub;
    public TextView night_sub;
    public TextView sunrise;
    public TextView sunset;
    private SwipeRefreshLayout swipeRefreshLayout;
    public SharedPreferences sharedPreferences;
    public Menu optionsMenu;

    static String location;
    String resolved_location;
    static String unit;
    static String unit_value;
    String unit_icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        temperature = findViewById(R.id.temperature);
        datetime = findViewById(R.id.datetime);
        feelslike = findViewById(R.id.feelslike);
        description = findViewById(R.id.description);
        winds = findViewById(R.id.winds);
        humidity = findViewById(R.id.humidity);
        uvIndex = findViewById(R.id.uvIndex);
        visibility = findViewById(R.id.visibility);
        weatherIcon = findViewById(R.id.weatherIcon);
        morning = findViewById(R.id.morning);
        afternoon = findViewById(R.id.afternoon);
        evening = findViewById(R.id.evening);
        night = findViewById(R.id.night);
        morning_sub = findViewById(R.id.morning_sub);
        afternoon_sub = findViewById(R.id.afternoon_sub);
        evening_sub = findViewById(R.id.evening_sub);
        night_sub = findViewById(R.id.night_sub);
        sunrise = findViewById(R.id.sunrise);
        sunset = findViewById(R.id.sunset);
        hourlyWeathers = new ArrayList<>();
        dailyWeathers = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerViewMain);
        unit_icon = "units_f";
        location = "Chicago,IL";
        resolved_location = "";
        unit = "us";
        unit_value = "°F";
        loadPreferences();
        queue = Volley.newRequestQueue(this);
        doDownload();
        setAdapter();

        swipeRefreshLayout = findViewById(R.id.swiper);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dailyWeathers.removeAll(dailyWeathers);
                hourlyWeathers.removeAll(hourlyWeathers);
                doDownload();
                swipeRefreshLayout.setRefreshing(false);
                Log.d("KEY", "onRefresh: ");
            }
        });
    }


    private void setAdapter() {
        hourlyRecyclerAdapter = new HourlyRecyclerAdapter(hourlyWeathers);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(hourlyRecyclerAdapter);
    }

    @Override
    public void onPause() {
        Log.d("Key", "onPause: ");
        saveData();
        super.onPause();
    }
    public void doDownload(){
        if(!hasNetworkConnection()){
            datetime.setText("No internet connection!");
            return;
        }
        String url = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/"+location+"/?unitGroup="+unit+"&key=TBSE8ECJGMHYGBA9BBRL6RLMZ";
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONObject currentConditions = response.getJSONObject("currentConditions");
                    JSONArray days = response.getJSONArray("days");
                    JSONArray hours_array = days.getJSONObject(0).getJSONArray("hours");
                    resolved_location = response.get("resolvedAddress").toString();
                    getSupportActionBar().setTitle(resolved_location);
                    String temp = currentConditions.get("temp").toString();
                    String datetimeEpoch = currentConditions.get("datetimeEpoch").toString();
                    String feels = currentConditions.get("feelslike").toString();
                    String icon = currentConditions.get("icon").toString().replace("-", "_");
                    String conditions = currentConditions.get("conditions").toString();
                    String cloudcover = currentConditions.get("cloudcover").toString();
                    String winddir = getDirection(Double.parseDouble(currentConditions.get("winddir").toString()));
                    String windspeed = currentConditions.get("windspeed").toString();
                    String windgust = currentConditions.get("windgust").toString();
                    String humid = currentConditions.get("humidity").toString();
                    String uv = currentConditions.get("uvindex").toString();
                    String vis = currentConditions.get("visibility").toString();
                    String morning_temp = ((JSONObject)hours_array.get(8)).get("temp").toString();
                    String noon_temp = ((JSONObject)hours_array.get(13)).get("temp").toString();
                    String evening_temp = ((JSONObject)hours_array.get(17)).get("temp").toString();
                    String night_temp = ((JSONObject)hours_array.get(23)).get("temp").toString();
                    String sunriseEpoch = currentConditions.get("sunriseEpoch").toString();
                    String sunsetEpoch = currentConditions.get("sunsetEpoch").toString();

                    temperature.setText(temp+ unit_value);
                    datetime.setText(fullDateSet(datetimeEpoch));
                    feelslike.setText("Feels like "+feels+ unit_value);
                    int icon_id = getApplicationContext().getResources().getIdentifier(icon, "drawable", getApplicationContext().getPackageName());
                    weatherIcon.setImageResource(icon_id);
                    weatherIcon.setVisibility(View.VISIBLE);
                    description.setText(conditions+" ("+cloudcover+"% clouds)");
                    /*
                    Wind gust can be null if there is no significant difference. Therefore handling
                    it in the below lines of code.
                     */
                    if(windgust.equals("null")){
                        winds.setText("Winds: "+winddir+" at "+windspeed+" mph"+"\n(not gusting)");

                    }
                    else{
                        winds.setText("Winds: "+winddir+" at "+windspeed+" mph"+"\ngusting to "+windgust+" mph");

                    }
                    humidity.setText("Humidity: "+humid+"%");
                    uvIndex.setText("UV Index: "+uv);
                    visibility.setText("Visibility: "+vis+"mi");
                    morning.setText(morning_temp+ unit_value);
                    morning_sub.setVisibility(View.VISIBLE);
                    afternoon.setText(noon_temp+ unit_value);
                    afternoon_sub.setVisibility(View.VISIBLE);
                    evening.setText(evening_temp+ unit_value);
                    evening_sub.setVisibility(View.VISIBLE);
                    night.setText(night_temp+ unit_value);
                    night_sub.setVisibility(View.VISIBLE);
                    sunrise.setText("Sunrise: "+timeOnlySet(sunriseEpoch));
                    sunset.setText("Sunset: "+timeOnlySet(sunsetEpoch));

                    setHourlyWeathers(days, getHourOfDay(datetimeEpoch));
                    setDailyWeathers(days);
                    recyclerView.setVisibility(View.VISIBLE);
                    hourlyRecyclerAdapter.notifyDataSetChanged();
                }
                catch(Exception exception){
                    Log.d("KEY", "ExceptionListener"+exception.toString());
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(error.networkResponse.data));
                }
                catch(Exception exception){
                    Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
                    loadPreferences();
                    Log.d("KEY", "ExceptionErrorListener");

                }
            }
        };
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, listener, errorListener);
        queue.add(jsonObjectRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        int icon_id = getApplicationContext().getResources().getIdentifier(unit_icon, "drawable", getApplicationContext().getPackageName());
        MenuItem menuItem = menu.findItem(R.id.convert);
        menuItem.setIcon(icon_id);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        if(!hasNetworkConnection()){
            return false;
        }
        if(menuItem.getItemId() == R.id.daily){
            Intent intent = new Intent(this, DailyWeatherActivity.class);
            intent.putExtra("incoming", dailyWeathers);
            intent.putExtra("location", resolved_location);
            startActivity(intent);
        }
        else if(menuItem.getItemId() == R.id.convert){
            if(unit_icon.equals("units_f")){
                unit_icon = "units_c";
                unit_value = "°C";
                unit = "uk";
            }
            else{
                unit_icon = "units_f";
                unit_value = "°F";
                unit = "us";
            }
            int icon_id = getApplicationContext().getResources().getIdentifier(unit_icon, "drawable", getApplicationContext().getPackageName());
            menuItem.setIcon(icon_id);
            dailyWeathers.removeAll(dailyWeathers);
            hourlyWeathers.removeAll(hourlyWeathers);
            doDownload();
        }
        else{
            showDialogLocation();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public String fullDateSet(String datetime){
        Date date = new Date(Long.parseLong(datetime) * 1000L);
        SimpleDateFormat fullDate = new SimpleDateFormat("EEE MMM dd h:mm a, yyyy", Locale.getDefault());
        String fullDateStr = fullDate.format(date); // Thu Sep 29 12:00 AM, 2022 String timeOnlyStr = timeOnly.format(dateTime); // 12:00 AM
        return fullDateStr;
    }

    public String timeOnlySet(String datetime){
        Date date = new Date(Long.parseLong(datetime) * 1000L);
        SimpleDateFormat timeOnly = new SimpleDateFormat("h:mm a", Locale.getDefault());
        String timeOnlyStr = timeOnly.format(date); // 12:00 AM
        return timeOnlyStr;
    }

    public String dayOnlySet(String datetime){
        Date date = new Date(Long.parseLong(datetime) * 1000L);
        SimpleDateFormat dayOnly = new SimpleDateFormat("EEE", Locale.getDefault());
        String dayOnlyStr = dayOnly.format(date); // 12:00 AM
        return dayOnlyStr;
    }

    public String dayDateSet(String datetime){
        Date date = new Date(Long.parseLong(datetime) * 1000L);
        SimpleDateFormat dayDate = new SimpleDateFormat("EEEE MM/dd", Locale.getDefault());
        String dayDateStr = dayDate.format(date);
        return dayDateStr;
    }

    public int getHourOfDay(String datetime){
        Date date = new Date(Long.parseLong(datetime) * 1000L);
        return date.getHours();
    }



    private String getDirection(double degrees) { if (degrees >= 337.5 || degrees < 22.5)
        return "N";
        if (degrees >= 22.5 && degrees < 67.5)
            return "NE";
        if (degrees >= 67.5 && degrees < 112.5)
            return "E";
        if (degrees >= 112.5 && degrees < 157.5)
            return "SE";
        if (degrees >= 157.5 && degrees < 202.5)
            return "S";
        if (degrees >= 202.5 && degrees < 247.5)
            return "SW";
        if (degrees >= 247.5 && degrees < 292.5)
            return "W";
        if (degrees >= 292.5 && degrees < 337.5)
            return "NW";
        return "X"; // We'll use 'X' as the default if we get a bad value
    }

    public void showDialogLocation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setGravity(Gravity.CENTER_HORIZONTAL);
        builder.setTitle("Enter a location");
        builder.setMessage("For US locations enter as 'City', or 'City, State'.\n\n" +
                "For International locations enter as 'City, Country'.");
        builder.setView(editText);
        builder.setCancelable(true);

        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        location = editText.getText().toString();
                        dailyWeathers.removeAll(dailyWeathers);
                        hourlyWeathers.removeAll(hourlyWeathers);
                        doDownload();

                    }
                });

        builder.setNegativeButton(
                "CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();

    }

    public void setDailyWeathers(JSONArray days) throws Exception{
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
        for(int i=0; i<15; i++){
            date = dayDateSet(((JSONObject)days.get(i)).get("datetimeEpoch").toString());
            temp_max = ((JSONObject)days.get(i)).get("tempmax").toString();
            temp_min = ((JSONObject)days.get(i)).get("tempmin").toString();
            description = ((JSONObject)days.get(i)).get("description").toString();
            precip = ((JSONObject)days.get(i)).get("precipprob").toString();
            uv = ((JSONObject)days.get(i)).get("uvindex").toString();
            morning = ((JSONObject)((JSONArray)((JSONObject)days.get(i)).get("hours")).get(8)).get("temp").toString();
            afternoon = ((JSONObject)((JSONArray)((JSONObject)days.get(i)).get("hours")).get(13)).get("temp").toString();
            evening = ((JSONObject)((JSONArray)((JSONObject)days.get(i)).get("hours")).get(17)).get("temp").toString();
            night = ((JSONObject)((JSONArray)((JSONObject)days.get(i)).get("hours")).get(23)).get("temp").toString();
            icon_id = getApplicationContext().getResources().getIdentifier(((JSONObject)days.get(i)).get("icon").toString().replace("-", "_"), "drawable", getApplicationContext().getPackageName());
            dailyWeathers.add(new DailyWeather(date, temp_max, temp_min, description, precip, uv, morning, afternoon, evening, night, icon_id));
        }
    }

    public void setHourlyWeathers(JSONArray days, int currentTimeIndex) throws Exception{
        String day;
        String time;
        int icon_id;
        String temperature;
        String description;
        JSONObject hour;
        JSONArray hours_array;
        for(int i=0; i<4; i++){
            hours_array = days.getJSONObject(i).getJSONArray("hours");
            if(i == 0){
                day = "Today";
                for(int j=currentTimeIndex+1; j<24; j++){
                    hour = hours_array.getJSONObject(j);
                    time = timeOnlySet(hour.get("datetimeEpoch").toString());
                    icon_id = getApplicationContext().getResources().getIdentifier(hour.get("icon").toString().replace("-", "_"), "drawable", getApplicationContext().getPackageName());
                    temperature = hour.get("temp").toString();
                    description = hour.get("conditions").toString();
                    hourlyWeathers.add(new HourlyWeather(day, time, icon_id, temperature, description));
                }
            }
            else{
                day = dayOnlySet(((JSONObject)days.get(i)).get("datetimeEpoch").toString());
                for(int j=0; j<24; j++){
                    hour = hours_array.getJSONObject(j);
                    time = timeOnlySet(hour.get("datetimeEpoch").toString());
                    icon_id = getApplicationContext().getResources().getIdentifier(hour.get("icon").toString().replace("-", "_"), "drawable", getApplicationContext().getPackageName());
                    temperature = hour.get("temp").toString();
                    description = hour.get("conditions").toString();
                    hourlyWeathers.add(new HourlyWeather(day, time, icon_id, temperature, description));

                }
            }
        }
    }

    private boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }
    public void loadPreferences(){
        sharedPreferences = getSharedPreferences("loc", Context.MODE_PRIVATE);
        if(!sharedPreferences.getString("location", "").equals("")){
            location = sharedPreferences.getString("location", "");
        }

        if(!sharedPreferences.getString("unit", "").equals("")){
            unit = sharedPreferences.getString("unit", "");
        }

        if(!sharedPreferences.getString("unit_value", "").equals("")){
            unit_value = sharedPreferences.getString("unit_value", "");
        }

        if(!sharedPreferences.getString("unit_icon", "").equals("")){
            unit_icon = sharedPreferences.getString("unit_icon", "");
        }

    }
    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences("loc", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString("location", location);
        prefsEditor.putString("unit", unit);
        prefsEditor.putString("unit_value", unit_value);
        prefsEditor.putString("unit_icon", unit_icon);
        prefsEditor.apply();
    }
}