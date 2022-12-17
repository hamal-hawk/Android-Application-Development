package com.example.civiladvocacy;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.w3c.dom.Text;

public class OfficialActivity extends AppCompatActivity {

    public TextView location_official;
    public TextView name;
    public TextView title;
    public TextView party;
    public TextView address;
    public TextView phone;
    public TextView email;
    public TextView website;
    public TextView address_head;
    public TextView phone_head;
    public TextView email_head;
    public TextView website_head;
    public ImageView facebook;
    public ImageView twitter;
    public ImageView youtube;
    public ImageView picture;
    public ImageView party_logo;
    public Official official;
    public ConstraintLayout constraintLayout_official;
    int logo_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);
        location_official = findViewById(R.id.location_official);
        location_official.setText(MainActivity.resolved_address);
        name = findViewById(R.id.name_official);
        title = findViewById(R.id.title_official);
        party = findViewById(R.id.party_official);
        address = findViewById(R.id.address_official);
        phone = findViewById(R.id.phone_official);
        email = findViewById(R.id.email_official);
        website = findViewById(R.id.website_official);
        address_head = findViewById(R.id.address_official_head);
        phone_head = findViewById(R.id.phone_official_head);
        email_head = findViewById(R.id.email_official_head);
        website_head = findViewById(R.id.website_official_head);
        facebook = findViewById(R.id.facebook_official);
        twitter = findViewById(R.id.twitter_official);
        youtube = findViewById(R.id.youtube_official);
        picture = findViewById(R.id.picture_official);
        constraintLayout_official = findViewById(R.id.constraint_official);


        party_logo = findViewById(R.id.partylogo_official);
        Intent intent = getIntent();
        official = (Official) intent.getSerializableExtra("TO_OFFICIAL");
        if(!official.getFacebook().equals("")){
            facebook.setVisibility(View.VISIBLE);
        }
        if(!official.getTwitter().equals("")){
            twitter.setVisibility(View.VISIBLE);
        }
        if(!official.getYoutube().equals("")){
            youtube.setVisibility(View.VISIBLE);
        }
        name.setText(official.getName());
        title.setText(official.getOffice());
        party.setText("("+official.getParty()+")");
        if(!official.getAddress().equals("")){
            address.setText(official.getAddress());
            address.setPaintFlags(address.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
            address_head.setVisibility(View.VISIBLE);
        }
        if(!official.getPhone().equals("")){
            phone.setText(official.getPhone());
            phone.setPaintFlags(phone.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
            phone_head.setVisibility(View.VISIBLE);
        }
        if(!official.getEmail().equals("")){
            email.setText(official.getEmail());
            email.setPaintFlags(email.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
            email_head.setVisibility(View.VISIBLE);
        }
        if(!official.getWebsite().equals("")){
            website.setText(official.getWebsite());
            website.setPaintFlags(website.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
            website_head.setVisibility(View.VISIBLE);
        }

        if(official.getParty().equals("Democratic Party")){
            logo_id = getApplicationContext().getResources().getIdentifier("dem_logo", "drawable", getApplicationContext().getPackageName());
            constraintLayout_official.setBackgroundColor(Color.BLUE);
            party_logo.setImageResource(logo_id);
            party_logo.setVisibility(View.VISIBLE);
        }
        else if(official.getParty().equals("Republican Party")){
            logo_id = getApplicationContext().getResources().getIdentifier("rep_logo", "drawable", getApplicationContext().getPackageName());
            constraintLayout_official.setBackgroundColor(Color.RED);
            party_logo.setImageResource(logo_id);
            party_logo.setVisibility(View.VISIBLE);
        }

        Glide.with(getApplicationContext())
                .load(official.photo_url)
                .placeholder(RecyclerAdapter.picture_placeholder)
                .error(official.photo_url.equals("")?RecyclerAdapter.picture_placeholder:RecyclerAdapter.broken_picture)
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if(official.photo_url.equals("")){
                            picture.setClickable(false);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(picture);

    }


    public void clickPicture(View view){
        Intent intent = new Intent(this, PhotoActivity.class);
        intent.putExtra("TO_PHOTO", official);
        startActivity(intent);
    }

    public void clickLogo(View view){
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

    public void goFacebook(View view){
        Intent intent;
        try{
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.facebook.katana");
            intent.setData(Uri.parse("fb://facewebmodal/f?href=https://www.facebook.com/"+official.getFacebook()));
            startActivity(intent);
        }
        catch(Exception exception){
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.facebook.com/"+official.getFacebook()));
            startActivity(intent);
        }
    }

    public void goTwitter(View view){
        Intent intent;
        try{
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.twitter.android");
            intent.setData(Uri.parse("twitter://user?screen_name="+official.getTwitter()));
            startActivity(intent);
        }
        catch(Exception exception){
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.twitter.com/"+official.getTwitter()));
            startActivity(intent);
        }
    }

    public void goYoutube(View view){
        Intent intent;
        try{
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/"+official.getYoutube()));
            startActivity(intent);
        }
        catch(Exception exception){
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.youtube.com/"+official.getYoutube()));
            startActivity(intent);
        }
    }

    public void sendEmail(View view){
        try{
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"+official.getEmail()));
            startActivity(intent);
        }
        catch (Exception exception){
            Toast.makeText(this, "No application to handle task!", Toast.LENGTH_LONG);
        }

    }

    public void dial(View view){
        try{
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"+official.getPhone()));
            startActivity(intent);
        }
        catch (Exception exception){
            Toast.makeText(this, "No application to handle task!", Toast.LENGTH_LONG);
        }
    }

    public void browse(View view){
        try{
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(official.getWebsite()));
            startActivity(intent);
        }
        catch (Exception exception){
            Toast.makeText(this, "No application to handle task!", Toast.LENGTH_LONG);
        }
    }

    public void clickAddress(View view){
        try{
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("geo:0,0?q="+Uri.encode(official.getAddress())));
            startActivity(intent);
        }
        catch(Exception exception){

        }
    }
}