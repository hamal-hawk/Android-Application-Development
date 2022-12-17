package com.example.notes;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements RecyclerViewClick{

    private ArrayList<Note> notesList;
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;




    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        Intent intent;
        if(menuItem.getItemId() == R.id.add_button){
            intent = new Intent(this, Editor.class);
            startActivityForResult(intent, 1);
        }
        else if(menuItem.getItemId() == R.id.info_button){
            intent = new Intent(this, About.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notesList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);

        Gson gson = new GsonBuilder().create();
        String listData = Operations.readFile(this);
        if(!listData.isEmpty()){
            notesList = gson.fromJson(listData, new TypeToken<ArrayList<Note>>(){}.getType());
        }
        Log.d("key", "val: "+" "+notesList);
        setAdapter();


    }

    public void onResume(){
        super.onResume();
        getSupportActionBar().setTitle("Notes ("+adapter.getItemCount()+")");
        Gson gson = new GsonBuilder().create();
        if(notesList != null) {
            String listData = gson.toJson(notesList);
            Operations.writeFile(listData, this);
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK) {
                setNote(data.getStringExtra("Save Title"), data.getStringExtra("Save Text"));
            }
        }
        if(requestCode == 2){
            if(resultCode == Activity.RESULT_OK) {
                replaceNote(data.getStringExtra("Save Title"), data.getStringExtra("Save Text"), data.getIntExtra("Position", 0));
            }
        }
    }

    private void setAdapter() {
        adapter = new RecyclerAdapter(notesList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void setNote(String title, String text) {
        notesList.add(new Note(title, new Date(), text));
        Collections.sort(notesList);
        adapter.notifyDataSetChanged();
        getSupportActionBar().setTitle("Notes ("+adapter.getItemCount()+")");
    }

    public void replaceNote(String title, String text, int position){

        notesList.set(position, new Note(title, new Date(), text));
        Collections.sort(notesList);
        adapter.notifyDataSetChanged();
    }

    public void deleteNote(int position){
        notesList.remove(position);
        Collections.sort(notesList);
        adapter.notifyDataSetChanged();
        onResume();
    }

    @Override
    public void onItemClick(int position) {

        Intent intent = new Intent(this, Editor.class);
        intent.putExtra("Open Title", notesList.get(position).getTitle());
        intent.putExtra("Open Text", notesList.get(position).getText());
        intent.putExtra("Position", position);
        startActivityForResult(intent, 2);
    }

    @Override
    public void onItemLongClick(int position) {
        showDialogDelete(position);
    }

    public void showDialogDelete(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete Note?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        builder.setNegativeButton(
                "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteNote(position);
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();

    }
}