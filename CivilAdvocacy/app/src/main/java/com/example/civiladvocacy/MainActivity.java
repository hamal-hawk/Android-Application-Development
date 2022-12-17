package com.example.civiladvocacy;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;


import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Array;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements RecyclerViewClick {
    private RequestQueue queue;
    public ArrayList<Official> officialsList;
    public RecyclerAdapter recyclerAdapter;
    public RecyclerView recyclerView;
    public TextView location_main;
    public TextView network_error;
    String location;
    static String resolved_address;
    FusedLocationProviderClient fusedLocationProviderClient;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        officialsList = new ArrayList<>();
        location_main = findViewById(R.id.location_main);
        network_error = findViewById(R.id.network_error);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();
        queue = Volley.newRequestQueue(this);
        setAdapter();
    }


    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onSuccess(Location loc) {
                        Log.d("KEY", "getLocation: ");
                        if (loc != null) {
                            double longitude = loc.getLongitude();
                            double latitude = loc.getLatitude();
                            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                            List<Address> addressesList = null;
                            try {
                                addressesList = geocoder.getFromLocation(latitude, longitude, 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            location = addressesList.get(0).getAddressLine(0);
                            doDownload();
                        }
                        else{
                            location_main.setText("NO DATA FOR LOCATION");
                            Toast.makeText(MainActivity.this, "TURN ON LOCATION AND TRY AGAIN", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            if(permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)){
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.d("KEY", "onRequestPermissionsResult: ");
                    getLocation();
                }
                else{
                    location_main.setText("NO DATA FOR LOCATION");                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void doDownload(){
        if(!hasNetworkConnection()){
            network_error.setVisibility(View.VISIBLE);
            location_main.setText("NO DATA FOR LOCATION");
            return;
        }
        String url = "https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyB-Kqz7tXGLVWyP7kxc2jhieZAylxkgLZI&address="+location;
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONObject normalizedInput = response.getJSONObject("normalizedInput");
                    resolved_address="";
                    //resolved_address = normalizedInput.get("line1")+" "+normalizedInput.get("city")+", "+normalizedInput.get("state")+", "+normalizedInput.get("zip");
                    if(!normalizedInput.get("line1").equals("")){
                        resolved_address+=normalizedInput.get("line1")+", ";
                    }
                    if(!normalizedInput.get("city").equals("")){
                        resolved_address+=normalizedInput.get("city")+", ";
                    }
                    if(!normalizedInput.get("state").equals("")){
                        resolved_address+=normalizedInput.get("state")+" ";
                    }
                    if(!normalizedInput.get("zip").equals("")){
                        resolved_address+=normalizedInput.get("zip");
                    }
                    location_main.setText(resolved_address);
                    JSONArray offices = response.getJSONArray("offices");
                    JSONArray officials = response.getJSONArray("officials");
                    setOfficialsList(offices, officials);
                    recyclerAdapter.notifyDataSetChanged();
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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, listener, errorListener);
        queue.add(jsonObjectRequest);
    }

    private void setAdapter() {
        recyclerAdapter = new RecyclerAdapter(officialsList, this, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        if(menuItem.getItemId() == R.id.about){
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }
        else if(menuItem.getItemId() == R.id.location){
            showDialogLocation();
        }
        return true;
    }

    public void setOfficialsList(JSONArray offices, JSONArray officials) throws Exception{
        for(int i=0; i<offices.length(); i++){
            JSONObject office = offices.getJSONObject(i);
            String title = office.get("name").toString();
            JSONArray officialIndices = office.getJSONArray("officialIndices");
            for(int j=0; j<officialIndices.length(); j++){
                JSONObject official = officials.getJSONObject((int)officialIndices.get(j));
                String name = official.get("name").toString();
                String address="";
                if(official.has("address")){
                    JSONObject addressObject = official.getJSONArray("address").getJSONObject(0);
                    if(addressObject.has("line1")){
                        address += addressObject.get("line1")+", ";
                    }
                    if(addressObject.has("line2")){
                        address += addressObject.get("line2")+", ";
                    }
                    if(addressObject.has("line3")){
                        address += addressObject.get("line3")+", ";
                    }
                    address += addressObject.get("city")+", "+addressObject.get("state")+", "+addressObject.get("zip");
                }
                String party = official.get("party").toString();
                String phone = "";
                if(official.has("phones")){
                    phone = official.getJSONArray("phones").get(0).toString();
                }
                String url = "";
                if(official.has("urls")){
                    url = official.getJSONArray("urls").get(0).toString();
                }
                String email = "";
                if(official.has("emails")){
                    email = official.getJSONArray("emails").get(0).toString();
                }
                String facebook = "";
                String twitter = "";
                String youtube = "";
                if(official.has("channels")){
                    JSONArray channelArray = official.getJSONArray("channels");
                    for(int k=0; k<channelArray.length(); k++){
                        JSONObject channel = channelArray.getJSONObject(k);
                        if(channel.get("type").toString().equals("Facebook")){
                            facebook = channel.get("id").toString();
                        }
                        else if(channel.get("type").toString().equals("Twitter")){
                            twitter = channel.get("id").toString();
                        }
                        else {
                            youtube = channel.get("id").toString();
                        }
                    }
                }
                String photoUrl = "";
                if(official.has("photoUrl")){
                    photoUrl = official.get("photoUrl").toString().replace("http:", "https:");
                    Log.d("KEY", "setOfficialsList: "+photoUrl);
                }

                officialsList.add(new Official(name, title, party, facebook, twitter, youtube, address, phone, email, url, photoUrl));
            }
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, OfficialActivity.class);
        intent.putExtra("TO_OFFICIAL", officialsList.get(position));
        startActivity(intent);
        //Toast.makeText(this, "Clicked on: "+position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(int position) {}

    public void clearList(){
        officialsList.removeAll(officialsList);
    }

    public void showDialogLocation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setGravity(Gravity.CENTER_HORIZONTAL);
        builder.setTitle("Enter address");
        builder.setView(editText);
        builder.setCancelable(true);

        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    public void onClick(DialogInterface dialogInterface, int i) {
                        location = editText.getText().toString();
                        clearList();
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
}