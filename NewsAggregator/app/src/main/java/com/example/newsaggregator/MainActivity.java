package com.example.newsaggregator;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsaggregator.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private RequestQueue queue;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    static boolean flag = false;
    private ConstraintLayout mConstraintLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private HashMap<String, ArrayList<String>> topicMap;
    static HashMap<String, Integer> colorMap;
    private HashMap<String, String> idMap;
    //private ArrayAdapter arrayAdapter;
    private int[] colorArray;
    private CustomAdapter arrayAdapter;
    private String savedTitle;
    private Menu opt_menu;
    static ArrayList<Source> sourcesList;
    static ArrayList<String> sourcesNameList;
    private  ArrayList<Article> articlesList;
    private ActivityMainBinding binding;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        colorArray = getResources().getIntArray(R.array.colors);
        topicMap = new HashMap<>();
        colorMap = new HashMap<>();
        idMap = new HashMap<>();
        sourcesList = new ArrayList<>();
        sourcesNameList = new ArrayList<>();
        articlesList = new ArrayList<>();
        queue = Volley.newRequestQueue(this);
        mDrawerLayout = binding.drawerLayout;
        mConstraintLayout = binding.cLayout;
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        );


        mDrawerList = binding.leftDrawer;
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
        arrayAdapter = new CustomAdapter(this, R.layout.drawable_list_item, sourcesNameList);
        mDrawerList.setAdapter(arrayAdapter);
        doDownloadSources();
        if(savedInstanceState != null){
            articlesList = (ArrayList<Article>) savedInstanceState.getSerializable("ARTICLES_LIST");
            savedTitle = savedInstanceState.getString("TITLE");
            binding.fragmentContainer.setVisibility(View.VISIBLE);
            if(savedTitle.length() >= 9 && savedTitle.substring(5,9).equals("Gate")){
                binding.fragmentContainer.setVisibility(View.INVISIBLE);
            }
        }

    }

    public void doDownloadSources(){
        String url = "https://newsapi.org/v2/sources?apiKey=2c4dfd16096d439abdf3135312ef72bc";
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("RestrictedApi")
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray sources = response.getJSONArray("sources");
                    createSourcesList(sources);
                    createSourcesNameList();

                    if(getSupportActionBar() != null){
                        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setTitle(savedTitle!=null?savedTitle:"News Gateway ("+sourcesNameList.size()+")");

                    }
                    arrayAdapter.notifyDataSetChanged();
                    makeMap(sourcesList);
                    makeColorMap();
                    setOptionsMenu();

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
                    Log.d("KEY", "ExceptionErrorListener");

                }
            }
        };
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, listener, errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError { Map<String, String> headers = new HashMap<>();
                headers.put("User-Agent", "News-App");
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public void doDownloadContent(String source){
        String url = "https://newsapi.org/v2/top-headlines?sources="+source+"&apiKey=2c4dfd16096d439abdf3135312ef72bc";
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("RestrictedApi")
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray articles = response.getJSONArray("articles");
                    createArticlesList(articles);
                    openFragment();

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
                    Log.d("KEY", "ExceptionErrorListener");

                }
            }
        };
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, listener, errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError { Map<String, String> headers = new HashMap<>();
                headers.put("User-Agent", "News-App");
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public void createSourcesList(JSONArray sources) throws JSONException {
        for(int i=0; i<sources.length(); i++){
            sourcesList.add(new Source(((JSONObject)sources.get(i)).get("id").toString(),((JSONObject)sources.get(i)).get("name").toString(), ((JSONObject)sources.get(i)).get("category").toString()));
            idMap.put(((JSONObject)sources.get(i)).get("name").toString(), ((JSONObject)sources.get(i)).get("id").toString());
        }
    }

    public void createSourcesNameList(){
        for(int i=0; i<sourcesList.size(); i++){
            sourcesNameList.add(sourcesList.get(i).getName());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void makeColorMap(){
        Random rnd = new Random();

        for(String topic : topicMap.keySet()){
            if(!topic.equals("all")){
                float r = (float) (rnd.nextFloat() / 2f + 0.5);
                float g = (float) (rnd.nextFloat() / 2f + 0.5);
                float b = (float) (rnd.nextFloat() / 2f + 0.5);
                colorMap.put(topic, colorArray[new Random().nextInt(colorArray.length)]);
            }
        }
        flag = true;

    }

    public void makeMap(ArrayList<Source> sourcesList){
        for(Source source : sourcesList){
            if(!topicMap.containsKey(source.getTopic())) {
                topicMap.put(source.getTopic(), new ArrayList<String>());
            }
        }
        topicMap.put("all", new ArrayList<String>());
        for(Source source : sourcesList){
            topicMap.get("all").add(source.getName());
            topicMap.get(source.getTopic()).add(source.getName());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createArticlesList(JSONArray articles) throws JSONException {
        articlesList.clear();
        for(int i=0; i<articles.length(); i++){
            articlesList.add(new Article(((JSONObject)articles.get(i)).get("author").toString(),
                    ((JSONObject)articles.get(i)).get("title").toString(), ((JSONObject)articles.get(i)).get("description").toString(),
                    ((JSONObject)articles.get(i)).get("url").toString(), ((JSONObject)articles.get(i)).get("urlToImage").toString(),
                    setDateFormat(((JSONObject)articles.get(i)).get("publishedAt").toString().substring(0,19)+"Z")));

        }

    }

    public void setOptionsMenu(){
        for(String str : topicMap.keySet()){
            opt_menu.add(str);
        }
        for(int i=1; i<topicMap.keySet().size(); i++){
            MenuItem item = opt_menu.getItem(i);
            SpannableString spanString = new SpannableString(opt_menu.getItem(i).getTitle().toString());
            spanString.setSpan(new ForegroundColorSpan(colorMap.get(item.getTitle().toString())), 0,     spanString.length(), 0); //fix the color to white
            item.setTitle(spanString);
        }
    }

    public void resetActionBar(){
        getSupportActionBar().setTitle("News Gateway ("+sourcesNameList.size()+")");
    }

    private void selectItem(int position){
        String id = idMap.get(sourcesNameList.get(position));
        getSupportActionBar().setTitle(sourcesNameList.get(position));
        doDownloadContent(id);
        mDrawerLayout.closeDrawer(mConstraintLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        opt_menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        sourcesNameList.clear();
        for(String str : topicMap.get(item.getTitle().toString())){
            sourcesNameList.add(str);
        }
        arrayAdapter.notifyDataSetChanged();
        resetActionBar();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void openFragment(){
        binding.fragmentContainer.setVisibility(View.VISIBLE);
        Bundle args = new Bundle();
        args.putSerializable("ARTICLES_LIST", articlesList);
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBackPressed() {
        resetActionBar();
        binding.fragmentContainer.setVisibility(View.INVISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String setDateFormat(String date){
        Instant instant = Instant.parse(date);
        long datetimeEpoch = instant.toEpochMilli();
        Date dateTime = new Date(datetimeEpoch); // Java time values need milliseconds
        SimpleDateFormat fullDate = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
        String fullDateStr = fullDate.format(dateTime);
        return fullDateStr;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("ARTICLES_LIST", articlesList);
        outState.putString("TITLE", getSupportActionBar().getTitle().toString());
    }
}