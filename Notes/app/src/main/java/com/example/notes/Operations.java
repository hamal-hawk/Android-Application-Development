package com.example.notes;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Operations {


    public static void writeFile(String listData, Context context){
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("storage.json", Context.MODE_PRIVATE));
            outputStreamWriter.write(listData);
            outputStreamWriter.close();
        }
        catch(IOException exception){
            Log.d("exception", "caught");
        }
    }

    public static String readFile(Context context){
        String listData="";
        try{
            InputStream inputStream = context.openFileInput("storage.json");
            if(inputStream != null){
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String readString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (readString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(readString);
                }

                inputStream.close();
                listData = stringBuilder.toString();
            }
        }
        catch(IOException exception){
            Log.d("exception", "caught");
        }
        return listData;
    }
}
