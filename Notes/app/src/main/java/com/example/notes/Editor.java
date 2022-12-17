package com.example.notes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Date;

public class Editor extends AppCompatActivity {

    EditText title;
    EditText text;
    Integer position;
    String hold_title="";
    String hold_text="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Intent intent = getIntent();
        title = findViewById(R.id.notes_title);
        text = findViewById(R.id.notes_text);
        hold_title = intent.getStringExtra("Open Title");
        hold_text = intent.getStringExtra("Open Text");
        title.setText(intent.getStringExtra("Open Title"));
        text.setText(intent.getStringExtra("Open Text"));
        position = intent.getIntExtra("Position", 0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        Intent intent;
        if(menuItem.getItemId() == R.id.save_button){
            onSaveButtonClick();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void onSaveButtonClick(){
        title = findViewById(R.id.notes_title);
        text = findViewById(R.id.notes_text);
        if(title.getText().toString().equals("")){
            showDialogNoTitle();
        }
        else{
            Intent returnIntent = new Intent();
            returnIntent.putExtra("Save Title", title.getText().toString());
            returnIntent.putExtra("Save Text", text.getText().toString());
            returnIntent.putExtra("Position", position);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }


    @Override
    public void onBackPressed(){
        title = findViewById(R.id.notes_title);
        text = findViewById(R.id.notes_text);

        if(title.getText().toString().equals(hold_title) && text.getText().toString().equals(hold_text)){
            super.onBackPressed();
        }

        else if(!text.getText().toString().isEmpty() || !title.getText().toString().isEmpty()){
            showDialogBackPress();
        }
        else{
            super.onBackPressed();
        }
    }


    public void showDialogNoTitle()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(Editor.this);
        builder.setMessage("Could not save without a title!");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        builder.setNegativeButton(
                "CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(Editor.this, "Could not save without a title! ", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();

    }

    public void showDialogBackPress(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Editor.this);
        builder.setMessage("You forgot to save changes!");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "SAVE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onSaveButtonClick();
                        dialogInterface.cancel();
                    }
                });

        builder.setNegativeButton(
                "EXIT",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

}