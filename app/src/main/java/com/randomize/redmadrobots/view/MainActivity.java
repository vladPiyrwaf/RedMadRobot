package com.randomize.redmadrobots.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.randomize.redmadrobots.R;
import com.randomize.redmadrobots.api.NetworkService;
import com.randomize.redmadrobots.models.Photo;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String CLIENT_ID = "e1302c9b61d67d3011bfed17ff854fa7aa0426c2adbe9c9fd18528a073476682";

    private ImageView imageView;
    private Button btnSearchPhoto, btnCollections;
    private Photo photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.image_daily);

        btnSearchPhoto = findViewById(R.id.btn_search_photo);
        btnCollections = findViewById(R.id.btn_collections);

        btnSearchPhoto.setOnClickListener(this);
        btnCollections.setOnClickListener(this);

        setRequestRandomPhoto();
    }

    private void setRequestRandomPhoto(){

        NetworkService.getInstance()
                .getJSONApi()
                .getRandomPhoto(1,1,"daily", CLIENT_ID)
                .enqueue(new Callback<Photo>() {
                    @Override
                    public void onResponse(Call<Photo> call, Response<Photo> response) {
                        photo = response.body();
                        Picasso.get().load(photo.getUrls().getSmall()).into(imageView);
                        Toast.makeText(MainActivity.this, photo.getId(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Photo> call, Throwable t) {

                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_collections:
                startActivity(new Intent(MainActivity.this, CollectionsActivity.class));
                break;
            case R.id.btn_search_photo:
                startActivity(new Intent(MainActivity.this, SearchPhotoActivity.class));
                break;
        }
    }
}
