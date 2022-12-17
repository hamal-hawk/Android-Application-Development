package com.example.civiladvocacy;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.w3c.dom.Text;

public class PhotoActivity extends AppCompatActivity {
    public TextView location;
    public TextView name;
    public TextView title;
    public ImageView photo;
    public ImageView party_logo;
    Official official;
    public ConstraintLayout constraintLayout_photo;
    int logo_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        Intent intent = getIntent();
        official = (Official) intent.getSerializableExtra("TO_PHOTO");
        location = findViewById(R.id.location_photo);
        name = findViewById(R.id.name_photo);
        title = findViewById(R.id.title_photo);
        photo = findViewById(R.id.picture_photo);
        party_logo = findViewById(R.id.partylogo_photo);
        constraintLayout_photo = findViewById(R.id.constraint_photo);

        if(official.getParty().equals("Democratic Party")){
            logo_id = getApplicationContext().getResources().getIdentifier("dem_logo", "drawable", getApplicationContext().getPackageName());
            constraintLayout_photo.setBackgroundColor(Color.BLUE);
            party_logo.setImageResource(logo_id);
            party_logo.setVisibility(View.VISIBLE);
        }
        else if(official.getParty().equals("Republican Party")){
            logo_id = getApplicationContext().getResources().getIdentifier("rep_logo", "drawable", getApplicationContext().getPackageName());
            constraintLayout_photo.setBackgroundColor(Color.RED);
            party_logo.setImageResource(logo_id);
            party_logo.setVisibility(View.VISIBLE);
        }

        location.setText(MainActivity.resolved_address);
        name.setText(official.getName());
        title.setText(official.getOffice());

        Glide.with(getApplicationContext())
                .load(official.photo_url)
                .error(RecyclerAdapter.broken_picture)
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(photo);
    }

    public void clickLogoPhoto(View view){
        try{
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if(official.getParty().equals("Democratic Party")){
                intent.setData(Uri.parse("https://democrats.org"));
                startActivity(intent);
            }
            else{
                intent.setData(Uri.parse("https://www.gop.com"));
                startActivity(intent);
            }

        }
        catch (Exception exception){
            exception.printStackTrace();
        }
    }
}